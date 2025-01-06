package com.gbursali.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class DataManager {
    protected DataManager(){ }
    public static final List<BaseData> createdDataList = new ArrayList<>();

    /**
     * Finds the first {@link BaseData} of the given type in the collection.
     *
     * @param type The type of the data to be found.
     * @return An {@link Optional} containing the first data of the given type, or an empty {@link Optional} if no such data exists.
     */
    public static <T extends BaseData> Optional<T> getFirst(Class<T> type) {
        return getWithType(type)
                .findFirst();
    }

    /**
     * Returns a stream of all elements in the `createdDataList` that are instances of the specified type.
     *
     * @param type The class type to filter the elements by.
     * @param <T>  The type parameter extending BaseData.
     * @return A stream of elements of the specified type.
     */
    public static <T extends BaseData> Stream<T> getWithType(Class<T> type) {
        return createdDataList
                .stream()
                .filter(type::isInstance)
                .map(type::cast);
    }

    /**
     * Finds the first {@link BaseData} with the given identifier in the collection.
     *
     * @param id The identifier of the data to be found.
     * @return An {@link Optional} containing the first data with the given identifier, or an empty {@link Optional} if no such data exists.
     */
    public static Optional<BaseData> getWithId(String id) {
        return createdDataList
                .stream()
                .filter(x -> x.identifier.equalsIgnoreCase(id))
                .findFirst();
    }

    /**
     * Finds the first {@link BaseData} of the given type with the given identifier in the collection.
     *
     * @param type The type of the data to be found.
     * @param id   The identifier of the data to be found.
     * @return An {@link Optional} containing the first data of the given type with the given identifier, or an empty {@link Optional} if no such data exists.
     */
    public static <T extends BaseData> Optional<T> getWithId(Class<T> type, String id) {
        return getWithType(type)
                .filter(x -> x.identifier.equalsIgnoreCase(id))
                .findFirst();
    }

    /**
     * Saves the given data into the collection at the top of the list.
     *
     * @param data The data to be saved.
     */
    public static <T extends BaseData> void save(T data) {
        boolean canAdd = createdDataList
                .stream()
                .filter(x -> Objects.equals(x.identifier, data.identifier))
                .findFirst()
                .isEmpty();
        if (canAdd)
            createdDataList.add(0, data);
    }

    /**
     * Clears the collection of saved data.
     * <p>
     * This will remove all the saved data that has been stored in the collection.
     */
    public static void clear() {
        createdDataList.clear();
    }

    /**
     * Clears the collection of saved data.
     * <p>
     * This will remove all the saved data that has been stored in the collection.
     */
    public static <T extends BaseData> void clear(Class<T> type) {
        createdDataList.removeIf(type::isInstance);
    }
}
