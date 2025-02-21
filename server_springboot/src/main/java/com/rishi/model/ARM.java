package com.rishi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
		name="arm",
		uniqueConstraints = @UniqueConstraint(columnNames = {"armEmail"}))
public class ARM {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer armId;
	
	@NotNull
	private String armName;
	
	@NotNull
	@Email
	private String armEmail;
	
	@NotNull
	private String dept;
	
	@ManyToOne
	@JoinColumn(name="hod_id",nullable = false)
	@JsonBackReference // Prevents infinite recursion
	private Hod supervisor;
	
	
//	   @OneToMany(mappedBy = "arm")
//	   @JsonIgnore
//	  private Set<DomainRequest> dnsRequests; 
//	   @OneToMany(mappedBy ="supervisingArm")
//	   @JsonIgnore
//	   private Set<DRM> supervisedDrms;
	 
	
}
