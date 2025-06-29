package com.example.Repo;

import com.example.Model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidateRepo extends JpaRepository<Candidate, Long> {
    public Candidate findByName(String candidate) ;
    public Candidate findByEmail(String email) ;

    @Query("SELECT c FROM Candidate c LEFT JOIN FETCH c.documents WHERE c.id = :id")
    Optional<Object> findByIdWithDocuments(Long id);
}
