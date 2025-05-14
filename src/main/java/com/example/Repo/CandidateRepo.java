package com.example.Repo;

import com.example.Model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepo extends JpaRepository<Candidate, Long> {
    public Candidate findByName(String candidate) ;
    public Candidate findByEmail(String email) ;
}
