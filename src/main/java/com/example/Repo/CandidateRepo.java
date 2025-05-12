package com.example.Repo;

import com.example.Model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepo extends JpaRepository<Candidate, Long> {
    public Candidate findByname(String candidate) ;
}
