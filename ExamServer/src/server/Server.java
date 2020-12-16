package server;

import common.Question;
import common.Exam;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    private static Registry startRegistry(Integer port)
            throws RemoteException {
        if(port == null) {
            port = 1099;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list( );
            // The above call will throw an exception
            // if the registry does not already exist
            return registry;
        }
        catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry= LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }
    public static void main(String[] args){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try{
            Exam exam = new ExamImpl();
            startRegistry(null);
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("WELCOME", exam);
            exam.makeExam();


            synchronized (exam) {
                while(true) {
                    System.out.println(exam);
                    exam.wait();
                }
            }

        }catch(Exception e){
            System.out.println(e);
        }
    }
}
