package 八数码1A星;
/**
 * https://github.com/mengxingyun/AI_homework
 */

import java.awt.*;
import java.util.*;
import java.util.List;

public class Eight_Digit {

    static int[][] INIT_STATE = new int[3][3]; //用来记录初状态
    static int[][] GOAL_STATE = new int[3][3]; //用来记录目标状态
    static int[] INIT_ARRAY = new int[8];//用来记录初态的一维形式
    static int[] GOAL_ARRAY = new int[8];//用来记录目标态的一维形式
    static HashMap<Integer, Point> GOAL_COORDINATE = new HashMap<Integer, Point>();//用来记录目标每个数字的详细坐标

    static int[][] MOVE = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};//空白滑块的每次移动只有四种情况

    static List<int[][]> TEMP_STATE = new ArrayList<int[][]>();//用来存储遍历过的中间状态

    static public class Cur_State implements Cloneable//存储当前状态
    {
        Point p; //空白滑块的坐标
        int steps; //用来记录移动滑块的步数
        int g; //初态到当前状态的实际代价
        int h; //当前状态到目标状态的估计代价
        int[][] temp = new int[3][3];//记录当前数字九宫格的状态
        List<int[][]> CLOSE = new ArrayList<int[][]>();//用来存储从初态到当前态的每一步

        public Cur_State(Point p, int steps, int g, int h, int[][] temp, List<int[][]> CLOSE)//构造函数
        {
            super();
            this.p = p;
            this.steps = steps;
            this.g = g;
            this.h = h;
            this.temp = temp;
            this.CLOSE = CLOSE;
        }

        public Object clone() throws CloneNotSupportedException//重写克隆方法，由浅克隆转为深克隆
        {
            Object o = super.clone();
            Cur_State cur = (Cur_State) o;

            //对空白块坐标进行深克隆
            cur.p = (Point) cur.p.clone();

            //对记录当前数字九宫格状态的二维数组进行深克隆
            cur.temp = new int[3][3];
            for (int i = 0; i < 3; i++) {
                cur.temp[i] = this.temp[i].clone();
            }

            //对向存储目标态移动的每一步的状态空间进行深克隆
            cur.CLOSE = new ArrayList<int[][]>();
            cur.CLOSE.addAll(this.CLOSE);
            return cur;
        }
    }

    static Comparator<Cur_State> compare = new Comparator<Cur_State>() //比较器，用于从OPEN表中筛选出最优结点
    {
        @Override
        public int compare(Cur_State o1, Cur_State o2) {
            return (o1.g + o1.h) - (o2.g + o2.h);
        }
    };

    static void Input()//输入问题的初态和目标态
    {
        Scanner in = new Scanner(System.in);
        System.out.println("请输入初态(0代表滑块)：");
        int cur = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                INIT_STATE[i][j] = in.nextInt();
                if (INIT_STATE[i][j] != 0) {
                    INIT_ARRAY[cur++] = INIT_STATE[i][j];//得到初态的一维数组形式
                }
            }
        }
        cur = 0;

        System.out.println("请输入目标态(0代表滑块)：");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                GOAL_STATE[i][j] = in.nextInt();
                Point temp = new Point();
                temp.x = i;
                temp.y = j;
                GOAL_COORDINATE.put(GOAL_STATE[i][j], temp);//把目标态每个数字的详细坐标输入并保存在hashmap中
                if (GOAL_STATE[i][j] != 0) {
                    GOAL_ARRAY[cur++] = GOAL_STATE[i][j];//得到目标态的一维数组形式
                }
            }
        }
    }

    static boolean Judge()//判断问题是否有解
    {
        int INIT_NUM = 0;//用来记录初态的逆序数
        int GOAL_NUM = 0;//用来记录目标态的逆序数

        for (int i = 7; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (INIT_ARRAY[i] < INIT_ARRAY[j]) {
                    INIT_NUM++;
                }
                if (GOAL_ARRAY[i] < GOAL_ARRAY[j]) {
                    GOAL_NUM++;
                }
            }
        }

        return (INIT_NUM % 2) == (GOAL_NUM % 2);//奇偶性相同返回true
    }

    static int Count_h_1(int h, int [][] temp)//h1(n)
    {
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                if (temp[m][n] != GOAL_STATE[m][n]) {
                    h = h + Math.abs(m - GOAL_COORDINATE.get(temp[m][n]).x) + Math.abs(n - GOAL_COORDINATE.get(temp[m][n]).y);
                }
            }
        }
        return h;
    }

    static int Count_h_2(int h, int [][] temp)//h2(n)
    {
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                if(temp[i][j] != GOAL_STATE[i][j])
                {
                    h = h + 1;
                }
            }
        }
        return h;
    }

    static void Show_Steps(List<int [][]> list)//展示移动步骤
    {
        for(int i = 0; i < list.size(); i++)
        {
            System.out.println("第" + i + "步：");
            for(int m = 0; m < 3; m++)
            {
                System.out.println(list.get(i)[m][0] + " " + list.get(i)[m][1] + " " + list.get(i)[m][2]);
            }
            System.out.println();
        }
    }

    static int Solve(int[][] INIT_STATE) throws CloneNotSupportedException {
        //定位初始状态的滑块坐标
        boolean flag = false;
        Point p1 = new Point();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (INIT_STATE[i][j] == 0) {
                    flag = true;
                    p1.x = i;
                    p1.y = j;
                    break;
                }
            }
            if (flag) break;
        }

        Queue<Cur_State> OPEN = new PriorityQueue<Cur_State>(compare);//优先级队列(也就是open表)，存放生成的结点并按启发式函数值进行排序
        int[][] CLONE_CUR_1 = new int[3][3];
        int[][] CLONE_CUR_2 = new int[3][3];

        for (int i = 0; i < 3; i++) {
            CLONE_CUR_1[i] = INIT_STATE[i].clone();
            CLONE_CUR_2[i] = INIT_STATE[i].clone();
        }

        TEMP_STATE.add(CLONE_CUR_2);//把所有出现过的中间状态都加入到这个表中
        List<int[][]> CLOSE = new ArrayList<int[][]>();//CLOSE表
        CLOSE.add(INIT_STATE);//初状态加入close表中

        Cur_State Search = new Cur_State(p1, 0, 0, 0, CLONE_CUR_1, CLOSE);//初始化
        OPEN.add(Search);//初状态入队

        while (!OPEN.isEmpty())//只要OPEN表不为空
        {
            Search = (Cur_State) OPEN.poll().clone();
            boolean tag = true;//用于判断是否达到了目标状态
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (Search.temp[i][j] != GOAL_STATE[i][j]) {
                        tag = false;
                    }
                }
            }

            if (tag) {
                System.out.println("为了搜索该解，一共扩展了" + TEMP_STATE.size() + "个结点");
                Show_Steps(Search.CLOSE);
                return Search.steps;
            }

            for (int i = 0; i < 4; i++)//遍历四个方向上的移动
            {
                Cur_State NEXT = (Cur_State) Search.clone();
                NEXT.p.x = Search.p.x + MOVE[i][0];
                NEXT.p.y = Search.p.y + MOVE[i][1];

                if (NEXT.p.x >= 0 && NEXT.p.x <= 2 && NEXT.p.y >= 0 && NEXT.p.y <= 2) {
                    NEXT.temp[Search.p.x][Search.p.y] = NEXT.temp[NEXT.p.x][NEXT.p.y];
                    NEXT.temp[NEXT.p.x][NEXT.p.y] = 0;

                    boolean HasSearched = false;//用于判断当前的状态是否出现过
                    for (int j = 0; j < TEMP_STATE.size(); j++) {
                        boolean mark = false;
                        for (int x = 0; x < 3; x++) {
                            for (int y = 0; y < 3; y++) {
                                if (TEMP_STATE.get(j)[x][y] != NEXT.temp[x][y]) {
                                    mark = true;
                                    break;
                                }
                            }
                            if (mark) break;
                        }
                        if (!mark) {
                            HasSearched = true;
                            break;
                        }
                    }

                    if (!HasSearched)//如果这个状态没有出现过
                    {
                        NEXT.steps++;//更新步数
                        NEXT.g++;
                        NEXT.CLOSE.add(NEXT.temp);//当前结点加入到CLOSE表中

                        int h = 0;//计算h(n)
                        h = Count_h_1(h, NEXT.temp);

                        NEXT.h = h;

                        int[][] STATE = new int[3][3];
                        for (int m = 0; m < 3; m++) {
                            STATE[m] = NEXT.temp[m].clone();
                        }
                        TEMP_STATE.add(STATE);
                        OPEN.add((Cur_State) NEXT.clone());
                    }
                }
            }
        }
        return 0;
    }//解决问题

    public static void main(String [] args) throws CloneNotSupportedException {
        Input();
        boolean flag = Judge();
        if(!flag)
        {
            System.out.print("问题无解");
        }
        else
        {
            int ans = Solve(INIT_STATE);
            System.out.println("移动步数" + ans);
        }
    }
}
