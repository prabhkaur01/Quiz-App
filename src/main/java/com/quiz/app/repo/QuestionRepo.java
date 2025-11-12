package com.quiz.app.repo;

import com.quiz.app.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    // Custom finder to filter by topic
    List<Question> findByTopicIgnoreCase(String topic);
}
