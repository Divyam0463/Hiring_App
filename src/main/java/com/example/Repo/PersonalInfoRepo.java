package com.example.Repo;

import com.example.Model.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalInfoRepo extends JpaRepository<PersonalInfo, Long> {
}
