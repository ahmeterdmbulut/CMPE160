# Migros Delivery Route Optimization

This project implements a solution to the Migros delivery problem using two different algorithms: **Brute-Force** and 
**Ant Colony Optimization**. The aim is to determine the shortest route for a Migros delivery vehicle, which starts from a 
Migros store, visits several houses (each house is visited only once), and then returns to the store. The application 
reads the coordinates of the Migros store and houses from input files and outputs the optimal delivery path and the 
corresponding distance for each approach.

## Features

- **Shortest Route Calculation**: Determines the shortest route for the Migros delivery car using both
  **Brute-Force** and **Ant Colony Optimization** algorithms.
- **Graphical Output**: Visualizes the delivery route and pheromone intensity (for ACO) using the **StdDraw** library.
- **Performance Comparison**: Compares the time taken by both algorithms and their respective results.

## Project Structure

- **`AhmetErdemBulut.java`**: Main class responsible for controlling the program flow and selecting the algorithm 
(Brute-Force or Ant Colony Optimization).
- **`AntColonyOptimization.java`**: Implements the Ant Colony Optimization (ACO) algorithm to find a near-optimal delivery route.
- **`BruteForce.java`**: Implements the Brute-Force approach to calculate the optimal delivery route by trying all possible permutations.
- **Input Files**:
    - **`input01.txt`**, **`input02.txt`**, **`input03.txt`**, **`input04.txt`**, **`input05.txt`**: Text files containing the coordinates of the Migros store and the houses. The first line contains the coordinates of the Migros store, and subsequent lines represent house coordinates.

### Input File Format

Each input file contains the coordinates of the Migros store and the houses. The format is as follows:
- Migros X-coordinate, Migros Y-coordinate
- House1 X-coordinate, House1 Y-coordinate
- House2 X-coordinate, House2 Y-coordinate ...

## How It Works

1. **Brute-Force Approach**:
    - This method generates all possible permutations of house visits and evaluates each one to find the shortest path. 
It is computationally expensive but guarantees the optimal solution.

2. **Ant Colony Optimization (ACO) Approach**:
    - In this optimization method, simulated ants traverse the graph of houses and leave pheromones on edges (paths). 
Over multiple iterations, the pheromone trails guide the ants towards shorter paths. This method does not guarantee an optimal solution but is much faster and usually provides near-optimal results.


