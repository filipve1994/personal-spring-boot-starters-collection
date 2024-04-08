package io.filipvde.customspringbootstarter.utils;

import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionUtilsTest {

    @Test
    public void testGetRemaining() {
        List<String> testList = Arrays.asList("1", "2", "3");
        List<String> remaining = CollectionUtils.getRemaining("2", testList);
        assertThat(testList).containsExactly("1", "2", "3");
        assertThat(remaining).containsExactly("1", "3");
        remaining = CollectionUtils.getRemaining("NOT", testList);
        assertThat(remaining).containsExactly("1", "2", "3");
        remaining = CollectionUtils.getRemaining("NOT", new ArrayList<>());
        assertThat(remaining).isEmpty();
    }

    @Test
    public void testCopyList() {
        assertThat(CollectionUtils.copy(Arrays.asList("1", "2", "3"))).containsExactly("1", "2", "3");
        assertThat(CollectionUtils.copy(Collections.emptyList())).isEmpty();
    }

    @Test
    public void testCopyCollection() {
        assertThat(CollectionUtils.copy((Collection<String>) Arrays.asList("1", "2", "3"))).containsExactly("1", "2", "3");
        assertThat(CollectionUtils.copy((Collection<?>) Collections.emptyList())).isEmpty();
    }

    @Test
    public void testNullableCopy() {
        assertThat(CollectionUtils.nullableCopy(Arrays.asList("1", "2", "3"))).containsExactly("1", "2", "3");
        assertThat(CollectionUtils.nullableCopy(null)).isNull();
    }

    @Test
    public void testCopySet() {
        assertThat(CollectionUtils.copy(new HashSet<>(Arrays.asList("1", "2", "3")))).containsExactly("1", "2", "3");
        assertThat(CollectionUtils.copy(new HashSet<>())).isEmpty();
    }

    @Test
    public void testCopyMap() {
        Map<String, String> map = new HashMap<>();
        map.put("K1", "V1");
        assertThat(CollectionUtils.copy(map)).containsExactly(MapEntry.entry("K1", "V1"));
        assertThat(CollectionUtils.copy(new HashMap<>())).isEmpty();
    }

    @Test
    public void testAppendToCopy() {
        List<String> testList = Arrays.asList("1", "2", "3");
        List<String> appended = CollectionUtils.appendToCopy(testList, "Q");
        assertThat(appended).containsExactly("1", "2", "3", "Q");
        assertThat(testList).containsExactly("1", "2", "3");
        appended = CollectionUtils.appendToCopy(new ArrayList<>(), "Q");
        assertThat(appended).containsExactly("Q");
    }

    @Test
    public void testPutToCopy() {
        Map<String, String> orgMap = new HashMap<>();
        orgMap.put("K1", "V1");

        Map<String, String> copyMap = CollectionUtils.putToCopy(orgMap, "K2", "V2");
        assertThat(copyMap).containsExactly(MapEntry.entry("K1", "V1"), MapEntry.entry("K2", "V2"));

        copyMap = CollectionUtils.putToCopy(orgMap, "K1", "V2");
        assertThat(copyMap).containsExactly(MapEntry.entry("K1", "V2"));
        assertThat(orgMap).containsExactly(MapEntry.entry("K1", "V1"));
    }

    @Test
    public void testToImmutableListHappyflow() {
        final List<String> copy = Stream.of("1", "2", "3").collect(CollectionUtils.toImmutableList());
        assertThat(copy).containsExactly("1", "2", "3");
    }

    @Test
    public void testToImmutableListMustBeImmutable() {
        final List<String> copy = Stream.of("1").collect(CollectionUtils.toImmutableList());
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> copy.add("2")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testImmutableListHappy() {
        assertThat(CollectionUtils.immutable(Arrays.asList("1", "2", "3"))).containsExactly("1", "2", "3");
        assertThat(CollectionUtils.immutable(Collections.emptyList())).isEmpty();
    }

    @Test
    public void testImmutableListMustBeImmutable() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> CollectionUtils.immutableList("0", "1").add("2")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testImmutableSetHappy() {
        assertThat(CollectionUtils.immutableSet("1", "2", "3")).containsExactly("1", "2", "3");
        assertThat(CollectionUtils.immutable(new HashSet<String>())).isEmpty();
    }

    @Test
    public void testImmutableSetMustBeImmutable() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> CollectionUtils.immutable(new HashSet<String>()).add("2")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testImmutableMapHappy() {
        Map<String, String> map = new HashMap<>();
        map.put("K1", "V1");
        assertThat(CollectionUtils.immutable(map)).containsExactly(MapEntry.entry("K1", "V1"));
        assertThat(CollectionUtils.immutable(new HashMap<>())).isEmpty();
    }

    @Test
    public void testImmutableMapMustBeImmutable() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> CollectionUtils.immutable(new HashMap<String, String>()).put("K", "V")).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void testNullImmutableMapMustBeNull() {
        final Map<Object, Object> map = CollectionUtils.immutable((Map<Object, Object>) null);
        assertThat(map).isNull();
    }

    @Test
    public void testImmutableMapReturnSameIfAlreadyImmutable() {
        assertThat(CollectionUtils.immutable(new HashMap<>())).isEmpty();
        Map<String, String> map = new HashMap<>();
        map.put("K1", "V1");
        final Map<String, String> immutable = CollectionUtils.immutable(map);
        assertThat(immutable).containsExactly(MapEntry.entry("K1", "V1"));
        assertThat(CollectionUtils.immutable(immutable)).isEqualTo(immutable);
    }

    @Test
    public void testMapPath() {
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();
        map1.put("K11", "V11");
        map1.put("K12", map2);
        map2.put("K21", "V21");
        assertThat(CollectionUtils.mapPath(null, null)).isNull();
        assertThat(CollectionUtils.mapPath(map1, "")).isNull();
        assertThat(CollectionUtils.mapPath(map1, "NOTFOUND")).isNull();
        assertThat(CollectionUtils.mapPath(map1, "NOTFOUND.")).isNull();
        assertThat(CollectionUtils.mapPath(map1, ".NOTFOUND")).isNull();
        assertThat(CollectionUtils.mapPath(map1, "K11")).isEqualTo("V11");
        assertThat(CollectionUtils.mapPath(map1, "K12")).isEqualTo(map2);
        assertThat(CollectionUtils.mapPath(map1, "K12.K21")).isEqualTo("V21");
        assertThat(CollectionUtils.mapPath(map1, "K11.K21.NOTFOUND")).isNull();
        assertThat(CollectionUtils.mapPath(map1, "K11.K21.NOT.FOUND")).isNull();
    }
}
