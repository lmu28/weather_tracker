package org.example.util;

import org.example.service.dto.LocationDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JsonParserTest {


    double lon = -0.1257;
    double lat = 51.5085;
    double temp = 289.44;

    String name = "London";


    String json = "{\n" +
            "    \"coord\": {\n" +
            "        \"lon\": -0.1257,\n" +
            "        \"lat\": 51.5085\n" +
            "    },\n" +
            "    \"weather\": [\n" +
            "        {\n" +
            "            \"id\": 803,\n" +
            "            \"main\": \"Clouds\",\n" +
            "            \"description\": \"broken clouds\",\n" +
            "            \"icon\": \"04d\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"base\": \"stations\",\n" +
            "    \"main\": {\n" +
            "        \"temp\": 289.44,\n" +
            "        \"feels_like\": 289.19,\n" +
            "        \"temp_min\": 288.59,\n" +
            "        \"temp_max\": 290.45,\n" +
            "        \"pressure\": 1030,\n" +
            "        \"humidity\": 79,\n" +
            "        \"sea_level\": 1030,\n" +
            "        \"grnd_level\": 1026\n" +
            "    },\n" +
            "    \"visibility\": 10000,\n" +
            "    \"wind\": {\n" +
            "        \"speed\": 3.09,\n" +
            "        \"deg\": 190\n" +
            "    },\n" +
            "    \"clouds\": {\n" +
            "        \"all\": 75\n" +
            "    },\n" +
            "    \"dt\": 1729692518,\n" +
            "    \"sys\": {\n" +
            "        \"type\": 2,\n" +
            "        \"id\": 2075535,\n" +
            "        \"country\": \"GB\",\n" +
            "        \"sunrise\": 1729665516,\n" +
            "        \"sunset\": 1729702241\n" +
            "    },\n" +
            "    \"timezone\": 3600,\n" +
            "    \"id\": 2643743,\n" +
            "    \"name\": \"London\",\n" +
            "    \"cod\": 200\n" +
            "}";

    JsonParser jsonParser = new JsonParser();

    @Test
    void fromJson() {

        LocationDTO ld = jsonParser.fromJson(json, LocationDTO.class);

        assertThat(ld).
                matches(l -> l.getName().equals(name)).
                matches(l -> l.getMain().getTemp() == temp).
                matches(l -> l.getCoord().getLon() == lon).
                matches(l -> l.getCoord().getLat() == lat);

    }
}