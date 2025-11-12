package com.quiz.app;

import com.quiz.app.model.Question;
import com.quiz.app.repo.QuestionRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class QuizApplication {
    public static void main(String[] args) {
        SpringApplication.run(QuizApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(QuestionRepo repo) {
        return args -> {
            if (repo.count() > 0) return;
            repo.saveAll(List.of(
                    new Question(
                            "What is the output of System.out.println(\"1\" + 2 + 3)?",
                            """
                            Consider Java operator evaluation. What prints?
                            """,
                            List.of("33", "123", "6", "Compilation error"),
                            1, "Java", 1
                    ),
                    new Question(
                            "Time Complexity of Binary Search",
                            "Given a sorted array, the worst-case time complexity of binary search is?",
                            List.of("O(n)", "O(log n)", "O(n log n)", "O(1)"),
                            1, "DSA", 1
                    ),
                    new Question(
                            "ConcurrentHashMap Property",
                            "Which is true about ConcurrentHashMap in Java?",
                            List.of(
                                    "Allows null keys and values",
                                    "Segmented locks in old impl; uses CAS & synchronized in newer JDKs",
                                    "Iteration is fail-fast",
                                    "Values are strongly consistent like a global lock"
                            ),
                            1, "Java", 2
                    ),
                    new Question(
                            "Stable Sorting",
                            "Which Java sort is stable?",
                            List.of("Arrays.sort(int[]) in OpenJDK", "Collections.sort(List<T>) (TimSort)", "QuickSort for objects", "ParallelSort for primitives"),
                            1, "Java", 2
                    ),
                    new Question(
                            "Graph",
                            "Minimum edges in a connected undirected graph with n nodes?",
                            List.of("n-1", "n", "n+1", "2n-1"),
                            0, "DSA", 2
                    )
            ));
        };
    }
}