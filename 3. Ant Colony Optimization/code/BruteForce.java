import java.util.ArrayList;

/**
 * Implements a brute-force approach to find the shortest path for the problem.
 * This class iteratively evaluates all possible routes to determine the one with the least distance.
 */
public class BruteForce {

    private static double bestDistance = Double.MAX_VALUE; // Stores the shortest distance found
    private static Integer[] bestRoute; // Stores the best route corresponding to the shortest distance.

    /**
     * Main method that finds the shortest route by examining all permutations of routes.
     * It uses a brute-force method which, while computationally expensive, guarantees to find the shortest path.
     * @param locations List of all locations with coordinates
     */
    public static void findRoute(ArrayList<ArrayList<Double>> locations) {
        double startTime = System.currentTimeMillis(); // Starting time for timing the method execution

        // Initialize route with locations indices, starting from 1 since 0 is considered the starting point
        Integer[] route = new Integer[locations.size() - 1];
        for (int i = 0; i < route.length; i++){
            route[i] = i + 1;
        }
        bestRoute = new Integer[locations.size() + 1];
        permute(route, 0, locations); // Begin permutation to explore all possible routes

        drawPath(locations); // Visualization of the best route

        double endTime = System.currentTimeMillis(); // Ending time
        double duration = (endTime - startTime) / 1000; // Duration in seconds
        System.out.println("Method: Brute-Force Method");
        System.out.printf("Shortest Distance: %.5f%n", bestDistance);
        System.out.print("Shortest path: [");
        // Printing each node in the route
        for (int i = 0; i < bestRoute.length; i++){
            if (i == bestRoute.length - 1){
                System.out.println(bestRoute[i] + 1 + "]");
            } else {
                System.out.print(bestRoute[i] + 1 + ", ");
            }
        }
        System.out.printf("Time it takes to find the shortest path: %.2f seconds%n", duration);

    }

    /**
     * Method to recursively permute routes and evaluate their distances
     * @param route Current permutation of routes being evaluated
     * @param k Current index in the permutation process
     * @param locations List of all locations with coordinates
     */
    private static void permute(Integer[] route, int k, ArrayList<ArrayList<Double>> locations) {
        if (k == route.length) {
            double currentDistance = calculateRouteDistance(route, locations);
            if (currentDistance < bestDistance) {
                bestDistance = currentDistance;
                bestRoute[0] = 0; // Start from location 0
                System.arraycopy(route, 0, bestRoute, 1, route.length);
                bestRoute[bestRoute.length - 1] = 0; // Return to location 0
            }
        } else {
            for (int i = k; i < route.length; i++) {
                int temp = route[k];
                route[k] = route[i];
                route[i] = temp;

                permute(route, k + 1, locations);

                temp = route[k];
                route[k] = route[i];
                route[i] = temp;
            }
        }
    }

    /**
     * Helper method to calculate the distance between two points
     * @param a First point as a list of doubles
     * @param b Second point as a list of doubles
     * @return Distance between point a and b
     */
    private static double distance(ArrayList<Double> a, ArrayList<Double> b){
        return Math.sqrt(Math.pow(a.get(0) - b.get(0), 2) + Math.pow(a.get(1) - b.get(1), 2));
    }

    /**
     * Calculates the total distance of a given route
     * @param route An array of indices indicating the route taken
     * @param locations List of all locations with coordinates
     * @return The total distance of the route
     */
    private static double calculateRouteDistance(Integer[] route, ArrayList<ArrayList<Double>> locations){
        double totalDistance = distance(locations.get(0), locations.get(route[0]));
        for (int i = 0; i < route.length - 1; i++){
            totalDistance += distance(locations.get(route[i]), locations.get(route[i + 1]));
        }
        totalDistance += distance(locations.get(route[route.length - 1]), locations.get(0));
        return totalDistance;
    }

    /**
     * Visualizes the best route
     * @param locations List of all locations with coordinates
     */
    private static void drawPath(ArrayList<ArrayList<Double>> locations){
        int canvasWidth = 800;
        int canvasHeight = 800;
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
        ArrayList<Double> firstLocation = locations.get(0);
        double locationX = firstLocation.get(0);
        double locationY = firstLocation.get(1);
        // Drawing the lines representing paths between points
        for (int idx : bestRoute) {
            ArrayList<Double> nextLocation = locations.get(idx);
            double nextLocationX = nextLocation.get(0);
            double nextLocationY = nextLocation.get(1);
            StdDraw.line(locationX, locationY, nextLocationX, nextLocationY);
            locationX = nextLocationX;
            locationY = nextLocationY;
        }

        StdDraw.line(locationX, locationY, firstLocation.get(0), firstLocation.get(1));
        // Drawing the points representing the houses
        for (int i = 0; i < locations.size(); i++) {
            ArrayList<Double> location = locations.get(i);
            if (i == 0) {
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            } else {
                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            }
            StdDraw.filledCircle(location.get(0), location.get(1), 0.02);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(location.get(0), location.get(1), Integer.toString(i + 1));
        }
        StdDraw.show();
    }
}
