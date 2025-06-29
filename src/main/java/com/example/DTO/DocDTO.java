package com.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocDTO {
    private Long id ;
    private String fileName ;
    private String filePath ;
    private CandidateDTO candidate;
}
