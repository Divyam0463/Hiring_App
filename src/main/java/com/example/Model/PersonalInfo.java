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
public class PersonalInfo {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id ;

    private String education_information ;
    private String bank ;
    private String password ;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Candidate candidate ;
}
