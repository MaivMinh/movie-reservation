package com.foolish.moviereservation.controller;

import com.azure.storage.blob.BlobClient;
import com.foolish.moviereservation.model.Genre;
import com.foolish.moviereservation.model.Movie;
import com.foolish.moviereservation.model.MovieGenre;
import com.foolish.moviereservation.records.UpdatedMovie;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.response.ResponseError;
import com.foolish.moviereservation.service.AzureBlobService;
import com.foolish.moviereservation.service.GenreService;
import com.foolish.moviereservation.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
  private final AzureBlobService azureBlobService;
  private final MovieService movieService;
  private final GenreService genreService;


  // Phương thức tạo ra một phim mới.
  @PostMapping(value = "/movies", consumes = {"multipart/form-data"})
  public ResponseEntity<ResponseData> createMovie(@RequestPart("movie") Movie movie, @RequestPart("poster") MultipartFile poster, @RequestPart("genres") List<Integer> genreIds) {

    /*
    * {
      "movie": {
          "movie_id": Integer,
          "name": String,
          "description": String,
          "trailer": String,
          "releaseDate": Date
      },
      "poster": MultipartFile,
      "genres": []
      }
    * */

    // Lưu poster lên AWS hoặc Azure. Sau đó lưu Movie mới vào hệ thống.
    String url = azureBlobService.writeBlobFile(poster);
    if (url == null || url.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Can't upload poster"));
    }
    movie.setPoster(url);
    Set<MovieGenre> movieGenres = genreIds.stream().map(id -> {
      MovieGenre movieGenre = new MovieGenre();
      movieGenre.setMovie(movie);
      Genre genre = genreService.findGenreByIdOrElseThrow(id);
      movieGenre.setGenre(genre);
      return movieGenre;
    }).collect(Collectors.toSet());
    movie.setMovieGenres(movieGenres);
    Movie saved = movieService.save(movie);
    if (saved == null || saved.getId() <= 0) {
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Can't save movie"));
    }
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", null));
  }

  // Phương thức update movie.
  @PatchMapping(value = "/movies/{id}", consumes = {"multipart/form-data"})
  public ResponseEntity<ResponseData> updateMovie(@RequestPart(value = "movie", required = false) @Valid UpdatedMovie movie, @RequestPart(value = "poster", required = false) MultipartFile poster, @RequestPart(value = "genres", required = false) List<Integer> genreIds, @PathVariable Integer id) {
    /* Dữ liệu update tuỳ thuộc vào client.
    * "movie": {
          "name": String,
          "description": String,
          "trailer": String,
          "releaseDate": Date,
      }
    *
    * */

    Movie saved = movieService.findMovieByIdOrElseThrow(id);
    Map<String, String> map = new HashMap<>();


    if (movie != null) {
      if (movie.getName() != null && !movie.getName().isEmpty()) {
        saved.setName(movie.getName());
      }
      if (movie.getDescription() != null && !movie.getDescription().isEmpty()) {
        saved.setDescription(movie.getDescription());
      }
      if (movie.getTrailer() != null && !movie.getTrailer().isEmpty()) {
        saved.setTrailer(movie.getTrailer());
      }
      if (movie.getReleaseDate() != null) {
        saved.setReleaseDate(movie.getReleaseDate());
      }
    }
    if (poster != null) {
      String url = azureBlobService.writeBlobFile(poster);
      if (url == null || url.isEmpty()) {
        map.put("poster", "Can't upload poster");
      } else {
        azureBlobService.deleteBlobFile(saved.getPoster());
        saved.setPoster(url);
      }
    }
    if (genreIds != null) {
      Set<MovieGenre> movieGenres = genreIds.stream().map(genreId -> {
        Genre genre = genreService.findGenreByIdOrElseThrow(genreId);
        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setGenre(genre);
        movieGenre.setMovie(saved);
        return movieGenre;
      }).collect(Collectors.toSet());
      saved.setMovieGenres(movieGenres);
    }
    movieService.save(saved); // Cập nhật lại phim vào DB.

    if (!map.isEmpty()) {
      return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(new ResponseData(HttpStatus.OK.value(), "Partially successful", map));
    }
    return ResponseEntity.noContent().build();
  }

}
