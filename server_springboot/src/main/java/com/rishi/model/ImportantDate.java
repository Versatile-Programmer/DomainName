package com.rishi.model;

import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportantDate {

	private LocalDate drmRequestedDate;
	private LocalDate armReviewedDate;
	private LocalDate hodReviewedDate;
	private LocalDate dateOfExpiry;
}
