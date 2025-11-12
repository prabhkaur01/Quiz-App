package com.quiz.app.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Attempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int totalQuestions;
    private int correct;
    private int score; // weighted by difficulty
    private long durationMs;

    private Instant createdAt = Instant.now();

    public Attempt() {}
    public Attempt(String username, int totalQuestions, int correct, int score, long durationMs) {
        this.username = username; this.totalQuestions = totalQuestions; this.correct = correct;
        this.score = score; this.durationMs = durationMs;
    }
    // getters/setters ...
}