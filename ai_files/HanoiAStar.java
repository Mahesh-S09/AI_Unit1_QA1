import java.util.*;

public class HanoiAStar {

    static class State {
        int[] pegs;

        State(int[] p) {
            pegs = Arrays.copyOf(p, p.length);
        }

        boolean isGoal() {
            return pegs[0] == 2 && pegs[1] == 2 && pegs[2] == 2;
        }

        List<State> getSuccessors() {
            List<State> next = new ArrayList<>();
            int[] top = {-1, -1, -1};

            for (int d = pegs.length - 1; d >= 0; d--) {
                top[pegs[d]] = d;
            }

            for (int from = 0; from < 3; from++) {
                if (top[from] == -1) continue;
                for (int to = 0; to < 3; to++) {
                    if (from == to) continue;
                    if (top[to] == -1 || top[to] > top[from]) {
                        State s = new State(pegs);
                        s.pegs[top[from]] = to;
                        next.add(s);
                    }
                }
            }
            return next;
        }

        @Override public boolean equals(Object o) {
            return o instanceof State && Arrays.equals(pegs, ((State)o).pegs);
        }
        @Override public int hashCode() { return Arrays.hashCode(pegs); }
        @Override public String toString() { return Arrays.toString(pegs); }
    }

    // Simple heuristic: count disks not on goal peg (peg 2)
    static int heuristic(State s) {
        int h = 0;
        for (int peg : s.pegs)
            if (peg != 2) h++;
        return h;
    }

    public static List<State> aStar(State start) {
        PriorityQueue<List<State>> pq = new PriorityQueue<>(
                Comparator.comparingInt(path ->
                        path.size() + heuristic(path.get(path.size() - 1)))
        );

        Set<State> visited = new HashSet<>();
        pq.add(Arrays.asList(start));

        while (!pq.isEmpty()) {
            List<State> path = pq.poll();
            State s = path.get(path.size() - 1);

            if (s.isGoal()) return path;
            if (!visited.add(s)) continue;

            for (State nxt : s.getSuccessors()) {
                List<State> newPath = new ArrayList<>(path);
                newPath.add(nxt);
                pq.add(newPath);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        State start = new State(new int[]{0, 0, 0});
        List<State> result = aStar(start);

        System.out.println("A* Solution:");
        result.forEach(System.out::println);
    }
}
