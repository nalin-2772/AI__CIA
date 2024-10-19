import java.util.*;

public class AStarAlgorithm {

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

    // Define the heuristic values (estimated cost to reach goal)
    static Map<String, Integer> heuristic = new HashMap<>() {{
        put("S", 7);
        put("A", 6);
        put("B", 5);
        put("C", 8);
        put("D", 1);
        put("E", 9);
        put("G", 0);
    }};

    // A* Algorithm implementation
    public static Path aStar(String start, String goal) {
        // Priority queue to store (f_cost, g_cost, current_node, path)
        PriorityQueue<Path> queue = new PriorityQueue<>();
        queue.add(new Path(heuristic.get(start), 0, start, List.of(start)));

        // Dictionary to track the cost to reach each node
        Map<String, Integer> visited = new HashMap<>();

        while (!queue.isEmpty()) {
            // Pop the node with the lowest f(n) value
            Path current = queue.poll();

            // If we've reached the goal, return the solution
            if (current.node.equals(goal)) {
                return current;  // Return the complete path
            }

            // If this node has already been visited with a lower cost, skip it
            if (visited.containsKey(current.node) && visited.get(current.node) <= current.gCost) {
                continue;
            }

            // Mark this node as visited
            visited.put(current.node, current.gCost);

            // Explore the neighbors of the current node
            for (Map.Entry<String, Integer> neighbor : graph.getOrDefault(current.node, Map.of()).entrySet()) {
                int totalCost = current.gCost + neighbor.getValue();  // g(n)
                int fNeighborCost = totalCost + heuristic.getOrDefault(neighbor.getKey(), Integer.MAX_VALUE);  // f(n) = g(n) + h(n)

                // Add the neighbor to the queue
                queue.add(new Path(fNeighborCost, totalCost, neighbor.getKey(), 
                                    new ArrayList<>(current.nodes) {{ add(neighbor.getKey()); }}));
            }
        }

        return null;  // If no path is found
    }

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

    // Main method to test the A* algorithm
    public static void main(String[] args) {
        String startNode = "S";
        String goalNode = "G";

        Path result = aStar(startNode, goalNode);

        if (result != null) {
            System.out.println("A* Algorithm Solution Path: " + result.nodes);
            System.out.println("Total Path Cost: " + result.gCost);
        } else {
            System.out.println("No path found from " + startNode + " to " + goalNode);
        }
    }
}
