package common;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable{

    private final int questionID;
    private final String questionText;
    private final String correctAnswer;

    public Question(int questionID, String questionText, String correctAnswer){
        this.questionID = questionID;
        this. questionText = questionText;
        this.correctAnswer = correctAnswer;
    }
    public int getQuestionID(){
        return questionID;
    }

    public String getQuestionText(){
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }


}