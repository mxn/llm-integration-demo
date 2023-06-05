package org.novomax.llm.integration.spring;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TextUpdateListener {
    private static ObjectProcessor objectProcessor;
    private final Logger logger = LoggerFactory.getLogger(TextUpdateListener.class);

    @Autowired
    public void setJmsTemplate(ObjectProcessor objectProcessor) {
        TextUpdateListener.objectProcessor = objectProcessor;
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    void onUpdate(Object object) {
        logger.warn(object + " is updated");
        objectProcessor.update(object);
    }
}