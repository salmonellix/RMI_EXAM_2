package student;

import common.Question;
import common.Exam;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;


public class Student {
    public static void main(String args[]){
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try{
//            System.setProperty("java.security.policy","file:./student.policy");
            Registry registry = LocateRegistry.getRegistry();
            Exam exam = (Exam) registry.lookup("WELCOME");
            String c = exam.getQuestion(1);
            System.out.println(c);
        }catch(Exception e){
            System.out.println(e);
        }
    }
}
