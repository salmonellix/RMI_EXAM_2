package server;

import common.Question;
import common.Exam;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

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
            //
            System.err.println("Server ready. register students");
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Scanner in = new Scanner(System.in);
            String ifStart = "NO";
            Date date2=null;
            String csvPath;
            String ifFinish = "" ;
            ArrayList<String> studentsIDs = new ArrayList<>();


            // get starting date
//            System.out.println("Enter the Starting Date");
//            String date = in.nextLine();
//            try {
//                date2 = ft.parse(date);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            System.out.println("EXAM DATE IS " +ft.format(date2));

            // create exam
            exam.makeExam();

            // wait for exam start
            Date d = new Date();
            System.out.println("WAITING");
//            while (!(ft.format(d)).equals(ft.format(date2))){
//                d = new Date();
//            }


            synchronized (exam) {
                System.out.println("Starting Exam!!!!!!");
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
