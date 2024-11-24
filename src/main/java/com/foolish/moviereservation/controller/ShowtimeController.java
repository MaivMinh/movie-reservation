package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.ShowtimeDTO;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.service.ShowtimeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/showtimes")
public class ShowtimeController {
  private final ShowtimeService showtimeService;


  // Hàm thực hiên tìm kiếm Showtimes thông qua các tiêu chí - criteria.
  @PostMapping(value = "/search")
  public ResponseEntity<ResponseData> searchShowtimesByCriteria(@RequestBody Map<String, String> criteria, @RequestParam(defaultValue = "0") Integer pageNumber, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(name = "sort", required = false) String sort) {

    int size = pageSize < 1 ? 10 : pageSize;
    int number = pageNumber < 0 ? 0 : pageNumber - 1;
    Pageable pageable;

    if (sort != null) {
      // sort=id:desc,date:asc
      List<String> list = Arrays.stream(sort.split(",")).toList();
      List<Sort.Order> orders = new ArrayList<>();
      for (String element : list) {
        // Nếu fromString bị lỗi nó sẽ throw ra IllegalException và GlobalExceptionHandling sẽ catch nó trong RuntimeException.
        orders.add(new Sort.Order(Sort.Direction.fromString(element.split(":")[1].toUpperCase()), element.split(":")[0]));
      }

      pageable = PageRequest.of(number, size, Sort.by(orders));
    } else pageable = PageRequest.of(number, size);

    Page<ShowtimeDTO> result = showtimeService.getShowtimesByCriteria(criteria, pageable);
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", result));
  }


  @GetMapping("/{id}")
  public ResponseEntity<ResponseData> getShowtimeById(Integer id) {
    ShowtimeDTO showtimeDTO = showtimeService.getShowtimeDTOByIdOrElseThrow(id);
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", showtimeDTO));
  }

}
