package com.example.Service;

import com.example.DTO.BankInfoDTO;
import com.example.Model.Candidate;
import com.example.DTO.EducationalInfoDTO;
import com.example.Model.PersonalInfo;
import com.example.Repo.CandidateRepo;
import com.example.Repo.PersonalInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Personal_InfoService {

    @Autowired
    private CandidateRepo candidateRepo ;

    public String addPersonalInfoToCandidate(PersonalInfo personalInfo,Long id){
        Candidate candidate = candidateRepo.findById(id).orElseThrow(()-> new IllegalArgumentException("Candidate not found")) ;

        candidate.setPersonalInfo(personalInfo);

        candidateRepo.save(candidate) ;
        return "updated and saved" ;
    }

    //updating educationalInfo by mapping educationInfoDTO to the personalInfo
    public Candidate update_educationalInfo(Long id, EducationalInfoDTO educationalInfoDTO){
        Candidate candidate = candidateRepo.findById(id).orElseThrow(()->new IllegalArgumentException("candidate not found"));
        PersonalInfo personalInfo = candidate.getPersonalInfo() ;

        if (personalInfo == null) {
            personalInfo = new PersonalInfo(); //new object created and personalInfo is referring to the new object
            personalInfo.setCandidate(candidate);
            candidate.setPersonalInfo(personalInfo); //Now,candidate's personal-info set to new object
        }
        personalInfo.setEducation_information(educationalInfoDTO.getEducational_info());
        candidateRepo.save(candidate) ;
        return candidate ;
    }

    //updating  by mapping bankInfoDTO to the personalInfo
    public Candidate update_bankInfoDTO(Long id, BankInfoDTO bankInfoDTO){
        Candidate candidate = candidateRepo.findById(id).orElseThrow(()->new IllegalArgumentException("candidate not found"));
        PersonalInfo personalInfo = candidate.getPersonalInfo() ;

        if (personalInfo == null) {
            personalInfo = new PersonalInfo(); //new object created and personalInfo is referring to the new object
            personalInfo.setCandidate(candidate);
            candidate.setPersonalInfo(personalInfo); //Now,candidate's personal-info set to new object
        }
        personalInfo.setBank(bankInfoDTO.getBank());
        candidateRepo.save(candidate) ;
        return candidate ;
    }
}
