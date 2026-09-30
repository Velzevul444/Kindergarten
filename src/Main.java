import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import utils.Database;
import utils.ExcelExporter;
import Enumes.Status;
import classes.Enrollment;
import classes.Parents;
import exceptions.BusinessException;
import exceptions.EntityNotFound;
import repositories.EnrollmentRepo;
import repositories.JdbcEnrollRepo;
import repositories.JdbcParentRepo;
import repositories.ParentRepo;
import services.EnrollService;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = Database.getConnection()) {
            System.out.println("Database connected successfully.");
        } catch (SQLException error) {
            System.out.println("Database connection error: "+ error.getMessage());
        }
        EnrollmentRepo repository = new JdbcEnrollRepo();
        ParentRepo parentRepository = new JdbcParentRepo();
        EnrollService service = new EnrollService(repository, parentRepository);

       
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("kindergarten enrollment system");
            System.out.println("1. View parent status");
            System.out.println("2. View enrollment");
            System.out.println("3. Search by nameChild");
            System.out.println("4. Search by date");
            System.out.println("5. Filter by status");
            System.out.println("6. Filter by date range");
            System.out.println("7. Statistic");
            System.out.println("8. Sort by nameChild");
            System.out.println("9. Sort by date");
            System.out.println("10. Create enrollment");
            System.out.println("11. Read enrollment");
            System.out.println("12. Update enrollment");
            System.out.println("13. Delete enrollment");
            System.out.println("14. Download all Excel tables");
            System.out.println("15. Exit");
            System.out.print("Enter your choice: ");
            choice = readInt(scanner, "Invalid input. Please enter a number.");

            switch (choice) {
                case 1:
                    for (Parents parent : parentRepository.findAll()) {
                        System.out.println("Parent Name: " + parent.getName());
                        System.out.println("Parent ID: " + parent.getId());
                        System.out.println("Parent Email: " + parent.getEmail());
                        System.out.println("Parent Phone: " + parent.getPhone());
                    }
                    break;
                case 2:
                    printAll(service);
                    break;
                case 3:
                    scanner.nextLine();
                    System.out.print("Enter nameChild: ");
                    String name = scanner.nextLine();
                    for (Enrollment enrollment : service.findAll()) {
                        if (enrollment.getChildName().equalsIgnoreCase(name)) {
                            print(enrollment);
                        }
                    }
                    break;
                case 4:
                    scanner.nextLine();
                    System.out.print("Enter date yyyy-MM-dd: ");
                    try {
                        LocalDate date = LocalDate.parse(scanner.nextLine());
                        for (Enrollment enrollment : service.findAll()) {
                            if (enrollment.getCreatedAt().equals(date)) {
                                print(enrollment);
                            }
                        }
                    } catch (Exception error) {
                        System.out.println("Invalid date");
                    }
                    break;
                case 5:
                    scanner.nextLine();
                    System.out.println("Available statuses: New, Review, Approved, Rejected, Enrolled, Cancelled");
                    System.out.print("Enter status: ");
                    try {
                        Status status = Status.valueOf(scanner.nextLine());
                        for (Enrollment enrollment : service.findAll()) {
                            if (enrollment.getStatus() == status) {
                                print(enrollment);
                            }
                        }
                    } catch (IllegalArgumentException error) {
                        System.out.println("Invalid status");
                    }
                    break;
                case 6:
                    scanner.nextLine();
                    System.out.print("Enter start date yyyy-MM-dd: ");
                    String startText = scanner.nextLine();
                    System.out.print("Enter end date yyyy-MM-dd: ");
                    String endText = scanner.nextLine();
                    try {
                        LocalDate start = LocalDate.parse(startText);
                        LocalDate end = LocalDate.parse(endText);
                        for (Enrollment enrollment : service.findAll()) {
                            LocalDate created = enrollment.getCreatedAt();
                            if (!created.isBefore(start) && !created.isAfter(end)) {
                                print(enrollment);
                            }
                        }
                    } catch (Exception error) {
                        System.out.println("Invalid date");
                    }
                    break;
                case 7:
                    statistics(service, parentRepository.findAll().size());
                    break;
                case 8:
                    service.findAll().sort(Comparator.comparing(Enrollment::getChildName));
                    printAll(service);
                    break;
                case 9:
                    service.findAll().sort(Comparator.comparing(Enrollment::getCreatedAt));
                    printAll(service);
                    break;
                case 10:
                    create(scanner, service);
                    break;
                case 11:
                    read(scanner, service);
                    break;
                case 12:
                    update(scanner, service);
                    break;
                case 13:
                    delete(scanner, service);
                    break;
                case 14:
                    ExcelExporter.exportAllTables();
                    break;
                case 15:
                    System.out.println("Exit");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 15);
        scanner.close();
    }

    private static void printAll(EnrollService service) {
        for (Enrollment enrollment : service.findAll()) {
            print(enrollment);
        }
    }

    private static void print(Enrollment enrollment) {
        System.out.println("Enrollment ID: " + enrollment.getId());
        System.out.println("Child Name: " + enrollment.getChildName());
        System.out.println("Parent ID: " + enrollment.getParentId());
        System.out.println("Created At: " + enrollment.getCreatedAt());
        System.out.println("Status: " + enrollment.getStatus());
        System.out.println("--------------------");
    }

    private static void statistics(EnrollService service, int parentCount) {
        int newCount = 0;
        int reviewCount = 0;
        int approvedCount = 0;
        int rejectedCount = 0;
        int enrolledCount = 0;
        int cancelledCount = 0;
        for (Enrollment enrollment : service.findAll()) {
            switch (enrollment.getStatus()) {
                case New -> newCount++;
                case Review -> reviewCount++;
                case Approved -> approvedCount++;
                case Rejected -> rejectedCount++;
                case Enrolled -> enrolledCount++;
                case Cancelled -> cancelledCount++;
            }
        }
        System.out.println("Total parents: " + parentCount);
        System.out.println("Total enrollments: " + service.findAll().size());
        System.out.println("New applications: " + newCount);
        System.out.println("Under review: " + reviewCount);
        System.out.println("Approved applications: " + approvedCount);
        System.out.println("Rejected applications: " + rejectedCount);
        System.out.println("Enrolled children: " + enrolledCount);
        System.out.println("Cancelled applications: " + cancelledCount);
    }

    private static void create(Scanner scanner, EnrollService service) {
        scanner.nextLine();
        System.out.print("Enter child name: ");
        String childName = scanner.nextLine();
        System.out.print("Enter parent ID: ");
        int parentId = readInt(scanner, "Parent ID must be a number.");
        try {
            if (childName.isBlank()) {
                throw new BusinessException("Child name cannot be empty.");
            }
            int newId = 1;
            for (Enrollment enrollment : service.findAll()) {
                newId = Math.max(newId, enrollment.getId() + 1);
            }
            service.create(new Enrollment(newId, childName, parentId, LocalDate.now(), Status.New));
            System.out.println("Enrollment created successfully.");
        } catch (BusinessException error) {
            System.out.println("Business error: " + error.getMessage());
        }
    }

    private static void read(Scanner scanner, EnrollService service) {
        int id = readInt(scanner, "ID must be a number.");
        try {
            print(service.findById(id));
        } catch (EntityNotFound error) {
            System.out.println(error.getMessage());
        }
    }

    private static void update(Scanner scanner, EnrollService service) {
        int id = readInt(scanner, "ID must be a number.");
        scanner.nextLine();
        try {
            Enrollment enrollment = service.findById(id);
            System.out.print("Enter new child name: ");
            enrollment.setChildName(scanner.nextLine());
            System.out.print("Enter new status: ");
            enrollment.setStatus(Status.valueOf(scanner.nextLine()));
            service.update(enrollment);
            System.out.println("Enrollment updated successfully.");
        } catch (EntityNotFound error) {
            System.out.println(error.getMessage());
        } catch (BusinessException error) {
            System.out.println("Business error: " + error.getMessage());
        } catch (IllegalArgumentException error) {
            System.out.println("Invalid status.");
        }
    }

    private static void delete(Scanner scanner, EnrollService service) {
        int id = readInt(scanner, "ID must be a number.");
        try {
            service.delete(id);
            System.out.println("Enrollment deleted successfully.");
        } catch (EntityNotFound error) {
            System.out.println(error.getMessage());
        }
    }

    private static int readInt(Scanner scanner, String message) {
        while (!scanner.hasNextInt()) {
            System.out.println(message);
            scanner.next();
        }
        return scanner.nextInt();
    }
}
