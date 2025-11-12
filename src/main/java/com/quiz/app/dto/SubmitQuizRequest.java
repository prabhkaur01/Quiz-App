package com.quiz.app.dto;

import java.util.List;

public record SubmitQuizRequest(
        String username,
        long sessionStartedAt,
        List<Answer> answers, // {questionId, selectedIndex}
        long durationMs
) {
    public record Answer(Long questionId, Integer selectedIndex){}
}
