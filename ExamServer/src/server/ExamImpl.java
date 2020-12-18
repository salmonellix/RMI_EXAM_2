package server;
import common.Exam;
import common.Question;
import common.StudentInterface;
import constants.Constants;
import csvrw.CSVreader;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ExamImpl extends UnicastRemoteObject implements Exam{


    ArrayList<Question> questionList = new ArrayList<>();
    ArrayList<Question> answered = new ArrayList<>();
    ArrayList<String> studentsIDs = new ArrayList<>();
    Question question = null;
    HashMap<String, Double> studentsGrades = new HashMap<String, Double>();
    HashMap<String, StudentInterface> studentsInterfaces = new HashMap<String, StudentInterface>();
    boolean ifStarted = false;




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

    public synchronized void saveGrades() throws IOException {
        try{
            Scanner in = new Scanner(System.in);
            System.out.println("Give path to save csv with exam grades");
            String csvPath = null;
            csvPath = in.nextLine();
            FileWriter writer;
            writer = new FileWriter(csvPath, true);
            for (String i : studentsGrades.keySet()){
                writer.write(i);
                writer.write(";");
                writer.write(studentsGrades.get(i).toString());
                writer.write("\r\n");
            }
            writer.close();
        }catch (IOException ignored){
    }}




    @Override
    public synchronized void sendAnswer(String answer, int questionID, String studentID) throws RemoteException{
        try {
            question = questionList.get(questionID);
            String goodAnswer = question.getCorrectAnswer();
            int points = 0;
            if ((answer.toLowerCase()).equals(goodAnswer.toLowerCase())) {
                points = 1;
                studentsGrades.put(studentID, studentsGrades.get(studentID) + points);
            }
            this.notify();
        } catch (Exception e) {
            this.notify();
        }
    }
    public synchronized void registerInterface(String ID, StudentInterface student) throws RemoteException{
        synchronized (this) {
            if(studentsInterfaces.size() < Constants.maxStudents) {
                studentsInterfaces.put(ID, student);
                this.notify();
            }
        }
    }


    public synchronized void removeID(String ID) throws RemoteException{
        synchronized (this) {
            if(studentsIDs.size() < Constants.maxStudents) {
                studentsIDs.remove(ID);
                this.notify();
            }
        }
    }


    public synchronized void sendID(String ID) throws RemoteException{
        try {
            if(studentsInterfaces.size() < Constants.maxStudents) {
                //studentsInterfaces.put(ID, student);
                studentsIDs.add(ID);
                studentsGrades.put(ID, 0.0);
                this.notify();
            }
        } catch (Exception e) {
            this.notify();
        }
    }

    public String toString(){
        return ("Students number: "+ studentsIDs.size());
    }
    @Override
    public synchronized int getNumStudent() throws RemoteException{
        this.notify();
        return studentsIDs.size();
    }

    public synchronized HashMap<String, Double> getGrades() throws RemoteException{
        try{
            for (String i : studentsGrades.keySet()){
                System.out.println(i + "Grade: " + studentsGrades.get(i));
            }
            this.notify();
            return studentsGrades;

        } catch (Exception e) {
            this.notify();
            return null;
        }
    }



    @Override
    public void notifyStart() throws RemoteException{
        ifStarted = true;
        //this.notify();
    }

    public void notifyEnd() throws RemoteException{
        ifStarted = false;
        //this.notify();
    }

    public boolean checkStart() throws RemoteException{
        //this.notify();
        return ifStarted;

    }

    public synchronized int questionsNumber() throws RemoteException{
        try{
            int size;
            size = questionList.size();
            this.notify();
            return size;
        } catch (Exception e) {
            this.notify();
            return 0;
        }


    }


}