package com.foolish.moviereservation.mapper;

import com.foolish.moviereservation.DTOs.CinemaDTO;
import com.foolish.moviereservation.model.Cinema;
import com.foolish.moviereservation.model.Province;
import lombok.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {ProvinceMapperImpl.class})
public interface CinemaMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "address", target = "address")

  /*
  * Ở @Mapping phía dưới, chúng ta mong muốn Mapping tử Province -> ProvinceDTO. Nhiệm vụ của chúng ta là chỉ cần Inject ProvinceMapperImpl dependency vào cho CinemaMapper. Sau đó, MapStruct sẽ tự động @Autowired ProvinceMapperImpl bên trong CinemaMapperImpl cho chúng ta và nó sẽ tạo ra operation cho việc mapping này.
  * */
  @Mapping(source = "province", target = "province")
  CinemaDTO toDTO(Cinema cinema);
}
