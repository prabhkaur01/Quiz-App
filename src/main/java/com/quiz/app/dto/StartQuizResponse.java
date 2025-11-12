package com.quiz.app.dto;

import java.util.List;

public record StartQuizResponse(
        String username,
        long sessionStartedAt,
        List<QuestionDTO> questions
) {
    public record QuestionDTO(Long id, String title, String description, java.util.List<String> choices, Integer difficulty, String topic) {}
}
