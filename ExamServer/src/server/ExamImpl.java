package server;
import common.Exam;
import common.Question;
import common.StudentInterface;
import constants.Constants;
import csvrw.CSVreader;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ExamImpl extends UnicastRemoteObject implements Exam{


    ArrayList<Question> questionList = new ArrayList<>();
    ArrayList<Question> answered = new ArrayList<>();
    ArrayList<String> studentsIDs = new ArrayList<>();
    Question question = null;
    HashMap<String, Double> studentsGrades = new HashMap<String, Double>();
    HashMap<String, StudentInterface> studentsInterfaces = new HashMap<String, StudentInterface>();




    public ExamImpl () throws RemoteException {
        super();

    }

    @Override
    public synchronized String getQuestion(int qID) throws RemoteException {
        try {
            Question c = questionList.get(qID);
            this.notify();
            String qTXT = c.getQuestionText();
            this.notify();
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
//        System.out.println(questionList.get(0).getQuestionText());
    }



    @Override
    public synchronized void sendAnswer(String answer, int questionID, String studentID) throws RemoteException{
        question = questionList.get(questionID);
        String goodAnswer = question.getCorrectAnswer();
        int points = 0;
        if ((answer.toLowerCase()).equals(goodAnswer.toLowerCase())){
            points = 1;
            studentsGrades.put(studentID, studentsGrades.get(studentID) + points);
        }
        this.notify();
    }
    public synchronized void registerInterface(String ID, StudentInterface student) throws RemoteException{
        synchronized (this) {
            if(studentsInterfaces.size() < Constants.maxStudents) {
                studentsInterfaces.put(ID, student);
                this.notify();
            }
        }
    }



    public synchronized void sendID(String ID) throws RemoteException{
        synchronized (this) {
            if(studentsInterfaces.size() < Constants.maxStudents) {
                //studentsInterfaces.put(ID, student);
                studentsIDs.add(ID);
                studentsGrades.put(ID, 0.0);
                this.notify();
            }
        }
    }

    public String toString(){
        return ("Students number: "+ studentsIDs.size());
    }
    @Override
    public int getNumStudent() throws RemoteException{
        return studentsIDs.size();
    }
    @Override
    public void notifyStart() throws RemoteException{
        List<StudentInterface> error_students = new ArrayList<>();
        for (StudentInterface c : studentsInterfaces.values()) {
            try{
                c.notifyStartS();
                this.notify();
            }catch(RemoteException e){
                System.out.println("Client is not reachable");
                error_students.add(c);
            }
        }
        for(StudentInterface c: error_students){
            this.studentsInterfaces.remove(c);
        }
    }

}