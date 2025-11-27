import java.util.*;

class DFSHanoi {
    static void dfs(int n, char from, char to, char aux) {
        if (n == 0) return;
        dfs(n - 1, from, aux, to);
        System.out.println("Move disk " + n + " from " + from + " to " + to);
        dfs(n - 1, aux, to, from);
    }

    public static void main(String[] args) {
        System.out.println("DFS Solution:");
        dfs(3, 'A', 'C', 'B'); // 3 disks, from A to C using B
    }
}
