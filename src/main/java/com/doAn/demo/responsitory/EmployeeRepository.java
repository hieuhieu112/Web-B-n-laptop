package com.doAn.demo.responsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doAn.demo.entity.Employee;
import com.doAn.demo.entity.Product;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	@Query(value = "SELECT e from Employee e WHERE e.department.id = :id")
	List<Employee> findByDepartment(@Param("id")String departmentid);
	
	@Query(value = "SELECT e from Employee e WHERE e.empolyeetype.id = :id")
	List<Employee> findByType(@Param("id")String typeID);
}
