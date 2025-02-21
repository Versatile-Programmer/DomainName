package com.rishi.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rishi.model.AppUser;

import jakarta.validation.constraints.Email;

@Repository
public interface AppUserRepo  extends JpaRepository<AppUser, Integer>{

	Optional<AppUser> findByEmail(@Email String email);

}
