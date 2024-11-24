package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProvinceRepo extends JpaRepository<Province, Integer> {
  Optional<Province> findById(Integer id);
}
