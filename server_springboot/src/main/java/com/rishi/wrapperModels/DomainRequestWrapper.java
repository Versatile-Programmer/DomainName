package com.rishi.wrapperModels;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DomainRequestWrapper {

	@NotNull
	private String domainName;
	
	@NotNull
	private Integer drmId;
	
	private LocalDate neededTillDate;
	

	private String dept;
	
	
}
