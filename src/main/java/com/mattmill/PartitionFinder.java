package com.mattmill;

/**
 * PartitionFinder
 * Given a String with some number of a's and b's, and a number of partitions, find all the unique
 * ways that you can partition the String such that each partition has an equal number of A's.
 */
public interface PartitionFinder {

    long search(String theString, int numPartitions);
}
