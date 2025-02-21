package com.rishi.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.stereotype.Repository;

import com.rishi.model.DomainRequest;

@Repository
public interface DomainRequestRepo extends JpaRepository<DomainRequest, Integer>{

	@NativeQuery("select * from dns_request dr where (dr.date_of_expiry-CURRENT_DATE)<=30")
	List<DomainRequest> findAllDomainsExpiringSoon();

//	@NativeQuery("update dns_request dr set dr.active_status=true and ")
//	List<DomainRequest> findActiveRequests();

}
