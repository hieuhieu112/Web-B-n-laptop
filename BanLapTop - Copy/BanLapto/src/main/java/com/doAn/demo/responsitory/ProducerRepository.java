package com.doAn.demo.responsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doAn.demo.entity.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Integer>{

}
