package com.example.Service;

import com.example.Model.Candidate;
import com.example.Model.Doc;
import com.example.Repo.CandidateRepo;
import com.example.Repo.DocRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DocService {
    @Autowired
    private DocRepo docRepo ;

    @Autowired
    private CandidateRepo candidateRepo ;

    public List<Doc> getDocuments(){
        return docRepo.findAll() ;
    }

    @Transactional
    public ResponseEntity<Doc> addDocumentToCandidate(Doc doc, Long id){
        try{
            Candidate candidate = candidateRepo.findById(id).orElseThrow(
                    () -> new IllegalArgumentException("Candidate not found")
            ) ;

            doc.setCandidate(candidate);
            Doc saved_doc = docRepo.save(doc) ; //saving in doc table

            candidate.getDocuments().add(saved_doc) ; //adding in the candidate
            candidateRepo.save(candidate) ; //saving in candidate table

            return new ResponseEntity<>(HttpStatus.ACCEPTED) ;
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;
        }
    }

    public boolean verify_document(Long id){
        Doc doc = docRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("document not found")) ;
        String regex = "^[\\w,\\s-]+\\.(pdf|docx|jpg|jpeg|png)$";
        Pattern pattern = Pattern.compile(regex) ;
        Matcher matcher = pattern.matcher(doc.getFileName());

        return matcher.matches() ;
    }

    public ResponseEntity<Doc> add_document_normally(Doc doc) {
        try{
            docRepo.save(doc) ;
            return new ResponseEntity<>(HttpStatus.OK) ;
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;
        }
    }
}
