package com.zaknesler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws IOException, JSONException
    {
        System.out.println("Sunrise and Sunset");
        System.out.println("------------------");

        Scanner input = new Scanner(System.in);

        System.out.print("Enter a city: ");
        String city = input.nextLine();

        System.out.print("Enter a state (abbreviation): ");
        String state = input.nextLine();

        System.out.println("-----------\n");

        if (state.isEmpty() || city.isEmpty()) {
            System.out.println("Invalid input.");

            System.exit(0);
        }

        JSONObject json = readJsonFromUrl("http://api.wunderground.com/api/[Redacted]/astronomy/q/" + state.replace(" ", "_") + "/" + city.replace(" ", "_") + ".json");

        String sunrise = json.getJSONObject("sun_phase").getJSONObject("sunrise").getString("hour") + ":" + json.getJSONObject("sun_phase").getJSONObject("sunrise").getString("minute");
        String sunset = json.getJSONObject("sun_phase").getJSONObject("sunset").getString("hour") + ":" + json.getJSONObject("sun_phase").getJSONObject("sunset").getString("minute");

        System.out.println("In " + city + ", " + state + " today, the sunrise time is " + sunrise + " and the sunset time is " + sunset + ".");
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException
    {
        InputStream is = new URL(url).openStream();

        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);

            return new JSONObject(jsonText);
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException
    {
        StringBuilder sb = new StringBuilder();

        int cp;

        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }

        return sb.toString();
    }
}
