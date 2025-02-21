package com.rishi.dto;

import com.rishi.model.ARM;
import com.rishi.model.DRM;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
	
	

	private Integer id;
	
	private String name;
	
	private String email;

	private String dept;
	
	private String role;
	
	public LoginDTO(DRM drm) {
		this.id=drm.getId();
		this.name=drm.getName();
		this.email=drm.getEmail();
		this.dept=drm.getDept();
		this.role="ROLE_DRM";
		
	}
	
	public LoginDTO(ARM arm) {
		this.id=arm.getArmId();
		this.name=arm.getArmName();
		this.email=arm.getArmEmail();
		this.dept=arm.getDept();
		this.role="ROLE_ARM";
		
	}
}
