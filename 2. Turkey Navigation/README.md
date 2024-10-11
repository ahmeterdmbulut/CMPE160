# Turkey City Navigation Application

This project implements a Turkey city navigation application in Java. The goal of the application is to find and display
the shortest path between two input cities on a 2D map using the **StdDraw** graphics library. The project reads city 
and road data from text files and uses an efficient path-finding algorithm, which is **Dijkstra's algorithm** in this project,
to calculate the shortest path between the cities, while also outputting the total distance and the route taken.

## Features

- **Shortest Path Calculation**: The program calculates the shortest path between two cities using **Dijkstra's algorithm**.
- **Graphical Representation**: Cities and connections are displayed on a graphical map using the **StdDraw** library.
- **Console Output**: The total distance and the route between two cities are printed to the console.
- **Error Handling**: Includes validation for invalid city names, unreachable cities, and pathfinding within the same city.
- **Flexible Input**: The program can adapt to different sets of cities and roads by simply changing the input files.

## Project Structure

- **`AhmetErdemBulut.java`**: Main class that handles user input and controls the city navigation.
- **`City.java`**: A class representing individual cities, including their coordinates and connections to other cities.
- **`Connection.java`**: A class representing the connections (roads) between cities and their distances.
- **`city_coordinates.txt`**: Contains the coordinates (x, y) of each city. Example format: CityName, XCoordinate, YCoordinate
- **`city_connections.txt`**: Contains the road connections between cities. Each line represents a connection between two cities:
City1, City2, Distance

## How It Works
- The program reads city data from **city_coordinates.txt** and road data from **city_connections.txt**.
- It constructs a graph where each city is a node, and the roads are edges with associated distances.
- Using **Dijkstra's algorithm**, the shortest path is calculated and displayed graphically.
- The program outputs the total distance and the path in both graphical and console formats.

### Example Scenarios
1. **Valid Path**:

   - **Input**: Starting city: "**Edirne**", Destination city: "**Giresun**".
   - **Output**: Total distance and shortest path.

2. **Invalid City Name**:

   - **Input**: Starting city: "**Anka**".
   - **Output**: City named '**Anka**' not found. Please enter a valid city name.

3. **Same City**:

   - **Input**: Starting and destination city: "**Istanbul**".
   - **Output**: Total Distance: 0.00. Path: **Istanbul**

4. **Unreachable Cities**:

   - **Input**: Starting city: "**Izmir**", Destination city: "**Van**".
   - **Output**: No path could be found.