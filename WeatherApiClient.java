import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeatherApiClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your city name:");
        String city = scanner.nextLine().trim();

        String apiKey = "8671e27df22e659c257dd16574216c22"; 
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";

        try {
            // Create HTTP client and request
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            // Send the request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            if (root.has("main")) {
                String cityName = root.path("name").asText();
                double temperature = root.path("main").path("temp").asDouble();
                int humidity = root.path("main").path("humidity").asInt();
                String weatherDescription = root.path("weather").get(0).path("description").asText();

                // Display data in structured format
                System.out.println("\n--- Weather Data for " + cityName + " ---");
                System.out.println("Temperature: " + temperature + " °C");
                System.out.println("Humidity: " + humidity + " %");
                System.out.println("Condition: " + weatherDescription);
            } else {
                System.out.println("Error fetching data. Please check city name or API key.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
