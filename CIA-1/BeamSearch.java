import java.util.*;

public class BeamSearch {

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

    // Define the heuristic values (estimated cost to reach the goal)
    static Map<String, Integer> heuristic = new HashMap<>() {{
        put("S", 7);
        put("A", 6);
        put("B", 5);
        put("C", 8);
        put("D", 1);
        put("E", 9);
        put("G", 0);
    }};

    // Helper method to get successors of a given node
    public static Set<String> getSuccessors(String node) {
        return graph.getOrDefault(node, Collections.emptyMap()).keySet();
    }

    // Beam Search Algorithm
    public static PathResult beamSearch(String start, String goal, int beamWidth) {
        // Priority queue for the current beam (sorted by heuristic value)
        Queue<Node> beam = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
        beam.add(new Node(start, heuristic.get(start), List.of(start)));

        while (!beam.isEmpty()) {
            // List to store new paths for the expanded beam
            List<Node> newBeam = new ArrayList<>();

            // Iterate over the current beam nodes
            while (!beam.isEmpty()) {
                Node current = beam.poll();
                String currentNode = current.path.get(current.path.size() - 1);

                // If the goal is reached, return the solution path and cost
                if (currentNode.equals(goal)) {
                    return new PathResult(current.path, current.cost);
                }

                // Expand the neighbors of the current node
                for (String neighbor : getSuccessors(currentNode)) {
                    if (!current.path.contains(neighbor)) {  // Avoid cycles
                        List<String> newPath = new ArrayList<>(current.path);
                        newPath.add(neighbor);
                        int newCost = current.cost + heuristic.get(neighbor);

                        // Add the new path to the expanded beam
                        newBeam.add(new Node(neighbor, newCost, newPath));
                    }
                }
            }

            // Keep only the best `beamWidth` paths based on their cost
            beam = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost));
            newBeam.stream()
                    .sorted(Comparator.comparingInt(n -> n.cost))
                    .limit(beamWidth)
                    .forEach(beam::add);
        }

        // If no solution is found, return failure
        return new PathResult(null, Integer.MAX_VALUE);
    }

    // Helper class to store a node's state (cost and path)
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

    // Helper class to store the final path and its total cost
    static class PathResult {
        List<String> path;
        int cost;

        PathResult(List<String> path, int cost) {
            this.path = path;
            this.cost = cost;
        }
    }

    // Main method to run the Beam Search algorithm
    public static void main(String[] args) {
        String startNode = "S";
        String goalNode = "G";
        int beamWidth = 2;

        PathResult result = beamSearch(startNode, goalNode, beamWidth);

        if (result.path != null) {
            System.out.println("Beam Search Solution Path: " + result.path);
            System.out.println("Beam Search Path Cost: " + result.cost);
        } else {
            System.out.println("No solution found from " + startNode + " to " + goalNode);
        }
    }
}
