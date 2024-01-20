package pl.ksikora.filmreviewerbackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ksikora.filmreviewerbackend.dto.ReviewRequest;
import pl.ksikora.filmreviewerbackend.dto.ReviewResponse;
import pl.ksikora.filmreviewerbackend.entity.ReviewEntity;
import pl.ksikora.filmreviewerbackend.entity.UserEntity;
import pl.ksikora.filmreviewerbackend.repository.ReviewRepository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository repository;
    private final ObjectMapper objectMapper;

    public ArrayList<ReviewResponse> getReviews(Long movieId) {
        return repository.getAllByTmdbId(movieId).stream().map(
                review -> ReviewResponse.builder()
                        .movieId(review.getTmdbId())
                        .score(review.getScore())
                        .title(review.getTitle())
                        .content(review.getContent())
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
                .content(request.getContent())
                .createdAt(Instant.now())
                .build());

        return ReviewResponse.builder()
                .movieId(review.getTmdbId())
                .score(review.getScore())
                .title(review.getTitle())
                .content(review.getContent())
                .accountId(review.getUser().getId())
                .createdAt(review.getCreatedAt())
                .nickname(review.getUser().getNickname())
                .build();
    }

    public ObjectNode checkIfUserReviewedMovie(UserEntity currentUser, Long movieId) {

        final ObjectNode node = objectMapper.createObjectNode();

        Optional<ReviewResponse> userReview = repository.getAllByUserIdAndTmdbId(currentUser.getId(), movieId).stream()
                .findFirst()
                .map(
                        review -> ReviewResponse.builder()
                                .movieId(review.getTmdbId())
                                .score(review.getScore())
                                .title(review.getTitle())
                                .content(review.getContent())
                                .accountId(review.getUser().getId())
                                .nickname(review.getUser().getNickname())
                                .createdAt(review.getCreatedAt())
                                .build()
                );

        if (userReview.isEmpty()) {
            node.put("user_reviewed", false);
            return node;
        }

        JsonNode reviewNode = objectMapper.valueToTree(userReview.get());

        node.put("user_reviewed", true);
        node.set("review", reviewNode);
        return node;
    }

    public ArrayList<ReviewResponse> deleteReview(UserEntity currentUser, Long movieId) {
        return repository.deleteAllByUserIdAndTmdbId(currentUser.getId(), movieId).stream().map(
                review -> ReviewResponse.builder()
                        .movieId(review.getTmdbId())
                        .score(review.getScore())
                        .title(review.getTitle())
                        .content(review.getContent())
                        .accountId(review.getUser().getId())
                        .nickname(review.getUser().getNickname())
                        .createdAt(review.getCreatedAt())
                        .build()
        ).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}
