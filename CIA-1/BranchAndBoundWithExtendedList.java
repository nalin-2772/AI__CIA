import java.util.*;

public class BranchAndBoundWithExtendedList {

    // Define the graph with weighted edges (costs)
    static Map<String, Map<String, Integer>> graph = new HashMap<>() {{
        put("S", Map.of("A", 6, "B", 5));
        put("A", Map.of("B", 5, "D", 1));
        put("B", Map.of("C", 8, "A", 6));
        put("C", Map.of("E", 9));
        put("D", Map.of("G", 0));
        put("E", Map.of());
        put("G", Map.of());
    }};

    // Helper class to represent a path in the priority queue
    static class Path implements Comparable<Path> {
        int cost;
        String node;
        List<String> nodes;

        Path(int cost, String node, List<String> nodes) {
            this.cost = cost;
            this.node = node;
            this.nodes = new ArrayList<>(nodes);
        }

        @Override
        public int compareTo(Path other) {
            return Integer.compare(this.cost, other.cost);  // Min-heap based on cost
        }
    }

    // Branch and Bound with Dead Horse Principle
    public static Path branchAndBoundWithExtendedList(String start, String goal) {
        PriorityQueue<Path> queue = new PriorityQueue<>();
        queue.add(new Path(0, start, List.of(start)));

        // Extended list to store the minimum cost at which each node was visited
        Map<String, Integer> extendedList = new HashMap<>();
        
        int bestCost = Integer.MAX_VALUE;
        Path bestPath = null;

        while (!queue.isEmpty()) {
            Path current = queue.poll();

            // If goal is reached and cost is better than the best known cost
            if (current.node.equals(goal) && current.cost < bestCost) {
                bestCost = current.cost;
                bestPath = current;
            }

            // Check if the current node has been visited with a lower or equal cost before
            if (extendedList.containsKey(current.node) && extendedList.get(current.node) <= current.cost) {
                continue; // Skip this node
            }

            // Update the extended list with the new minimum cost for this node
            extendedList.put(current.node, current.cost);

            // Explore neighbors of the current node
            for (Map.Entry<String, Integer> neighbor : graph.getOrDefault(current.node, Map.of()).entrySet()) {
                int newCost = current.cost + neighbor.getValue();

                // Only add the neighbor if its total cost is less than the best known cost
                if (newCost < bestCost) {
                    List<String> newPath = new ArrayList<>(current.nodes);
                    newPath.add(neighbor.getKey());
                    queue.add(new Path(newCost, neighbor.getKey(), newPath));
                }
            }
        }

        return bestPath;  // Return the best path found
    }

    // Main method to test the Branch and Bound with Dead Horse Principle
    public static void main(String[] args) {
        String startNode = "S";
        String goalNode = "G";

        Path result = branchAndBoundWithExtendedList(startNode, goalNode);

        if (result != null) {
            System.out.println("Best Path Found (Branch and Bound with Extended List): " + result.nodes);
            System.out.println("Total Path Cost: " + result.cost);
        } else {
            System.out.println("No path found from " + startNode + " to " + goalNode);
        }
    }
}

