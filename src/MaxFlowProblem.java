import java.util.*;

public class MaxFlowProblem {

    static final int INF = (int) 1e9;
    static int n, m;
    static int[][] capacity;


    static class Edge {
        int from, to, weight;
        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    static class DiGraph {
        int V;
        List<Edge>[] adj;

        @SuppressWarnings("unchecked")
        DiGraph(int v) {
            this.V = v;
            adj = new ArrayList[v + 1];
            for (int i = 0; i <= v; i++) {
                adj[i] = new ArrayList<>();
            }
        }

        void addEdge(int from, int to, int weight) {
            adj[from].add(new Edge(from, to, weight));
            adj[to].add(new Edge(to, from, 0));
        }

        List<Edge> getAdj(int u) {
            return adj[u];
        }
    }

    static int bfs(DiGraph graph, int s, int t, int[] parent) {
        Arrays.fill(parent, -1);
        parent[s] = -2;
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{s, INF});

        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int u = curr[0], flow = curr[1];

            for (Edge e : graph.getAdj(u)) {
                int v = e.to;
                if (parent[v] == -1 && capacity[u][v] > 0) {
                    parent[v] = u;
                    int new_flow = Math.min(flow, capacity[u][v]);
                    if (v == t)
                        return new_flow;
                    q.add(new int[]{v, new_flow});
                }
            }
        }
        return 0;
    }

    static int edmondsKarp(DiGraph graph, int s, int t) {
        int flow = 0;
        int[] parent = new int[n + 1];
        int new_flow;

        while ((new_flow = bfs(graph, s, t, parent)) != 0) {
            flow += new_flow;
            int cur = t;
            while (cur != s) {
                int prev = parent[cur];
                capacity[prev][cur] -= new_flow;
                capacity[cur][prev] += new_flow;
                cur = prev;
            }
        }
        return flow;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        m = sc.nextInt();
        DiGraph graph = new DiGraph(n);
        capacity = new int[n + 1][n + 1];

        for (int i = 0; i < m; i++) {
            int a = sc.nextInt(), b = sc.nextInt(), c = sc.nextInt();
            capacity[a][b] += c;
            graph.addEdge(a, b, c);
        }

        System.out.println(edmondsKarp(graph, 1, n));
    }
}