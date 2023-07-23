package com.springchat.demo.ui;

import com.springchat.demo.entity.Candidate;
import com.springchat.demo.services.CandidateService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;

@Route(value = "candidate-edit/:id?")
public class CandidateEditView extends VerticalLayout implements HasUrlParameter<Long> {
    static final int MIN_HEIGHT_RESUME_TEXT_AREA = 20;
    private final CandidateService candidateService;
    private Long candidateId;
    private Binder<Candidate> binder = new Binder<>(Candidate.class);

    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private EmailField email = new EmailField("Email");
    private TextArea resume = new TextArea("Resume");

    private Button saveButton = new Button("Save");
    private Button cancelButton = new Button("Cancel");

    public CandidateEditView(CandidateService candidateService) {
        this.candidateService = candidateService;
        setWidthFull();
        FormLayout formLayout = new FormLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(saveButton, cancelButton);
        formLayout.add(firstName, lastName, email, resume, horizontalLayout);
        saveButton.addClickListener(event -> saveCandidate());
        cancelButton.addClickListener(event -> navigateBack());
        resume.setPlaceholder("Add resume");
        resume.setMinHeight(MIN_HEIGHT_RESUME_TEXT_AREA, Unit.EM);
        binder.bindInstanceFields(this);
        binder.bind(resume, Candidate::getResume, Candidate::setResume);
        add(formLayout);
    }

    private void cancel() {

    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        Optional<Long> id = UiHelper.getId(event);
        if (id.isPresent()) {
            candidateId = id.get();
            showCandidateDetails();
        } else {
            clearCandidateDetails();
        }
    }

    private void showCandidateDetails() {
        Candidate candidate = candidateService.findById(candidateId); // Assuming CandidateService has a method to fetch a candidate by ID

        if (candidate != null) {
            binder.setBean(candidate);
        } else {
            clearCandidateDetails();
        }
    }

    private void clearCandidateDetails() {
        binder.setBean(new Candidate());
    }

    private void saveCandidate() {
        Candidate candidate = binder.getBean();
        candidateService.save(candidate); // Assuming CandidateService has a method to save/update a candidate
        clearCandidateDetails();
        navigateBack();
    }

    private void navigateBack() {
        getUI().ifPresent(ui -> ui.navigate(CandidateListView.class));
    }
}