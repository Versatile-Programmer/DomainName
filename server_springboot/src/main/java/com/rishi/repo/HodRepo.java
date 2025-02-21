package com.rishi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rishi.model.Hod;

@Repository
public interface HodRepo extends JpaRepository<Hod, Integer>{

}
