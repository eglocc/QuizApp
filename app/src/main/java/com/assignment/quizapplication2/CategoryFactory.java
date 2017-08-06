package com.assignment.quizapplication2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;

import static com.assignment.quizapplication2.QuestionBank.sMusicQuestions;
import static com.assignment.quizapplication2.QuestionBank.sMusicScores;

class CategoryFactory {

    public CategoryFactory() {
    }

    public Category createCategory(String name) {
        Category c = new Category(name);
        switch (name) {
            case "Music":
                ArrayList<Question> musicQuestionList = createQuestionList(c, sMusicQuestions, sMusicScores);
                musicQuestionList = setAnswersToQuestions(QuestionBank.getMusicAnswers(), musicQuestionList);
                c.setmQuestionList(musicQuestionList);
                return c;
            default:
                return null;
        }
    }

    public ArrayList<Question> createQuestionList(Category c, String[] questionTexts, int[] scores) {
        ArrayList<Question> questionList = new ArrayList<>();
        for (int i = 0; i < questionTexts.length; i++) {
            questionList.add(new Question(questionTexts[i], scores[i], c.toString(), i));
        }

        //actually unnecessary
        Collections.sort(questionList);

        return questionList;
    }

    public ArrayList<Question> setAnswersToQuestions(EnumMap<QuestionBank.Questions, HashMap<String, Boolean>> enumMap, ArrayList<Question> questions) {
        int id = 0;
        for (QuestionBank.Questions q : QuestionBank.Questions.values()) {
            questions.get(id).setmAnswerMap(enumMap.get(q));
            id++;
        }
        return questions;
    }
}
