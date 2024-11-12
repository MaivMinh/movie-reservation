package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.model.Genre;
import com.foolish.moviereservation.model.Movie;
import com.foolish.moviereservation.model.MovieGenre;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.service.GenreService;
import com.foolish.moviereservation.service.MovieService;
import jakarta.validation.constraints.NotNull;
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
@RequestMapping(value = "/api/v1/movies")
public class MovieController {
  private final MovieService movieService;

  // Hàm thực hiện chức năng tìm kiếm theo tiêu chí, sắp xếp và phân trang.
  @PostMapping(value = "/search")
  public ResponseEntity<ResponseData> getMoviesByCriteria(@RequestBody Map<String, String> criteria, @RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "pageNumber", required = false) Integer pageNumber, @RequestParam(value = "pageSize", required = false) Integer pageSize) {

    int pageNum = (pageNumber != null ? pageNumber : 1) - 1;
    int size = pageSize != null ? pageSize : 10;
    Pageable pageable = null;

    if (sort != null) {
      // sort=id:desc,releaseDate:asc
      List<String> list = Arrays.stream(sort.split(",")).toList();
      List<Sort.Order> orders = new ArrayList<>();
      for (String element : list) {
        // Nếu fromString bị lỗi nó sẽ throw ra IllegalException và GlobalExceptionHandling sẽ catch nó trong RuntimeException.
        orders.add(new Sort.Order(Sort.Direction.fromString(element.split(":")[1].toUpperCase()), element.split(":")[0]));
      }

      pageable = PageRequest.of(pageNum, size, Sort.by(orders));
    } else pageable = PageRequest.of(pageNum, size);
    Page<Movie> page = movieService.findByCriteria(criteria, pageable);
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", page));
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<ResponseData> getMovieDetails(@PathVariable Integer id) {
    /*
    * Response:
    * {
      "movie": {
          "movie_id": Integer,
          "name": String,
          "description": String,
          "trailer": String,
          "poster": String,
          "releaseDate": Date,
          * voteCount: Integer,
          * voteAverage: Integer,
      },
      "genres": [],
    * */

    Movie movie = movieService.findMovieByIdOrElseThrow(id);
    List<Genre> genres = movie.getMovieGenres().stream().map(MovieGenre::getGenre).toList();
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", Map.of("movie", movie, "genres", genres)));
  }
}
