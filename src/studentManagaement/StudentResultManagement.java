package studentManagaement;

import java.io.*;
import java.util.*;

class Student implements Serializable {
	private String name;
	private int rollNumber;
	private int[] marks;
	private int total;
	private String grade;

	public Student(String name, int rollNumber, int[] marks) {
		this.name = name;
		this.rollNumber = rollNumber;
		this.marks = marks;
		calculateTotalAndGrade();
	}

	private void calculateTotalAndGrade() {
		total = 0;
		for (int mark : marks) {
			total += mark;
		}
		double percentage = total / (double) marks.length;

		if (percentage >= 90)
			grade = "A+";
		else if (percentage >= 75)
			grade = "A";
		else if (percentage >= 60)
			grade = "B";
		else if (percentage >= 40)
			grade = "C";
		else
			grade = "Fail";
	}

	public String getName() {
		return name;
	}

	public int getRollNumber() {
		return rollNumber;
	}

	public int getTotal() {
		return total;
	}

	public String getGrade() {
		return grade;
	}

	@Override
	public String toString() {
		return "Roll No: " + rollNumber + ", Name: " + name + ", Total: " + total + ", Grade: " + grade;
	}
}

public class StudentResultManagement {
	private static final String FILE_NAME = "students.dat";
	private static ArrayList<Student> studentList = new ArrayList<>();

	public static void main(String[] args) {
		loadStudentsFromFile();
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("\n--- Student Result Management System ---");
			System.out.println("1. Add Student");
			System.out.println("2. View All Students");
			System.out.println("3. Sort by Marks");
			System.out.println("4. Exit");
			System.out.print("Enter choice: ");
			int choice = sc.nextInt();

			switch (choice) {
			case 1 -> addStudent(sc);
			case 2 -> viewAllStudents();
			case 3 -> sortByMarks();
			case 4 -> {
				saveStudentsToFile();
				System.out.println("Data saved. Exiting...");
				return;
			}
			default -> System.out.println("Invalid choice!");
			}
		}
	}

	private static void addStudent(Scanner sc) {
		System.out.print("Enter name: ");
		sc.nextLine();
		String name = sc.nextLine();
		System.out.print("Enter roll number: ");
		int rollNumber = sc.nextInt();

		System.out.print("Enter number of subjects: ");
		int subjects = sc.nextInt();
		int[] marks = new int[subjects];
		for (int i = 0; i < subjects; i++) {
			System.out.print("Enter marks for subject " + (i + 1) + ": ");
			marks[i] = sc.nextInt();
		}

		Student student = new Student(name, rollNumber, marks);
		studentList.add(student);
		System.out.println("Student added successfully!");
	}

	private static void viewAllStudents() {
		if (studentList.isEmpty()) {
			System.out.println("No students found!");
			return;
		}
		for (Student s : studentList) {
			System.out.println(s);
		}
	}

	private static void sortByMarks() {
		studentList.sort(Comparator.comparingInt(Student::getTotal).reversed());
		System.out.println("Students sorted by total marks:");
		viewAllStudents();
	}

	private static void saveStudentsToFile() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
			oos.writeObject(studentList);
		}catch (IOException e){
			System.out.println("Error saving data: " + e.getMessage());
		}
	}

	private static void loadStudentsFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            studentList = (ArrayList<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
            }
        }
}

