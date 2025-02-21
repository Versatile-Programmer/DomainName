package com.rishi.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Status {
	private boolean activeStatus;
	
	private String armStatus;
	
	private String hodStatus;

}
