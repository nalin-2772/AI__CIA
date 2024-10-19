import java.util.*;

public class HillClimbing {

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

    // Heuristic values
    static Map<String, Integer> heuristic = new HashMap<>() {{
        put("S", 7);
        put("A", 6);
        put("B", 5);
        put("C", 8);
        put("D", 1);
        put("E", 9);
        put("G", 0);
    }};

    // Generate an initial greedy solution
    public static List<String> greedySolution(String start, String goal) {
        List<String> solution = new ArrayList<>();
        String currentNode = start;
        solution.add(currentNode);

        // Build the path by always selecting the neighbor with the lowest heuristic value
        while (!currentNode.equals(goal)) {
            Map<String, Integer> neighbors = graph.get(currentNode);
            if (neighbors.isEmpty()) break;

            String nextNode = neighbors.keySet().stream()
                    .min(Comparator.comparingInt(heuristic::get))
                    .orElse(null);

            if (nextNode != null && !solution.contains(nextNode)) {  // Avoid cycles
                solution.add(nextNode);
                currentNode = nextNode;
            }
        }
        return solution;
    }

    // Calculate the path cost based on heuristics
    public static int pathCost(List<String> solution) {
        return solution.stream().mapToInt(heuristic::get).sum();
    }

    // Generate neighbors of a solution by swapping nodes
    public static List<List<String>> generateNeighbors(List<String> solution) {
        List<List<String>> neighbors = new ArrayList<>();
        for (int i = 1; i < solution.size() - 1; i++) {  // Exclude start and goal nodes from swapping
            for (int j = i + 1; j < solution.size() - 1; j++) {
                List<String> neighbor = new ArrayList<>(solution);
                // Swap nodes
                Collections.swap(neighbor, i, j);
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    // Find the best neighbor based on the path cost
    public static List<String> bestNeighbor(List<String> currentSolution) {
        List<List<String>> neighbors = generateNeighbors(currentSolution);

        List<String> bestNeighbor = currentSolution;
        int bestCost = pathCost(currentSolution);

        for (List<String> neighbor : neighbors) {
            int currentCost = pathCost(neighbor);
            if (currentCost < bestCost) {
                bestCost = currentCost;
                bestNeighbor = neighbor;
            }
        }
        return bestNeighbor;
    }

    // Hill Climbing Algorithm
    public static List<String> hillClimbing(String start, String goal) {
        List<String> currentSolution = greedySolution(start, goal);
        int currentCost = pathCost(currentSolution);

        while (true) {
            List<String> neighbor = bestNeighbor(currentSolution);
            int neighborCost = pathCost(neighbor);

            if (neighborCost >= currentCost) {
                System.out.println("Reached local optimum at " + currentSolution + " with cost " + currentCost);
                break;
            }

            currentSolution = neighbor;
            currentCost = neighborCost;
        }
        return currentSolution;
    }

    public static void main(String[] args) {
        String start = "S";
        String goal = "G";

        List<String> solution = hillClimbing(start, goal);
        int cost = pathCost(solution);

        System.out.println("Final Solution Path: " + solution);
        System.out.println("Final Path Cost: " + cost);
    }
}
