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
import com.fasterxml.jackson.databind.node.JsonNodeType;

import io.github.cdimascio.dotenv.Dotenv;

public class App {
    public static void main(String[] args) {

        System.out.println();

        Scanner scanner = new Scanner(System.in);

        // String word = "wor";

        Dotenv dotenv = Dotenv.load();
        // String collegeDictionaryAPIKey =
        // dotenv.get("MERRIAM_WEBSTER_COLLEGIATE_DICTIONARY_API_KEY");
        String learnerDictionaryAPIKey = dotenv.get("MERRIAM_WEBSTER_LEARNERS_DICTIONARY_API_KEY");

        // System.out.println(collegeDictionaryAPIKey);
        // System.out.println(learnerDictionaryAPIKey);

        while (true) {

            System.out.println("----------Welcome to the learner's dictionary----------\n");

            System.out.print("Enter an English word to search: ");
            String word = scanner.nextLine();

            System.out.println();

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

                    // System.out.println(root.get(0).getNodeType() == JsonNodeType.STRING);

                    // JsonNode definitions = root.get(0).get("shortdef");

                    // JsonNodeType typeOfNode = definitions.getNodeType();

                    // System.out.println(typeOfNode + "\n");

                    int numberOfEntries = root.size();
                    int numberOfDefinitions = 0;

                    int i = 0;
                    int j;

                    System.out.println("\nWord: " + word + "\n");

                    if (numberOfEntries > 0) {

                        while (i < numberOfEntries) {

                            if (root.get(i).isTextual()) {

                                System.out.println(
                                        "Could not find any definitions for the provided word. Try these words instead:-\n");

                                System.out.print("{");

                                j = 0;

                                while (j < numberOfEntries) {

                                    if (j == numberOfEntries - 1) {

                                        System.out.print(root.get(j).asText());

                                    } else {

                                        System.out.print(root.get(j).asText() + ", ");

                                    }

                                    j++;

                                }

                                System.out.print("}");

                                System.out.println();

                                return;

                            } else if (root.get(i).isObject()) {

                                if (root.get(i).has("shortdef")) {

                                    JsonNode definition = root.get(i).get("shortdef");
                                    // definitions_exist = true;
                                    j = 0;

                                    if (definition.isNull()) {

                                        // definitions_exist = false;
                                        i++;
                                        continue;

                                    } else if (definition.getNodeType() == JsonNodeType.ARRAY) {

                                        if (definition.size() == 0) {

                                            i++;
                                            continue;

                                        } else {

                                            while (j < definition.size()) {

                                                numberOfDefinitions++;
                                                System.out.println("Definition: " + definition.get(j).asText());
                                                j++;

                                            }

                                        }

                                    }

                                } else {

                                    i++;
                                    continue;

                                }

                            }

                            System.out.println();
                            i++;

                        }

                        if (numberOfDefinitions == 0) {

                            System.out.println("No definitions were found for the word!");

                        } else {

                            System.out.println("-------------------------------\n");

                            continue;

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

                scanner.close();

            } catch (ConnectException e) {
                System.out.println("Network Issue! Could not establish connection to the server.");
            } catch (InterruptedException g) {
                System.out.println("Unexpected Interruption.");
            } catch (IOException i) {
                System.out.println("Input Output issue.");
            } catch (IllegalArgumentException h) {
                System.out.println("Runtime error.");
            }
        }
    }
}