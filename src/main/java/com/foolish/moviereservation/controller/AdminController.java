package com.foolish.moviereservation.controller;

import com.azure.storage.blob.BlobClient;
import com.foolish.moviereservation.model.Movie;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.response.ResponseError;
import com.foolish.moviereservation.service.AzureBlobService;
import com.foolish.moviereservation.service.MovieService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
  private final AzureBlobService azureBlobService;
  private final MovieService movieService;

  @GetMapping(value = "/movies/{id}")
  public ResponseEntity<ResponseData> getMovieDetails(@PathVariable Integer id) {
    Movie movie = movieService.findMovieByIdOrElseThrow(id);
    ByteArrayResource resource = azureBlobService.readBlobFile(movie.getPoster());
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", resource.getByteArray()));
  }

  // Phương thức tạo ra một phim mới.
  @PostMapping(value = "/movies", consumes = {"multipart/form-data"})
  public ResponseEntity<ResponseData> createMovie(@RequestPart("movie") Movie movie, @RequestPart("poster") MultipartFile poster) {

    /*
    * {
      "movie": {
          "movie_id": Integer,
          "name": String,
          "description": String,
          "trailer": String,
          "releaseDate": Date
      },
      "poster": MultipartFile.
      }
    * */

    // Lưu poster lên AWS hoặc Azure. Sau đó lưu Movie mới vào hệ thống.
    String blobName = azureBlobService.writeBlobFile(poster);
    if (blobName == null || blobName.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Can't upload poster"));
    }
    movie.setPoster(blobName);
    Movie saved = movieService.save(movie);
    if (saved == null || saved.getId() <= 0) {
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Can't save movie"));
    }
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", null));
  }
}
