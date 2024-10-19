import java.util.*;

public class BFS{
    // BFS algorithm function
    public static boolean bfs(Map<String, List<String>> graph, String start, String goal) {
        Set<String> visited = new HashSet<>();  // Set to keep track of visited nodes
        Queue<Node> queue = new LinkedList<>(); // Queue to store nodes and paths

        // Initialize the queue with the starting node and its path
        queue.add(new Node(start, Arrays.asList(start)));

        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Get the current node and path
            String currentNode = current.node;
            List<String> path = current.path;

            System.out.println("Visiting: " + currentNode);

            // Check if the current node is the goal
            if (currentNode.equals(goal)) {
                System.out.println("Goal " + goal + " found!");
                System.out.println("Path: " + String.join(" -> ", path));
                return true; // Goal found
            }

            // If the current node has not been visited
            if (!visited.contains(currentNode)) {
                visited.add(currentNode); // Mark the current node as visited

                // Add all unvisited neighbors to the queue along with the updated path
                for (String neighbor : graph.get(currentNode)) {
                    if (!visited.contains(neighbor)) {
                        List<String> newPath = new ArrayList<>(path);
                        newPath.add(neighbor); // Add neighbor to the path
                        queue.add(new Node(neighbor, newPath)); // Add neighbor and the updated path
                    }
                }
            }
        }

        System.out.println("Goal not found");
        return false; // Return false if the goal was not found
    }

    // Helper class to store the current node and the path taken to reach it
    private static class Node {
        String node;
        List<String> path;

        public Node(String node, List<String> path) {
            this.node = node;
            this.path = path;
        }
    }

    // Main method to test the BFS algorithm
    public static void main(String[] args) {
        // Graph structure with S as the source and G as the goal
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("S", Arrays.asList("A", "B"));
        graph.put("A", Arrays.asList("B", "D"));
        graph.put("B", Arrays.asList("C", "A"));
        graph.put("C", Arrays.asList("E"));
        graph.put("D", Arrays.asList("G"));
        graph.put("E", new ArrayList<>()); // E is a dead end
        graph.put("G", new ArrayList<>()); // G is the goal

        // Test BFS with 'S' as the source and 'G' as the goal
        bfs(graph, "S", "G");
    }
}