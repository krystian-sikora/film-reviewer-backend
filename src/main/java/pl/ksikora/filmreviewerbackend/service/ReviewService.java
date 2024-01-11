package pl.ksikora.filmreviewerbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ksikora.filmreviewerbackend.dto.ReviewRequest;
import pl.ksikora.filmreviewerbackend.dto.ReviewResponse;
import pl.ksikora.filmreviewerbackend.entity.ReviewEntity;
import pl.ksikora.filmreviewerbackend.entity.UserEntity;
import pl.ksikora.filmreviewerbackend.repository.ReviewRepository;

import java.time.Instant;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;

    public ArrayList<ReviewResponse> getReviews(Long movieId) {
        return repository.getAllByTmdbId(movieId).stream().map(
                review -> ReviewResponse.builder()
                        .movieId(review.getTmdbId())
                        .score(review.getScore())
                        .title(review.getTitle())
                        .overview(review.getOverview())
                        .accountId(review.getUser().getId())
                        .nickname(review.getUser().getNickname())
                        .createdAt(review.getCreatedAt())
                        .build()
        ).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public ReviewResponse addReview(ReviewRequest request, UserEntity user) {
        ReviewEntity review = repository.save(ReviewEntity.builder()
                .user(user)
                .tmdbId(request.getMovieId())
                .score(request.getScore())
                .title(request.getTitle())
                .overview(request.getOverview())
                .createdAt(Instant.now())
                .build());

        return ReviewResponse.builder()
                .movieId(review.getTmdbId())
                .score(review.getScore())
                .title(review.getTitle())
                .overview(review.getOverview())
                .accountId(review.getUser().getId())
                .createdAt(review.getCreatedAt())
                .nickname(review.getUser().getNickname())
                .build();
    }
}
