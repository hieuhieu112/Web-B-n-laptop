package com.doAn.demo.responsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doAn.demo.entity.Departments;

public interface DepartmentRepository extends JpaRepository<Departments, String> {

}
