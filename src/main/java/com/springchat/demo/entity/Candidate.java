package com.springchat.demo.entity;

import com.springchat.demo.llm.integ.LlmTextGetter;
import com.springchat.demo.llm.integ.TextUpdateListener;
import jakarta.persistence.*;

@Entity
@EntityListeners({TextUpdateListener.class})
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String resume;

    public Candidate(String firstNamem, String lastName) {
        this.firstName = firstNamem;
        this.lastName = lastName;
    }

    protected Candidate() {
        //JPA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @LlmTextGetter
    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
}
