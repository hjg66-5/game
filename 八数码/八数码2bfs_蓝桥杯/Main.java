package 八数码2bfs_蓝桥杯;
/**
 * https://blog.csdn.net/a1439775520/article/details/105810595/
 * java A*算法解决八数码、十五数码
 */

import java.util.*;

/**
 * 2 8 3 1 6 4 7 0 5
 * 1 2 3 8 0 4 7 6 5
 * 5
 */
public class Main {
    static int[]dx = {0,0,1,-1};
    static int[]dy = {1,-1,0,0};
    static int[][]st = new int[3][3];
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        int [][]st1 = new int[3][3];
        for(int i=0;i<3;++i){
            for(int j=0;j<3;++j){
                st1[i][j]=scan.nextInt();
            }
        }
        for(int i=0;i<3;++i){
            for(int j=0;j<3;++j){
                st[i][j]=scan.nextInt();
            }
        }
        System.out.println(bfs(st1));
    }
    public static int[][] swap(int[][]st1,int i,int j,int sx,int sy){
        int[][]st2 = new int[3][3];
        for(int w=0;w<3;++w){
            for(int e=0;e<3;++e){
                st2[w][e]=st1[w][e];
            }
        }
        int x = st2[i][j];
        st2[i][j]=st1[sx][sy];
        st2[sx][sy]=x;
        return st2;
    }
    public static int bfs(int[][]st1){
        Queue<int[][]> q = new LinkedList<>();
        HashMap<int[][],Integer> m = new HashMap<>();
        q.offer(st1);
        m.put(st1, 0);

        while(!q.isEmpty()){
            int[][]st2 = q.poll();
            boolean b1 = true;
            for(int w=0;w<3;++w){
                for(int e=0;e<3;++e){
                    if(st2[w][e]!=st[w][e]){
                        b1=false;
                    }
                }
            }
            if(b1){
                return m.get(st2);
            }
            for(int i=0;i<3;++i){
                for(int j=0;j<3;++j){
                    if(st2[i][j]==0){
                        for(int k=0;k<4;++k){
                            int sx = i+dx[k];
                            int sy = j+dy[k];
                            if(sx<0||sx>=3||sy<0||sy>=3){
                                continue;
                            }
                            int[][]st3=swap(st2,i,j,sx,sy);

                            if(!m.containsKey(st3)){
                                q.offer(st3);
                                m.put(st3, m.get(st2)+1);
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }
}

