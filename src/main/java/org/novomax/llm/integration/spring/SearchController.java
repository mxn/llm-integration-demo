package org.novomax.llm.integration.spring;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.novomax.llm.integration.AiService;
import org.novomax.llm.integration.SearchResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@RestController
public class SearchController {

    public static final String LLM_SEARCH_FORM = "templates/llm-search-form";
    private final AiService aiService;
    private final UiEntityUrlConfiguration uiEntityUrlConfiguration;

    SearchController(AiService aiService, UiEntityUrlConfiguration uiEntityUrlConfiguration) {
        this.aiService = aiService;
        this.uiEntityUrlConfiguration = uiEntityUrlConfiguration;
    }

    @GetMapping(value = "/llm-integration/ui/search-one", produces = MediaType.TEXT_HTML_VALUE)
    public String getSearchForm() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setSuffix(".html");

        var context = new Context();
        context.setVariable("search", "");

        var templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
        return templateEngine.process(LLM_SEARCH_FORM, context);
    }

    @PostMapping("/llm-integration/ui/search-one")
    public void processSearch(@RequestParam("search-text") String searchText, HttpServletResponse response,
                              HttpServletRequest request) throws IOException {
        List<SearchResult> searchResultList = aiService.findByFreeText(searchText, 1);

        SearchResult searchResult = searchResultList.get(0);
        String urlTemplate = uiEntityUrlConfiguration.getSearchOneMapping().get(searchResult.entityClass());
        String urlPartToShowEntity = new MessageFormat(urlTemplate).format(new String[]{searchResult.entityId()});
        response.sendRedirect(getBaseUrlPart(request) + urlPartToShowEntity);
    }

    private String getBaseUrlPart(HttpServletRequest request) {
        if (request.getContextPath() == null || "".equals(request.getContextPath())) {
            return String.format("%s://%s:%s", request.getScheme(), request.getServerName(), request.getServerPort());
        }
        return String.format("%s://%s:%s/%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
    }
}
