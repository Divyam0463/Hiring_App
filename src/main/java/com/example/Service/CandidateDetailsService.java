package com.example.Service;

import com.example.Model.Candidate;
import com.example.DTO.CandidateDetails;
import com.example.Model.PersonalInfo;
import com.example.Repo.CandidateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CandidateDetailsService implements UserDetailsService {
    @Autowired
    private CandidateRepo candidateRepo ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Candidate target_candidate =  candidateRepo.findByName(username);

        if (target_candidate == null){
            throw new UsernameNotFoundException("Candidate not found") ;
        }

        PersonalInfo personalInfo = target_candidate.getPersonalInfo() ;

        System.out.println("Trying to authenticate user: " + username);
        System.out.println("DB password: " + personalInfo.getPassword());

        return new CandidateDetails(target_candidate,personalInfo) ;
    }
}
