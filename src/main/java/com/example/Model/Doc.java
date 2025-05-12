package com.example.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Doc{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id ;

    private String fileName ;
    private String filePath ;

    @ManyToOne
    @JsonBackReference
    private Candidate candidate ;

}