package com.mattmill;

import org.junit.Assert;
import org.junit.Test;

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

    @Test
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
        Assert.assertEquals(expectedResult, PartitionFinder.fastSearch(input, numPartitions));
    }
}