package pl.ksikora.filmreviewerbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_review")
public class ReviewEntity {

    @Id @GeneratedValue
    private Long id;
    @ManyToOne() @JoinColumn(name = "user_id")
    private UserEntity user;
    @NotNull
    private Integer score;
    private Long tmdbId;
    private String title;
    private String overview;
    private Instant createdAt;
}
