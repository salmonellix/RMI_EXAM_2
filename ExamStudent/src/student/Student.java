package student;

import common.Exam;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;


public class Student {
    public static void main(String args[]){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try{

            Registry registry = LocateRegistry.getRegistry();

            Exam exam = (Exam) registry.lookup("WELCOME");
            try{
                Exam examStarted = (Exam) registry.lookup("EXAM STARTED");
                if (examStarted != exam){
                    System.out.println("Exam has started -- gates are closed");
                }} catch (RemoteException | NotBoundException ignored) {
            }

            //StudentImpl studentI = new StudentImpl();
            Scanner keyboard = new Scanner(System.in);
            System.out.println("enter an ID");
            String studentID = keyboard.next();
            exam.sendID(studentID);
            boolean examOpen = false;
            while (!examOpen){
                examOpen = exam.checkStart();
            }

            System.out.println("STARTING EXAM");
            //exam.registerInterface(studentID, studentI);
            int questionsNumber = exam.questionsNumber();

            synchronized (exam) {

                AtomicBoolean finalExamOpen = new AtomicBoolean(true);
                IntStream.range(0, questionsNumber).forEachOrdered(n -> {
                    String c = null;
                    try {
                        finalExamOpen.set(exam.checkStart());
                    } catch (RemoteException e) {
                    }
                    if (finalExamOpen.get()){
                        try {
                            c = exam.getQuestion(n);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        System.out.println(c);
                        System.out.println("enter an answer");
                        String answer = keyboard.next();
                        try {
                            exam.sendAnswer(answer, n, studentID);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        System.out.println("Time is over");
                        n = questionsNumber;
                    }

                });
                exam.removeID(studentID);
            }





            System.out.println("END EXAM");
            System.exit(0);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
// C:\Users\hp\Desktop\erasmus_studies\DC\RMI_exam\csvFiles\exam1.csv