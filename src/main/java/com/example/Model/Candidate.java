package com.example.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Candidate {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;

	private String name ;
	private String email ;
	private String phone ;

	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus ;

	@OneToOne(cascade = CascadeType.ALL)
	private PersonalInfo personalInfo ;

	@OneToMany(mappedBy = "candidate",cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Doc> documents = new ArrayList<>();

	//Admin or HR (for secure API access)
	private String role ;
}
