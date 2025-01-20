package org.example;

import java.util.Scanner;

public class UDDIApplication {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Select an option:");
            System.out.println("1. Register a new service");
            System.out.println("2. Search for and invoke a service");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    logInfo("Initiating service registration process...");
                    UDDIServiceManager.registerService();
                    break;
                case 2:
                    logInfo("Starting service search process...");
                    UDDIServiceManager.searchAndInvokeService();
                    break;
                default:
                    logError("Invalid input. Please choose a valid option.");
            }
        } catch (Exception e) {
            logError("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void logInfo(String message) {
        System.out.println("[INFO] " + message);
    }

    private static void logError(String message) {
        System.err.println("[ERROR] " + message);
    }
}
