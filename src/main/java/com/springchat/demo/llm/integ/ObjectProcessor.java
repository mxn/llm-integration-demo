package com.springchat.demo.llm.integ;

import jakarta.persistence.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ObjectProcessor {
    public static final String DESTINATION_NAME = "llmProcessing";
    static final String ENTITY_CLASS_KEY = "entityClass";
    static final String ID_KEY = "id";
    static final String TEXT_KEY = "text";
    private static JmsTemplate jmsTemplate;
    private final Logger logger = LoggerFactory.getLogger(ObjectProcessor.class);

    private static String getText(Object candidate) {
        return Arrays.stream(candidate.getClass().getDeclaredFields()) //
                .filter(field -> field.getAnnotation(LlmTextGetter.class) != null) //
                .map(field -> {
                    try {
                        return String.valueOf(field.get(candidate));
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }) //
                .collect(Collectors.joining("\\n\\n"));
    }

    private static Object getIdValue(Object candidate) {
        Field idField = Arrays.stream(candidate.getClass().getDeclaredFields()) //
                .filter(field -> field.getAnnotation(Id.class) != null) //
                .findAny() //
                .orElseThrow(() -> new IllegalStateException(candidate.getClass() + " has no Id Field!"));
        idField.setAccessible(true);
        try {
            return idField.get(candidate);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        ObjectProcessor.jmsTemplate = jmsTemplate;
    }

    Map<String, Object> toMap(final Object candidate) {
        Map<String, Object> res = new HashMap<>();
        res.put(ENTITY_CLASS_KEY, candidate.getClass().getName());
        Object idValue = getIdValue(candidate);
        //candidate.§
        res.put(ID_KEY, idValue);
        String text = getText(candidate);
        res.put(TEXT_KEY, text);
        return res;
    }

    public void update(Object object) {
        jmsTemplate.convertAndSend(DESTINATION_NAME, toMap(object));
    }


    @JmsListener(destination = DESTINATION_NAME)
    void onMessage(Map<String, Object> message) {
        logger.warn("got {}: {} {}", message.get(ENTITY_CLASS_KEY), message.get(ID_KEY), message.get(TEXT_KEY));
    }
}
