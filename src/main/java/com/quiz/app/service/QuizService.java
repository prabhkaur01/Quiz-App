package com.quiz.app.service;

import com.quiz.app.dto.StartQuizRequest;
import com.quiz.app.dto.StartQuizResponse;
import com.quiz.app.dto.SubmitQuizRequest;
import com.quiz.app.dto.SubmitQuizResponse;
import com.quiz.app.model.Question;
import com.quiz.app.repo.QuestionRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    private final QuestionRepo questionRepository;

    public QuizService(QuestionRepo questionRepository) {
        this.questionRepository = questionRepository;
    }

    // ✅ Start Quiz
    public StartQuizResponse startQuiz(StartQuizRequest request) {
        List<Question> questions = questionRepository.findAll();

        // Filter by topic if provided
        if (request.topic() != null && !request.topic().isEmpty()) {
            questions = questions.stream()
                    .filter(q -> request.topic().equalsIgnoreCase(q.getTopic()))
                    .toList();
        }

        // Filter by difficulty if provided
        if (request.difficulty() != null) {
            questions = questions.stream()
                    .filter(q -> q.getDifficulty() == request.difficulty())
                    .toList();
        }

        // Apply limit
        if (request.limit() != null && request.limit() > 0 && request.limit() < questions.size()) {
            questions = questions.subList(0, request.limit());
        }

        // Map to DTOs
        List<StartQuizResponse.QuestionDTO> questionDTOs = questions.stream()
                .map(q -> new StartQuizResponse.QuestionDTO(
                        q.getId(),
                        q.getTitle(),      // instead of getText()
                        q.getDescription(),
                        q.getOptions(),    // already a List<String>
                        q.getDifficulty(),
                        q.getTopic()
                ))
                .toList();


        return new StartQuizResponse(request.username(), System.currentTimeMillis(), questionDTOs);
    }

    // ✅ Submit Quiz
    public SubmitQuizResponse submitQuiz(SubmitQuizRequest request) {
        List<Question> questions = questionRepository.findAll();

        int correct = 0;
        List<SubmitQuizResponse.Result> results = new ArrayList<>();

        for (SubmitQuizRequest.Answer answer : request.answers()) {
            Question q = questions.stream()
                    .filter(que -> que.getId().equals(answer.questionId()))
                    .findFirst()
                    .orElse(null);

            if (q == null) {
                results.add(new SubmitQuizResponse.Result(answer.questionId(), false, -1, answer.selectedIndex()));
                continue;
            }

            int correctIndex = q.getCorrectAnswerIndex();
            boolean isCorrect = (answer.selectedIndex() == correctIndex);
            if (isCorrect) {
                correct++;
            }

            results.add(new SubmitQuizResponse.Result(q.getId(), isCorrect, correctIndex, answer.selectedIndex()));
        }

        int percentage = results.isEmpty() ? 0 : (correct * 100) / results.size();

        return new SubmitQuizResponse(results.size(), correct, percentage, results);
    }
}
