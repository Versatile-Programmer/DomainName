package com.rishi.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.rishi.model.DRM;
import com.rishi.model.DomainRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
@Repository
public interface DrmRepo extends JpaRepository<DRM, Integer>{

	Optional<DRM> findByDept(String dept);

	Optional<DRM> findByEmail(@Email String email);

	@NativeQuery("select * from dns_request dr where drm_id=:drmId")
	List<DomainRequest> findAllRequestedDomainsByDrmId(@Positive Integer drmId);

}
