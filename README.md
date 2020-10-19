# partition-finder
A coding challenge to find all unique ways to partition a string with some 
number of A's and B's into some number of parts such that all partitions have 
the same number of A's.

The project includes 2 strategies for partitioning the string:  
- <code>ExhaustiveSearchPartitionFinder</code> is a brute force approach which 
uses a recursive method to generate all permutations.  The BigO complexity of 
this approach is O(N^M)
- <code>FastPartitionFinder</code> which uses an approach which attempts to 
determine the number of A's in each partition and then determine how many 
different combinations are available within those groupings.  The BigO complexity
of this approach is much better, O(N+M).

The main class implements two ways to compare these two algorithms:
- <code>exerciseApproach</code> will attempt to determine the maximum String
size that the algorithm can process in a certain amount of time and for a certain
number of partitions.  In this approach, you can see the exponential behavior of 
the brute force approach, and the linear behavior of the fast approach.
- <code>compareApproaches</code> attempts to find the average run time for 
each strategy for every combination of String size and partition size. 