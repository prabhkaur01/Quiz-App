package com.quiz.app.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @ElementCollection
    private List<String> options;
    private int correctAnswerIndex;
    private String topic;
    private int difficulty;

    public Question() {}

    public Question(String title, String description, List<String> options,
                    int correctAnswerIndex, String topic, int difficulty) {
        this.title = title;
        this.description = description;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.topic = topic;
        this.difficulty = difficulty;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getOptions() { return options; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getTopic() { return topic; }
    public int getDifficulty() { return difficulty; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setOptions(List<String> options) { this.options = options; }
    public void setCorrectAnswerIndex(int correctAnswerIndex) { this.correctAnswerIndex = correctAnswerIndex; }
    public void setTopic(String topic) { this.topic = topic; }
    public void setDifficulty(int difficulty) { this.difficulty = difficulty; }
}
