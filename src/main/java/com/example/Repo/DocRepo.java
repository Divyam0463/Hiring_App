package com.example.Repo;

import com.example.Model.Candidate;
import com.example.Model.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocRepo extends JpaRepository<Doc,Long> {
}
