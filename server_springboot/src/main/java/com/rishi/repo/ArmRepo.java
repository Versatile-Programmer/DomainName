package com.rishi.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.rishi.model.ARM;
import com.rishi.model.DomainRequest;

@Repository
public interface ArmRepo extends JpaRepository<ARM, Integer>{

	@NativeQuery("select * from dns_request where arm_id=:armId")
	Optional<List<DomainRequest>> findRequestedDomainsByArmId(Integer armId);
	
	Optional<ARM>findByArmEmail(String email);

}
