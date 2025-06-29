package com.example;

import com.example.Model.Candidate;
import com.example.DTO.CandidateDTO;
import com.example.DTO.DocDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapper {
    public CandidateDTO toDTO(Candidate candidate){
//        candidate.getDocuments().size() ; //force-loading of documents to ensure the lazy fields are
                                          // fetched before session closes

        CandidateDTO candidateDTO = new CandidateDTO() ;
        candidateDTO.setId(candidate.getId());
        candidateDTO.setName(candidate.getName());
        candidateDTO.setEmail(candidate.getEmail());
        candidateDTO.setDocuments(toDocDtoList(candidate));
        candidateDTO.setRole(candidate.getRole());
        candidateDTO.setApplicationStatus(candidate.getApplicationStatus());

        return candidateDTO ;
    }

    public List<DocDTO> toDocDtoList(Candidate candidate){
        List<DocDTO> docDTOList = candidate.getDocuments().stream()
                .map(doc -> new DocDTO(
                        doc.getId(),
                        doc.getFileName(),
                        doc.getFilePath(),
                        null
                )).toList();

        return docDTOList ;
    }
}
