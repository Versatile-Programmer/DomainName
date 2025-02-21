package com.rishi.wrapperModels;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DrmWrapper {
	
	@NotNull
	private String drmName;
	@Email
	private String drmEmail;
	@NotNull
	private String drmPassword;
	@NotNull
	private String drmDept;

}
