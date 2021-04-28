package 八数码2bfs_gui;

import java.util.*;

public class EightNumPath {
    final static int dx[] = {-1, 1, 0, 0};
    final static int dy[] = { 0, 0,-1, 1};
    final static String dir = "UDLR";
    static int maxstate = 400000;
    static int [][]st = new int[maxstate][9];
    static int []goal = {1,2,3,4,5,6,7,8,0};
    static int []dist = new int[maxstate];
    static int []fa = new int[maxstate];
    static int []move = new int[maxstate];
    static boolean []vis = new boolean[maxstate];
    static int []fact = new int[9];
    static StringBuffer path;
    public static boolean isok(int []a) {
        int sum=0;
        for(int i=0; i < 9; i++)
            for(int j=i+1; j < 9; j++)
                if(a[j] != 0 && a[i] != 0 && a[i] > a[j])
                    sum++;
        if(sum % 2 == 0) {
            return true;
        }
        return false;
    }
    private static void init_lookup_table() {
        fact[0] = 1;
        for(int i = 1; i < 9; i++) {
            fact[i] = fact[i-1] * i;
        }
        Arrays.fill(vis, false);
    }
    private static boolean try_to_insert(int s) {
        int code = 0;
        for(int i = 0; i < 9; i++) {
            int cnt = 0;
            for(int j = i+1; j < 9; j++) {
                if(st[s][j] < st[s][i]) {
                    cnt++;
                }
            }
            code += fact[8-i] * cnt;
        }
        if(vis[code]) {
            return false;
        }
        return vis[code] = true;
    }

    private static void print_path(int cur) {
        while(cur != 1) {
            path.insert(0,dir.charAt(move[cur]));
            cur = fa[cur];
        }
    }
    private static int bfs() {
        init_lookup_table();
        int front = 1 , rear = 2;
        try_to_insert(front);
        while(front < rear) {
            if(Arrays.equals(st[front], goal)) {
                return front;
            }
            int z;
            for(z = 0; z < 9; z++) {
                if(st[front][z] == 0) {
                    break;
                }
            }
            int x = z/3, y = z%3;
            for(int d = 0; d < 4; d++) {
                int newx = x + dx[d];
                int newy = y + dy[d];
                int newz = newx * 3 + newy;
                if(newx >= 0 && newx < 3 && newy >= 0 && newy < 3) {
                    st[rear] = Arrays.copyOf(st[front], st[front].length);
                    st[rear][newz] = st[front][z];
                    st[rear][z] = st[front][newz];
                    dist[rear] = dist[front] + 1;

                    if(try_to_insert(rear)) {
                        fa[rear] = front;
                        move[rear] = d;
                        rear++;
                    }
                }
            }
            front++;
        }
        return 0;
    }
    public static String solve(String state) {
        path = new StringBuffer();
        for(int i = 0; i < state.length(); i++) {
            st[1][i] = Integer.valueOf(state.charAt(i)) - '0';
        }
        int ans = bfs();
        print_path(ans);
        return path.toString();
    }
}