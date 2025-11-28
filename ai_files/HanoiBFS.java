import java.util.*;

public class HanoiBFS {

    static class State {
        List<List<Integer>> pegs;

        State(List<List<Integer>> pegs) {
            this.pegs = pegs;
        }

        // Create a deep copy
        State copy() {
            List<List<Integer>> newPegs = new ArrayList<>();
            for (List<Integer> peg : pegs) {
                newPegs.add(new ArrayList<>(peg));
            }
            return new State(newPegs);
        }

        // Serialize for hashing
        String encode() {
            return pegs.toString();
        }

        // Check if goal state
        boolean isGoal() {
            return pegs.get(0).isEmpty() && pegs.get(1).isEmpty()
                    && pegs.get(2).equals(Arrays.asList(3, 2, 1));
        }

        @Override
        public String toString() {
            return pegs.toString();
        }
    }

    // Generate all valid next moves
    static List<State> getNeighbors(State s) {
        List<State> neighbors = new ArrayList<>();

        for (int from = 0; from < 3; from++) {
            if (s.pegs.get(from).isEmpty()) continue;

            int disk = s.pegs.get(from).get(s.pegs.get(from).size() - 1);

            for (int to = 0; to < 3; to++) {
                if (from == to) continue;

                if (s.pegs.get(to).isEmpty()
                        || s.pegs.get(to).get(s.pegs.get(to).size() - 1) > disk) {

                    State newState = s.copy();
                    newState.pegs.get(from).remove(newState.pegs.get(from).size() - 1);
                    newState.pegs.get(to).add(disk);
                    neighbors.add(newState);
                }
            }
        }

        return neighbors;
    }

    // BFS search
    static List<State> bfs(State start) {

        Queue<List<State>> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.add(Arrays.asList(start));
        visited.add(start.encode());

        while (!queue.isEmpty()) {
            List<State> path = queue.poll();
            State curr = path.get(path.size() - 1);

            if (curr.isGoal()) {
                return path;
            }

            for (State next : getNeighbors(curr)) {
                String code = next.encode();
                if (!visited.contains(code)) {
                    visited.add(code);

                    List<State> newPath = new ArrayList<>(path);
                    newPath.add(next);

                    queue.add(newPath);
                }
            }
        }

        return null; // no solution (should not happen)
    }

    public static void main(String[] args) {

        List<List<Integer>> pegs = new ArrayList<>();
        pegs.add(new ArrayList<>(Arrays.asList(3, 2, 1))); // peg A
        pegs.add(new ArrayList<>());                       // peg B
        pegs.add(new ArrayList<>());                       // peg C

        State start = new State(pegs);

        List<State> solution = bfs(start);

        System.out.println("Moves required: " + (solution.size() - 1));
        int step = 0;
        for (State st : solution) {
            System.out.println((step++) + ": " + st);
        }
    }
}


