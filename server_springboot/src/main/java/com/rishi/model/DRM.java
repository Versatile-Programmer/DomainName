package com.rishi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
		name="drm",
		uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))

public class DRM {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	private String name;
	
	@Email
	private String email;
	
	@NotNull
	private String dept;
	
	//@OneToMany(mappedBy = "initiatingDrm")
//	@JoinTable(
//			name = "drm_domains",
////			joinColumns = @JoinColumn(name="drm_id"),
////			inverseJoinColumns = @JoinColumn(name="domain_id"))
	//@JsonIgnore
	//private Set<DomainRequest> domainsRequested;
	
	@ManyToOne
	@JoinColumn(name = "arm_id")
	@JsonIgnore
	private ARM supervisingArm;
	
	
}
