package pl.ksikora.filmreviewerbackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/movies")
public class MovieController {

    @GetMapping("/popular")
    public String popular() {
        return "popular movies";
    }
}
