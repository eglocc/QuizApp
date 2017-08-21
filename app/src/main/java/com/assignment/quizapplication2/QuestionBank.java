package com.assignment.quizapplication2;

import java.util.EnumMap;
import java.util.HashMap;

import static com.assignment.quizapplication2.QuestionBank.Questions.QUESTION1;
import static com.assignment.quizapplication2.QuestionBank.Questions.QUESTION2;
import static com.assignment.quizapplication2.QuestionBank.Questions.QUESTION3;
import static com.assignment.quizapplication2.QuestionBank.Questions.QUESTION4;
import static com.assignment.quizapplication2.QuestionBank.Questions.QUESTION5;
import static com.assignment.quizapplication2.QuestionBank.Questions.QUESTION6;
import static com.assignment.quizapplication2.QuestionBank.Questions.QUESTION7;
import static com.assignment.quizapplication2.QuestionBank.Questions.QUESTION8;

class QuestionBank {

    //Music quiz answers
    enum Questions {
        QUESTION1,
        QUESTION2,
        QUESTION3,
        QUESTION4,
        QUESTION5,
        QUESTION6,
        QUESTION7,
        QUESTION8
    }

    static String[] sMusicQuestions = {
            "How many notes are there in musical alphabet?",
            "What is the name of the song from Titanic?",
            "What is the Beatles's biggest selling single?",
            "What is George Michael's first solo hit called?",
            "Which one of the following composers wrote the six Brandenburg Concertos?",
            "Which country won the Eurovision Song Contest in 2004?",
            "What does the musical term da capo mean?",
            "Who is the singer of Born Free?"};

    static String[] sHistoryQuestions = {
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""};

    static String[] sScienceQuestions = {
            "Which physicist came up with the theory of special relativity?",
            "Which Scientist found the way to synthesize Ammonia during WW1?",
            "Who is the famous British mathematician who also invented the computer?",
            "What is the IUPAC name of Glucose?",
            "Who is the discoverer of Radium?",
            "Who is the inventor of Alternating Current (AC)?",
            "What does LCD stand for?",
            "Who is the businessman/inventor that invented Light Bulbs?",
            "What does the Avogadro Constant stand for?",
            "What is the powerhouse of a cell?",
            "Which of these animals have a 4 chambered heart?",
            "What is the latin word for brain?"};

    static String[] sFoodQuestions = {
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""};

    static int[] sMusicScores = {100, 100, 200, 400, 300, 200, 400, 300};
    static int[] sHistoryScores = {200, 300, 100, 300, 400, 200, 400, 100, 400, 300};
    static int[] sScienceScores = {100, 400, 200, 100, 300, 100, 100, 200, 300, 200, 300, 400};
    static int[] sFoodScores = {100, 100, 200, 300, 200, 100, 400, 300};

    private static EnumMap<Questions, HashMap<String, Boolean>> eMusic = new EnumMap<Questions, HashMap<String, Boolean>>(Questions.class);

    private static final HashMap<String, Boolean> sMusicAnswers1 = new HashMap<String, Boolean>() {{
        put("key_6", false);
        put("key_7", true);
        put("key_8", false);
        put("key_9", false);
    }};

    private static final HashMap<String, Boolean> sMusicAnswers2 = new HashMap<String, Boolean>() {{
        put("My Heart Will Go On", true);
        put("Another Way To Die", false);
        put("Still Loving You", false);
        put("New York, New York", false);
    }};

    private static final HashMap<String, Boolean> sMusicAnswers3 = new HashMap<String, Boolean>() {{
        put("Love Me Do", false);
        put("Hey Jude", false);
        put("This Boy", false);
        put("I Wanna Hold Your Hand", true);
    }};

    private static final HashMap<String, Boolean> sMusicAnswers4 = new HashMap<String, Boolean>() {{
        put("One More Try", false);
        put("Father Figure", false);
        put("Careless Whisper", true);
        put("Roxanne", false);
    }};

    private static final HashMap<String, Boolean> sMusicAnswers5 = new HashMap<String, Boolean>() {{
        put("Johann Sebastian Bach", true);
        put("Fryderyk Franciszek Chopin", false);
        put("Franz Liszt", false);
        put("Wolfgang Amadeus Mozart", false);
    }};

    private static final HashMap<String, Boolean> sMusicAnswers6 = new HashMap<String, Boolean>() {{
        put("England", false);
        put("Ireland", false);
        put("Ukraine", true);
        put("Belgium", false);
    }};

    private static final HashMap<String, Boolean> sMusicAnswers7 = new HashMap<String, Boolean>() {{
        put("Slower", false);
        put("From the beginning", true);
        put("Faster", false);
        put("Lively", false);
    }};

    private static final HashMap<String, Boolean> sMusicAnswers8 = new HashMap<String, Boolean>() {{
        put("Matt Monro", false);
        put("Paul McCartney", true);
        put("Elvis Presley", false);
        put("Bob Marley", false);
    }};


    public static EnumMap<Questions, HashMap<String, Boolean>> getMusicAnswers() {
        eMusic.put(QUESTION1, sMusicAnswers1);
        eMusic.put(QUESTION2, sMusicAnswers2);
        eMusic.put(QUESTION3, sMusicAnswers3);
        eMusic.put(QUESTION4, sMusicAnswers4);
        eMusic.put(QUESTION5, sMusicAnswers5);
        eMusic.put(QUESTION6, sMusicAnswers6);
        eMusic.put(QUESTION7, sMusicAnswers7);
        eMusic.put(QUESTION8, sMusicAnswers8);
        return eMusic;
    }
}
