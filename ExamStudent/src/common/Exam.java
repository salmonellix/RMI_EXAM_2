package common;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Exam extends Remote {
    public String getQuestion(int qID) throws RemoteException;
    public ArrayList<Question> makeExam() throws RemoteException, FileNotFoundException;
    public void sendID(String ID) throws RemoteException;
    public int getNumStudent() throws RemoteException;
    public void sendAnswer(String answer, int questionID, String studentID) throws RemoteException;
    public void notifyStart() throws RemoteException;
    public void registerInterface(String ID, StudentInterface student) throws RemoteException;
    public HashMap<String, Double> getGrades() throws RemoteException;
    public Boolean checkStart() throws RemoteException;
    public int questionsNumber() throws RemoteException;
    public Boolean notifyEnd() throws RemoteException;
    public void removeID(String ID) throws RemoteException;
}

