package edu.lifo.statistics;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;

public class KruskalWallisTest {

    public KruskalWallisTest() {
    }

    @Test
    public void testCompute() throws IOException, InterruptedException {
        ArrayListMultimap<String, Double> result = ArrayListMultimap.create();
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);

        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);

        HashMap<String, HashMap<String, Boolean>> resultTest = KruskalWallis.test(result);
        assertFalse(resultTest.get("A").get("B"));
        assertFalse(resultTest.get("B").get("A"));
    }

    @Test
    public void testCompute2() throws IOException, InterruptedException {
        ArrayListMultimap<String, Double> result = ArrayListMultimap.create();
        result.put("C", 8D);
        result.put("C", 8D);
        result.put("C", 8D);
        result.put("C", 8D);
        result.put("C", 8D);
        result.put("C", 8D);
        result.put("C", 8D);
        result.put("C", 8D);
        result.put("C", 8D);
        result.put("C", 8D);

        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);
        result.put("A", 10D);

        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);
        result.put("B", 10D);

        HashMap<String, HashMap<String, Boolean>> resultTest = KruskalWallis.test(result);
        assertFalse(resultTest.get("A").get("B"));
        assertFalse(resultTest.get("B").get("A"));
        assertTrue(resultTest.get("A").get("C"));
        assertTrue(resultTest.get("C").get("A"));
        assertTrue(resultTest.get("B").get("C"));
        assertTrue(resultTest.get("C").get("B"));
    }
    
}