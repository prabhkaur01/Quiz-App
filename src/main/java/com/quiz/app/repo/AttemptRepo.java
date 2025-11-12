package com.quiz.app.repo;

import com.quiz.app.model.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttemptRepo extends JpaRepository<Attempt, Long> {
    // Get Top 20 players sorted by score (leaderboard)
    List<Attempt> findTop20ByOrderByScoreDesc();
}
