package pl.ksikora.filmreviewerbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ksikora.filmreviewerbackend.entity.ReviewEntity;

import java.util.ArrayList;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    ArrayList<ReviewEntity> getAllByTmdbId(Long movieId);
    ArrayList<ReviewEntity> getAllByUserIdAndTmdbId(Long userId, Long movieId);

}
