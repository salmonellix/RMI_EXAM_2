package server;

import common.Question;
import common.Exam;
import constants.Constants;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Server {

    private static Registry startRegistry(Integer port)
            throws RemoteException {
        if(port == null) {
            port = Constants.server1Port;
        }

        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list( );
            return registry;
        }
        catch (RemoteException ex) {
            System.out.println("RMI registry cannot be located ");
            Registry registry= LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port: " + port);
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
            System.err.println("Server ready. register students");
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Scanner in = new Scanner(System.in);
            String ifStart = "NO";
            Date date2=null;
            String csvPath;
            String ifFinish = "" ;
            ArrayList<String> studentsIDs = new ArrayList<>();
            HashMap<String, Double> studentGrades = new HashMap<>();


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

            // notify about start
            registry.bind("EXAM STARTED", exam);

            synchronized (exam) {
                exam.notifyStart();
                //measure time
                long start = System.currentTimeMillis();
                long elapsedTimeMillis = System.currentTimeMillis() - start;
                float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);

                if (exam.getNumStudent() != 0) {

                    while (elapsedTimeMin < Constants.examTime) {


                        elapsedTimeMillis = System.currentTimeMillis() - start;
                        elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);

                        try {
                            ifFinish = in.nextLine();
                            if (ifFinish.toUpperCase().equals("FINISH")) {
                                elapsedTimeMin = Constants.examTime;
                            }
                        } catch (Exception ignored) {

                        }
                        exam.wait();
                    }
                }
                System.out.println("End Exam!!");
                studentGrades = exam.getGrades();
                exam.notifyEnd();
            }



//            synchronized (exam) {
//                System.out.println("Starting Exam!!!!!!");
//                while(true) {
//                    System.out.println(exam);
//                    studentGrades = exam.getGrades();
//                    exam.wait();
//                }
//            }

        }catch(Exception e){
            System.out.println(e);
        }
    }
}
