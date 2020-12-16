package student;

import common.Question;
import common.Exam;

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
//            System.setProperty("java.security.policy","file:./student.policy");
            Registry registry = LocateRegistry.getRegistry();
            Exam exam = (Exam) registry.lookup("WELCOME");
            Scanner keyboard = new Scanner(System.in);
            System.out.println("enter an ID");
            String studentID = keyboard.next();
            exam.sendID(studentID);
            String c = exam.getQuestion(0);
            System.out.println(c);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
// C:\Users\hp\Desktop\erasmus_studies\DC\RMI_exam\csvFiles\exam1.csv