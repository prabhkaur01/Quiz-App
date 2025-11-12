package com.quiz.app.dto;

public record StartQuizRequest(
        String username,
        String topic,
        Integer difficulty,  // ✅ Use Integer not String
        Integer limit
) {}
