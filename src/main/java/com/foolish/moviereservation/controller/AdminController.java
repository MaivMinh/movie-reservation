package com.foolish.moviereservation.controller;

import com.foolish.moviereservation.DTOs.CinemaDTO;
import com.foolish.moviereservation.mapper.CinemaMapperImpl;
import com.foolish.moviereservation.model.*;
import com.foolish.moviereservation.records.UpdatedMovie;
import com.foolish.moviereservation.response.ResponseData;
import com.foolish.moviereservation.response.ResponseError;
import com.foolish.moviereservation.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1/admin")
public class AdminController {
  private final AzureBlobService azureBlobService;
  private final MovieService movieService;
  private final GenreService genreService;
  private final CinemaService cinemaService;
  private final ProvinceService provinceService;
  private final CinemaMapperImpl cinemaMapperImpl;


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
    List<MovieGenre> movieGenres = genreIds.stream().map(id -> {
      MovieGenre movieGenre = new MovieGenre();
      movieGenre.setMovie(movie);
      Genre genre = genreService.findGenreByIdOrElseThrow(id);
      movieGenre.setGenre(genre);
      return movieGenre;
    }).collect(Collectors.toList());
    movie.setMovieGenres(movieGenres);
    Movie saved = movieService.save(movie);
    if (saved == null || saved.getId() <= 0) {
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Can't save movie"));
    }
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", saved));
  }

  // Phương thức update movie.
  @Transactional
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
      List<MovieGenre> movieGenres = genreIds.stream().map(genreId -> {
        Genre genre = genreService.findGenreByIdOrElseThrow(genreId);
        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setGenre(genre);
        movieGenre.setMovie(saved);
        return movieGenre;
      }).collect(Collectors.toList());
      saved.setMovieGenres(movieGenres);
    }
    movieService.save(saved); // Cập nhật lại phim vào DB.

    if (!map.isEmpty()) {
      return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(new ResponseData(HttpStatus.OK.value(), "Partially successful", map));
    }
    return ResponseEntity.noContent().build();
  }

  // Phương thức tạo mới Cinema.
  @PostMapping(value = "/cinemas")
  public ResponseEntity<ResponseData> createCinema(@RequestBody @NotNull CinemaDTO dto) {
    /*Thống nhất là ADMIN chỉ tạo các thông tin Cinema phía dưới và không có đính kèm thêm ảnh. Vì đơn giản là tại thời điểm tạo Cinema thì không có id để cho các Banner có thể refer tới.*/

    /*
     * Cinema:
     * {
     *   name: String,
     *   address: String,
     *   province: Integer
     * }
     * */

    Province province = null;
    try {
      province = provinceService.findByIdOrElseThrow(dto.getProvince().getId());
    } catch (NullPointerException e) {
      throw new RuntimeException("Can't find province");
    }
    Cinema cinema = new Cinema();
    cinema.setName(dto.getName());
    cinema.setAddress(dto.getAddress());
    cinema.setProvince(province);
    cinema.setBanners(new ArrayList<>());
    Cinema saved = cinemaService.save(cinema);
    if (saved == null || saved.getId() <= 0) {
      return ResponseEntity.ok(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Can't save cinema"));
    }
    dto = cinemaMapperImpl.toDTO(saved);
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", dto));
  }


  // Hàm tìm kiếm theo tiêu chí cho Role ADMIN. Chỉ thiết kế chức năng tìm kiếm và trả về theo dạng danh sách cho Role này.
  @PostMapping(value = "/cinemas/search")
  public ResponseEntity<ResponseData> searchCinemasByCriteria(@RequestBody @NotNull Map<String, String> criteria, @RequestParam(name = "sort", required = false) String sort, @RequestParam(name = "pageSize", required = false) Integer pageSize, @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {

    int pageNum = (pageNumber != null ? pageNumber : 1) - 1;
    int size = pageSize != null ? pageSize : 10;
    Pageable pageable = null;

    if (sort != null) {
      // sort=id:desc,name:asc
      List<String> list = Arrays.stream(sort.split(",")).toList();
      List<Sort.Order> orders = new ArrayList<>();
      for (String element : list) {
        // Nếu fromString bị lỗi nó sẽ throw ra IllegalException và GlobalExceptionHandling sẽ catch nó trong RuntimeException.
        orders.add(new Sort.Order(Sort.Direction.fromString(element.split(":")[1].toUpperCase()), element.split(":")[0]));
      }

      pageable = PageRequest.of(pageNum, size, Sort.by(orders));
    } else pageable = PageRequest.of(pageNum, size);

    Page<CinemaDTO> page = cinemaService.findCinemasByCriteria(criteria, pageable);
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", page));
  }

  // Phương thức lấy thông tin của một Cinema. Lưu ý, nếu như Role ADMIN thì sẽ trả về CinemaDTO có chứa ProvinceDTO.
  @GetMapping(value = "/cinemas/{id}")
  public ResponseEntity<ResponseData> getCinema(@PathVariable Integer id) {
    Cinema cinema = cinemaService.getCinemaByIdOrElseThrow(id);
    CinemaDTO data = cinemaMapperImpl.toDTO(cinema);
    return ResponseEntity.ok(new ResponseData(HttpStatus.OK.value(), "Success", data));
  }

  // Phương thức update Cinema
  @Transactional
  @PatchMapping(value = "/cinemas/{id}")
  public ResponseEntity<ResponseData> updateCinema(@PathVariable Integer id, @RequestBody @NotNull CinemaDTO dto) {
    Cinema cinema = cinemaService.getCinemaByIdOrElseThrow(id);
    if (StringUtils.hasText(dto.getName())) {
      cinema.setName(dto.getName());
    }
    if (StringUtils.hasText(dto.getAddress())) cinema.setAddress(dto.getAddress());
    if (dto.getProvince() != null)  cinema.setProvince(provinceService.findByIdOrElseThrow(dto.getProvince().getId()));

    Cinema saved = cinemaService.save(cinema);
    if (saved == null || saved.getId() <= 0) {
      return ResponseEntity.ok(new ResponseError(HttpStatus.BAD_REQUEST.value(), "Can't save cinema"));
    }
    return ResponseEntity.ok(new ResponseData(HttpStatus.NO_CONTENT.value(), "Success", null));
  }
}
