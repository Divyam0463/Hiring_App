package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Candidate implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id ;

	private String name ;
	private String email ;
	private String phone ;

	@Enumerated(EnumType.STRING)
	private ApplicationStatus applicationStatus ;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "personal_info_id")
	private PersonalInfo personalInfo ;

	@OneToMany(mappedBy = "candidate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Doc> documents;

	//Admin or HR (for secure API access)
	private String role ;
}
