package com.springchat.demo.ui;

import com.springchat.demo.entity.Candidate;
import com.springchat.demo.services.CandidateService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

@Route(value = RouteConstants.CANDIDATE_DETAIL)
public class CandidateDetailView extends VerticalLayout implements HasUrlParameter<Long> {
    private final CandidateService candidateService;
    private final Paragraph firstNameParagraph = new Paragraph();
    private final Paragraph lastNameParagraph = new Paragraph();
    private final Paragraph emailParagraph = new Paragraph();
    private final TextArea resume = new TextArea();
    private final Button buttonToList = new Button("Back to List");
    private final Button buttonToEdit = new Button("Edit");
    private Long candidateId;

    public CandidateDetailView(CandidateService candidateService) {
        this.candidateService = candidateService;
        buttonToList.addClickListener(this::navigateToListView);
        buttonToEdit.addClickListener(this::navigateToEdit);
        setWidthFull();
    }

    private void navigateToEdit(final ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate(RouteConstants.CANDIDATE_EDIT + "/" + candidateId));
    }

    private void navigateToListView(ClickEvent<Button> buttonClickEvent) {
        getUI().ifPresent(ui -> ui.navigate(RouteConstants.CANDIDATE_LIST));
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if (parameter != null) {
            candidateId = parameter;
            showCandidateDetails();
        } else {
            clearCandidateDetails();
        }
    }

    private String cleanNullInStr(final String string) {
        return string == null ? "" : string;
    }

    private void showCandidateDetails() {
        Candidate candidate = candidateService.findById(candidateId); // Assuming CandidateService has a method to fetch a candidate by ID

        if (candidate != null) {
            firstNameParagraph.setText("First Name: " + cleanNullInStr(candidate.getFirstName()));
            lastNameParagraph.setText("Last Name: " + cleanNullInStr(candidate.getLastName()));
            emailParagraph.setText("Email: " + cleanNullInStr(candidate.getEmail()));
            resume.setValue(cleanNullInStr(candidate.getResume()));
            resume.setWidthFull();
            resume.setEnabled(false);
            add(new HorizontalLayout(buttonToList, buttonToEdit), //
                    new HorizontalLayout(firstNameParagraph, lastNameParagraph), //
                    emailParagraph, resume);
        } else {
            clearCandidateDetails();
        }
    }

    private void clearCandidateDetails() {
        removeAll();
        add(new Div());
    }
}

