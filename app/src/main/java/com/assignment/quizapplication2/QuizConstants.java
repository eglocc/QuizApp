package com.assignment.quizapplication2;

import java.util.List;

public interface QuizConstants {

    String NICKNAME = "nickname";
    String FROM_CATEGORY_TO_LOGIN = "from_category_to_login";
    String CLICKED_CATEGORY_POSITION = "clicked_category_position";
    String CLICKED_QUESTION_POSITION = "clicked_question_position";
    String CLICKED_QUESTION = "clicked_question";
    String QUESTION_LIST = "question_list";
    String CATEGORY_CLICKED = "category_clicked?";
    String QUESTION_ON = "question_on?";
    String POINTS_ON = "points_on?";
    String CATEGORY_COMPLETED_HAS_BEEN_SHOWED = "category_completed_has_been_showed";
    String QUIZ_FINISHED = "quiz_finished";


    int findUnAnsweredQuestion(int id, List<Question> list);

}
