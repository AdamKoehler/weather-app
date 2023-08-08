package com.example;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        String apiKey = "a438dc4b1c63b9a83f6dbb4ef23c00cc";
        System.out.println("Please enter the city name: ");
        String city = userInput.nextLine();
        userInput.close();
        try {
            //make the API request
            URL url = new URL("http://api.weatherstack.com/current" + "?access_key=" + apiKey + "&query=" +  city);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            //extract the JSON object from the callback function before parsing it
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

            String jsonResponse = response.toString();
            // FOR PRINTING RAW JSON DATA
                //remove the callback function from json
                //jsonResponse = jsonResponse.replace("CALLBACK_FUNCTION(", "").replace("})", "}");
                // print the raw json data
                //System.out.println(jsonResponse + "\n");

            // Parse the JSON data into a JSONObject
            JSONObject jsonObject = new JSONObject(jsonResponse);  
            //for this app I just want to return the location/observation time, temperature(f), humidity, precipitation, wind speed
                JSONObject requestObject = jsonObject.getJSONObject("request");
                JSONObject currentObject = jsonObject.getJSONObject("current");
                String cityQuery = requestObject.getString("query");
                String observedTime = currentObject.getString("observation_time");
                int tempF = ((currentObject.getInt("temperature") * (9/5)) + 32); // converts C to F on the call
                int humidity = currentObject.getInt("humidity");
                double precipitation = currentObject.getInt("precip") / 25.4; // converts mm to inch
                double windSpeed = currentObject.getInt("wind_speed") / 1.609; // converts km/h to mph
                String windDirection = currentObject.getString("wind_dir");
                // will use this to format data
                DecimalFormat decimalFormat = new DecimalFormat("#.#"); 
                String roundedPrecip = decimalFormat.format(precipitation);
                String roundedWindSpeed = decimalFormat.format(windSpeed);
                // basic information has been called and stored. print it
                System.out.println("The most recent API data shows:\n");
                System.out.println(cityQuery + "\n" + observedTime+ "\n" + "\nTemperature in Fahrenheit: " + tempF + "\n" + "Humidity: " + humidity + "%rh");
                System.out.println("Precipitation: " + roundedPrecip + " Inches\n" + "WindSpeed/Direction: " + roundedWindSpeed + "MPH "  + windDirection + "\n" + "Thanks for using the weather app!"); 
            }
            else {
                System.out.println("API request failed. Response code: " + responseCode);
            }
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
}