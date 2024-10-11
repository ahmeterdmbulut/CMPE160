/**
 * Represents a city with a name and coordinates on a map.
 */
public class City {
    public String cityName; // Name of the city
    public int x, y; // X and Y coordinates of the city on the map

    /**
     * Constructs a City instance with specified name and coordinates.
     * @param cityName Name of the city
     * @param x X coordinate of the city
     * @param y Y coordinate of the city
     */
    public City(String cityName, int x, int y){
        this.cityName = cityName;
        this.x = x;
        this.y = y;
    }
}
