package com.foolish.moviereservation.records;

import java.sql.Timestamp;

public record LoginResponse(String token_type, String access_token, Timestamp expires_in) {
}
