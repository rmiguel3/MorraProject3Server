import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class MorraServer {
    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    MorraInfo morraInfo = new MorraInfo();
    MorraInfo serverInfo = new MorraInfo();
    int portNum;
    private Consumer<Serializable> callback;


    MorraServer(Consumer<Serializable> call, int port){
        callback = call;
        server = new TheServer();
        server.start();
        portNum = port;
    }


    public class TheServer extends Thread{

        public void run() {
            //start the server and wait until clients are connected
            try(ServerSocket mysocket = new ServerSocket(portNum);) {
                System.out.println("Server is waiting for a client!");
                while(true) {
                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c);
                    c.start();
                    if(count == 1){
                        callback.accept("It is just you my friend...");
                        serverInfo.setPlayerString("It is just you my friend...");
                    }
                    //2 clients have joined the game
                    else if(count > 1){
                        callback.accept("There are 2 people connected to the server. Time to play Morra!");
                        serverInfo.setPlayerString("There are 2 people connected to the server. Time to play Morra!");
                        morraInfo.setTwoPlayers(true);
                    }
                    count++;
                }
            }//end of try
            catch(Exception e) {
                callback.accept("Server socket did not launch");
            }
        }//end of while
    }


    class ClientThread extends Thread{

        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        //sends and updates the clients
        public void updateClients() {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    //serverInfo = (MorraInfo) t.in.readObject();
                    serverInfo.setpNum(i + 1);
                    t.out.writeObject(serverInfo);
                    t.out.reset();
                    t.out.flush();
                }
                catch(Exception e) {}
            }
        }

        //sends and updates the clients
        public void updateServer() {
            for(int i = 0; i < 2; i++) {
                ClientThread t = clients.get(i);
                try {
                    morraInfo = (MorraInfo) t.in.readObject();
                    //receives the morraInfo from client and evaluates who won
                    if (morraInfo.getpNum() == 1) {
                        callback.accept("client: " + morraInfo.getpNum() + " chooses: " + morraInfo.getP1Plays());
                    } else if (morraInfo.getpNum() == 2) {
                        callback.accept("client: " + morraInfo.getpNum() + " chooses: " + morraInfo.getP2Plays());
                    }
                }
                catch(Exception e) {}
            }
        }

        //Evaluate who won the round between 2 players
        public int morraGameLogic(int p1, int p2){
            int total = Integer.parseInt(morraInfo.getP1Plays()) + Integer.parseInt(morraInfo.getP2Plays());
            if(p1 == total && p2 != total){
                return 1;
            }
            else if(p2 == total && p1 != total){
                return 2;
            }
            return 0;
        }

        public void determineWinner(MorraInfo morraInfo){
            int win = morraGameLogic(Integer.parseInt(morraInfo.getP1Plays()), Integer.parseInt(morraInfo.getP2Plays()));
            if(win == 1){
                morraInfo.setP1Points(win);
                morraInfo.setPlayerString("client #"+ morraInfo.getpNum() + " has won this round!");
            }
            else if(win == 2){
                morraInfo.setP2Points(win);
                morraInfo.setPlayerString("client #"+ morraInfo.getpNum() + " has won this round!");
            }
            else{
                morraInfo.setPlayerString("No one won game is tied!");
                updateClients();
            }
        }

        public void run() {

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            } catch (Exception e) {
                System.out.println("Streams not open");
            }
            updateClients();
            //updateClients("new client on server: client #" + count);
            while (true) {
                try {
                    if(morraInfo.isTwoPlayers()){
                        updateServer();
                        //determines who won and prints out the winner
                        determineWinner(morraInfo);
                        updateClients();
                    }
                } catch (Exception e) {
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + morraInfo.getpNum() + "....closing down!");
                    //updateClients("Client #" + count + " has left the server!");
                    clients.remove(this);
                    break;
                }
            }
        }//end of run


    }//end of client thread
}
