package com.example.Controller;

import com.example.Mapper;
import com.example.Model.Candidate;
import com.example.DTO.CandidateDTO;
import com.example.Repo.CandidateRepo;
import com.example.Service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@EnableMethodSecurity
@RestController
public class CandidateController {

    @Autowired
    private CandidateService candidateService ;

    @Autowired
    private CandidateRepo candidateRepo ;

    @Autowired
    private Mapper mapper ;

    @PostMapping("/user/register")
    public String addNormally(@RequestBody Candidate candidate){
        candidateService.saveEntry(candidate);
        return "saved" ;
    }

    //admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/all-users")
    public List<CandidateDTO> getAllCandidates(){
        return candidateService.getCandidates() ;
    }


    //HR Only
    // Add @CacheEvict to controller methods that modify data
    @PreAuthorize("hasRole('HR')")
    @PostMapping("/api/{id}/status")
    @CacheEvict(value = "candidates", allEntries = true)
    public String addCandidates(@PathVariable Long id, @RequestBody Candidate candidate){
        Candidate target_candidate = candidateService.getCandidate_byId(id);
        target_candidate.setApplicationStatus(candidate.getApplicationStatus());
        target_candidate.setName(candidate.getName());
        target_candidate.setRole(candidate.getRole());
        candidateService.saveEntry(target_candidate);
        return "updated and saved" ;
    }

    //HR only
    @PreAuthorize("hasRole('HR')")
    @GetMapping("/api/candidates/offered")
    public List<CandidateDTO> getCandidatesByHiredStatus(){
        List<CandidateDTO> candidates_list = candidateService.getCandidates() ;

        List<CandidateDTO> hired_candidates = candidates_list.stream()
                .filter(candidate -> "offered".equalsIgnoreCase(candidate.getApplicationStatus().name()))
                .toList();

        return hired_candidates ;
    }

    //Admin only - THIS IS THE PROBLEMATIC ENDPOINT
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/candidates/{id}")
    @Transactional(readOnly = true) // Add transaction here
    public CandidateDTO getCandidateById(@PathVariable Long id){
        // Use repository method with fetch join
        Candidate candidate = candidateRepo.findById(id).orElseThrow(
                ()-> new RuntimeException("Candidate not found with id: "+id)
        ) ;

        return mapper.toDTO(candidate);

        // Alternative approach - use service method
        // return candidateMapper.toDTO(candidateService.getCandidate_byId(id));
    }

    //Admin only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/api/candidates/count")
    public String get_candidate_count(){
        return candidateService.get_candidate_count() ;
    }

    //admin and HR
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PutMapping("/api/candidates/{id}/onboard-status")
    public String update_candidate(@PathVariable Long id,@RequestBody Candidate candidate){
        return candidateService.update_candidate(id,candidate) ;
    }
}
