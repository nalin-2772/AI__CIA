import java.util.*;

public class BranchAndBoundWithHeuristics {

    // Define the graph with weighted edges (costs)
    static Map<String, Map<String, Integer>> graph = new HashMap<>() {{
        put("S", Map.of("A", 6, "B", 5));
        put("A", Map.of("B", 5, "D", 1));
        put("B", Map.of("C", 8, "A", 6));
        put("C", Map.of("E", 9));
        put("D", Map.of("G", 2));
        put("E", Map.of());
        put("G", Map.of());
    }};

    // Heuristic values (estimated cost to reach goal)
    static Map<String, Integer> heuristic = new HashMap<>() {{
        put("S", 7);
        put("A", 6);
        put("B", 5);
        put("C", 8);
        put("D", 1);
        put("E", 9);
        put("G", 0);
    }};

    // Helper class to represent a path in the priority queue
    static class Path implements Comparable<Path> {
        int fCost;  // f(n) = g(n) + h(n)
        int gCost;  // g(n)
        String node;
        List<String> nodes;

        Path(int fCost, int gCost, String node, List<String> nodes) {
            this.fCost = fCost;
            this.gCost = gCost;
            this.node = node;
            this.nodes = new ArrayList<>(nodes);
        }

        @Override
        public int compareTo(Path other) {
            return Integer.compare(this.fCost, other.fCost);  // Min-heap based on f(n)
        }
    }

    // Branch and Bound with Cost and Heuristics
    public static Path branchAndBoundWithHeuristic(String start, String goal) {
        PriorityQueue<Path> queue = new PriorityQueue<>();
        queue.add(new Path(heuristic.get(start), 0, start, List.of(start)));

        // Extended list (visited nodes with the minimum cost at which they were visited)
        Map<String, Integer> extendedList = new HashMap<>();

        int bestCost = Integer.MAX_VALUE;
        Path bestPath = null;

        while (!queue.isEmpty()) {
            Path current = queue.poll();

            // If we have reached the goal and the cost is lower than the best known cost
            if (current.node.equals(goal) && current.gCost < bestCost) {
                bestCost = current.gCost;
                bestPath = current;
                continue;  // Continue searching for potentially better paths
            }

            // Check if this node was visited with a lower or equal cost before (Dead Horse Principle)
            if (extendedList.containsKey(current.node) && extendedList.get(current.node) <= current.gCost) {
                continue;  // Skip this node as we've visited it with a better or equal cost
            }

            // Update the extended list with the new minimum cost for this node
            extendedList.put(current.node, current.gCost);

            // Explore neighbors of the current node
            for (Map.Entry<String, Integer> neighbor : graph.getOrDefault(current.node, Map.of()).entrySet()) {
                int totalCost = current.gCost + neighbor.getValue();
                int fNeighborCost = totalCost + heuristic.getOrDefault(neighbor.getKey(), Integer.MAX_VALUE);

                // Only add the neighbor to the queue if its total cost is less than the best known cost
                if (totalCost < bestCost) {
                    queue.add(new Path(fNeighborCost, totalCost, neighbor.getKey(), 
                                        new ArrayList<>(current.nodes) {{ add(neighbor.getKey()); }}));
                }
            }
        }

        return bestPath;  // Return the best path found
    }

    // Main method to test the Branch and Bound with Cost and Heuristics
    public static void main(String[] args) {
        String startNode = "S";
        String goalNode = "G";

        Path result = branchAndBoundWithHeuristic(startNode, goalNode);

        if (result != null) {
            System.out.println("Best Path Found (Branch and Bound with Cost + Heuristics): " + result.nodes);
            System.out.println("Total Path Cost: " + result.gCost);
        } else {
            System.out.println("No path found from " + startNode + " to " + goalNode);
        }
    }
}
