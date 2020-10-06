package com.mattmill;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartitionFinderTest {


    @Test
    public void testSplit_empty() {
        testInput("", 0);
    }

    @Test
    public void testSplit_onlyAs() {
        testInput("aaa", 1);
        testInput("aaaa", 0);
        testInput("aaaaa", 0);
        testInput("aaaaaa", 1);
    }

    @Test
    public void testSplit_onlyBs() {
        testInput("b", 0);
        testInput("bb", 0);
        testInput("bbb", 0);
    }

    @Test
    public void testSplit_ababab() {
        testInput("ababab", 4);
        testInput("ababbab", 6);
        testInput("abbabab", 6);
        testInput("abbababb", 6);
        testInput("abbabbab", 9);
        testInput("abbabbabb", 9);
    }

    @Test
    public void testSplit_manyAs() {
        testInput("aaaaaaaaa", 1);
        testInput("aaabbabbabbabaaa", 6);
    }

    @Test
    public void testSplit_notEnoughAs() {
        testInput("abbabb", 0);
    }

    private void testInput(String input, int expectedNumberOfPartitions){
        Assert.assertEquals(expectedNumberOfPartitions, PartitionFinder.fastSearch(input));
    }
}