package pl.ksikora.filmreviewerbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    @JsonProperty("movie_id")
    private Long movieId;
    private Integer score;
    private String title;
    private String overview;

}
