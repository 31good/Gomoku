package ChestBoard.src;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author 15801
 */

public class BlackGammonServer {
    private static int Port = 5110;
    private static String url = "jdbc:mariadb://localhost:3306/chatappdb";
    private static String user = "root";
    private static String hostpassword = "12345678";
    private Connection conn;
    private Map<String, userHandler> map = new HashMap<>();
    private List<userHandler> unmatchUsersArr = new ArrayList<>();
    
    private List<userHandler> usersArr = new ArrayList<>();
    
    public static void main(String[] args) {
        BlackGammonServer server = new BlackGammonServer();
        server.startServer();
    }
    
    private void startServer(){
        try{
            ServerSocket ss = new ServerSocket(Port);
            System.out.println("Server start on port:" + Port);
            while (true){
                //System.out.println("port 5190!");
                Socket userSock = ss.accept(); 
                //System.out.println("Got a connection from: "+userSock.getInetAddress());
                userHandler newUserHandler = new userHandler(userSock);
                usersArr.add(newUserHandler);
                newUserHandler.start();
            }
        }
        catch(IOException e){
            System.out.println(e);
            System.out.println("Unable to bind to port" + Port);
        }
    }
    
    private boolean storeChessHistory(String username1, String username2, String winner){
        return true;
    }
    private boolean accountAuthenticate(String username, String password, String postUseraddress){
        return true;
        /*
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,hostpassword);
            String query = "select * from user where username = " + username;
            Statement s = conn.createStatement();
            ResultSet rs= s.executeQuery(query);
            while (rs.next()){
                String dbpassword = rs.getString("password"); //attrib name comes from DB
                if (dbpassword.equals(password)){
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String timeInput = now.format(formatter);
                    String insertQuery = "INSERT INTO record (username, IP, time) VALUES ("+username+", "+postUseraddress+", "+timeInput+" )";
                    s.executeQuery(insertQuery);
                    conn.close();
                    return true;
                }
            }
            return false;
        }
        catch(Exception e){
            System.out.println("Caught exception: "+e.toString());
            return false;
        }
        */
    }
    
    private boolean registerAcctount(String email, String username, String password){
        return true;
        /*
        
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url,user,hostpassword);
            String query = "select * from user where username = " + username;
            Statement s = conn.createStatement();
            ResultSet rs= s.executeQuery(query);
            while (rs.next()){
                return false;
            }
            String dbpassword = rs.getString("password"); //attrib name comes from DB
            String insertQuery = "INSERT INTO record (email, username, password) VALUES ("+email+", "+username+", "+password+" )";
            s.executeQuery(insertQuery);
            conn.close();
            return true;
        }
        catch(Exception e){
            System.out.println("Caught exception: "+e.toString());
            return false;
        }
        */
    }
    
    private void castMessage(String postMessage, userHandler postUser){
        for (userHandler currUser:usersArr){
            currUser.sendMessage(postUser.getUsername()+ ": " + postMessage);
        }
    }
    
    class userHandler extends Thread{
        private Socket userSocket;
        private String username;
        private PrintStream sout;
        private userHandler connectedSocket;
        private Boolean ifTerm;

        public userHandler(Socket inputUserSocket){
            userSocket = inputUserSocket;
        }
        
        public void setUpChess(userHandler ConnectUser, Boolean Term){
            connectedSocket = ConnectUser;
            ifTerm = Term;
            sendMessage("found_"+Term);
        }
        
        public String getUsername(){
            return username;
        }
        
        public void run(){
            try{
                Scanner sin = new Scanner(userSocket.getInputStream());
                sout = new PrintStream(userSocket.getOutputStream());
                //1 is registration
                while(true){
                    String inputIfRegistration = sin.nextLine();
                    if(inputIfRegistration.equals("1")){
                        sout.println("Please enter your email:");
                        String email = sin.nextLine();
                        sout.println("Please enter your username:");
                        String inputUsername = sin.nextLine();
                        sout.println("Please enter your password:");
                        String password = sin.nextLine();
                        Boolean ifSuccess = registerAcctount(email, username, password);
                        if(ifSuccess){
                            sout.println("200");
                            break;
                        }else
                            sout.println("500");
                    }else{
                        sout.println("Please enter your username:");
                        String inputUsername = sin.nextLine();
                        sout.println("Please enter your password:");
                        String password = sin.nextLine();
                        Boolean accountValidation = accountAuthenticate(inputUsername, password, userSocket.getInetAddress().getHostAddress());
                        if (!accountValidation){
                            sout.println("500");
                        }else{
                            sout.println("300");
                            username = inputUsername;
                            break;
                        }
                    }
                }
                while(true){
                    if(connectedSocket == null){
                        String menuOption = sin.nextLine();
                        if (menuOption.equals("search")){
                            unmatchUsersArr.add(this);
                            while (unmatchUsersArr.size() >= 2){
                                unmatchUsersArr.get(0).setUpChess(unmatchUsersArr.get(1), true);
                                unmatchUsersArr.get(1).setUpChess(unmatchUsersArr.get(0), false);
                                unmatchUsersArr.remove(0);
                                unmatchUsersArr.remove(0);
                                System.out.println("Match Found");
                            }
                        }else if (menuOption.equals("EXIT")){
                            userSocket.close();
                            usersArr.remove(this);
                            break;
                        }
                        else
                            unmatchUsersArr.remove(this);
                    }
                    else{
                        if(sin.hasNextLine()) {
                            String ChessStep = sin.nextLine();
                            System.out.println(ChessStep);
                            if (ChessStep.equals("endChess")) {
                                //update record to db
                                storeChessHistory(username, connectedSocket.getUsername(), username);
                                connectedSocket.disconnectPlayer();
                                connectedSocket = null;
                            }
                            else if (ifTerm) {
                                connectedSocket.sendMessage(ChessStep);
                                connectedSocket.setIfTermToTrue();
                                //ifTerm = false;
                            }
                        }
                    }
                }
            userSocket.close();
            }
            catch(IOException e){
                System.out.println("Error occur for" +userSocket.getInetAddress()+ "disconnected from server");
            }
        }
        
        public void setIfTermToTrue(){
            ifTerm = true;
        }
        
        public void disconnectPlayer(){
             connectedSocket = null;
        }
        public void sendMessage(String postMessage){
            sout.println(postMessage);
        }
    }
}