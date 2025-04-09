package org.example.bankcards.dao.repository;

import org.example.bankcards.dao.entity.CardLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardLimitRepository extends JpaRepository<CardLimit, Long> {

}
