package com.example.Controller;

import com.example.Config.EmailConfig;
import com.example.DTO.BankInfoDTO;
import com.example.DTO.EducationalInfoDTO;
import com.example.DTO.EmailStatus;
import com.example.Model.Candidate;
import com.example.Model.PersonalInfo;
import com.example.Repo.CandidateRepo;
import com.example.Service.Consumer_Service;
import com.example.Service.Mail_Service;
import com.example.Service.Personal_InfoService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class Personal_infoController {

    @Autowired
    private Personal_InfoService personalInfoService;

    @Autowired
    private Consumer_Service consumer_service ;

    @Autowired
    private Mail_Service mailService ;

    @Autowired
    private CandidateRepo candidateRepo ;

    @PutMapping("/api/candidates/{id}/personal-info")
    public String addPersonalInfoToCandidate(@RequestBody PersonalInfo personalInfo, @PathVariable Long id){
        return personalInfoService.addPersonalInfoToCandidate(personalInfo,id) ;
    }

    @PutMapping("/api/candidates/{id}/educational-info")
    public Candidate update_educationalInfo(@RequestBody EducationalInfoDTO educationalInfoDTO, @PathVariable Long id){
        return personalInfoService.update_educationalInfo(id, educationalInfoDTO) ;
    }

    @PutMapping("/api/candidates/{id}/bank-info")
    public Candidate update_bankInfo(@RequestBody BankInfoDTO bankInfoDTO, @PathVariable Long id){
        return personalInfoService.update_bankInfoDTO(id, bankInfoDTO) ;
    }

    //for rabbitmq -> publishing message to queue
    @PostMapping("/api/candidates/{id}/notify-job-offer")
    public void queuingMessages(@RequestBody EmailStatus emailStatus, @PathVariable Long id) throws MessagingException {
        Candidate candidate = candidateRepo.findById(id).orElseThrow(()->new IllegalArgumentException("candidate not found !")) ;
        consumer_service.sendEmailToQueue(emailStatus);
        mailService.sendEmail(emailStatus.getEmail(), candidate.getName(), candidate.getApplicationStatus().toString(),null);
        System.out.println("Mail successfully sent");
    }
}
