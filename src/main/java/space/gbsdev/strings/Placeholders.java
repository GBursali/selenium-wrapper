package space.gbsdev.strings;

import org.apache.hc.core5.function.Supplier;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for replacing placeholders in strings.
 */
public class Placeholders {
    /**
     * Default value for a Prefix
     */
    private String prefix = "{";

    /**
     * Default value for a Suffix
     */
    private String suffix = "}";

    /**
     * Map to store placeholders and their corresponding value suppliers.
     */
    Map<String, Supplier<String>> placeholderList = new HashMap<>();

    /**
     * Map to store instant placeholders and their values.
     */
    Map<String, String> instantPlaceholderList = new HashMap<>();

    /**
     * Map to store placeholders that changes their value according to the match groups.
     */
    Map<String, Function<Matcher,String>> groupedPlaceholderList = new HashMap<>();

    /**
     * Hidden constructor to prevent direct instantiation.
     */
    protected Placeholders() {/*hidden*/}

    /**
     * Creates a new instance of {@link Placeholders}, loaded from a Properties file.
     * @param path Path of the file.
     * @return New instance of filled {@link Placeholders}
     */
    public static Placeholders ofConfiguration(String path) {
        Placeholders place = of();
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            properties.load(fileInputStream);
            for (String key : properties.stringPropertyNames()) {
                place.add(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return place;
    }


    /**
     * Creates an empty instance of Placeholders.
     *
     * @return A {@link Placeholders} object ready to receive {@link Placeholders#add(String,String)}.
     */
    public static Placeholders of() {
        return new Placeholders();
    }

    /**
     * Adds a new value to the placeholder list.
     *
     * @param key   Key to trigger this replacement.
     * @param value Value to replace.
     * @return Instance itself for chain support.
     */
    public Placeholders add(String key, String value) {
        instantPlaceholderList.put(key,value);
        return this;
    }

    /**
     * Adds a new value to the placeholder list.
     *
     * @param key   Key to trigger this replacement.
     * @param value Value lambda to be replaced.
     * @return Instance itself for chain support.
     */
    public Placeholders add(String key, Supplier<String> value) {
        placeholderList.put(key, value);
        return this;
    }

    /**
     * Adds a new value to the placeholder list.
     *
     * @param key   Key to trigger this replacement.
     * @param value Value lambda to be replaced.
     * @return Instance itself for chain support.
     */
    public Placeholders add(String key, Function<Matcher,String> value) {
        groupedPlaceholderList.put(key, value);
        return this;
    }

    /**
     * Define the prefix for placeholders.
     *
     * @param start The prefix to be set.
     * @return Instance itself for chain support.
     */
    public Placeholders definePrefix(String start) {
        prefix = start;
        return this;
    }

    /**
     * Define the suffix for placeholders.
     *
     * @param end The suffix to be set.
     * @return Instance itself for chain support.
     */
    public Placeholders defineSuffix(String end) {
        suffix = end;
        return this;
    }

    /**
     * Get placeholders with prefix applied.
     *
     * @return Map of placeholders with prefix applied.
     */
    private Map<String, String> getPlaceHoldersPrefixed() {
        Map<String, String> tempPlaceHolders = new HashMap<>();
        instantPlaceholderList.forEach((key, value) -> tempPlaceHolders.put(wrapWithKeys(key), value));
        placeholderList.forEach((key, value) -> tempPlaceHolders.put(wrapWithKeys(key), value.get()));
        return tempPlaceHolders;
    }

    /**
     * Replace placeholders in the input string with their corresponding values.
     *
     * @param input The input string containing placeholders.
     * @return The modified input string with placeholders replaced.
     */
    public String apply(String input) {
        final StringBuilder modifiedInput = new StringBuilder(input);
        getPlaceHoldersPrefixed().forEach((keyword, action) -> {
            Matcher matches = Pattern.compile(keyword).matcher(input);
            while (matches.find()) {
                modifiedInput.replace(matches.start(), matches.end(), action);
            }
        });
        groupedPlaceholderList.forEach((keyword,action)->{
            Matcher matches = Pattern.compile(keyword).matcher(input);
            while (matches.find()){
                modifiedInput.replace(matches.start(),matches.end(),action.apply(matches));
            }
        });
        return modifiedInput.toString();
    }

    /**
     * Get the value of a placeholder by key.
     *
     * @param key The key of the placeholder.
     * @return The value of the placeholder.
     */
    public Object get(String key) {
        return placeholderList.get(key).get();
    }

    /**
     * Wrap an input string with placeholders.
     *
     * @param input The input string to wrap.
     * @return The input string wrapped with placeholders.
     */
    public String wrapWithKeys(String input) {
        return prefix + input + suffix;
    }
}
