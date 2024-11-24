package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.CinemaDTO;
import com.foolish.moviereservation.DTOs.ProvinceDTO;
import com.foolish.moviereservation.model.Province;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.service.CinemaService;
import com.foolish.moviereservation.service.ProvinceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/cinemas")
@AllArgsConstructor
public class CinemaController {
  private final CinemaService cinemaService;
  private final ProvinceService provinceService;

  @GetMapping(value = "/{id}")
  public ResponseEntity<ResponseData> getCinemaById(@PathVariable Integer id) {
    /*
    response-data: {
        cinema: CinemaDTO,
        provinces: Province[],
    }
    */

    CinemaDTO dto = cinemaService.getCinemaDTOByIdOrElseThrow(id);
    List<ProvinceDTO> provinces = provinceService.findAll();
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", Map.of("cinema", dto, "provinces", provinces)));
  }

  @GetMapping(value = "/search")
  public ResponseEntity<ResponseData> getCinemasByProvince(@RequestParam(value = "province_id") Integer provinceId) {
    /*
    response-data: {
      cinemas: CinemaDTO[]
    }
    */

    List<CinemaDTO> cinemas = cinemaService.getCinemasByProvinceId(provinceId);
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", Map.of("cinemas", cinemas)));
  }
}
