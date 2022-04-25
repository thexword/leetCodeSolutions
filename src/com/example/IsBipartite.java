package com.example;

public class IsBipartite {
    boolean res = true;
    boolean[] visited;
    boolean[] color;

    public boolean isBipartite(int[][] graph) {
        int n = graph.length;
        visited = new boolean[n];
        color = new boolean[n];

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                traverse(graph, i);
            }
        }

        return res;
    }

    private void traverse(int[][] graph, int cur) {
        if (!res) {
            return; // 已经得出结果，不需要再做判断
        }

        visited[cur] = true;

        for (int neighbor : graph[cur]) {
            if (!visited[neighbor]) {
                color[neighbor] = !color[cur];
                traverse(graph, neighbor);
            } else {
                if (color[neighbor] == color[cur]) {
                    res = false;
                    return;
                }
            }
        }
    }
}
