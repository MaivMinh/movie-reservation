package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.SeatDTO;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.service.SeatService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/seats")
public class SeatController {
  private final SeatService seatService;

  @GetMapping("")
  public ResponseEntity<ResponseData> getSeatsByRoom(@RequestParam(value = "roomId", required = true) Integer roomId) {
    List<SeatDTO> result = seatService.findAllByRoomId(roomId);
    return ResponseEntity.ok(new ResponseData(200, "Success", result));
  }

}
