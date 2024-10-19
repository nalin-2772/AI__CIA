import java.util.*;

public class LeastCostOracle {

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

    // Method to find the least-cost path using a Dijkstra-like algorithm
    public static PathResult findLeastCostOracle(String start, String goal) {
        // Priority queue (min-heap) to store (cost, current_node, path)
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
        queue.add(new Node(start, 0, List.of(start)));  // Start with initial node

        // Map to store the minimum cost to reach each node
        Map<String, Integer> minCosts = new HashMap<>();
        for (String node : graph.keySet()) {
            minCosts.put(node, Integer.MAX_VALUE);
        }
        minCosts.put(start, 0);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // If we reach the goal node, return the path and total cost
            if (current.name.equals(goal)) {
                return new PathResult(current.path, current.cost);
            }

            // Explore neighbors of the current node
            for (Map.Entry<String, Integer> neighbor : graph.get(current.name).entrySet()) {
                String neighborName = neighbor.getKey();
                int edgeCost = neighbor.getValue();
                int newCost = current.cost + edgeCost;

                // If a cheaper path to the neighbor is found, update and add to queue
                if (newCost < minCosts.get(neighborName)) {
                    minCosts.put(neighborName, newCost);
                    List<String> newPath = new ArrayList<>(current.path);
                    newPath.add(neighborName);
                    queue.add(new Node(neighborName, newCost, newPath));
                }
            }
        }
        return new PathResult(null, Integer.MAX_VALUE);  // Return if no path is found
    }

    // Helper class to store information about a node in the priority queue
    static class Node {
        String name;
        int cost;
        List<String> path;

        Node(String name, int cost, List<String> path) {
            this.name = name;
            this.cost = cost;
            this.path = path;
        }
    }

    // Helper class to store the final path and its cost
    static class PathResult {
        List<String> path;
        int cost;

        PathResult(List<String> path, int cost) {
            this.path = path;
            this.cost = cost;
        }
    }

    public static void main(String[] args) {
        String startNode = "S";
        String goalNode = "G";

        PathResult result = findLeastCostOracle(startNode, goalNode);

        if (result.path != null) {
            System.out.println("Least Cost Oracle Path: " + result.path);
            System.out.println("Total Path Cost: " + result.cost);
        } else {
            System.out.println("No path found from " + startNode + " to " + goalNode);
        }
    }
}
