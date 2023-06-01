package com.springchat.demo.services;

import com.springchat.demo.entity.Candidate;
import com.springchat.demo.entity.CandidateRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
public class CandidateService {
    public static final String API_PREFIX = "api/candidate/";
    private final CandidateRepository candidateRepository;

    public CandidateService(final CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @GetMapping(path = API_PREFIX + "listAll", produces = {"application/hal+json"})
    public List<Candidate> listAll() {
        return candidateRepository.findAll();
    }

}
