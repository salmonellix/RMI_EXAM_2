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


    ArrayList<Question> questionList = new ArrayList<>();
    ArrayList<Question> answered = new ArrayList<>();
    ArrayList<String> studentsIDs = new ArrayList<>();




    public ExamImpl () throws RemoteException {
        super();

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
    public synchronized void makeExam() throws RemoteException, FileNotFoundException {
        Scanner in = new Scanner(System.in);
        System.out.println("Give path to csv with exam questions");
        String csvPath = null;
        csvPath = in.nextLine();
        CSVreader mycsv = new CSVreader();
        questionList = mycsv.createExam(csvPath);
        System.out.println(questionList.get(0).getQuestionText());
    }



    @Override
    public synchronized void sendAnswer(Question c) throws RemoteException{
        this.answered.add(c);
        this.notify();
    }

    @Override
    public synchronized void sendID(String ID) throws RemoteException{
        studentsIDs.add(ID);
        this.notify();
    }

    public String toString(){
        return ("Students number: "+ studentsIDs.size());
    }

    public int getNumStudent() throws RemoteException{
        return studentsIDs.size();
    }
}