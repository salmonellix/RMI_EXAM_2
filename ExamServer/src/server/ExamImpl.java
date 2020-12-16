package server;
import common.Exam;
import common.Question;
import csvrw.CSVreader;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;

public class ExamImpl extends UnicastRemoteObject implements Exam{


    ArrayList<Question> questionList = null;
    ArrayList<Question> answered = null;



    public ExamImpl () throws RemoteException {
        super();
        this.questionList = new ArrayList<>();
        this.answered = new ArrayList<>();

    }

    @Override
    public synchronized String getQuestion(int qID) throws RemoteException {
        try {
            Question c = questionList.get(qID);
            this.notify();
            String qTXT = c.getQuestionText();
            return qTXT;
        }catch(Exception e){
            this.notify();
            return null;
        }
    }
    @Override
    public synchronized ArrayList<Question> makeExam() throws RemoteException, FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.println("Give path to csv with exam questions");
        String csvPath = null;
        csvPath = in.nextLine();
        CSVreader mycsv = new CSVreader();
        questionList = mycsv.createExam(csvPath);
        return questionList;
    }



    @Override
    public synchronized void sendAnswer(Question c) throws RemoteException{
        this.answered.add(c);
        this.notify();
    }

    public String toString(){
        return ("Questions");
    }
}