package com.doAn.demo.responsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doAn.demo.entity.Bonus;

public interface BonusRespository extends JpaRepository<Bonus, Integer> {

}
