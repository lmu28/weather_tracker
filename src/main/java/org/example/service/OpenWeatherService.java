package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.service.dto.LocationDTO;
import org.example.util.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;



public class OpenWeatherService {

    public static final String API_KEY = "593e75bebaa36ba789c333f5553ac921";
    private final static String URL = "https://api.openweathermap.org/data/2.5/weather";

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    private final JsonParser jsonParser = new JsonParser();



    public LocationDTO getLocationByName(String name){
        Map<String, String> params = Map.of("q", name, "appid", API_KEY);
        String url = buildRequestUrl(params);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());;
            LocationDTO locationDTO = jsonParser.fromJson(response.body(), LocationDTO.class);
            if (locationDTO.getName() == null){
                return null;
            }else  return locationDTO;
        } catch (IOException e) {
//            throw new RuntimeException(e);
        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
        }

        return null;


    }

    private String buildRequestUrl(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(URL);
        sb.append("?");
        for (Map.Entry<String,String> p: params.entrySet()){
            sb.append(p.getKey());
            sb.append("=");
            sb.append(p.getValue());
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
