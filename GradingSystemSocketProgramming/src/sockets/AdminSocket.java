package sockets;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class AdminSocket {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Socket clientSocket = new Socket("localhost", 8000);
        System.out.println("Connected to a server");

        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
        outputStream.writeUTF("3");
        System.out.println("Enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();
        outputStream.writeUTF(username);
        outputStream.writeUTF(password);
        boolean val = inputStream.readBoolean();
        if(val){
            System.out.println(inputStream.readUTF());
            boolean end = false;
            while(!end){
                System.out.println("What do you want to do");
                System.out.println("1. Register a student");
                System.out.println("2. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                outputStream.writeInt(choice);
                switch (choice){
                    case 1:
                        System.out.println("Enter the student number: ");
                        String ssn = scanner.nextLine();
                        System.out.println("First name: ");
                        String firstName = scanner.nextLine();
                        System.out.println("Enter middle name: ");
                        String mi = scanner.nextLine();
                        System.out.println("Enter last name: ");
                        String lastName = scanner.nextLine();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date birthDate = null;
                        boolean validInput = false;
                        while (!validInput) {
                            System.out.print("Enter birth date (yyyy-MM-dd): ");
                            String dateString = scanner.nextLine();
                            try {
                                java.util.Date parsedDate = dateFormat.parse(dateString);
                                birthDate = new java.sql.Date(parsedDate.getTime());
                                System.out.println("Entered birth date: " + dateFormat.format(birthDate));
                                validInput = true;
                            } catch (ParseException e) {
                                System.out.println("Invalid date format. Please enter the date in the format yyyy-MM-dd.");
                            }
                        }
                        System.out.println("Enter street: ");
                        String street = scanner.nextLine();
                        System.out.println("Enter phone number: ");
                        String phone = scanner.nextLine();
                        System.out.println("Enter zipcode: ");
                        String zipcode = scanner.nextLine();
                        System.out.println("Enter department id: ");
                        String deptId = scanner.nextLine();
                        System.out.println("Enter a password: ");
                        String pass = scanner.nextLine();
                        String hashed = hashPassword(pass);
                        outputStream.writeUTF(ssn);
                        outputStream.writeUTF(firstName);
                        outputStream.writeUTF(mi);
                        outputStream.writeUTF(lastName);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                        objectOutputStream.writeObject(birthDate);
                        outputStream.writeUTF(street);
                        outputStream.writeUTF(phone);
                        outputStream.writeUTF(zipcode);
                        outputStream.writeUTF(deptId);
                        assert hashed != null;
                        outputStream.writeUTF(hashed);
                        System.out.println(inputStream.readUTF());
                        break;
                    case 2:
                        end = true;
                        System.out.println("Bye!");
                        break;
                    default:
                        System.out.println("Wrong choice");
                }
            }
        }else{
            System.out.println(inputStream.readUTF());
            try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());

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
