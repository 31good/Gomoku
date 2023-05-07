import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientFrame {
    private JFrame jf;
    private JPanel jp;

    private JMenuBar menu;

    public ClientFrame() {
        //settings for frame
        jf = new JFrame("Backgammon");
        jf.setSize(620, 670);
        jf.getContentPane().setBackground(new Color(210, 150, 20));
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setResizable(false);
        jf.setLocationRelativeTo(null);
        //settings for panel
        jp = new JPanel();
        //settings for menu
        setupMenu();
        jf.setVisible(true);
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

    }

    public static void main(String[] args) {
        ClientFrame frame = new ClientFrame();
    }

}
class rules implements ActionListener  {
    @Override
    public void actionPerformed(ActionEvent e) {

        JFrame jf = new JFrame("Rules");
        jf.setSize(300,200);

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