package sockets;

import model.Course;
import model.StudentInfo;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class InstructorClient {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Socket clientSocket = new Socket("localhost", 8000);
        System.out.println("Connected to a server");

        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
        outputStream.writeUTF("2");
        System.out.println("Enter Your Instructor id:");
        String isn = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        String hashedPassword = hashPassword(password);
        assert hashedPassword != null;
        outputStream.writeUTF(isn);
        outputStream.writeUTF(hashedPassword);
        boolean val = inputStream.readBoolean();
        if(val){
            System.out.println(inputStream.readUTF());
            System.out.println(inputStream.readUTF());
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
            List<Course> courses = (List<Course>) objectInputStream.readObject();
            for(Course course : courses){
                System.out.println("CourseId " + course.getCourseId() + " " + " Course Ttile: " + course.getTitle() + " Number of registered students: " + course.getNumOfStudents());
                System.out.print("\n");
            }
            boolean end = false;
            while(!end){
                System.out.println("What do you want to do");
                System.out.println("1. Add marks");
                System.out.println("2. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                outputStream.writeInt(choice);
                switch (choice) {
                    case 1:
                        System.out.println("Enter The course ID for the course you want to update the students' marks");
                        String courseID  = scanner.nextLine();
                        outputStream.writeUTF(courseID);
                        List<StudentInfo> students = (List<StudentInfo>) objectInputStream.readObject();
                        for(StudentInfo student  : students){
                            System.out.print(student.getSsn() + " " + student.getFirstName() + " " + student.getLastName() + "\n");
                        }
                        System.out.println("Enter the Student number for the student you want to add marks for");
                        String ssn = scanner.nextLine();
                        outputStream.writeUTF(ssn);
                        System.out.println("Enter mark (0-100)");
                        int mark = readIntWithRangeValidation(scanner, 0 , 100);
                        outputStream.writeInt(mark);
                        scanner.nextLine();
                        System.out.println("Enter Grade");
                        String grade = scanner.nextLine();
                        outputStream.writeUTF(grade);
                        break;
                    case 2:
                        System.out.println("Bye");
                        end = true;
                        break;
                    default:
                        System.out.println(inputStream.readUTF());
                }
            }
        }
        else{
            System.out.println(inputStream.readUTF());
            try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        }


    }
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    private static int readIntWithRangeValidation(Scanner scanner, int min, int max) {
        int input;
        while (true) {
            try {
                input = scanner.nextInt();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Invalid input. Please enter an integer within the specified range.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }
}
