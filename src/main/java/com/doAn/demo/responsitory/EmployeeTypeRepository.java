package com.doAn.demo.responsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doAn.demo.entity.EmployeeTypes;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeTypes, String> {

}
