import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StudentCRUDTerminal {

    private static final String URL = "jdbc:postgresql://localhost:5432/school";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "123";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to the database.");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. 5");
                System.out.println("2. Display Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        addStudent(connection);
                        break;
                    case 2:
                        displayStudents(connection);
                        break;
                    case 3:
                        updateStudent(connection);
                        break;
                    case 4:
                        deleteStudent(connection);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        connection.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please choose again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void addStudent(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student class: ");
        String className = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = scanner.nextInt();

        String sql = "INSERT INTO students (name, class, age) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.setString(2, className);
        statement.setInt(3, age);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Student added successfully.");
        }
    }

    private static void displayStudents(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

        System.out.println("ID\tName\tClass\tAge");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id") + "\t" +
                    resultSet.getString("name") + "\t" +
                    resultSet.getString("class") + "\t" +
                    resultSet.getInt("age"));
        }
    }

    private static void updateStudent(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student ID to update: ");
        int id = scanner.nextInt();

        String sql = "SELECT * FROM students WHERE id = ?";
        PreparedStatement selectStatement = connection.prepareStatement(sql);
        selectStatement.setInt(1, id);
        ResultSet resultSet = selectStatement.executeQuery();

        if (resultSet.next()) {
            System.out.print("Enter new name: ");
            String name = scanner.next();
            System.out.print("Enter new class: ");
            String className = scanner.next();
            System.out.print("Enter new age: ");
            int age = scanner.nextInt();

            String updateSql = "UPDATE students SET name = ?, class = ?, age = ? WHERE id = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, name);
            updateStatement.setString(2, className);
            updateStatement.setInt(3, age);
            updateStatement.setInt(4, id);

            int rowsUpdated = updateStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully.");
            }
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }

    private static void deleteStudent(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM students WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);

        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }
}
