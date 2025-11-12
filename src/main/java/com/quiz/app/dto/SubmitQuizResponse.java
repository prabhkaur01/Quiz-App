package com.quiz.app.dto;

import java.util.List;

public record SubmitQuizResponse(
        int totalQuestions,
        int correct,
        int score,
        List<Result> results
) {
    public record Result(Long questionId, boolean correct, int correctIndex, int selectedIndex){}
}
