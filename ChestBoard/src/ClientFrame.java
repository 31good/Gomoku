import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

class Grid extends JPanel {
    Points[][] points = new Points[15][15];
    String color;
    String status = "start";
    Socket socket;
    int count = 0;
    PrintStream sout;

    Grid(String color, Socket socket, PrintStream sout) throws IOException {
        this.color = color;
        this.socket = socket;
        this.setOpaque(false);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                points[i][j] = new Points(i, j, j * 40 + 26, i * 40 + 26);
            }
        }
        listen_mouse();
        mouseclick();
    }

    void listen_mouse() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if ("end".equals(status)) return;
                int x = e.getX();
                int y = e.getY();
                Points curr;
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        curr = points[i][j];
                        if (curr.isMousearound(x, y) && Objects.equals(curr.occupied, "None")) curr.show = true;
                        else curr.show = false;
                    }
                }
                repaint();
            }
        };
        addMouseMotionListener(adapter);
        addMouseListener(adapter);
    }

    void mouseclick() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ("end".equals(status)) return;
                int x = e.getX();
                int y = e.getY();
                Points curr;
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        curr = points[i][j];
                        if (curr.isMousearound(x, y) && Objects.equals(curr.occupied, "None")) {
                            curr.occupied = color;
                            curr.show = false;
                            count += 1;
                            sout.println(i + "," + j);
                            repaint();
                            if (Objects.equals(checkover(points), "win")) {
                                //System.out.println("Win");
                                Gameover("win");
                            }
                            if (Objects.equals(checkover(points), "draw")) {
                                Gameover("draw");
                            }
                            return;
                        }
                    }
                }
            }
        };
        addMouseListener(adapter);
    }

    void add_point(int i,int j){
        if(Objects.equals(color, "B")){
            points[i][j].occupied="W";
        }
        else{
            points[i][j].occupied="B";
        }
        points[i][j].show = false;
        count += 1;
        repaint();
    }

    public void drawgrid(Graphics graphic) {
        int x;
        int y;
        //draw line
        for (int i = 0; i < 15; i++) {
            y = i * 40 + 26;
            graphic.drawLine(26, y, 586, y);
        }
        for (int i = 0; i < 15; i++) {
            x = i * 40 + 26;
            graphic.drawLine(x, 26, x, 586);
        }
        //draw points
        graphic.fillArc(142, 142, 8, 8, 0, 360);
        graphic.fillArc(462, 142, 8, 8, 0, 360);
        graphic.fillArc(142, 462, 8, 8, 0, 360);
        graphic.fillArc(462, 462, 8, 8, 0, 360);
        graphic.fillArc(302, 302, 8, 8, 0, 360);
    }

    @Override
    public void paint(Graphics graphic) {
        super.paint(graphic);
        drawgrid(graphic);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                points[i][j].draw(graphic);
            }
        }
    }

    public String checkover(Points[][] points) {
        Points curr;
        if (count == 15 * 15) return "draw";
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                curr = points[i][j];
                if (Objects.equals(curr.occupied, "None")) continue;
                String curr_color = curr.occupied;
                int curr_i;
                int curr_j;
                if (i + 4 < 15) {
                    curr_i = i;
                    while (curr_i <= i + 4) {
                        if (!Objects.equals(points[curr_i][j].occupied, curr_color)) break;
                        curr_i += 1;
                    }
                    if (curr_i == i + 5) {
                        return "win";
                    }
                }
                if (j + 4 < 15) {
                    curr_j = j;
                    while (curr_j <= j + 4) {
                        if (!Objects.equals(points[i][curr_j].occupied, curr_color)) break;
                        curr_j += 1;
                    }
                    if (curr_j == j + 5) {
                        return "win";
                    }
                }
                if (i + 4 < 15 && j + 4 < 15) {
                    curr_i = i;
                    curr_j = j;
                    while (curr_j <= j + 4) {
                        if (!Objects.equals(points[curr_i][curr_j].occupied, curr_color)) break;
                        curr_j += 1;
                        curr_i += 1;
                    }
                    if (curr_j == j + 5) {
                        return "win";
                    }
                }
                if (i + 4 < 15 && j - 4 > -1) {
                    curr_i = i;
                    curr_j = j;
                    while (curr_j <= j + 4) {
                        if (!Objects.equals(points[curr_i][curr_j].occupied, curr_color)) break;
                        curr_j -= 1;
                        curr_i += 1;
                    }
                    if (curr_i == i + 5) {
                        return "win";
                    }
                }
            }

        }
        return "lose";
    }

    public void Gameover(String win) {
        status = "end";
        if (Objects.equals(win, "win")) {
            JOptionPane.showMessageDialog(this, "Congratulation, You win!");
            sout.println("win");
        } else if (Objects.equals(win, "lose")) {
            JOptionPane.showMessageDialog(this, "Sorry, You lose");
            sout.println("lose");
        } else {
            JOptionPane.showMessageDialog(this, "It's a draw");
            sout.println("draw");
        }
    }
}


class Points {
    private int row;
    private int col;
    int x;
    int y;
    private int h = 40;
    boolean show = false;

    //boolean occupied = false;
    String occupied = "None";

    Points(int row, int col, int x, int y) {
        this.row = row;
        this.col = col;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics graphic) {
        graphic.setColor(Color.RED);
        if (show) {
            Graphics2D g = (Graphics2D) graphic;
            g.setStroke(new BasicStroke(1.5f));
            //horizontal
            g.drawLine(this.x - h / 2, this.y - h / 2, this.x - h / 4, this.y - h / 2);
            g.drawLine(this.x + h / 4, this.y - h / 2, this.x + h / 2, this.y - h / 2);
            g.drawLine(this.x - h / 2, this.y + h / 2, this.x - h / 4, this.y + h / 2);
            g.drawLine(this.x + h / 4, this.y + h / 2, this.x + h / 2, this.y + h / 2);
            //vertical
            g.drawLine(this.x + h / 2, this.y - h / 2, this.x + h / 2, this.y - h / 4);
            g.drawLine(this.x + h / 2, this.y + h / 4, this.x + h / 2, this.y + h / 2);
            g.drawLine(this.x - h / 2, this.y - h / 2, this.x - h / 2, this.y - h / 4);
            g.drawLine(this.x - h / 2, this.y + h / 4, this.x - h / 2, this.y + h / 2);
        } else {
            if (Objects.equals(occupied, "B")) {
                graphic.setColor(Color.BLACK);
                graphic.drawOval(x - 33 / 2, y - 33 / 2, 33, 33);
                graphic.fillOval(x - 33 / 2, y - 33 / 2, 33, 33);
            } else if (Objects.equals(occupied, "W")) {
                graphic.setColor(Color.WHITE);
                graphic.drawOval(x - 33 / 2, y - 33 / 2, 33, 33);
                graphic.fillOval(x - 33 / 2, y - 33 / 2, 33, 33);
            }
        }
    }

    boolean isMousearound(int x, int y) {
        return x > this.x - h / 2 && y > this.y - h / 2 && x < this.x + h / 2 && y < this.y + h / 2;
    }
}

public class ClientFrame {
    private JFrame jf;
    //private String status="playing";
    private static Grid grid;
    private JMenuBar menu;
    static Socket socket;
    static Scanner sin;
    static Waiting_draw wd;
    static Yes_or_no draw;

    static Refuse refuse = new Refuse();
    public ClientFrame(String color, Socket socket, Scanner sin, PrintStream sout) throws IOException {
        ClientFrame.socket = socket;
        //settings for frame
        jf = new JFrame("Gomoku");
        jf.setSize(620, 670);
        jf.getContentPane().setBackground(new Color(210, 150, 20));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        setupMenu();
        //listen_mouse();
        grid = new Grid(color, socket, sout);
        //paint the chessboard
        jf.add(grid);
        //grid.status="end";
        jf.setVisible(true);
        System.out.println("11111");
        start();
    }


    void setupMenu() {
        menu = new JMenuBar();
        JMenu menu1 = new JMenu("Game");
        JMenu menu2 = new JMenu("Help");
        menu.add(menu1);
        menu.add(menu2);
        JMenuItem item1 = new JMenuItem("Surrender");
        JMenuItem item2 = new JMenuItem("Ask for a Draw");
        menu1.add(item1);
        menu1.add(item2);
        JMenuItem item3 = new JMenuItem("Instruction");
        menu2.add(item3);
        menu.setBackground(Color.WHITE);
        jf.setJMenuBar(menu);
        item1.addActionListener(new Surrender(grid, socket));
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wd=new Waiting_draw(socket,grid);
            }});
        item3.addActionListener(new rules());
    }

    public static void main(String[] args) throws IOException {
        //"B" for black and "W" for white
        Socket socket = new Socket("localhost", 5110);
        Scanner scanner = new Scanner(socket.getInputStream());
        PrintStream sout = new PrintStream(socket.getOutputStream());
        ClientFrame frame = new ClientFrame("B", socket,scanner,sout);
        start();
    }

    public static void start() throws IOException {
        String message = sin.nextLine();
        while (true) {
            if(message.contains(",")){
                String[] parts = message.split(","); // split the string into two parts using the comma as the delimiter
                int firstNum = Integer.parseInt(parts[0]); // convert the first part to an integer
                int secondNum = Integer.parseInt(parts[1]);
                grid.add_point(firstNum,secondNum);
            }
            if (Objects.equals(message, "win")) {
                if (!Objects.equals(grid.status, "end")) {
                    grid.Gameover("lose");
                    break;
                }
            } else if (Objects.equals(message, "lose")) {
                if (!Objects.equals(grid.status, "end")) {
                    grid.Gameover("win");
                    break;
                }
            } else if (Objects.equals(message, "draw") ) {
                if (!Objects.equals(grid.status, "end")) {
                    grid.Gameover("draw");
                    break;
                }
            } else if(Objects.equals(message, "asking_draw")){
                grid.status="end";
                draw=new Yes_or_no(socket,grid);
            }
            else if(Objects.equals(message,"yes")){
                grid.Gameover("draw");
                break;
            }
            else if (Objects.equals(message,"no")){
                wd.setvisible(false);
                refuse.setvisible(true);
                try {
                    Thread.sleep(5000); // wait for 5 seconds
                } catch (InterruptedException e) {
                }
                grid.status="start";
                refuse.setvisible(false);
            }
            message = sin.nextLine();
            }

        socket.close();
    }
}


class rules implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

        JFrame jf = new JFrame("Rules");
        jf.setSize(300, 200);

        JPanel jp = new JPanel(new BorderLayout());

        JTextArea text = new JTextArea(
                "Players alternate turns placing a stone of their color on an empty intersection. " +
                        "Black plays first. The winner is the first player to form an unbroken line of five stones of their color horizontally, vertically, or diagonally. " +
                        "If the board is completely filled and no one can make a line of 5 stones, then the game ends in a draw." +
                        "In Game bar, there is an ask for draw button, if your opponent agree, this game will end and record as a draw" +
                        "Surrender button would directly end the game and record it as a lose.");
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(text);
        jp.add(scroll, BorderLayout.CENTER);

        jf.getContentPane().add(jp);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setVisible(true);
    }
}

class Surrender implements ActionListener {
    Grid grid;
    Socket socket;

    Surrender(Grid grid, Socket socket) {
        this.grid = grid;
        this.socket = socket;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Objects.equals(grid.status, "end")) return;
        grid.Gameover("lose");
        try {
            PrintStream sout = new PrintStream(socket.getOutputStream());
            sout.println("lose");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

class Waiting_draw implements ActionListener {
    Socket socket;
    Grid grid;
    JFrame jf;
    Waiting_draw(Socket socket, Grid grid) {
        this.grid = grid;
        this.socket = socket;
    }

    public void setvisible(boolean bool) {
        jf.setVisible(bool);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Objects.equals(grid.status, "end")) return;
        grid.status="end";
        jf = new JFrame();
        jf.setSize(250, 100);
        JPanel jp = new JPanel(new BorderLayout());

        JTextArea text = new JTextArea("Waiting for your opponent's response");
        jp.add(text, BorderLayout.CENTER);

        jf.getContentPane().add(jp);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jf.setVisible(true);
        try {
            PrintStream sout = new PrintStream(socket.getOutputStream());
            sout.println("asking_draw");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}

class Refuse{
    JFrame jf;
    Refuse(){
        jf = new JFrame();
        jf.setSize(250, 100);
        JPanel jp = new JPanel(new BorderLayout());

        JTextArea text = new JTextArea("Your opponent refuse your request");
        jp.add(text, BorderLayout.CENTER);

        jf.getContentPane().add(jp);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void setvisible(boolean bool){
        jf.setVisible(bool);
    }

}

class Yes_or_no {
    Yes_or_no(Socket socket,Grid grid) throws IOException {
        PrintStream sout = new PrintStream(socket.getOutputStream());
        JFrame jf = new JFrame("");
        jf.setSize(400, 200);
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
        int result = JOptionPane.showConfirmDialog(jf, "Your opponent is asking for a draw", "Agree", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            sout.println("yes");
        }
        else{
            sout.println("no");
            grid.status="start";
        }

        jf.setVisible(false);
    }

}