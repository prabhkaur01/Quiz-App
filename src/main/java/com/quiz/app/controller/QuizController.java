package com.quiz.app.controller;

import com.quiz.app.dto.StartQuizRequest;
import com.quiz.app.dto.StartQuizResponse;
import com.quiz.app.dto.SubmitQuizRequest;
import com.quiz.app.dto.SubmitQuizResponse;
import com.quiz.app.service.QuizService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
@CrossOrigin(origins = "*")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // ✅ Start Quiz
    @PostMapping("/start")
    public StartQuizResponse startQuiz(@RequestBody StartQuizRequest request) {
        return quizService.startQuiz(request);
    }

    // ✅ Submit Quiz
    @PostMapping("/submit")
    public SubmitQuizResponse submitQuiz(@RequestBody SubmitQuizRequest request) {
        return quizService.submitQuiz(request);
    }
}
