package com.example.DTO;

import com.example.Model.Candidate;
import com.example.Model.PersonalInfo;
import com.example.Repo.PersonalInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CandidateDetails implements UserDetails {
    private final PersonalInfo personalInfo ;
    private final Candidate candidate ;

    public CandidateDetails(Candidate candidate, PersonalInfo personalInfo){
        this.candidate = candidate ;
        this.personalInfo = personalInfo ;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+candidate.getRole()));
    }

    @Override
    public String getPassword() {
        return personalInfo.getPassword() ;
    }

    @Override
    public String getUsername() {
        return candidate.getName();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
