import javax.swing.*;
import java.awt.*;

public class ClientFrame {
    private JFrame jf;
    private JPanel jp;

    private JMenuBar menu;
    public ClientFrame(){
        //settings for frame
        jf = new JFrame("Backgammon");
        jf.setSize(620,670);
        jf.getContentPane().setBackground(new Color(210,150,20));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        //settings for panel
        jp= new JPanel();
        //settings for menu
        menu=new JMenuBar();
        JMenu menu1 = new JMenu("Game");
        JMenu menu2 = new JMenu("Help");
        menu.add(menu1);
        menu.add(menu2);
        menu.setBackground(Color.WHITE);
        jf.setJMenuBar(menu);






        jf.setVisible(true);
    }

    public static void main(String[] args) {
        ClientFrame frame = new ClientFrame();
    }
}