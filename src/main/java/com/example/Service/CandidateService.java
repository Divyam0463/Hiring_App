package com.example.Service;

import com.example.Model.*;
import com.example.Repo.CandidateRepo;
import com.example.Repo.DocRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CandidateService {
    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private CandidateRepo candidateRepo ;

    @Autowired
    private DocRepo docRepo ;

    public void saveEntry(Candidate candidate){
        String raw_password = candidate.getPersonalInfo().getPassword() ;
        String encoded_password = passwordEncoder.encode(raw_password) ;
        candidate.getPersonalInfo().setPassword(encoded_password); //set the encoded password
        System.out.println(encoded_password);
        candidateRepo.save(candidate) ;
    }

    public Candidate getCandidate_byId(Long id){
        return candidateRepo.findById(id).orElseThrow(() -> new RuntimeException("candidate not found with id: "+id) );
    }//for find a candidate by id only

    public List<Candidate> getCandidates(){
        return candidateRepo.findAll() ;
    }//for listing all the candidates

    public List<Doc> getDocs(){
        return docRepo.findAll() ;
    } //for listing all documents


    public String get_candidate_count(){
        List<Candidate> candidateList = candidateRepo.findAll() ;
        long cnt = candidateList.stream()
                .count() ;
        return "Total candidates: "+cnt ;
    }

    public String setStatus(Candidate candidate, ApplicationStatus applicationStatus){
        candidate.setApplicationStatus(applicationStatus);
        return candidate.getName() + " : status updated" ;
    }

    public String verify_candidate(Long id){
        Candidate candidate = candidateRepo.findById(id).orElseThrow(() ->
                new IllegalArgumentException("candidate not found"));

        List<Doc> documents = candidate.getDocuments();
        if(documents.isEmpty()){
            throw  new IllegalArgumentException("Candidate must submit required documents");
        }

        boolean hasResume = documents.stream().anyMatch(document -> "Resume".equalsIgnoreCase(document.getFileName())) ;
        //document-validation
        boolean hasID = documents.stream()
                .anyMatch(document ->  "ID".equalsIgnoreCase(document.getFileName()));
        //personal-info validation

        if (hasResume && hasID){
            return "candidate validated..." ;
        }
        else{
            throw new IllegalArgumentException("Candidate must contain the required documents.") ;
        }
    }

    public String update_candidate(Long id, Candidate candidate){
        Candidate target_candidate = candidateRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("candidate not found")) ; //candidate found with id
        target_candidate.setApplicationStatus(candidate.getApplicationStatus());

        candidateRepo.save(target_candidate) ;

        return "updated...kindly check now !" ;
    }


}
