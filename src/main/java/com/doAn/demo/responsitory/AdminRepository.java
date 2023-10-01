package com.doAn.demo.responsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doAn.demo.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

}
