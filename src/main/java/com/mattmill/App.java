package com.mattmill;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Random;

/**
 * Compare two strategies for partition finding.
 */
public class App {
    public static void main(String[] args) {
        PartitionFinder fastSearch = new FastPartitionFinder();
        PartitionFinder exhaustiveSearch = new ExhaustiveSearchPartitionFinder();

        exerciseApproach(exhaustiveSearch, 5, 750);
        exerciseApproach(fastSearch, 10, 750);

        int maxChars = 20;
        int maxPartitions = 7;
        int maxRuns = 20;
        long exhaustiveTime = compareApproaches(exhaustiveSearch, maxChars, maxPartitions, maxRuns);
        long fastTime = compareApproaches(fastSearch, maxChars, maxPartitions, maxRuns);

        System.out.println("Exhaustive search took: " + exhaustiveTime + "ms.\nFast search took: " + fastTime + " ms.");
    }

    // Detects how large of a String the strategy can process in some time limit
    public static void exerciseApproach(PartitionFinder impl, int maxPartitions, int timeLimitMillis) {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance(Locale.US);

        for (int numPartitions = 3; numPartitions <= maxPartitions; numPartitions++) {
            // What was the last number of characters we succeeded with?
            // We'll use this value to reset and keep going with a smaller step size
            int lastNumChars = 1;
            int numChars = 1;
            for(double stepMultiplier = 3.0; stepMultiplier > 1; stepMultiplier = stepMultiplier * .6f){
                numChars = lastNumChars;
                while (true) {
                    Instant start = Instant.now();
                    impl.search(getRandomAsBs(numChars), numPartitions);
                    Instant stop = Instant.now();
                    if (Duration.between(start, stop).toMillis() > timeLimitMillis) {
                        break;
                    }
                    lastNumChars = numChars;
                    numChars = (int) (numChars * stepMultiplier);
                }
            }
            System.out.println("Handled " + numberFormat.format(lastNumChars) +
                    " characters with " + numPartitions +
                    " partitions in under " + timeLimitMillis + " ms");
        }

    }

    // Reports how long, on average, an approach takes to find a result for
    // every String size and partition size, up to some limit.
    public static long compareApproaches(PartitionFinder impl, int maxChars, int maxPartitions, int maxRuns) {
        Instant exhaustiveStart = Instant.now();
        //Exhaustive search
        for (int numChars = 4; numChars <= maxChars; numChars++) {
            for (int numPartitions = 2; numPartitions <= maxPartitions; numPartitions++) {
                long timeElapsed = 0;
                for (int numRuns = 0; numRuns < maxRuns; numRuns++) {
                    Instant start = Instant.now();
                    impl.search(getRandomAsBs(numChars), numPartitions);
                    Instant stop = Instant.now();
                    timeElapsed += Duration.between(start, stop).toMillis();
                }
                long avgTime = timeElapsed / maxRuns;
                System.out.println(
                        "Ran " + numChars + " characters with " + numPartitions + " partitions in " + avgTime
                                + " ms average");
            }
        }
        Instant exhaustiveStop = Instant.now();

        return Duration.between(exhaustiveStart, exhaustiveStop).toMillis();
    }


    public static String getRandomAsBs(int numChars) {
        Random random = new Random();
        return random.ints(0, 2)
                .mapToObj(bit -> {
                    if (bit == 0) {
                        return "a";
                    } else {
                        return "b";
                    }
                })
                .limit(numChars)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
