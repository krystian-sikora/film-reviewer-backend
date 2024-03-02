package pl.ksikora.filmreviewerbackend.review;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    ArrayList<ReviewEntity> getAllByTmdbId(Long movieId);
    ArrayList<ReviewEntity> getAllByUserIdAndTmdbId(Long userId, Long movieId);
    @Transactional
    ArrayList<ReviewEntity> deleteAllByUserIdAndTmdbId(Long userId, Long movieId);
}
