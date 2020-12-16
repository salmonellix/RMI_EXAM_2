package csvrw;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import common.Question;



public class CSVreader {
    private String path = "";
    int questionID;
    private ArrayList<Question> examQuestions = new ArrayList<Question> ();
    String questionText = "";
    ArrayList<String> choices = new ArrayList<String>();

    String correctAnswer = "";

    public CSVreader() throws FileNotFoundException{
    }


    public ArrayList<Question> createExam(String path){
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line = "";
            questionID = 0;



            while((line = br.readLine()) != null ){
                String[] values = line.split(";");
                String a1 = values[1];
                String a2 = values[2];
                String a3 = values[3];
                String a4 = values[4];
                questionText = values[0] + "\n" + a1 + "\n" + a2 + "\n" + a3 + "\n" + a4;
                correctAnswer = values[5];
                Question q1 = new Question(questionID,questionText,correctAnswer);
                examQuestions.add(q1);
                questionID ++;
            }

        } catch (IOException e){
            System.out.println("File not found ");

        }
        System.out.println("File loaded");

        return examQuestions;
    }
}