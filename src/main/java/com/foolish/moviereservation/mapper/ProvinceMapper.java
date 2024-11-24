package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.DTOs.ProvinceDTO;
import com.foolish.moviereservation.model.Province;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProvinceMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  ProvinceDTO toDTO(Province province);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  Province toEntity(ProvinceDTO provinceDTO);
}
