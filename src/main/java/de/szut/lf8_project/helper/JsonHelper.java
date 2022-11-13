package de.szut.lf8_project.helper;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.Set;

public class JsonHelper<T> {


    public static <T> T getDTOFromConnection(Class<T> c, HttpURLConnection connection) throws Exception{

        int responseCode = connection.getResponseCode();
        if (responseCode == connection.HTTP_OK) {
            // Create a reader with the input stream reader.
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;

            // Create a string buffer
            StringBuffer response = new StringBuffer();

            // Write each of the input line
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            var string = response.toString();

            var out = new Gson().fromJson(response.toString(), c);
            return new Gson().fromJson(response.toString(), c);

        } else {
            System.out.println("Error found !!!");
        }

        return null;
    }

}
