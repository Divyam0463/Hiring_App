package com.example.Controller;

import com.example.Model.Candidate;
import com.example.Service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CandidateController {

    @Autowired
    private CandidateService candidateService ;

    @PostMapping
    public String addNormally(@RequestBody Candidate candidate){
        candidateService.saveEntry(candidate);
        return "saved" ;
    }

    @PostMapping("/api/{id}/status")
    public String addCandidates(@PathVariable Long id, @RequestBody Candidate candidate){
        Candidate target_candidate = candidateService.getCandidate_byId(id);
        // target_candidate found

        target_candidate.setApplicationStatus(candidate.getApplicationStatus());
        // update only the status

        candidateService.saveEntry(target_candidate);
        return "updated and saved" ;
    }

    @GetMapping("/api/candidates/offered")
    public List<Candidate> getCandidatesByHiredStatus(){
        List<Candidate> candidates_list = candidateService.getCandidates() ; //the whole candidates_list

        List<Candidate> hired_candidates = candidates_list.stream()
                .filter(candidate -> "offered".equalsIgnoreCase(candidate.getApplicationStatus().name()))
                .toList(); // this will print all the hired_candidates...

        return hired_candidates ;
    }

    @GetMapping("/api/candidates/{id}")
    public Candidate getCandidateById(@PathVariable Long id){
        return candidateService.getCandidate_byId(id) ;
    }

    @GetMapping("/api/candidates/count")
    public String get_candidate_count(){
        return candidateService.get_candidate_count() ;
    }

    @GetMapping
    public List<Candidate> getAllCandidates(){
        return candidateService.getCandidates() ;
    }

    @PutMapping("/api/candidates/{id}/onboard-status")
    public String update_candidate(@PathVariable Long id,@RequestBody Candidate candidate){
        return candidateService.update_candidate(id,candidate) ;
    }
}
