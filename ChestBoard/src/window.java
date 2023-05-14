import javax.swing.*;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.net.UnknownHostException;
import java.io.IOException;

public class window extends JFrame {

    private JTextField usernameTextField, emailTextField, regUsernameTextField, regEmailTextField;
    private JPasswordField passwordField, regPasswordField;
    private JButton loginButton, registrationButton;
    private JLabel regSuccess, regFail;
    private int port = 5110;
    private static window currWindow;
    private JFrame frame;
    
    private Socket socket;
    private Scanner scanner;
    private PrintStream sout;

    public void windowLogin() {
        frame = new JFrame("Login or Registration");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        JLabel loginLabel = new JLabel("Login");
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.5;
        panel.add(loginLabel, c);

        JLabel registrationLabel = new JLabel("Registration");
        c.gridx = 2;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(registrationLabel, c);

        JLabel usernameLabel = new JLabel("Username:");
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(usernameLabel, c);

        JLabel emailLabel = new JLabel("Email:");
        c.gridx = 0;
        c.gridy = 2;
        panel.add(emailLabel, c);

        JLabel passwordLabel = new JLabel("Password:");
        c.gridx = 0;
        c.gridy = 3;
        panel.add(passwordLabel, c);

        usernameTextField = new JTextField(10);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.5;
        panel.add(usernameTextField, c);

        emailTextField = new JTextField(10);
        c.gridx = 1;
        c.gridy = 2;
        panel.add(emailTextField, c);

        passwordField = new JPasswordField(10);
        c.gridx = 1;
        c.gridy = 3;
        panel.add(passwordField, c);
        
        JLabel regUsernameLabel = new JLabel("Username:");
        c.gridx = 3;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(regUsernameLabel, c);

        JLabel regEmailLabel = new JLabel("Email:");
        c.gridx = 3;
        c.gridy = 2;
        panel.add(regEmailLabel, c);

        JLabel regPasswordLabel = new JLabel("Password:");
        c.gridx = 3;
        c.gridy = 3;
        panel.add(regPasswordLabel, c);
        
        regUsernameTextField = new JTextField(10);
        c.gridx = 4;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(regUsernameTextField, c);

        regEmailTextField = new JTextField(10);
        c.gridx = 4;
        c.gridy = 2;
        panel.add(regEmailTextField, c);

        regPasswordField = new JPasswordField(10);
        c.gridx = 4;
        c.gridy = 3;
        panel.add(regPasswordField, c);

        loginButton = new JButton("Login");
        loginButton.addActionListener(new sendMessageFromButton());
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_START;
        panel.add(loginButton, c);

        registrationButton = new JButton("Registration");
        loginButton.addActionListener(new sendMessageFromButton());
        c.gridx = 3;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(registrationButton, c);
        
        regSuccess = new JLabel("Registrasion Success");
        loginButton.addActionListener(new sendMessageFromButton());
        c.gridx = 4;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(registrationButton, c);
        
        regFail = new JLabel("Registrasion Fail");
        regFail.setForeground(Color.RED);
        loginButton.addActionListener(new sendMessageFromButton());
        c.gridx = 4;
        c.gridy = 4;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(registrationButton, c);
        frame.setVisible(true);
    }
    
    public void startApp(){
        try {
            socket = new Socket("localhost", port);
            scanner = new Scanner(socket.getInputStream());
            sout = new PrintStream(socket.getOutputStream());
        }  catch (UnknownHostException e2){
            System.out.println("Host not found: " + port);
        } catch (IOException e3) {
            System.out.println("I/O error from port " + port);
        }
    }
    
    public static void main(String[] args) {
        currWindow = new window();
        currWindow.windowLogin();
        currWindow.startApp();
    }
    
    public void showLabel(boolean showLabel1) {
        if (showLabel1) {
            regSuccess.setVisible(true);
            regFail.setVisible(false);
        } else {
            regSuccess.setVisible(false);
            regFail.setVisible(true);
        }

        Timer timer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showLabel1) {
                    regSuccess.setVisible(false);
                } else {
                    regFail.setVisible(false);
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    class sendMessageFromButton implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Object pressedButton = e.getSource();
            if (pressedButton == loginButton){
                String loginUsername = usernameTextField.getText();
                String loginEmail = emailTextField.getText();
                String loginpassword = passwordField.getText();
                sout.println('1');
                String returnCode = scanner.nextLine();
                sout.println(loginUsername);
                returnCode = scanner.nextLine();
                sout.println(loginEmail);
                returnCode = scanner.nextLine();
                sout.println(loginpassword);
                returnCode = scanner.nextLine();
                if (returnCode.equals("200")){
                    try {
                        frame.setVisible(false);
                        System.out.println("success");
                        new ClientFrame("B", socket, scanner, sout);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.dispose();
                    System.out.println("Login window close");
                }
            }else{
                String regUsername = regUsernameTextField.getText();
                String regEmail = regEmailTextField.getText();
                String regpassword = regPasswordField.getText();
                sout.println('1');
                String returnCode = scanner.nextLine();
                sout.println(regUsername);
                returnCode = scanner.nextLine();
                sout.println(regEmail);
                returnCode = scanner.nextLine();
                sout.println(regpassword);
                returnCode = scanner.nextLine();
                if (returnCode.equals("300")){
                    currWindow.showLabel(true);
                }
                else{
                    currWindow.showLabel(false);
                }
            }
            
        }
    }
}