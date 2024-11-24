package com.foolish.moviereservation.service;

import com.foolish.moviereservation.DTOs.ProvinceDTO;
import com.foolish.moviereservation.exceptions.ResourceNotFoundException;
import com.foolish.moviereservation.mapper.ProvinceMapper;
import com.foolish.moviereservation.mapper.ProvinceMapperImpl;
import com.foolish.moviereservation.model.Province;
import com.foolish.moviereservation.repository.ProvinceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProvinceService {
  private final ProvinceRepo provinceRepo;
  private final ProvinceMapperImpl provinceMapperImpl;

  public Province findByIdOrElseThrow(Integer id) {
    Optional<Province> result = provinceRepo.findById(id);
    return result.orElseThrow(() -> new ResourceNotFoundException("Province id not found", Map.of("province_id", String.valueOf(id))));
  }

  public List<ProvinceDTO> findAll() {
    List<Province> provinces = provinceRepo.findAll();
    return provinces.stream().map(provinceMapperImpl::toDTO).toList();
  }
}
