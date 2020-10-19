package com.mattmill;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.mattmill.App.getRandomAsBs;
import static com.mattmill.ExhaustiveSearchPartitionFinder.doAllStringsInListHaveSameNumberAs;
import static com.mattmill.ExhaustiveSearchPartitionFinder.recurseExplodeString;
import static org.junit.Assert.*;

public class PartitionFinderTest {

    @Test
    public void testSplit_empty() {
        testInput("", 3, 0);
    }

    @Test
    public void testSplit_onlyAs() {
        testInput("aaa", 3, 1);
        testInput("aaaa", 3, 0);
        testInput("aaaaa", 3, 0);
        testInput("aaaaaa", 3, 1);

        testInput("aaa", 4, 0);
        testInput("aaaa", 4, 1);
        testInput("aaaaa", 4, 0);
        testInput("aaaaaaaa", 4, 1);
    }

    @Ignore
    public void testSplit_onlyBs() {
        testInput("b", 3, 0);
        testInput("bb", 3, 0);
        testInput("bbb", 3, 0);

        testInput("b", 4, 0);
        testInput("bb", 4, 0);
        testInput("bbb", 4, 0);
    }

    @Test
    public void testSplit_ababab() {
        testInput("ababab", 3, 4);
        testInput("ababbab", 3, 6);
        testInput("abbabab", 3, 6);
        testInput("abbababb", 3, 6);
        testInput("abbabbab", 3, 9);
        testInput("abbabbabb", 3, 9);
    }

    @Test
    public void testSplit_ababab_4partitions() {
        testInput("abababab", 4, 8);
        testInput("ababbabab", 4, 12);
        testInput("abbababab", 4, 12);
        testInput("abbababbab", 4, 18);
        testInput("abbabbabab", 4, 18);
        testInput("abbabbabbab", 4, 27);
    }

    @Test
    public void testSplit_manyAs() {
        testInput("aaaaaaaaa", 3, 1);
        testInput("aaabbabbabbabaaa", 3, 6);
        testInput("aaaaaaaa", 4, 1);
        testInput("aaabbabbabbabaa", 4, 6);
    }

    @Test
    public void testSplit_notEnoughAs() {
        testInput("abbabb", 3, 0);
        testInput("abbabb", 4, 0);
    }

    private void testInput(String input, int numPartitions, int expectedResult) {
        PartitionFinder fastFinder = new FastPartitionFinder();
        Assert.assertEquals(expectedResult, fastFinder.search(input, numPartitions));
        PartitionFinder exhaustiveSearch = new ExhaustiveSearchPartitionFinder();
        Assert.assertEquals(expectedResult, exhaustiveSearch.search(input, numPartitions));
    }

    // Exhaustive search specific tests
    @Ignore
    public void testRecursiveExplodeString() {
        int maxStringLen = 10;
        int maxPartitions = 5;
        for (int numChars = 2; numChars <= maxStringLen; numChars++) {
            for (int numPartitions = 2; numPartitions <= maxPartitions; numPartitions++) {
                String randomAlphaString = getRandomAsBs(numChars);
                final List<List<String>> explodedStrings = testRecursiveExplodeStringHelper(randomAlphaString, numPartitions);
                System.out.println(
                        "String: " + randomAlphaString + " (len: " + numChars + ") with " + numPartitions + " partitions had " +
                                explodedStrings.size() + " permutations.");//  Expected: "+expectedPermutations);
                final String permutations = explodedStrings.stream()
                        .map(perm -> perm.stream().reduce("", (a, b) -> a + "[" + b + "]"))
                        .reduce("", (a, b) -> a + " [" + b + "] ");
                System.out.println("Permutations: " + permutations);

            }
        }
    }

    @Test
    public void testDoAllStringsHaveSameNumberAs() {
        List<String> strings = new ArrayList<>();
        strings.add("aa");
        strings.add("aa");
        strings.add("aab");
        boolean testResult = doAllStringsInListHaveSameNumberAs().test(strings);
        assertTrue(testResult);
        strings = new ArrayList<>();
        strings.add("aa");
        strings.add("a");
        strings.add("aab");
        testResult = doAllStringsInListHaveSameNumberAs().test(strings);
        assertFalse(testResult);
    }

    private List<List<String>> testRecursiveExplodeStringHelper(String testString, int partitions) {
        final List<List<String>> explodedStrings = recurseExplodeString(testString, partitions);
        explodedStrings.stream()
                .peek(testList -> {
                    assertEquals(partitions, testList.size());
                    assertEquals(testString, testList.stream()
                            .reduce("", (a, b) -> a + b));
                });
        return explodedStrings;
    }

    // Comparison test with random data using 3 partitions
    @Test
    public void testRandomSequencesWith3Partitions() {
        PartitionFinder exhaustiveSearch = new ExhaustiveSearchPartitionFinder();
        PartitionFinder fastSearch = new FastPartitionFinder();
        for (int numChars = 4; numChars < 50; numChars++) {
            int iterations = 0;
            do {
                String testString = getRandomAsBs(numChars);
                System.out.println("Testing [" + testString + "]");
                long exhaustiveResult = exhaustiveSearch.search(testString, 3);
                long fastResult = fastSearch.search(testString, 3);

                if (testString.contains("a")) {
                    Assert.assertEquals(exhaustiveResult, fastResult);
                }
                iterations++;
            } while (iterations < 100);
        }
    }
}