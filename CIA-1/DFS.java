import java.util.*;

public class DFS{

    // Depth-First Search (DFS) Algorithm
    public static boolean dfs(Map<String, List<String>> graph, String start, String goal, 
                              Set<String> visited, List<String> path) {
        visited.add(start);  // Mark the current node as visited
        path.add(start);  // Add the current node to the path
        System.out.println("Visiting: " + start);

        // If the goal is found, print the path and return true
        if (start.equals(goal)) {
            System.out.println("Goal " + goal + " found!");
            System.out.println("Path: " + String.join(" -> ", path));
            return true;
        }

        // Explore the neighbors of the current node
        for (String neighbor : graph.get(start)) {
            if (!visited.contains(neighbor)) {
                if (dfs(graph, neighbor, goal, visited, path)) {
                    return true;  // Stop if the goal is found
                }
            }
        }

        // Backtrack: remove the current node from the path
        path.remove(path.size() - 1);
        return false;  // Return false if the goal was not found
    }

    public static void main(String[] args) {
        // Define the graph
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("S", Arrays.asList("A", "B"));
        graph.put("A", Arrays.asList("B", "D"));
        graph.put("B", Arrays.asList("C", "A"));
        graph.put("C", Arrays.asList("E"));
        graph.put("D", Arrays.asList("G"));
        graph.put("E", new ArrayList<>());  // E is a dead end
        graph.put("G", new ArrayList<>());  // G is the goal

        // Initialize visited set and path list
        Set<String> visited = new HashSet<>();
        List<String> path = new ArrayList<>();

        // Run DFS starting from 'S' to find 'G'
        boolean found = dfs(graph, "S", "G", visited, path);

        // Check if the goal was found
        if (!found) {
            System.out.println("Goal not found.");
        }
    }
}
