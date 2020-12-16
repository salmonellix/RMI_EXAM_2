package common;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Exam extends Remote {
    public String getQuestion(int qID) throws RemoteException;
    public void sendAnswer(Question c) throws RemoteException;
    public ArrayList<Question> makeExam() throws RemoteException, FileNotFoundException;


}

