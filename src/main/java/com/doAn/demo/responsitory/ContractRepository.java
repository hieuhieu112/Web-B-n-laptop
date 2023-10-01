package com.doAn.demo.responsitory;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doAn.demo.entity.Contracts;
import com.doAn.demo.entity.Product;

public interface ContractRepository extends JpaRepository<Contracts, Integer> {
	@Query(value = "SELECT c from Contracts c WHERE c.employee.id = :id")
	List<Contracts> findByEmployee(@Param("id")String employeeID);
	
	@Query(value = "SELECT c from Contracts c WHERE c.namecontract LIKE :keyword")
	List<Contracts> findByKeyWord(@Param("keyword") String keyword);
	
}
