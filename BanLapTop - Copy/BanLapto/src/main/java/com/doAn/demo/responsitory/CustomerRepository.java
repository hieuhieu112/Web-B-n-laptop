package com.doAn.demo.responsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doAn.demo.entity.Customers;

public interface CustomerRepository extends JpaRepository<Customers, String>{

}
