package com.foolish.moviereservation.repository;

import com.foolish.moviereservation.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CinemaRepo extends JpaRepository<Cinema, Integer>, JpaSpecificationExecutor<Cinema> {
  @Query(value = "select * from Cinemas C where C.province = :province", nativeQuery = true)
  List<Cinema> findCinemasByProvinceId(@Param("province") Integer province);
}
