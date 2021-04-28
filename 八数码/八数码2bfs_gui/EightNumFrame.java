package 八数码2bfs_gui;
/**
 * https://blog.csdn.net/HelloWorld10086/article/details/41853389
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class EightNumFrame extends Frame implements ActionListener,KeyListener
{
    MenuBar menubar=new MenuBar();
    Menu menu_file = new Menu("文件(F)");
    MenuItem restart = new MenuItem("重新开始");
    MenuItem nextPath = new MenuItem("提示");
    MenuItem printPath = new MenuItem("还原");
    MenuItem exit = new MenuItem("退出");
    Button[] button;
    Panel panel;
    int row,col;
    private static int position,cellNum;
    final int dr[] = { 0,-1, 0, 1};
    final int dc[] = {-1, 0, 1, 0};
    public EightNumFrame(int row,int col) {
        super.setMenuBar(menubar);
        //Windows风格
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.row = row;
        this.col = col;
        cellNum = row*col;

        restart.addActionListener(this);
        exit.addActionListener(this);
        nextPath.addActionListener(this);
        printPath.addActionListener(this);
        menu_file.add(restart);
        menu_file.add(nextPath);
        menu_file.add(printPath);
        menu_file.add(exit);
        menubar.add(menu_file);

        panel = new Panel(new GridLayout(row,col)) ;
        button = new Button[cellNum];
        for(int i = 0; i < cellNum; i++) {
            if(i == cellNum - 1) {
                button[i] = new Button(" ");
            }else {
                button[i] = new Button(String.valueOf(i + 1));
            }
            button[i].setFont(new Font("Courier", 1, 20));
            button[i].addActionListener(this);
            button[i].addKeyListener(this);
            panel.add(button[i]);
        }
        position = cellNum - 1;
        this.add(BorderLayout.CENTER,panel);
        this.setTitle("八数码1A星");
        this.setVisible(true);
        this.setSize(300,300);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width/2;
        int screenHeight = screenSize.height/2;
        int height = this.getHeight();
        int width = this.getWidth();
        this.setLocation(screenWidth-width/2, screenHeight-height/2);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
    void start() {
        int a[] = new int[9];
        do {
            int k = 0;
            Random random=new Random();
            Set<Integer> set=new HashSet<Integer>();
            while(set.size() < cellNum-1) {
                int n=random.nextInt(cellNum-1)+1;
                if(!set.contains(n)) {
                    set.add(n);
                    a[k++] = n;
                }
            }
            a[k] = 0;
        }while(!EightNumPath.isok(a));
        for(int i = 0; i < 9; i++)
            button[i].setLabel(String.valueOf(a[i]));
        button[cellNum-1].setLabel(" ");
        position = cellNum - 1;
    }
    boolean win() {
        for(int i = 0; i < cellNum - 1; i++) {
            if(button[i].getLabel().equals(" ")) {
                return false;
            }else if(Integer.valueOf(button[i].getLabel()) != i+1) {
                return false;
            }
        }
        return true;
    }
    private boolean judge(Button a, Button b) {
        for(int i = 0; i < 4; i++) {
            if( (a.getX() == b.getX() + dr[i]*a.getWidth())
                    && (a.getY() == b.getY() + dc[i]*a.getHeight())) {
                return true;
            }
        }
        return false;
    }

    public void actionPerformed(ActionEvent e) {
        StringBuffer state = new StringBuffer();
        if(e.getSource() == restart) {
            start();
            return;
        }else if(e.getSource() == exit) {
            System.exit(0);
            return;
        }else if(e.getSource() == nextPath) {
            for(int i = 0; i < cellNum; i++) {
                if(button[i].getLabel().equals(" ")) {
                    state.append('0');
                }else {
                    state.append(button[i].getLabel());
                }
            }
            String path = EightNumPath.solve(state.toString());
            JOptionPane.showMessageDialog(this,"建议走："+path);
            System.out.println(path);
            return;
        }else if(e.getSource() == printPath) {
            for(int i = 0; i < cellNum; i++) {
                if(button[i].getLabel().equals(" ")) {
                    state.append('0');
                }else {
                    state.append(button[i].getLabel());
                }
            }
            String path = EightNumPath.solve(state.toString());
            for(int i = 0; i < path.length(); i++) {
                switch(path.charAt(i)) {
                    case 'U':
                        go(KeyEvent.VK_UP);
                        break;
                    case 'D':
                        go(KeyEvent.VK_DOWN);
                        break;
                    case 'L':
                        go(KeyEvent.VK_LEFT);
                        break;
                    case 'R':
                        go(KeyEvent.VK_RIGHT);
                        break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        for(int i = 0; i < cellNum; i++) {
            if(e.getSource() == button[i]) {
                if(!button[i].getLabel().equals(" ") && judge(button[i],button[position])) {
                    button[position].setLabel(button[i].getLabel());
                    button[i].setLabel(" ");
                    position = i;
                }
            }
        }
        if(win()) {
            JOptionPane.showMessageDialog(this,"Congratulations");
        }
    }
    void go(int dir) {
        int x = position / col;
        int y = position % col;
        switch(dir) {
            case KeyEvent.VK_UP:
                if(x != 0) {
                    button[position].setLabel(button[position-col].getLabel());
                    button[position-col].setLabel(" ");
                    position -= col;
                }
                break;
            case KeyEvent.VK_DOWN:
                if(x != row-1) {
                    button[position].setLabel(button[position+col].getLabel());
                    button[position+col].setLabel(" ");
                    position += col;
                }
                break;
            case KeyEvent.VK_LEFT:
                if(y != 0) {
                    button[position].setLabel(button[position-1].getLabel());
                    button[position-1].setLabel(" ");
                    position -= 1;
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(y != col-1) {
                    button[position].setLabel(button[position+1].getLabel());
                    button[position+1].setLabel(" ");
                    position += 1;
                }
                break;
        }
    }
    public void keyPressed(KeyEvent e) {
        go(e.getKeyCode());
        if(win()) {
            JOptionPane.showMessageDialog(this,"Congratulations");
        }
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public static void main(String[] args) {
        new EightNumFrame(3, 3);
    }
}