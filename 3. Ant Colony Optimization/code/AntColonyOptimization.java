import java.util.ArrayList;
import java.util.Random;

/**
 * Implements the Ant Colony Optimization algorithm to solve the problem by simulating the behavior of ants searching
 * for the shortest route between points
 */
public class AntColonyOptimization {
    private static final double ALPHA = 0.9; // Influence of pheromone trails on ant's route decision
    private static final double BETA = 2.3; // Influence of distance
    private static final double DEGRADATION = 0.5; // Rate at which pheromones evaporate
    private static final double Q = 0.0001; // Constant used to calculate pheromone deposition
    private static final double INIT_PHEROMONE = 0.1; // Initial pheromone level on paths
    private static final double NUMBER_OF_ITERATIONS = 150;
    private static final double ANT_PER_ITERATION = 100;
//    private static final double NUMBER_OF_ITERATIONS = 100; // default values
//    private static final double ANT_PER_ITERATION = 50;
    private static Random random = new Random(); // Random generator
    private static double[][] pheromones; // Matrix to store pheromone levels
    private static double[][] distances; // Matrix to store distances between cities

    /**
     * Optimizes the routing to find the shortest path between locations using ACO
     * @param locations List of all locations with coordinates
     * @param displayMethod Determines the type of visualization for the result (pheromone map or shortest path)
     */
    public static void optimize(ArrayList<ArrayList<Double>> locations, int displayMethod){
        double startTime = System.currentTimeMillis(); // Starting time for timing the method execution

        ArrayList<Integer> bestRoute = new ArrayList<>(); // List to store the best route found
        double bestDistance = Double.MAX_VALUE; // Variable to store the shortest distance found
//        double currentBest = Double.MAX_VALUE; // This is defined to make finding when it finds a new shortest path easier
        initializeRoads(locations); // Initialize distances and pheromones

        // Main loop to perform the optimization across a specified number of iterations
        for (int i = 0; i < NUMBER_OF_ITERATIONS; i++){
            for (int j = 0; j < ANT_PER_ITERATION; j++){
                ArrayList<Integer> route = createRoute(locations); // Create a route for one ant
                route = reorder(route); // Reorder the route starting from the first city
                double routeLength = calculateRouteDistance(route); // Calculate the total length of the route
                // Check if the new route is the best one found so far
                if (routeLength < bestDistance){
                    bestRoute = route;
                    bestDistance = routeLength;
                }
                updatePheromones(route, routeLength); // Update pheromones based on the route taken
            }
//            if (bestDistance < currentBest){ // This part is to use at finding when it finds a new shortest path
//                currentBest = bestDistance;
//                System.out.println("Shortest distance at " + (i + 1) + "th iteration is: " + currentBest);
//            }
            evaporatePheromones(); // Evaporate pheromones to simulate natural degradation
        }
        // Display results based on the specified method
        if (displayMethod == 1) {
            drawPheromoneMap(locations);
        } else if (displayMethod == 2) {
            drawShortestPath(locations, bestRoute);
        }

        double endTime = System.currentTimeMillis(); // Ending time
        double duration = (endTime - startTime) / 1000; // Duration in seconds
        System.out.println("Method: Ant Colony Optimization");
        System.out.printf("Shortest Distance: %.5f%n", bestDistance);
        System.out.print("Shortest path: [");
        // Printing each node in the route
        for (int i = 0; i < bestRoute.size(); i++){
            if (i == bestRoute.size() - 1){
                System.out.println(bestRoute.get(i) + 1 + "]");
            } else {
                System.out.print(bestRoute.get(i) + 1 + ", ");
            }
        }
        System.out.printf("Time it takes to find the shortest path: %.2f seconds%n", duration);
    }

    /**
     * Initializes pheromone levels and distances based on given locations
     * @param locations List of all locations with coordinates
     */
    private static void initializeRoads(ArrayList<ArrayList<Double>> locations){
        int n = locations.size();
        pheromones = new double[n][n]; // Keeps the pheromone levels between two cities
        distances = new double[n][n]; // Keeps the distances between two cities

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++){
                distances[i][j] = distance(locations.get(i), locations.get(j)); // Calculate distance
                pheromones[i][j] = INIT_PHEROMONE; // Initialize pheromones
            }
        }
    }

    /**
     * Creates a random route based on pheromone levels and distances
     * @param locations List of all locations with coordinates
     * @return A route as a list of indices representing the visitation order
     */
    private static ArrayList<Integer> createRoute(ArrayList<ArrayList<Double>> locations){
        ArrayList<Integer> route = new ArrayList<>();
        int n = locations.size();
        boolean[] visited = new boolean[n]; // Track visited points
        int current = random.nextInt(n); // Start from a random point
        int first = current;

        route.add(first);
        visited[first] = true;

        for (int i = 1; i < n; i++) {
            current = choosePoint(current, visited, locations);
            route.add(current);
            visited[current] = true;
        }
        route.add(first); // Complete the cycle by returning to the starting point
        return route;
    }

    /**
     * Selects the next point to visit based on probability calculated from pheromone levels and distances
     * @param current The current point index
     * @param visited Array indicating whether each point has been visited
     * @param locations A list of all locations with coordinates
     * @return The index of the next point to visit
     */
    private static int choosePoint(int current, boolean[] visited, ArrayList<ArrayList<Double>> locations){
        double[] probabilities = new double[locations.size()]; // Probabilities of moving to each point
        double sum = 0;
        for (int i = 0; i < locations.size(); i++) {
            if (!visited[i]){
                probabilities[i] = Math.pow(pheromones[current][i], ALPHA) / Math.pow(distances[current][i], BETA);
                sum += probabilities[i];
            }
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum; // Normalize probabilities
        }

        return randomSelect(probabilities); // Randomly select next point based on probabilities
    }

    /**
     * Randomly selects an index based on an array of probabilities
     * @param probabilities An array of probabilities of moving to each point
     * @return The selected index
     */
    private static int randomSelect(double[] probabilities){
        double r = random.nextDouble();
        double sum = 0;
        for (int i = 0; i < probabilities.length; i++){
            sum += probabilities[i];
            if (sum >= r){
                return i;
            }
        }
        return probabilities.length - 1; // Return the last index if no other index was selected
    }

    /**
     * Updates pheromone levels along the path taken by an ant
     * @param route The route taken by the ant
     * @param routeLength The total distance of the route
     */
    private static void updatePheromones(ArrayList<Integer> route, double routeLength){
        for (int i = 0; i < route.size() - 1; i++){
            int from = route.get(i);
            int to = route.get(i + 1);
            pheromones[from][to] += Q / routeLength;
            pheromones[to][from] = pheromones[from][to];
        }
    }

    /**
     * Reduces all pheromone levels to simulate evaporation over time
     */
    private static void evaporatePheromones(){
        for (int i = 0; i < pheromones.length; i++){
            for (int j = 0; j < pheromones[i].length; j++){
                pheromones[i][j] *= DEGRADATION; // Reduce pheromone level by the degradation rate
            }
        }
    }

    /**
     * Reorders the route to start from the point with index 0
     * @param route The original route
     * @return A reordered route starting and ending at the point with index 0
     */
    private static ArrayList<Integer> reorder(ArrayList<Integer> route){
        int startIndex = route.indexOf(0);
        if (startIndex == 0){
            return route;
        }
        ArrayList<Integer> reorderedRoute = new ArrayList<>();
        reorderedRoute.addAll(route.subList(startIndex, route.size()));
        reorderedRoute.addAll(route.subList(1, startIndex + 1));
        return reorderedRoute;
    }

    /**
     * Calculates the total distance for a given route
     * @param route The route for which to calculate distance
     * @return The total distance of the route
     */
    private static double calculateRouteDistance(ArrayList<Integer> route){
        double length = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            length += distances[route.get(i)][route.get(i + 1)];
        }
        return length;
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
     * Visualizes the pheromone levels on paths
     * @param locations A list of locations with coordinates
     */
    private static void drawPheromoneMap(ArrayList<ArrayList<Double>> locations){
        int canvasWidth = 800;
        int canvasHeight = 800;
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.enableDoubleBuffering();

        // Drawing the lines
        for (int i = 0; i < locations.size(); i++) {
            for (int j = 0; j < locations.size(); j++){
                if (i != j){
                    double x1 = locations.get(i).get(0);
                    double y1 = locations.get(i).get(1);
                    double x2 = locations.get(j).get(0);
                    double y2 = locations.get(j).get(1);

                    double pheromoneLevel = pheromones[i][j];
                    StdDraw.setPenRadius(pheromoneLevel);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.line(x1, y1, x2, y2);
                }
            }
        }
        // Drawing the points
        for (int i = 0; i< locations.size(); i++){
            ArrayList<Double> location = locations.get(i);
            double x = location.get(0);
            double y = location.get(1);
            StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            StdDraw.filledCircle(x, y, 0.02);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(x, y, Integer.toString(i + 1));
        }
        StdDraw.show();
    }

    /**
     * Visualizes the shortest route
     * @param locations A list of locations with coordinates
     * @param bestRoute A list of shortest route
     */
    private static void drawShortestPath(ArrayList<ArrayList<Double>> locations, ArrayList<Integer> bestRoute){
        int canvasWidth = 800;
        int canvasHeight = 800;
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);

        // Drawing the lines
        ArrayList<Double> firstLocation = locations.get(0);
        double locationX = firstLocation.get(0);
        double locationY = firstLocation.get(1);
        for (int i = 1; i < bestRoute.size(); i++) {
            ArrayList<Double> nextLocation = locations.get(bestRoute.get(i));
            double nextLocationX = nextLocation.get(0);
            double nextLocationY = nextLocation.get(1);
            StdDraw.line(locationX, locationY, nextLocationX, nextLocationY);
            locationX = nextLocationX;
            locationY = nextLocationY;
        }

        // Drawing the points
        for (int i = 0; i< locations.size(); i++){
            ArrayList<Double> location = locations.get(i);
            double x = location.get(0);
            double y = location.get(1);
            if (i == 0) {
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
            } else {
                StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
            }
            StdDraw.filledCircle(x, y, 0.02);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.text(x, y, Integer.toString(i + 1));
        }
        StdDraw.show();
    }
}
