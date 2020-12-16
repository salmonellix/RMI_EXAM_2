package common;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Exam extends Remote {
    public String getQuestion(int qID) throws RemoteException;
    public ArrayList<Question> makeExam() throws RemoteException, FileNotFoundException;
    public void sendID(String ID) throws RemoteException;
    public int getNumStudent() throws RemoteException;
    public void sendAnswer(String answer, int questionID, String studentID) throws RemoteException;
    public void notifyStart() throws RemoteException;
    public void registerInterface(String ID, StudentInterface student) throws RemoteException;
}

