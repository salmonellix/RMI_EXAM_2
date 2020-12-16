package common;

import java.rmi.RemoteException;

public interface StudentInterface {

    public void notifyEndS() throws RemoteException;
    public void notifyStartS() throws RemoteException;


}