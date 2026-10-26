package com.uqbah;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static void main(String[] args) {

        System.out.println();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter an English word to search: ");
        String word = scanner.nextLine();

        Dotenv dotenv = Dotenv.load();
        // String collegeDictionaryAPIKey =
        // dotenv.get("MERRIAM_WEBSTER_COLLEGIATE_DICTIONARY_API_KEY");
        String learnerDictionaryAPIKey = dotenv.get("MERRIAM_WEBSTER_LEARNERS_DICTIONARY_API_KEY");

        String url = "https://www.dictionaryapi.com/api/v3/references/learners/json/" + word + "?key=" + learnerDictionaryAPIKey;

        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("\n----------Status Code----------");

            System.out.println(response.statusCode());

            System.out.println("\n----------Printing Response----------");

            System.out.println(response.body());

            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
        
    }
}
