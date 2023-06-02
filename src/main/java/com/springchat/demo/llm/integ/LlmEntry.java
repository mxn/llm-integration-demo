package com.springchat.demo.llm.integ;

import jakarta.persistence.*;

@Entity
@Table(name = "_LLMENTRY",
        uniqueConstraints = {
                @UniqueConstraint(name = "uniqueEntityClassId", columnNames = {LlmEntry.ENTITY_CLASS_FIELD,
                        LlmEntry.ENTITY_ID_FIELD})
        }
)
public class LlmEntry {
    static final String ENTITY_CLASS_FIELD = "TEXT_ENTITY_CLASS";
    static final String ENTITY_ID_FIELD = "TEXT_ENTITY_ID";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = ENTITY_CLASS_FIELD)
    private String textEntityClass;
    @Column(name = ENTITY_ID_FIELD)
    private String textEntityId;
    private String textHash;
    private double[] vector;

    public String getTextEntityClass() {
        return textEntityClass;
    }

    public void setTextEntityClass(String textEntityClass) {
        this.textEntityClass = textEntityClass;
    }

    public String getTextEntityId() {
        return textEntityId;
    }

    public void setTextEntityId(String textEntityId) {
        this.textEntityId = textEntityId;
    }

    public String getTextHash() {
        return textHash;
    }

    public void setTextHash(String textHash) {
        this.textHash = textHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double[] getVector() {
        return vector;
    }

    public void setVector(double[] vector) {
        this.vector = vector;
    }
}
