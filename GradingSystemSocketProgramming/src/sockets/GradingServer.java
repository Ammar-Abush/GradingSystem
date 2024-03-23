package sockets;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import dao.*;
import model.Course;
import model.Enrollment;
import model.Instructor;
import model.StudentInfo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GradingServer {
    private static final int PORT = 8000;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection established");
                executorService.submit(() -> handleClient(clientSocket));
            }
        }
    }
    private static void handleClient(Socket clientSocket) {
        try (DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())) {

            String type = inputStream.readUTF();
            if (type.equals("1")) {
                String ssn = inputStream.readUTF();
                String password = inputStream.readUTF();
                StudentDaoInterface studentInfoDao = new StudentInfoDao();
                StudentInfo studentInfo = new StudentInfo();
                studentInfo.setSsn(ssn);
                studentInfo.setPassword(password);
                try {
                    boolean val = studentInfoDao.validate(studentInfo);
                    outputStream.writeBoolean(val);
                    if (val){
                        outputStream.writeUTF("Login successful!");
                        StudentInfo st = studentInfoDao.getStudentInfo(ssn);
                        outputStream.writeUTF("Welcome " + st.getFirstName() + " "+ st.getLastName());
                        boolean end = false;
                        while(!end){
                            int choice = inputStream.readInt();
                            switch (choice){
                                case 1:
                                    EnrollmentDaoInterface enrollmentDao = new EnrollmentDao();
                                    List<Enrollment> enrollments = enrollmentDao.getEnrollments(ssn);
                                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                                    objectOutputStream.writeObject(enrollments);
                                    break;
                                case 2:
                                    end = true;
                                    break;
                                default:
                                    System.out.println("Wrong input from the user");
                            }
                        }
                    }
                    else{
                        outputStream.writeUTF("Invalid SSN or password. Exiting...");
                    }
                } catch (ClassNotFoundException | SQLException e) {
                    System.out.println("An error has occurred");
                    throw new RuntimeException(e);
                }
            } else if (type.equals("2")){
                String isn = inputStream.readUTF();
                String password = inputStream.readUTF();
                InstructorDaoInterface instructorInfoDoa = new InstructorInfoDao();
                Instructor instructor = new Instructor();
                instructor.setIsn(isn);
                instructor.setPassword(password);
                boolean val = instructorInfoDoa.validate(instructor);
                outputStream.writeBoolean(val);
                if(val){
                    outputStream.writeUTF("Login successful!");
                    Instructor ins = instructorInfoDoa.getInstructorInfo(isn);
                    outputStream.writeUTF("Welcome " + ins.getInstructorRank() + " " + ins.getFirstName() + " "+ ins.getLastName());
                    TeachingDaoInterface teachingDao = new TeachingDao();
                    List<Course> courses = teachingDao.getCourses(isn);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    objectOutputStream.writeObject(courses);

                    boolean end = false;
                    while(!end){
                        int choice = inputStream.readInt();
                        switch (choice) {
                            case 1:
                                String courseId = inputStream.readUTF();
                                List<StudentInfo> students = teachingDao.getStudents(isn, courseId);
                                objectOutputStream.writeObject(students);
                                String ssn = inputStream.readUTF();
                                int mark = inputStream.readInt();
                                String grade = inputStream.readUTF();
                                EnrollmentDaoInterface enrollmentDao = new EnrollmentDao();
                                enrollmentDao.assignMarks(isn, courseId,ssn, mark, grade);
                                break;
                            case 2:
                                end = true;
                                break;
                            default:
                                System.out.println("Wrong input from the user");

                        }
                    }
                }
                else{
                    outputStream.writeUTF("Wrong number or password");
                }
            }
            else {
                String username = inputStream.readUTF();
                String password = inputStream.readUTF();
                boolean val = username.equals("Admin") && password.equals("Admin");
                outputStream.writeBoolean(val);
                if(val){
                    outputStream.writeUTF("Login successful!");
                    boolean end = false;
                    while(!end){
                        int choice = inputStream.readInt();
                        switch (choice){
                            case 1:
                                String ssn = inputStream.readUTF();
                                String firstName = inputStream.readUTF();
                                String mi = inputStream.readUTF();
                                String lastName = inputStream.readUTF();
                                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                                Date birthDate = (Date) objectInputStream.readObject();
                                String street = inputStream.readUTF();
                                String phone = inputStream.readUTF();
                                String zipcode = inputStream.readUTF();
                                String deptId = inputStream.readUTF();
                                String hashed = inputStream.readUTF();
                                StudentDaoInterface studentDao = new StudentInfoDao();
                                studentDao.registerStudent(ssn, firstName, mi, lastName,birthDate,street,phone,zipcode,deptId,hashed);
                                outputStream.writeUTF("Success");
                                break;
                            case 2:
                                end = true;
                                break;
                            default:
                                System.out.println("Wrong input from the user");
                        }
                    }
                }
                else{
                    outputStream.writeUTF("Wrong username or password");
                }
            }

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
