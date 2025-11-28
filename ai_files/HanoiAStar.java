import java.util.*;

public class HanoiAStar {

    static class State {
        List<List<Integer>> pegs;

        State(List<List<Integer>> pegs) {
            this.pegs = pegs;
        }

        State copy() {
            List<List<Integer>> newPegs = new ArrayList<>();
            for (List<Integer> peg : pegs) {
                newPegs.add(new ArrayList<>(peg));
            }
            return new State(newPegs);
        }

        boolean isGoal() {
            return pegs.get(0).isEmpty() &&
                   pegs.get(1).isEmpty() &&
                   pegs.get(2).equals(Arrays.asList(3, 2, 1));
        }

        String encode() {
            return pegs.toString();
        }

        @Override
        public String toString() {
            return pegs.toString();
        }
    }

    // Admissible heuristic:
    // Count how many disks are NOT on the goal peg (peg 2)
    static int heuristic(State s) {
        int goalSize = s.pegs.get(2).size();
        return 3 - goalSize; // 3 disks total
    }

    // Generate neighbor states
    static List<State> getNeighbors(State s) {
        List<State> neighbors = new ArrayList<>();

        for (int from = 0; from < 3; from++) {
            if (s.pegs.get(from).isEmpty()) continue;

            int disk = s.pegs.get(from).get(s.pegs.get(from).size() - 1);

            for (int to = 0; to < 3; to++) {
                if (from == to) continue;

                if (s.pegs.get(to).isEmpty() ||
                    s.pegs.get(to).get(s.pegs.get(to).size() - 1) > disk) {

                    State newState = s.copy();
                    newState.pegs.get(from).remove(newState.pegs.get(from).size() - 1);
                    newState.pegs.get(to).add(disk);
                    neighbors.add(newState);
                }
            }
        }

        return neighbors;
    }

    // A* Search
    static List<State> aStar(State start) {

        PriorityQueue<List<State>> open = new PriorityQueue<>(
            Comparator.comparingInt(path -> 
                path.size() - 1 + heuristic(path.get(path.size() - 1))
            )
        );

        Set<String> visited = new HashSet<>();

        open.add(Arrays.asList(start));

        while (!open.isEmpty()) {

            List<State> path = open.poll();
            State current = path.get(path.size() - 1);

            if (current.isGoal()) {
                return path;
            }

            if (visited.contains(current.encode())) continue;
            visited.add(current.encode());

            for (State next : getNeighbors(current)) {
                if (!visited.contains(next.encode())) {
                    List<State> newPath = new ArrayList<>(path);
                    newPath.add(next);
                    open.add(newPath);
                }
            }
        }

        return null; // should not happen
    }

    public static void main(String[] args) {

        List<List<Integer>> pegs = new ArrayList<>();
        pegs.add(new ArrayList<>(Arrays.asList(3, 2, 1))); // start
        pegs.add(new ArrayList<>());
        pegs.add(new ArrayList<>()); // goal peg = index 2

        State start = new State(pegs);

        List<State> solution = aStar(start);

        System.out.println("Moves required: " + (solution.size() - 1));
        int i = 0;
        for (State s : solution) {
            System.out.println((i++) + ": " + s);
        }
    }
}

