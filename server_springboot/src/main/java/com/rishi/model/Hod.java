package com.rishi.model;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
		name="hod",
		uniqueConstraints = @UniqueConstraint(columnNames = {"hodEmail"}))
public class Hod {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer hodId;
	
	@NotNull
	private String hodName;
	
	@Email
	private String hodEmail;
	
	@NotNull
	private String dept;
	
//	@OneToMany(mappedBy = "supervisor",cascade = CascadeType.ALL)
//	private Set<ARM> armsSupervised;
	
//	@OneToMany(mappedBy = "hod")
//	private Set<Integer> dnsRequestsIds;
}
