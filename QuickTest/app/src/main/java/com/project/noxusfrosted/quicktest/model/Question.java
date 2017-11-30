package com.project.noxusfrosted.quicktest.model;

/**
 * Created by Noxusfrosted on 11/12/2017.
 */

public class Question {
    private String nameQuestion;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String correctAnswer;


    public Question(String nameQuestion, String choice1, String choice2, String choice3, String choice4, String correctAnswer) {
        this.nameQuestion = nameQuestion;
        this.choice1 = choice1;
        this.choice2 = choice2;
        this.choice3 = choice3;
        this.choice4 = choice4;
        this.correctAnswer = correctAnswer;
    }

    public String getNameQuestion() {
        return nameQuestion;
    }

    public void setNameQuestion(String nameQuestion) {
        this.nameQuestion = nameQuestion;
    }

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getChoice4() {
        return choice4;
    }

    public void setChoice4(String choice4) {
        this.choice4 = choice4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}