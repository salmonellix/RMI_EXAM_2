package student;

import common.Question;
import common.Exam;
import common.StudentInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
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
            Boolean examOpen = Boolean.FALSE;
            while (examOpen.equals(Boolean.FALSE)){
                examOpen = exam.checkStart();
            }

            System.out.println("STARTING EXAM");
            //exam.registerInterface(studentID, studentI);
            int questionsNumber = exam.questionsNumber();

            synchronized (exam) {
                Boolean finalExamOpen = examOpen;
                Boolean finalExamOpen1 = examOpen;
                IntStream.range(0, questionsNumber).forEachOrdered(n -> {
                    String c = null;
                    if (finalExamOpen1.equals(Boolean.TRUE)){
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