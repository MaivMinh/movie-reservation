package com.foolish.moviereservation.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class FileUploadResponse {
  private String filePath;
  private LocalDateTime uploadTime;
}
