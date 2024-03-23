package sockets;

import dao.EnrollmentDao;
import model.Enrollment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Scanner;

public class StudentClient {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket clientSocket = new Socket("localhost", 8000);
        System.out.println("Connected to a server");
        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
        outputStream.writeUTF("1");
        System.out.println("Enter Your Student Number");
        String ssn = scanner.nextLine();
        System.out.println("Enter your password");
        String pass = scanner.nextLine();
        String passwordHashed = hashPassword(pass);
        outputStream.writeUTF(ssn);
        assert passwordHashed != null;
        outputStream.writeUTF(passwordHashed);
        boolean val = inputStream.readBoolean();
        if(val) {
            System.out.println(inputStream.readUTF());
            System.out.println(inputStream.readUTF());
            boolean end = false;
            try {
                while (!end) {
                    System.out.println("What do you want to do");
                    System.out.println("1. View marks");
                    System.out.println("2. Exit");
                    System.out.print("Choose an option: ");
                    int choice = readIntWithValidation(scanner);
                    outputStream.writeInt(choice);

                    switch (choice) {
                        case 1:
                            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                            List<Enrollment> enrollments = (List<Enrollment>) objectInputStream.readObject();
                            for (Enrollment enrollment : enrollments) {
                                System.out.print("Course: " + enrollment.getTitle() + " Grade: " + enrollment.getGrade() + " Mark: " + enrollment.getMark() + " Class Average: " + enrollment.getAverage() + " Highest Mark: " + enrollment.getHighest() + " Lowest Mark: " + enrollment.getLower());
                                System.out.print("\n");
                            }
                            break;
                        case 2:
                            System.out.println("Bye!");
                            end = true;
                            break;
                        default:
                            System.out.println("Invalid input please try again");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    inputStream.close();
                    outputStream.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace(); // Handle or log the exception appropriately
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
    private static int readIntWithValidation(Scanner scanner) {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume the invalid input to avoid an infinite loop
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
}
