package com.mattmill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExhaustiveSearchPartitionFinder implements PartitionFinder {
    @Override
    public long search(String theString, int numPartitions) {
        if (numPartitions < 2 || theString.isEmpty()) {
            return 0;
        }
        List<List<String>> combinations = recurseExplodeString(theString, numPartitions);

        return combinations.stream()
                // Only return lists in which all the members of the inner lists have the same number of a's
                .filter(doAllStringsInListHaveSameNumberAs())
                .count();
    }

    public static Predicate<List<String>> doAllStringsInListHaveSameNumberAs() {
        return strings -> strings.stream()
                .map(string -> string.chars().filter(ch -> ch == 'a').count())
                .distinct()
                .count() == 1L;
    }


    public static List<List<String>> recurseExplodeString(String s, int partitionsRemaining) {
        List<List<String>> returnList = new ArrayList<>();
        if (partitionsRemaining == 2) {
            // base case. return any combinations that are still available
            for (int i = 1; i < s.length(); i++) {
                String first = s.substring(0, i);
                String rest = s.substring(i);
                List<String> innerCombination = new ArrayList<>(2);
                innerCombination.add(first);
                innerCombination.add(rest);
                returnList.add(innerCombination);
            }
        } else {
            // recursive function.  For each iteration of string s, call the recursive function to break it down further,
            // when the function returns, zip the left half of the string with every iteration of the right half.
            for (int i = 1; i < s.length(); i++) {
                String first = s.substring(0, i);
                String intermediate = s.substring(i);
                final List<List<String>> explodedSubstringList =
                        recurseExplodeString(intermediate, partitionsRemaining - 1);
                // Zip the "first" string into each of the explodedSubstring values at the front.
                returnList.addAll(explodedSubstringList.stream()
                        .map(innerList -> {
                            List<String> zipped = new ArrayList<>(innerList.size() + 1);
                            zipped.add(first);
                            zipped.addAll(innerList);
                            return zipped;
                        })
                        .collect(Collectors.toList()));
            }
        }
        return returnList;
    }
}
