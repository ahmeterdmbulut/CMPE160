/**
 * Represents a connection between two cities along with the distance of the connection.
 */
public class Connection {
    public City city1, city2; // The two cities connected by this connection
    public double distance; // The distance between the two cities

    /**
     * Constructs a Connection instance between two cities.
     * @param city1 The first city of the connection.
     * @param city2 The second city of the connection.
     * @param distance The distance between cities.
     */
    public Connection(City city1, City city2, double distance){
        this.city1 = city1;
        this.city2 = city2;
        this.distance = distance;
    }

}
