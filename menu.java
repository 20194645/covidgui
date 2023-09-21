package com.covidclient;
import java.util.Scanner;

public class menu {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int option;
    
    while (true) {
      System.out.println("\nMain Menu");
      System.out.println("1. Option 1");
      System.out.println("2. Option 2");
      System.out.println("3. Option 3");
      System.out.println("4. Exit");
      System.out.print("Enter an option: ");
      
      option = scanner.nextInt();
      switch (option) {
        case 1:
          System.out.println("You chose option 1.");
          System.out.print("Cient: ");
          String text = scanner.nextLine();
          break;
        case 2:
          System.out.println("You chose option 2.");
          break;
        case 3:
          System.out.println("You chose option 3.");
          break;
        case 4:
          System.out.println("Exiting the program.");
          return;
        default:
          System.out.println("Invalid option. Try again.");
      }
    }
  }
}
