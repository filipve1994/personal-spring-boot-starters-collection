package io.filipvde.customspringbootstarter.utils;

import com.google.common.collect.Sets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings({"squid:S2176"})
public final class CollectionUtils extends org.springframework.util.CollectionUtils {

private static final String UNMODIFIABLE_CLASS_NAME_START = "Unmodifiable";

    private CollectionUtils() {
        // hide utility from public
    }

    @Nonnull
    public static <T> List<T> getRemaining(@Nonnull final T element, @Nonnull final List<? extends T> list) {
        return list.stream()
                .filter(e -> !e.equals(element))
                .collect(Collectors.toList());
    }

    @Nonnull
    public static <T> List<T> copy(@Nonnull final List<? extends T> currentList) {
        return new ArrayList<>(currentList);
    }

    @Nullable
    public static <T> List<T> nullableCopy(@Nullable final List<T> currentList) {
        return Optional.ofNullable(currentList)
                .map(CollectionUtils::copy)
                .orElse(null);
    }

    @Nonnull
    public static <T> Collection<T> copy(@Nonnull final Collection<? extends T> currentList) {
        return new ArrayList<>(currentList);
    }

    @Nonnull
    public static <T> Set<T> copy(@Nonnull final Set<? extends T> currentSet) {
        return new HashSet<>(currentSet);
    }

    @Nonnull
    public static <K, V> Map<K, V> copy(@Nonnull final Map<K, V> currentMap) {
        final Map<K, V> newMap = new HashMap<>(currentMap);
        return Collections.unmodifiableMap(newMap);
    }

    @Nonnull
    public static <T> List<T> appendToCopy(@Nonnull final List<? extends T> currentList,
                                           @Nonnull final T newElement) {
        final ArrayList<T> newList = new ArrayList<>(currentList);
        newList.add(newElement);
        return Collections.unmodifiableList(newList);
    }

    @Nonnull
    public static <K, V> Map<K, V> putToCopy(@Nonnull final Map<K, V> currentMap,
                                             @Nonnull final K newKey,
                                             @Nonnull final V newValue) {
        final Map<K, V> newMap = new HashMap<>(currentMap);
        newMap.put(newKey, newValue);
        return Collections.unmodifiableMap(newMap);
    }


    /**
     * Collects the stream to an immutable list, based on the given supplier.
     *
     * @param <T> type of the elements in the list
     * @return immutable list consisting of the collected elements
     */
    @Nonnull
    public static <T, A extends List<T>> Collector<T, A, List<T>> toImmutableList(@Nonnull final Supplier<A> supplier) {
        return Collector.of(
                supplier,
                List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collections::unmodifiableList);
    }

    /**
     * Collects the stream to an immutable list, based on an underlying ArrayList.
     *
     * @param <T> type of the elements in the list
     * @return immutable list consisting of the collected elements
     */
    @Nonnull
    public static <T> Collector<T, List<T>, List<T>> toImmutableList() {
        return Collector.of(
                ArrayList::new,
                List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collections::unmodifiableList);
    }

    /**
     * Make the given List immutable and immune to changes in the original list, return it unmodified if already immutable, return empty list if input is null.
     *
     * @param input List to convert, can be null
     * @param <T>   type of input and output Lists
     * @return immutable version of given List, or  empty list is input is null
     */
    @Nonnull
    public static <T> List<T> immutableOrEmpty(@Nullable final List<T> input) {
        return input == null ? Collections.emptyList() : immutable(input);
    }

    /**
     * Make the given List immutable and immune to changes in the original list, or return it unmodified if already immutable.
     *
     * @param input List to convert, can be null
     * @param <T>   type of input and output Lists
     * @return immutable version of given List, or null if input is null
     */
    @Nullable
    public static <T> List<T> immutable(@Nullable final List<T> input) {
        if (input == null) {
            return null;
        }
        if (input.getClass().getSimpleName().startsWith(UNMODIFIABLE_CLASS_NAME_START)) {
            // not sure this optimalisation is really useful, afaiks ArrayLists resolve to a simple array copy, even if wrapped
            return input;
        }
        // make a copy of it (otherwise list can still be changed through the original) and make it immutable
        return Collections.unmodifiableList(new ArrayList<>(input));
    }

    /**
     * Make a immutable List of the given items.
     *
     * @param items List of inputs to convert, cannot contain nulls
     * @param <T>   type of input and output Lists
     * @return immutable lis of given items, or null if input is null
     */
    @Nullable
    public static <T> List<T> immutableList(T... items) {
        return immutable(Arrays.asList(items));
    }

    /**
     * Make a immutable Set of the given items.
     *
     * @param items List of inputs to convert, cannot contain nulls
     * @param <T>   type of input and output Lists
     * @return immutable lis of given items, or null if input is null
     */
    @Nullable
    public static <T> Set<T> immutableSet(T... items) {
        return immutable(Sets.newHashSet(items));
    }

    /**
     * Make the given Set immutable and immune to changes in the original set, or return it unmodified if already immutable.
     *
     * @param input Set to convert, can be null
     * @param <T>   type of input and output Sets
     * @return immutable version of given Set, or null if input is null
     */
    @Nullable
    public static <T> Set<T> immutable(@Nullable final Set<T> input) {
        if (input == null) {
            return null;
        }
        if (input.getClass().getSimpleName().startsWith(UNMODIFIABLE_CLASS_NAME_START)) {
            // not sure this optimalisation is really useful
            return input;
        }
        // make a copy of it (otherwise set can still be changed through the original) and make it immutable
        return Collections.unmodifiableSet(new HashSet<>(input));
    }

    /**
     * Make the given Map immutable and immune to changes in the original Map, or return it unmodified if already immutable.
     *
     * @param input Map to convert, can be null
     * @param <K>   type of key of input and output Map
     * @param <K>   type of value of input and output Map
     * @return immutable version of given Map, or null if input is null
     */
    @Nullable
    public static <K, V> Map<K, V> immutable(@Nullable final Map<K, V> input) {
        if (input == null) {
            return null;
        }
        if (input.getClass().getSimpleName().startsWith(UNMODIFIABLE_CLASS_NAME_START)) {
            // not sure this optimalisation is really useful
            return input;
        }
        return Collections.unmodifiableMap(new HashMap<>(input));
    }

    @Nullable
    public static Object mapPath(@Nullable final Map map, @Nonnull final String path) {
        if (map == null || StringUtils.isEmpty(path)) {
            return null;
        }
        final String firstPart = StringUtils.substringBefore(path, ".");
        final Object found = map.get(firstPart);
        if (path.contains(".")) {
            // need more, so if found is a Map (and thus not-null), continue with that, otherwise return  null
            return found instanceof Map ? mapPath((Map) found, StringUtils.substringAfter(path, ".")) : null;
        }
        return found;
    }


    /**
     * See https://stackoverflow.com/questions/33242577/how-do-i-turn-a-java-enumeration-into-a-stream
     */    
    public static <T> Stream<T> enumerationAsStream(Enumeration<T> e) {
        return StreamSupport.stream(
                new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, Spliterator.ORDERED) {
                    @Override
                    public boolean tryAdvance(Consumer<? super T> action) {
                        if (e.hasMoreElements()) {
                            action.accept(e.nextElement());
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void forEachRemaining(Consumer<? super T> action) {
                        while (e.hasMoreElements()) {
                            action.accept(e.nextElement());
                        }
                    }
                }, false);
    }
}
