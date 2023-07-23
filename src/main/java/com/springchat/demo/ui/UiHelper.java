package com.springchat.demo.ui;

import com.vaadin.flow.router.BeforeEvent;

import java.util.Optional;

class UiHelper {
     private UiHelper() {
     }

     static Optional<Long> getId(BeforeEvent event) {
          return event.getRouteParameters().get("id").map(Long::valueOf);
     }
}
