package com.example.Service;

import com.example.Mapper;
import com.example.Model.*;
import com.example.DTO.CandidateDTO;
import com.example.Repo.CandidateRepo;
import com.example.Repo.DocRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CandidateService {
    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private CandidateRepo candidateRepo ;

    @Autowired
    private DocRepo docRepo ;

    @Autowired
    private Mapper mapper;

    @Autowired
    private RedisService redisService ;

    @Transactional
    public void saveEntry(Candidate candidate){
        String raw_password = candidate.getPersonalInfo().getPassword() ;
        String encoded_password = passwordEncoder.encode(raw_password) ;
        candidate.getPersonalInfo().setPassword(encoded_password); //set the encoded password
        System.out.println(encoded_password);
        candidateRepo.save(candidate) ;
    }

    @Transactional(readOnly = true)
    public Candidate getCandidate_byId(Long id){
        log.info("getCandidate_byId() method is called !!!");
        Candidate candidate = candidateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("candidate not found with id: "+id));

        // Force initialization of lazy collections
        candidate.getDocuments().size(); // This triggers loading
        return candidate;
    }

    @Transactional(readOnly = true)
    public List<CandidateDTO> getCandidates(){
        List<CandidateDTO> cachedList = redisService.get("candidates", CandidateDTO.class) ;
        if (cachedList!= null){
            log.info("returning candidates from redis cache!!!");
            return cachedList ;
        }
        log.info("getAll endpoint called !!");
        List<Candidate> candidateList = candidateRepo.findAll() ;

        List<CandidateDTO> dtoList= candidateList.stream().map(mapper::toDTO).toList();

        redisService.set("candidates", dtoList, 3600L); // TTL -> 1 hr
        return  dtoList ;

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

    @Transactional(readOnly = true)
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

    @CacheEvict(value = "candidates", allEntries = true)
    @Transactional
    public String update_candidate(Long id, Candidate candidate){
        Candidate target_candidate = candidateRepo.findById(id).orElseThrow(
                () -> new IllegalArgumentException("candidate not found")) ; //candidate found with id
        target_candidate.setApplicationStatus(candidate.getApplicationStatus());

        candidateRepo.save(target_candidate) ;

        return "updated...kindly check now !" ;
    }


}
