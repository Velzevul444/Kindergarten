import java.time.LocalDate;
import java.util.Scanner;
import classes.Enrollment;
import classes.Parents;
import Enumes.Status;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        ArrayList<Parents> parents = new ArrayList<>();
        ArrayList<Enrollment> enrollments = new ArrayList<>();

       parents.add(new Parents("Ivan", 1, "ivan@i", "88005353535"));
parents.add(new Parents("Anna", 2, "anna@i", "88005353536"));
parents.add(new Parents("Petr", 3, "petr@i", "88005353537"));
parents.add(new Parents("Maria", 4, "maria@i", "88005353538"));
parents.add(new Parents("Olga", 5, "olga@i", "88005353539"));

enrollments.add(new Enrollment(
        1, "Masha", 1, LocalDate.now(), Status.New
));
enrollments.add(new Enrollment(
        2, "Sasha", 2, LocalDate.now().minusDays(1), Status.Review
));
enrollments.add(new Enrollment(
        3, "Dima", 3, LocalDate.now().minusDays(2), Status.Approved
));
enrollments.add(new Enrollment(
        4, "Lena", 4, LocalDate.now().minusDays(3), Status.Rejected
));
enrollments.add(new Enrollment(
        5, "Nikita", 5, LocalDate.now().minusDays(4), Status.Enrolled
));
enrollments.add(new Enrollment(
        6, "Katya", 1, LocalDate.now().minusDays(5), Status.New
));
enrollments.add(new Enrollment(
        7, "Misha", 2, LocalDate.now().minusDays(6), Status.Review
));
enrollments.add(new Enrollment(
        8, "Sofia", 3, LocalDate.now().minusDays(7), Status.Approved
));
enrollments.add(new Enrollment(
        9, "Artem", 4, LocalDate.now().minusDays(8), Status.New
));
enrollments.add(new Enrollment(
        10, "Nina", 5, LocalDate.now().minusDays(9), Status.Cancelled
));
        
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
            System.out.println("14. Exit");
            System.out.print("Enter your choice: ");  

            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); 
            }
            choice = scanner.nextInt();

            switch (choice) {
                case 1: 
                    for (Parents parent : parents) {
                        System.out.println("Parent Name: " + parent.getName());
                        System.out.println("Parent ID: " + parent.getId());
                        System.out.println("Parent Email: " + parent.getEmail());
                        System.out.println("Parent Phone: " + parent.getPhone());
                    }
                    break;
                case 2:
                    for (Enrollment enrollment : enrollments) {
                        System.out.println("Enrollment ID: " + enrollment.getId());
                        System.out.println("Child Name: " + enrollment.getChildName());
                        System.out.println("Parent ID: " + enrollment.getParentId());
                        System.out.println("Created At: " + enrollment.getCreatedAt());
                        System.out.println("Status: " + enrollment.getStatus());
                    }
                    break;    
                case 3:
                    scanner.nextLine();

                    System.out.println("Enter nameChild");
                    String nameChild = scanner.nextLine();
                    for (Enrollment enrollment : enrollments) {
                        if (enrollment.getChildName().equalsIgnoreCase(nameChild)) {
                            System.out.println("Enrollment ID: " + enrollment.getId());
                            System.out.println("Child Name: " + enrollment.getChildName());
                            System.out.println("Parent ID: " + enrollment.getParentId());
                            System.out.println("Created At: " + enrollment.getCreatedAt());
                            System.out.println("Status: " + enrollment.getStatus());
                        }
                    }
                    break;
                case 4:
                    scanner.nextLine();
                    System.out.print("Enter date yyyy-MM-dd: ");
                    String date = scanner.nextLine();
                    try {
                        LocalDate searchDate = LocalDate.parse(date);

                        for (Enrollment enrollment : enrollments) {
                            if (enrollment.getCreatedAt().equals(searchDate)) {
                                System.out.println("Enrollment ID: " + enrollment.getId());
                                System.out.println("Child Name: " + enrollment.getChildName());
                                System.out.println("Created At: " + enrollment.getCreatedAt());
                                System.out.println("Status: " + enrollment.getStatus());
                            }
                        }
                    }
                    catch (Exception error){
                        System.out.println("Invalid date");
                    }
                    break;
                case 5:
                    scanner.nextLine();

                    
                    System.out.println("Available statuses: New, Review, Approved, Rejected, Enrolled, Cancelled");
                    System.out.println("Enter status:");
                    
                    String status = scanner.nextLine();

                    try {
                        Status selected = Status.valueOf(status);
                        for (Enrollment enrollment : enrollments) {
                            if (enrollment.getStatus() == selected) {
                                System.out.println("Enrollment ID: " + enrollment.getId());
                                System.out.println("Child Name: " + enrollment.getChildName());
                                System.out.println("Created At: " + enrollment.getCreatedAt());
                                System.out.println("Status: " + enrollment.getStatus());
                              }
                            }
                        } catch (IllegalArgumentException error) {
                             System.out.println("Invalid status");
                        }
                        break;

                case 6:   
                    scanner.nextLine();

                    System.out.print("Enter start date yyyy-MM-dd: ");
                    String startDate1 = scanner.nextLine();

                    System.out.print("Enter end date yyyy-MM-dd: ");
                    String endDate1 = scanner.nextLine();

                     try {
                        LocalDate startDate = LocalDate.parse(startDate1);
                        LocalDate endDate = LocalDate.parse(endDate1);

                        for (Enrollment enrollment : enrollments) {
                            LocalDate createdAt = enrollment.getCreatedAt();

                            if (!createdAt.isBefore(startDate)&& !createdAt.isAfter(endDate)) {
                                System.out.println("Enrollment ID: " + enrollment.getId());
                                System.out.println("Child Name: " + enrollment.getChildName());
                                System.out.println("Created At: " + enrollment.getCreatedAt());
                                System.out.println("Status: " + enrollment.getStatus());
                            }
                        }
                    } catch (Exception error) {
                        System.out.println("Invalid date");
                        }
                    break;
                case 7:
                    int newCount = 0;
                    int reviewCount = 0;
                    int approvedCount = 0;
                    int rejectedCount = 0;
                    int enrolledCount = 0;
                    int cancelledCount = 0;

                    for (Enrollment enrollment : enrollments){
                        switch (enrollment.getStatus()) {
                            case New:
                                newCount++;
                                break;
                            case Review:
                                reviewCount++;
                                break;
                            case Approved:
                                approvedCount++;
                                break;
                            case Rejected:
                                rejectedCount++;
                                break;
                            case Enrolled:
                                enrolledCount++;
                                break;
                            case Cancelled:
                                cancelledCount++;
                                break;
        }
                    }  
                    System.out.println("Total parents: " + parents.size());
                    System.out.println("Total enrollments: " + enrollments.size());
                    System.out.println("New applications: " + newCount);
                    System.out.println("Under review: " + reviewCount);
                    System.out.println("Approved applications: " + approvedCount);
                    System.out.println("Rejected applications: " + rejectedCount);
                    System.out.println("Enrolled children: " + enrolledCount);
                    System.out.println("Cancelled applications: " + cancelledCount);
    
                    break;
                case 8:
                    enrollments.sort(
                        Comparator.comparing(Enrollment::getChildName)
                    );

                    for (Enrollment enrollment : enrollments) {
                        System.out.println("Enrollment ID: " + enrollment.getId());
                        System.out.println("Child Name: " + enrollment.getChildName());
                        System.out.println("Created At: " + enrollment.getCreatedAt());
                        System.out.println("Status: " + enrollment.getStatus());
                    }
                    break;
                case 9: 
                    enrollments.sort(
                        Comparator.comparing(Enrollment::getCreatedAt)
                    );

                    for (Enrollment enrollment : enrollments) {
                        System.out.println("Enrollment ID: " + enrollment.getId());
                        System.out.println("Child Name: " + enrollment.getChildName());
                        System.out.println("Created At: " + enrollment.getCreatedAt());
                        System.out.println("Status: " + enrollment.getStatus());
                    }
                    break;   
                case 10: {
                    scanner.nextLine();

                    System.out.print("Enter child name: ");
                    String childName = scanner.nextLine();

                    System.out.print("Enter parent ID: ");
                    int parentId = scanner.nextInt();

                    boolean parentExists = false;

                    for (Parents parent : parents) {
                        if (parent.getId() == parentId) {
                            parentExists = true;
                            break;
                        }
                    }

                    if (!parentExists) {
                        System.out.println("Parent not found.");
                        break;
                    }

                    int newId = enrollments.size() + 1;

                    Enrollment newEnrollment = new Enrollment(
                        newId,
                        childName,
                        parentId,
                        LocalDate.now(),
                        Status.New
                    );

                    enrollments.add(newEnrollment);

                    System.out.println("Enrollment created successfully.");
                    break;
                }
                case 11: {
                    System.out.print("Enter enrollment ID: ");

                    while (!scanner.hasNextInt()) {
                        System.out.println("ID must be a number.");
                        scanner.next();
                    }

                    int enrollmentId = scanner.nextInt();
                    boolean found = false;

                    for (Enrollment enrollment : enrollments) {
                        if (enrollment.getId() == enrollmentId) {
                            System.out.println("Enrollment ID: " + enrollment.getId());
                            System.out.println("Child Name: " + enrollment.getChildName());
                            System.out.println("Parent ID: " + enrollment.getParentId());
                            System.out.println("Created At: " + enrollment.getCreatedAt());
                            System.out.println("Status: " + enrollment.getStatus());

                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        System.out.println("Enrollment not found.");
                    }
                    break;
                }    
                case 12: {
                    System.out.print("Enter enrollment ID: ");

                    while (!scanner.hasNextInt()) {
                        System.out.println("ID must be a number.");
                        scanner.next();
                    }

                    int enrollmentId = scanner.nextInt();
                    scanner.nextLine();

                    Enrollment selectedEnrollment = null;

                    for (Enrollment enrollment : enrollments) {
                        if (enrollment.getId() == enrollmentId) {
                            selectedEnrollment = enrollment;
                            break;
                        }
                    }

                    if (selectedEnrollment == null) {
                        System.out.println("Enrollment not found.");
                    break;
                    }

                    System.out.print("Enter new child name: ");
                    String newChildName = scanner.nextLine();
                    System.out.println("Enter new status: New, Review, Approved, Rejected, Enrolled, Cancelled");
                    String newStatus = scanner.nextLine();

                    try {
                        Status statusUpdate = Status.valueOf(newStatus);

                        selectedEnrollment.setChildName(newChildName);
                        selectedEnrollment.setStatus(statusUpdate);
                        System.out.println("Enrollment updated successfully.");
    
                    } catch (IllegalArgumentException error) {
                        System.out.println("Invalid status.");
                    }

                    break;
                }
                case 13: {
                    System.out.print("Enter enrollment ID: ");

                    while (!scanner.hasNextInt()) {
                        System.out.println("ID must be a number.");
                        scanner.next();
                    }

                    int enrollmentId = scanner.nextInt();
                    Enrollment enrollmentDelete = null;

                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getId() == enrollmentId) {
                        enrollmentDelete = enrollment;
                    break;
                    }
                }

                if (enrollmentDelete == null) {
                    System.out.println("Enrollment not found.");
                    break;
                }

                enrollments.remove(enrollmentDelete);
                System.out.println("Enrollment deleted successfully.");
                break;
                }
                case 14:
                    System.out.println("Exit");
                break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 14);
            scanner.close();        
                    
        }}