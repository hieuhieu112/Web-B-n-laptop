package com.doAn.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Category;
import com.doAn.demo.responsitory.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;
	
	public Category findByID(Integer id) {
		return repo.findById(id).get();
	}
	
	public List<Category> findAll(){
		return repo.findAll();
	}
	
	public void deleteByID(Integer id) {
		repo.deleteById(id);
	}
	
	public Category save(Category category) {
		return repo.save(category);
	}
}
