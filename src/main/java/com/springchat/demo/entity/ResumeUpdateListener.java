package com.springchat.demo.entity;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResumeUpdateListener {
    private static JmsTemplate jmsTemplate;
    private final Logger logger = LoggerFactory.getLogger(ResumeUpdateListener.class);

    @Autowired
    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        ResumeUpdateListener.jmsTemplate = jmsTemplate;
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    void onUpdate(Candidate candidate) {
        logger.warn(candidate.getLastName() + " is updated");
        jmsTemplate.convertAndSend("myDestination", toMap(candidate));
    }

    Map<String, Object> toMap(Candidate candidate) {
        Map<String, Object> res = new HashMap<>();
        res.put("entityClass", candidate.getClass().getName());
        res.put("id", candidate.getId());
        res.put("text", candidate.getResume());
        return res;
    }
}
