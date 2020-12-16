package student;

import common.Question;
import common.Exam;
import common.StudentInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;


public class Student {
    public static void main(String args[]){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try{

            Registry registry = LocateRegistry.getRegistry();

            Exam exam = (Exam) registry.lookup("WELCOME");
            StudentImpl studentI = new StudentImpl();
            Scanner keyboard = new Scanner(System.in);
            System.out.println("enter an ID");
            String studentID = keyboard.next();
            exam.sendID(studentID);
            exam.registerInterface(studentID, studentI);
            String c = exam.getQuestion(0);
            System.out.println(c);
            System.out.println("enter an answer");
            String answer = keyboard.next();
            exam.sendAnswer(answer,0,studentID);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
// C:\Users\hp\Desktop\erasmus_studies\DC\RMI_exam\csvFiles\exam1.csv