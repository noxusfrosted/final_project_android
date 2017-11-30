package com.project.noxusfrosted.quicktest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noxusfrosted on 11/12/2017.
 */


public class Quiz {

    private String quizName;

    private int questionNumber;

    private List<Question> questionList = new ArrayList<>();

    public void addQuestion(Question question) {
        this.questionList.add(question);
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public Question getQuestionByIndex(int index) {
        return questionList.get(index);
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }
}