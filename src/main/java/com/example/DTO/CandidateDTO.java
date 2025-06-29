package com.example.DTO;

import com.example.Model.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateDTO {
    private Long id;
    private String name;
    private String email;
    private ApplicationStatus applicationStatus ;
    private String role ;
    private List<DocDTO> documents;
}
