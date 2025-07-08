import java.util.Scanner;
public class task1 {
    public void add_student(int x, String[] student_name, int[] grade){
        Scanner sc= new Scanner(System.in);
        for (int i = 0; i < x; i++) {
            System.out.print("Enter the name of the student:");
            student_name[i]=sc.nextLine();
            System.out.print("Enter the marks of the student:");
            grade[i]=sc.nextInt();
            sc.nextLine();
        }
        System.out.println();
        System.out.println("Data Added Successfully");
    
    }
    public void show_student(int x,String[] student_name, int[] grade){
        System.out.println("***************Data Summary***************");
        System.out.println("Name     Grade");
        for (int i = 0; i <x ; i++) {
            System.out.println(student_name[i]+"     "+grade[i]);
        }
    }
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        int x=0;
        String[] student_name=new String[100];
        int[] grade=new int[100];
        int choice;
        task1 obj= new task1();
          do {
            System.out.println("\n===================================");
            System.out.println("        Student Grade Tracker     ");
            System.out.println("===================================");
            System.out.println("1.  Add Student Grade");
            System.out.println("2.  Show Summary Report");
            System.out.println("3.  Exit");
            System.out.println("-----------------------------------");
            System.out.print("Enter your choice (1-3): ");

            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                System.out.print("How many students you want to add:");
                     x=sc.nextInt();
                    obj.add_student(x, student_name, grade);
                    break;
                case 2:
                   obj.show_student(x, student_name, grade);
                    break;
                case 3:
                   System.out.println("Exiting");
                    break;
                default:
            }

        } while (choice != 3);
        sc.close();
    }
}
