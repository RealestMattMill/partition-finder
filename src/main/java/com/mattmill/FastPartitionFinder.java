package com.mattmill;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FastPartitionFinder implements PartitionFinder{
    @Override
    public long search(String theString, int numPartitions) {
        if (numPartitions < 2 || theString.isEmpty()) {
            return 0;
        }
        // List which describes the index of a given 'a'
        // If we want the 3rd 'a', then aIndexes.get(3) will give us it.
        // aIndexes.size() also gives us the total number of a's in the string
        final List<Integer> aIndexes = IntStream.range(0, theString.length())
                .filter(index -> theString.charAt(index) == 'a')
                .boxed()
                .collect(Collectors.toList());

        // If there are the wrong number of a's to partition them all evenly, then the number of
        // unique combinations is always 0
        if (aIndexes.size() % numPartitions != 0) {
            return 0;
        } else if (aIndexes.isEmpty()) {
            // there are no a's so every combination has an equal number of a's.  the problem now depends on
            // how many unique ways we can split the string up.
            return 0;
        } else {
            int numPerPartition = (aIndexes.size() / numPartitions);
            long uniqueWaysToSplit = 1; //accumulator
            for (int currentBucket = 1; currentBucket < numPartitions; currentBucket++) {
                int currentIdx = (numPerPartition * currentBucket) - 1;
                // The position in the string of the last A in the Nth partition
                Integer firstA = aIndexes.get(currentIdx);
                // The position in the string of the first A in the N+1th partition
                Integer secondA = aIndexes.get(currentIdx + 1);
                uniqueWaysToSplit = uniqueWaysToSplit * (secondA - firstA);
            }
            return uniqueWaysToSplit;
        }
    }
}
