import java.util.Arrays;

public class AlphaBetaPruning{
    // Method to perform alpha-beta pruning
    public static int alphaBetaPruning(int depth, int nodeIndex, boolean isMaximizingPlayer,
                                        int[] values, int alpha, int beta, int maxDepth) {
        // Terminal node (leaf nodes)
        if (depth == maxDepth) {
            System.out.println("Leaf node reached at depth " + depth + ", returning value: " + values[nodeIndex]);
            return values[nodeIndex];
        }

        if (isMaximizingPlayer) {
            int best = Integer.MIN_VALUE;

            System.out.println("Maximizer at depth " + depth + ", alpha: " + alpha + ", beta: " + beta);
            // Maximizer's choice (MAX player)
            for (int i = 0; i < 2; i++) {
                int value = alphaBetaPruning(depth + 1, nodeIndex * 2 + i, false, values, alpha, beta, maxDepth);
                System.out.println("Maximizer at depth " + depth + ", comparing value: " + value + " with best: " + best);
                best = Math.max(best, value);
                alpha = Math.max(alpha, best);
                System.out.println("Maximizer updated alpha: " + alpha);

                // Alpha-Beta Pruning
                if (beta <= alpha) {
                    System.out.println("Pruning at maximizer node at depth " + depth + ", alpha: " + alpha + ", beta: " + beta);
                    break;
                }
            }
            System.out.println("Maximizer at depth " + depth + ", selected best: " + best);
            return best;
        } else {
            int best = Integer.MAX_VALUE;

            System.out.println("Minimizer at depth " + depth + ", alpha: " + alpha + ", beta: " + beta);
            // Minimizer's choice (MIN player)
            for (int i = 0; i < 2; i++) {
                int value = alphaBetaPruning(depth + 1, nodeIndex * 2 + i, true, values, alpha, beta, maxDepth);
                System.out.println("Minimizer at depth " + depth + ", comparing value: " + value + " with best: " + best);
                best = Math.min(best, value);
                beta = Math.min(beta, best);
                System.out.println("Minimizer updated beta: " + beta);

                // Alpha-Beta Pruning
                if (beta <= alpha) {
                    System.out.println("Pruning at minimizer node at depth " + depth + ", alpha: " + alpha + ", beta: " + beta);
                    break;
                }
            }
            System.out.println("Minimizer at depth " + depth + ", selected best: " + best);
            return best;
        }
    }

    public static void main(String[] args) {
        // The depth of the game tree
        int maxDepth = 3;

        // Values of the leaf nodes
        int[] values = {-1, 4, 2, 6, -3, -5, 0, 7};

        // Initial values of alpha and beta
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Call the alpha-beta pruning algorithm
        int optimalValue = alphaBetaPruning(0, 0, true, values, alpha, beta, maxDepth);

        System.out.println("\nThe optimal value is: " + optimalValue);
    }
}
