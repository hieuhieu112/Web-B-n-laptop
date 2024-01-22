package com.doAn.demo.responsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doAn.demo.entity.Role;

public interface RoleResponsitory extends JpaRepository<Role, Integer>{
	
}
