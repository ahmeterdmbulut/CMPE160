import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * The main class for the Turkey Navigation assignment.
 * @author Ahmet Erdem Bulut, Student ID: 2022400093
 * @since Date: 24.03.2024
 */
public class AhmetErdemBulut {
    /**
     * The main method of the application. It initializes the city and connection data, asks the user for start
     * and destination cities, calculates the shortest path between them, and draws the map with the path highlighted.
     * @param args Command line arguments
     */
    public static void main(String[] args){
        List<City> cities = new ArrayList<>(); // List to store City objects
        List<Connection> connections = new ArrayList<>(); // List to store connections between cities

        // Loading city data from file
        try(Scanner sc = new Scanner(new FileInputStream("city_coordinates.txt"))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] lineSplit = line.split(", ");
                // Creating City objects and add them to the cities list
                cities.add(new City(lineSplit[0], Integer.parseInt(lineSplit[1]), Integer.parseInt(lineSplit[2])));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return;
        }

        // Loading connections data from file
        try (Scanner inp = new Scanner(new FileInputStream("city_connections.txt"))){
            while (inp.hasNextLine()){
                String line = inp.nextLine();
                String[] cityNames = line.split(",");
                // Find City objects by name
                City city1 = toCity(cities, cityNames[0]);
                City city2 = toCity(cities, cityNames[1]);

                // Creating Connection objects and add them to the connections list
                if (city1 != null && city2 != null){
                    connections.add(new Connection(city1, city2, Math.sqrt(Math.pow((city1.x - city2.x), 2) + Math.pow((city1.y - city2.y), 2))));
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
            return;
        }

        // Scanner for reading user input
        Scanner input = new Scanner(System.in);
        City startCity, endCity;

        // Asking the starting city
        while (true){
            System.out.print("Enter starting city: ");
            String startCityName = input.nextLine();
            String startCityCapitalized = startCityName.toUpperCase().charAt(0) + startCityName.toLowerCase().substring(1);
            startCity = toCity(cities, startCityName);
            if (startCity != null){
                break; // Exit loop if city is found
            }
            else {
                System.out.printf("City named '%s' not found. Please enter a valid city name.%n", startCityCapitalized);
            }
        }

        // Asking the destination city
        while (true){
            System.out.print("Enter destination city: ");
            String endCityName = input.nextLine();
            String endCityCapitalized = endCityName.toUpperCase().charAt(0) + endCityName.toLowerCase().substring(1);
            endCity = toCity(cities, endCityName);
            if (endCity != null){
                break; // Exit loop if city is found
            }
            else {
                System.out.printf("City named '%s' not found. Please enter a valid city name.%n", endCityCapitalized);
            }
        }

        // Calculate the shortest path between start and end cities
        List<City> shortestPath = findPath(cities, connections, startCity, endCity);

        // Check if a path is found
        if (shortestPath == null){
            System.out.println("No path could be found.");
        }
        else {
            // Calculate total distance of the path
            double totalDistance = getTotalDistance(shortestPath, connections);
            // Display the distance and the path. Used Locale package to display period instead of comma as a decimal separator
            System.out.printf(Locale.ROOT, "Total distance: %.2f. Path: ", totalDistance);
            for (int i = 0; i < shortestPath.size(); i++) {
                System.out.print(shortestPath.get(i).cityName);
                if (i < shortestPath.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            // Draw the map, cities, connections, and the shortest path
            drawMap(cities, connections, shortestPath);
        }
    }

    /**
     * Draws the map along with the cities, connections and the shortest path between two selected cities.
     * @param cities List of all cities.
     * @param connections List of all connections between cities.
     * @param shortestPath List of cities forming the shortest path between start and end city.
     */
    private static void drawMap(List<City> cities, List<Connection> connections, List<City> shortestPath){
        // Setting the drawing configurations and the map background
        StdDraw.setCanvasSize(2377/2, 1055/2);
        StdDraw.setXscale(0, 2377);
        StdDraw.setYscale(0, 1055);
        StdDraw.picture(2377/2.0, 1055/2.0, "map.png", 2377, 1055);
        StdDraw.enableDoubleBuffering(); // to improve performance

        // Loop through the cities to draw each city on the map
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.setFont(new Font("Arial", Font.BOLD, 12));
        for (City city: cities) {
            // Write city name and draw a filled circle at city's location
            StdDraw.text(city.x, city.y + 15, city.cityName);
            StdDraw.filledCircle(city.x, city.y, 5);
        }
        // Loop through the connections to draw lines between connected cities
        for (Connection connection: connections){
            // Draw a line representing the connection between two cities
            StdDraw.line(connection.city1.x, connection.city1.y, connection.city2.x, connection.city2.y);
        }

        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        // Check if the shortest path has more than one city
        if (shortestPath.size() > 1) {
            for (int i = 0; i < shortestPath.size() - 1; i++) {
                City city1 = shortestPath.get(i);
                City city2 = shortestPath.get(i + 1);
                // Highlight the path and the cities
                StdDraw.setPenRadius(0.006);
                StdDraw.line(city1.x, city1.y, city2.x, city2.y);
                StdDraw.setPenRadius();
                StdDraw.text(city1.x, city1.y + 15, city1.cityName);
                StdDraw.filledCircle(city1.x, city1.y, 5);
                if (city2.equals(shortestPath.get(shortestPath.size() - 1))) {
                    StdDraw.filledCircle(city2.x, city2.y, 5);
                    StdDraw.text(city2.x, city2.y + 15, city2.cityName);
                }
            }
        }
        // Special case when the start city and the destination city are the same
        else if (shortestPath.size() == 1){
            // Highlight only the single city
            StdDraw.filledCircle(shortestPath.get(0).x, shortestPath.get(0).y, 5);
            StdDraw.text(shortestPath.get(0).x, shortestPath.get(0).y + 15, shortestPath.get(0).cityName);
        }

        StdDraw.show();
    }

    /**
     * Calculates the shortest path between two cities using a simplified version of Dijkstra's algorithm.
     * @param cities List of all cities.
     * @param connections List of all connections between cities.
     * @param startCity The city from which to start.
     * @param endCity The destination city.
     * @return A list of cities forming the shortest path from start to end, or null if no path exists.
     */
    private static List<City> findPath(List<City> cities, List<Connection> connections, City startCity, City endCity) {
        // Special case when the start and end cities are the same
        if (startCity.equals(endCity)){
            List<City> sameCityPath = new ArrayList<>();
            sameCityPath.add(startCity);
            return sameCityPath;
        }

        boolean[] visited = new boolean[cities.size()]; // Array to store if the cities are visited or not
        double[] distances = new double[cities.size()]; // Array to store the distances of the cities to start city
        City[] previous = new City[cities.size()]; // Array to store the preceding city on the path

        for (int i = 0; i < cities.size(); i++) {
            distances[i] = Double.MAX_VALUE; // initializing the distances to max value
        }

        distances[indexOfCity(cities, startCity)] = 0; // Adjust the distance of the starting city to itself to 0

        // Loop for arranging the distances between cities and starting city
        while (true) {
            int minIndex = -1;
            double minDistance = Double.MAX_VALUE; // initializing the distance to maximum
            for (int i = 0; i < cities.size(); i++) {
                // If it finds an unvisited city whose distance to starting city is minimum, it takes the minimum distance
                // as its distance and minimum index as its index
                if (!visited[i] && distances[i] < minDistance) {
                    minDistance = distances[i];
                    minIndex = i;
                }
            }

            // If there is no unvisited city left or destination city is reached, it breaks out the loop
            if (minIndex == -1 || cities.get(minIndex).equals(endCity)) {
                break;
            }

            visited[minIndex] = true; // setting the city as visited

            // Loop through the connections to take the index of the neighbor cities and set their distances to starting cities
            for (Connection connection : connections) {
                if (connection.city1.equals(cities.get(minIndex))) {
                    int neighborIndex = indexOfCity(cities, connection.city2); // index of the neighbor city
                    double newDistance = distances[minIndex] + connection.distance; // distance of the neighbor to starting city
                    // If it finds a smaller distance than the value that had been defined before, assign the new value as its distance
                    if (newDistance < distances[neighborIndex]) {
                        distances[neighborIndex] = newDistance;
                        previous[neighborIndex] = cities.get(minIndex);
                    }
                }
                // for the other end of the connection
                else if (connection.city2.equals(cities.get(minIndex))) {
                    int neighborIndex = indexOfCity(cities, connection.city1);
                    double newDistance = distances[minIndex] + connection.distance;
                    if (newDistance < distances[neighborIndex]) {
                        distances[neighborIndex] = newDistance;
                        previous[neighborIndex] = cities.get(minIndex);
                    }
                }
            }
        }
        // If the destination city has no neighboring cities, which means there is no path, returns null
        if (previous[indexOfCity(cities, endCity)] == null){
            return null;
        }
        // Constructing the shortest path from end to start
        List<City> shortestPath = new ArrayList<>();
        for (City city = endCity; city != null; city = previous[indexOfCity(cities, city)]){
            shortestPath.add(0, city);
        }
        return shortestPath;

    }

    /**
     * Calculates the total distance of the shortest path.
     * @param shortestPath List of cities forming the shortest path.
     * @param connections List of all connections between cities.
     * @return The total distance of the path.
     */
    private static double getTotalDistance(List<City> shortestPath, List<Connection> connections) {
        double totalDistance = 0; // Initialize total distance
        // Loop through each pair of cities in the path
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            City city1 = shortestPath.get(i);
            City city2 = shortestPath.get(i + 1);
            // Find the connection between two cities and add its distance to the total distance
            for (Connection connection : connections) {
                if ((connection.city1.equals(city1) && connection.city2.equals(city2)) || (connection.city1.equals(city2) && connection.city2.equals(city1))) {
                    totalDistance += connection.distance;
                    break; // Break when the correct connection is found and total distance is incremented
                }
            }
        }
        return totalDistance;
    }

    /**
     * Finds the index of a city in the array of cities based on the city object.
     * @param cities List of all cities.
     * @param city The city to find.
     * @return The index of the city in the array, or -1 if not found.
     */
    private static int indexOfCity(List<City> cities, City city){
        // Loop through the list of cities
        for (int i = 0; i < cities.size(); i++) {
            // Return index if the current city matches the target city
            if (cities.get(i).equals(city)){
                return i;
            }
        }
        return -1; // Return -1 if not found
    }

    /**
     * Converts a city name to a City object by searching the array of cities.
     * @param cities List of all cities.
     * @param name The name of city to find.
     * @return The City object matching the name, or null if not found.
     */
    private static City toCity(List<City> cities, String name){
        // Loop through the list of cities
        for (City city : cities){
            // Return the city if its name matches the target name. case-insensitive
            if (city.cityName.equalsIgnoreCase(name)){
                return city;
            }
        }
        return null; // Return null if not found
    }
}
