package space.gbsdev.core;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assume;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Utilities {

    private Utilities(){}
    public static String getFileContents(Path file){
        Assume.assumeTrue("Requested file is not found: " + file,file.toFile().exists());
        try {
            return Files.readString(file);
        } catch (IOException e) {
            throw new IllegalArgumentException("There is a problem in reading the file: " + file);
        }
    }

    public static JsonObject getJsonFile(Path file){
        return JsonParser.parseString(getFileContents(file)).getAsJsonObject();
    }
}
