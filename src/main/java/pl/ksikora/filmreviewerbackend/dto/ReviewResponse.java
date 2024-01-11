package pl.ksikora.filmreviewerbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    @JsonProperty("movie_id")
    private Long movieId;
    private Integer score;
    private String title;
    private String overview;
    private Instant createdAt;
    private String nickname;
    private Long accountId;
}
