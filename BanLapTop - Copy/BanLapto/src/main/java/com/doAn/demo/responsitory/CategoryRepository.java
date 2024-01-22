package com.doAn.demo.responsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doAn.demo.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{
	
}
