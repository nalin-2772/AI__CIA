import java.util.*;

class BestFirstSearch{

    // Define the graph with weighted edges (costs)
    static Map<String, Map<String, Integer>> graph = new HashMap<>() {{
        put("S", new HashMap<>(Map.of("A", 6, "B", 5)));
        put("A", new HashMap<>(Map.of("B", 5, "D", 1)));
        put("B", new HashMap<>(Map.of("C", 8, "A", 6)));
        put("C", new HashMap<>(Map.of("E", 9)));
        put("D", new HashMap<>(Map.of("G", 0)));
        put("E", new HashMap<>());
        put("G", new HashMap<>());
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

    // Helper class to store the state in the priority queue
    static class Node implements Comparable<Node> {
        String name;
        List<String> path;
        int heuristicValue;
        int cumulativeCost;

        Node(String name, List<String> path, int heuristicValue, int cumulativeCost) {
            this.name = name;
            this.path = path;
            this.heuristicValue = heuristicValue;
            this.cumulativeCost = cumulativeCost;
        }

        // Priority based on heuristic value
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.heuristicValue, other.heuristicValue);
        }
    }

    // Best First Search (BFS) Algorithm
    public static Result bestFirstSearch(String start, String goal) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        openList.add(new Node(start, List.of(start), heuristic.get(start), 0));
        Set<String> visited = new HashSet<>();

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            // If the goal is reached, return the path and total cost
            if (current.name.equals(goal)) {
                return new Result(current.path, current.cumulativeCost);
            }

            visited.add(current.name);

            // Explore the neighbors
            for (Map.Entry<String, Integer> neighborEntry : graph.get(current.name).entrySet()) {
                String neighbor = neighborEntry.getKey();
                int edgeCost = neighborEntry.getValue();

                if (!visited.contains(neighbor)) {
                    int newCost = current.cumulativeCost + edgeCost;
                    List<String> newPath = new ArrayList<>(current.path);
                    newPath.add(neighbor);

                    // Add neighbor to the open list with updated path and cost
                    openList.add(new Node(neighbor, newPath, heuristic.get(neighbor), newCost));
                }
            }
        }
        return new Result(new ArrayList<>(), -1); // If no path is found
    }

    // Result class to store the solution path and cost
    static class Result {
        List<String> path;
        int totalCost;

        Result(List<String> path, int totalCost) {
            this.path = path;
            this.totalCost = totalCost;
        }
    }

    public static void main(String[] args) {
        String startNode = "S";
        String goalNode = "G";

        Result solution = bestFirstSearch(startNode, goalNode);

        if (solution.totalCost != -1) {
            System.out.println("Best First Search Solution Path: " + solution.path);
            System.out.println("Total Cost: " + solution.totalCost);
        } else {
            System.out.println("No path found from " + startNode + " to " + goalNode);
        }
    }
}
