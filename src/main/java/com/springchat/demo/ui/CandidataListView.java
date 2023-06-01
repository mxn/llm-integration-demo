package com.springchat.demo.ui;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class CandidataListView extends VerticalLayout {
    public CandidataListView() {
        add(new Text("Welcome to MainView."));
        add();
    }
}
