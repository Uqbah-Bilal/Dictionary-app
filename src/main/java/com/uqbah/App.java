package com.uqbah;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
// import java.nio.file.Paths;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

        System.out.println();
        // System.out.println(collegeDictionaryAPIKey);
        // System.out.println(learnerDictionaryAPIKey);

        String url = "https://www.dictionaryapi.com/api/v3/references/learners/json/" + word + "?key="
                + learnerDictionaryAPIKey;

        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("\n----------Status Code----------");

            int responseStatusCode = response.statusCode();

            System.out.println(responseStatusCode);

            if (responseStatusCode >= 200 && responseStatusCode < 300) {

                // System.out.println("Success.");
                // return;
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                System.out.println("Number of entries: " + root.size());
                System.out.println("\n----------Printing Response----------");
                System.out.println(root);
                System.out.println();

                int numberOfEntries = root.size();
                boolean definitions_exist = false;

                int i = 0;
                int j;

                System.out.println("Word: " + word + "\n");

                if (numberOfEntries > 0) {

                    while (i < numberOfEntries) {

                        JsonNode definitions = root.get(i).get("shortdef");
                        if (root.get(i).has("shortdef")) {

                            definitions_exist = true;
                            j = 0;

                            while (j < definitions.size()) {

                                System.out.println("Definition: " + definitions.get(j).asText());
                                j++;

                            }

                        } else {

                            i++;
                            continue;

                        }

                        System.out.println();
                        i++;

                    }

                    if (definitions_exist == false) {

                        System.out.println("No definitions were found for the word!");

                    } else {

                        return;

                    }

                } else {

                    System.out.println("Word not found.");

                }

            } else if (responseStatusCode >= 300 && responseStatusCode < 400) {

                System.out.println("Redirection.");

            } else if (responseStatusCode >= 400 && responseStatusCode < 500) {

                System.out.println("Client-side Error.");

            } else if (responseStatusCode >= 500 && responseStatusCode < 600) {

                System.out.println("Server-side Error!");

            }

        } catch (ConnectException e) {
            System.out.println("Network Issue! Could not establish connection to the server.");
        } catch (IOException i) {
            System.out.println("Input Output issue.");
        } catch (InterruptedException g) {
            System.out.println("Unexpected Interruption.");
        } catch (IllegalArgumentException h) {
            System.out.println("Runtime error.");
        }
    }
}
