package pl.ksikora.filmreviewerbackend.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ksikora.filmreviewerbackend.dto.ReviewRequest;
import pl.ksikora.filmreviewerbackend.dto.ReviewResponse;
import pl.ksikora.filmreviewerbackend.facade.AuthenticationFacade;
import pl.ksikora.filmreviewerbackend.service.ReviewService;

import java.util.ArrayList;

@RestController()
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService service;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/add")
    public ResponseEntity<ReviewResponse> addReview(
            @RequestBody ReviewRequest request
    ) {
        return ResponseEntity.ok(service.addReview(request, authenticationFacade.getCurrentUser()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReview() {
        return ResponseEntity.ok("delete review");
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateReview() {
        return ResponseEntity.ok("update review");
    }

    @GetMapping("/get/{movieId}")
    public ResponseEntity<ArrayList<ReviewResponse>> getReviews(
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(service.getReviews(movieId));
    }

    @GetMapping("/verify/{movieId}")
    public ResponseEntity<ObjectNode> checkIfUserReviewedMovie(
            @PathVariable Long movieId
    ) {
        return ResponseEntity.ok(service.checkIfUserReviewedMovie(authenticationFacade.getCurrentUser(), movieId));
    }
}
