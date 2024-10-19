import java.util.*;

public class AOStarAlgorithm {

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

    // AO* algorithm function
    public static Result aoStar(String node, String goal, Set<String> visited) {
        // If node is the goal, return 0 cost and the path as we have reached the goal
        if (node.equals(goal)) {
            return new Result(0, List.of(goal));
        }

        // If node is already visited, return a high cost to avoid cycles
        if (visited.contains(node)) {
            return new Result(Integer.MAX_VALUE, List.of());
        }

        visited.add(node);  // Mark the node as visited

        int minCost = Integer.MAX_VALUE;
        List<String> bestSubtreePath = new ArrayList<>();

        // Explore each OR subtree from the current node
        for (Map.Entry<String, Integer> entry : graph.getOrDefault(node, Map.of()).entrySet()) {
            String childNode = entry.getKey();
            int cost = entry.getValue();

            Result subtreeResult = aoStar(childNode, goal, visited);
            int totalCost = cost + subtreeResult.cost;  // Only consider the actual edge cost and subtree cost

            // Update if a cheaper subtree is found
            if (totalCost < minCost) {
                minCost = totalCost;
                bestSubtreePath = new ArrayList<>(List.of(node));
                bestSubtreePath.addAll(subtreeResult.path);  // Add current node to the path
            }
        }

        visited.remove(node);  // Unmark the node after processing
        return new Result(minCost, bestSubtreePath);
    }

    // Function to run the AO* algorithm
    public static Result runAoStar(String startNode, String goalNode) {
        Set<String> visited = new HashSet<>();  // Set to track visited nodes
        return aoStar(startNode, goalNode, visited);
    }

    // Helper class to store the result of the AO* search
    static class Result {
        int cost;
        List<String> path;

        Result(int cost, List<String> path) {
            this.cost = cost;
            this.path = path;
        }
    }

    // Main method to test the AO* algorithm
    public static void main(String[] args) {
        String startNode = "S";
        String goalNode = "G";

        Result result = runAoStar(startNode, goalNode);

        System.out.println("AO* Solution Path: " + result.path);
        System.out.println("Total Cost to Goal: " + result.cost);
    }
}
