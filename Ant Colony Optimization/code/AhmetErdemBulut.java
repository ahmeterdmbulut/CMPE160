import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main class for the Migros Delivery using Ant Colony Optimization assignment.
 * This class processes location data from a file and selects a method based on specified parameters
 * @author Ahmet Erdem Bulut, Student ID: 2022400093
 * @since Date: 02.05.2024
 */
public class AhmetErdemBulut {
    /**
     * Main method of the application. Reads locations from input files and decides the method to use
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        ArrayList<ArrayList<Double>> locations = new ArrayList<>(); // List to store locations
        // Reading location data from file
        try {
            File file = new File("input01.txt"); // File containing location data
            Scanner scanner = new Scanner(file); // Scanner to read the file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                ArrayList<Double> coordinates = new ArrayList<>(); // List to keep the coordinates
                coordinates.add(Double.parseDouble(parts[0])); // Parse and add x coordinate
                coordinates.add(Double.parseDouble(parts[1])); // Parse and add y coordinate
                locations.add(coordinates); // Add the coordinate pair to the list of locations
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
            return;
        }
        int chosenMethod = 1; // Method selector: 1 for brute-force, 2 for ACO
        int displayMethod = 1; // Display selector for ACO: 1 for pheromone map, 2 for shortest path

        // Switch to select and execute the method based on the chosenMethod
        switch (chosenMethod){
            case 1:
                bruteForce(locations);
                break;
            case 2:
                if (displayMethod == 1) {
                    antColonyOptimization(locations, 1); // Use ACO with pheromone map
                } else if (displayMethod == 2) {
                    antColonyOptimization(locations, 2); // Use ACO with shortest path
                }
                break;
        }
    }

    /**
     * Calls the brute-force method
     * @param locations A list of locations with coordinates
     */
    private static void bruteForce(ArrayList<ArrayList<Double>> locations){
        BruteForce.findRoute(locations);
    }

    /**
     * Calls the Ant Colony Optimization method
     * @param locations A list of locations with coordinates
     * @param displayMethod The display method (1 for pheromone map, 2 for shortest path)
     */
    private static void antColonyOptimization(ArrayList<ArrayList<Double>> locations, int displayMethod) {
        AntColonyOptimization.optimize(locations, displayMethod);
    }

}
