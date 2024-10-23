package com.foolish.moviereservation.records;

public record ApiResponse(boolean flag, Integer code, String message, Object data) {

}
