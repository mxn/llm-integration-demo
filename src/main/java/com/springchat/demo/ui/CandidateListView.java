package com.springchat.demo.ui;

import com.springchat.demo.entity.Candidate;
import com.springchat.demo.services.CandidateService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;


@Route("candidate-list")
public class CandidateListView extends VerticalLayout {
    private final CandidateService candidateService; // Assuming CandidateService handles fetching candidates

    private final Grid<Candidate> grid = new Grid<>(Candidate.class);
    private final Button addButton = new Button("Add Candidate");

    public CandidateListView(CandidateService candidateService) {
        this.candidateService = candidateService;
        setWidthFull();
        addButton.addClickListener(event -> addCandidate());
        grid.setColumns("firstName", "lastName", "email");
        grid.setItems(getAllCandidates());
        grid.addComponentColumn(this::createButtonsLayout);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS);
        add(grid, addButton);

    }

    private List<Candidate> getAllCandidates() {
        return candidateService.listAll(); // Assuming CandidateService has a method to fetch all candidates
    }

    private void addCandidate() {
        getUI().ifPresent(ui -> ui.navigate(CandidateEditView.class));
    }

    private HorizontalLayout createButtonsLayout(Candidate candidate) {
        Button viewButton = new Button("Details", event -> navigateToCandidateDetailView(candidate));
        Button editButton = new Button("Edit", event -> navigateToCandidateEditView(candidate));
        Button deleteButton = new Button("Delete", event -> deleteCandidate(candidate));

        return new HorizontalLayout(viewButton, editButton, deleteButton);
    }

    private void navigateToCandidateDetailView(Candidate candidate) {
        getUI().ifPresent(ui -> ui.navigate(CandidateDetailView.class, candidate.getId()));
    }

    private void navigateToCandidateEditView(Candidate candidate) {
        getUI().ifPresent(ui -> ui.navigate(CandidateEditView.class, candidate.getId()));
    }

    private void deleteCandidate(Candidate candidate) {
        candidateService.delete(candidate);
        grid.setItems(getAllCandidates());
    }
}