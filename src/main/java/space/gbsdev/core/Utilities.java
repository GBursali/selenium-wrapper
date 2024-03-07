package space.gbsdev.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.junit.Assume;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utilities {

    private Utilities(){}

    /**
     * Reads the contents of a file and returns them as a string.
     *
     * @param file The path to the file to read.
     * @return The contents of the file as a string.
     * @throws IllegalArgumentException If the file does not exist or if there is a problem reading the file.
     */
    public static String getFileContents(Path file) {
        if (!Files.exists(file))
            throw new IllegalArgumentException("Requested file is not found: " + file);

        try {
            return Files.readString(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("There is a problem in reading the file: " + file, e);
        }
    }

    /**
     * Reads the contents of a JSON file and parses it into a JsonObject.
     *
     * @param file The path to the JSON file to read.
     * @return The contents of the JSON file as a JsonObject.
     * @throws IllegalArgumentException If the file does not exist, if there is a problem reading the file, or if there is a syntax error in the JSON content.
     */
    public static JsonObject getJsonFile(Path file) {
        try {
            String fileContents = getFileContents(file);
            return JsonParser.parseString(fileContents).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("There is a syntax error in the JSON file: " + file, e);
        }
    }

}
