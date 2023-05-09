import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

class Grid extends JPanel {
    Points[][] points = new Points[15][15];
    //List<Chess> chesses= new ArrayList<Chess>();
    String color;
    String status="start";
    Grid(String color) {
        this.color=color;
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
                if("end".equals(status)) return;
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

    void mouseclick(){
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if("end".equals(status)) return;
                int x = e.getX();
                int y = e.getY();
                Points curr;
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        curr = points[i][j];
                        if (curr.isMousearound(x, y) && Objects.equals(curr.occupied, "None")){
                            curr.occupied=color;
                            curr.show=false;
                            repaint();
                            return;
                        }
                        /*
                        if (curr.isMousearound(x, y) && !curr.occupied){
                            //System.out.println(1);
                            chesses.add(new Chess(curr.x,curr.y,color));
                            curr.occupied=true;
                            repaint();
                            return;
                        }
                         */
                    }
                }
            }
        };
        addMouseListener(adapter);
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
        /*
        for(Chess chess: chesses){
            chess.draw(graphic);
        }*/
    }

    public boolean checkover(Points[][] points) {
        Points curr;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                curr = points[i][j];
                if(Objects.equals(curr.occupied, "None")) continue;
                String curr_color=curr.occupied;
                int curr_i;
                int curr_j;
                if(i+4<15){
                    curr_i=i;
                    while(curr_i<=i+4){
                        if(!Objects.equals(points[curr_i][j].occupied, curr_color))break;
                        curr_i+=1;
                    }
                    if(curr_i==i+5){return true;}
                }
                if(j+4<15){
                    curr_j=j;
                    while(curr_j<=j+4){
                        if(!Objects.equals(points[i][curr_j].occupied, curr_color))break;
                        curr_j+=1;
                    }
                    if(curr_j==j+5){return true;}
                }
                if(i+4<15 && j+4<15){
                    curr_i=i;
                    curr_j=j;
                    while(curr_j<=j+4){
                        if(!Objects.equals(points[curr_i][curr_j].occupied, curr_color))break;
                        curr_j+=1;
                        curr_i+=1;
                    }
                    if(curr_j==j+5){return true;}
                }
                if(i+4<15 && j-4>-1){
                    curr_i=i;
                    curr_j=j;
                    while(curr_j<=j+4){
                        if(!Objects.equals(points[curr_i][curr_j].occupied, curr_color))break;
                        curr_j-=1;
                        curr_i+=1;
                    }
                    if(curr_i==i+5){return true;}
                }
            }

        }
        return false;
    }
}

/*
class Chess{
    int x;
    int y;
    String color;

    Chess(int x, int y, String color){this.x=x;this.y=y;this.color=color;}

    public void draw(Graphics graphic){
        if(Objects.equals(color, "B")){
            graphic.setColor(Color.BLACK);
        }
        else{
            graphic.setColor(Color.WHITE);
        }
        graphic.drawOval(x-33/2, y-33/2, 33, 33);
        graphic.fillOval(x-33/2, y-33/2, 33, 33);
    }
}
*/


class Points {
    private int row = 0;
    private int col = 0;
    int x ;
    int y;
    private int h = 40;
    boolean show = false;

    //boolean occupied = false;
    String occupied="None";
    Points(int row, int col, int x, int y) {
        this.row = row;
        this.col = col;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics graphic) {
        graphic.setColor(Color.RED);
        if(show) {
            Graphics2D g = (Graphics2D) graphic;
            g.setStroke(new BasicStroke(1.5f));
            //horizontal
            g.drawLine(this.x-h/2,this.y - h / 2,this.x-h/4,this.y - h / 2);
            g.drawLine(this.x+h/4,this.y - h / 2,this.x+h/2,this.y - h / 2);
            g.drawLine(this.x-h/2,this.y + h / 2,this.x-h/4,this.y + h / 2);
            g.drawLine(this.x+h/4,this.y + h / 2,this.x+h/2,this.y + h / 2);
            //vertical
            g.drawLine(this.x+h/2,this.y - h / 2,this.x+h/2,this.y - h / 4);
            g.drawLine(this.x+h/2,this.y + h / 4,this.x+h/2,this.y + h / 2);
            g.drawLine(this.x-h/2,this.y - h / 2,this.x-h/2,this.y - h / 4);
            g.drawLine(this.x-h/2,this.y + h / 4,this.x-h/2,this.y + h / 2);
        }
        else {
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
    private Grid grid;
    private JMenuBar menu;

    //"B" for black and "W" for white
    public ClientFrame(String color) {
        //settings for frame
        jf = new JFrame("Gomoku");
        jf.setSize(620, 670);
        jf.getContentPane().setBackground(new Color(210, 150, 20));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        setupMenu();
        //listen_mouse();
        grid = new Grid(color);
        //paint the chessboard
        jf.add(grid);
        //grid.status="end";
        jf.setVisible(true);
        //Gameover(true);
    }


    public void Gameover(boolean win){
        if(win) {
            JOptionPane.showMessageDialog(jf, "Congratulation, You win!");
        }
        else{JOptionPane.showMessageDialog(jf, "Sorry, You lose");}
    }
    void setupMenu() {
        menu = new JMenuBar();
        JMenu menu1 = new JMenu("Game");
        JMenu menu2 = new JMenu("Help");
        menu.add(menu1);
        menu.add(menu2);
        JMenuItem item1 = new JMenuItem("Ask for Rematch");
        JMenuItem item2 = new JMenuItem("Surrender");
        JMenuItem item3 = new JMenuItem("Ask for a Draw");
        menu1.add(item1);
        menu1.add(item2);
        menu1.add(item3);
        JMenuItem item4 = new JMenuItem("Instruction");
        menu2.add(item4);
        menu.setBackground(Color.WHITE);
        jf.setJMenuBar(menu);
        //item1.addActionListener();
        //item2.addActionListener();
        item4.addActionListener(new rules());
        //item4.setActionCommand();
    }

    public static void main(String[] args) {
        ClientFrame frame = new ClientFrame("B");
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
                        "In Game bar, there is a button to ask for rematch which means this game will not be counted but the opponent can choose to fight with you again." +
                        "The ask for draw button, if your opponent agree, this game will end and record as a draw" +
                        "Surrender button would directly end the game and record it as a lose.");
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(text);
        jp.add(scroll, BorderLayout.CENTER);

        jf.getContentPane().add(jp);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setVisible(true);
    }
}
