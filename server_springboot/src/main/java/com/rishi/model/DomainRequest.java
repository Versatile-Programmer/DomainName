package com.rishi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
		name="dns_request",
		uniqueConstraints = @UniqueConstraint(columnNames = {"domainName"}))
public class DomainRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer requestId;
	
	@NotNull
	private String domainName;
	
	private Long daysLeftTillExpiry;
	
	@Embedded
	private Status status;
	
	@Embedded
	private ImportantDate dates;
	
	@ManyToOne
	@JoinColumn(name = "arm_id")
	@JsonIgnore
	private ARM arm;
	
	@ManyToOne
	@JoinColumn(name="hod_id")
	@JsonIgnore
	private Hod hod;
	
	@ManyToOne
	@JoinColumn(name="drm_id")
	@JsonIgnore
	private DRM initiatingDrm;
	
}
