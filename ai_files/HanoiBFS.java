import java.util.*;

public class HanoiBFS {

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

    public static List<State> bfs(State start) {
        Queue<List<State>> q = new LinkedList<>();
        Set<State> visited = new HashSet<>();

        q.add(Arrays.asList(start));
        visited.add(start);

        while (!q.isEmpty()) {
            List<State> path = q.poll();
            State s = path.get(path.size() - 1);

            if (s.isGoal()) return path;

            for (State nxt : s.getSuccessors()) {
                if (!visited.contains(nxt)) {
                    visited.add(nxt);
                    List<State> np = new ArrayList<>(path);
                    np.add(nxt);
                    q.add(np);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        State start = new State(new int[]{0, 0, 0});
        List<State> result = bfs(start);

        System.out.println("BFS Solution:");
        result.forEach(System.out::println);
    }
}
