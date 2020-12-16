package student;

import common.StudentInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StudentImpl extends UnicastRemoteObject implements StudentInterface {


    public StudentImpl() throws RemoteException {
    }

    public void notifyStartS() throws RemoteException {
        System.out.println("The exam is going to start");
        synchronized (this) {
            this.notify();
        }
    }

    public void notifyEndS() throws RemoteException {
        System.out.println("Time is over - end exam");
        synchronized (this) {
            this.notify();

        }
    }
}
