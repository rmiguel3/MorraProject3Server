import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class MorraServer{
    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
    TheServer server;
    MorraInfo morraInfo = new MorraInfo();
    MorraInfo serverInfo = new MorraInfo();
    MorraInfo p1Info = new MorraInfo();
    MorraInfo p2Info = new MorraInfo();
    private Consumer<Serializable> callback;


    MorraServer(Consumer<Serializable> call){
        callback = call;
        server = new TheServer();
        server.start();
    }


    public class TheServer extends Thread{

        public void run() {

            try(ServerSocket mysocket = new ServerSocket(5555);){
                System.out.println("Server is waiting for a client!");
                while(true) {
                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    callback.accept("client has connected to server: " + "client #" + count);
                    clients.add(c);
                    c.start();
                    if(count == 1){
                        serverInfo.setpNum(1);
                        callback.accept("It is just you my friend...");
                    }
                    else if(count == 2){
                        serverInfo.setpNum(2);
                        callback.accept("There are 2 people connected to the server. Time to play Morra!");
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

        //updates the serverInfo and morraInfo class
        public void updateServer() {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    serverInfo = (MorraInfo) t.in.readObject();
                    callback.accept("ServerInfo P1 = " + serverInfo.getP1Plays());
                    if(serverInfo.getpNum() == 1){
                        callback.accept("Player 1!");
                        morraInfo.setP1Plays(serverInfo.getP1Plays());
                    }
                    else if(serverInfo.getpNum() == 2){
                        callback.accept("Player 2!");
                        morraInfo.setP2Plays(serverInfo.getP2Plays());
                    }
                    else{
                        callback.accept("updateServer: ELSE");
                    }
                }
                catch(Exception e){}
            }
        }

        //sends and updates the clients
        public void updateClients(String message) {
            for(int i = 0; i < clients.size(); i++) {
                ClientThread t = clients.get(i);
                try {
                    t.out.writeObject(serverInfo);
                    t.out.reset();
                    t.out.flush();
                    t.out.writeObject(message);
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
                updateClients("client #"+ count + " has won this round!");
            }
            else if(win == 2){
                morraInfo.setP2Points(win);
                updateClients("client #"+ count + " has won this round!");
            }
            else{
                updateClients("No one won game is tied!");
            }
        }

        public void run(){

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            updateClients("new client on server: client #"+count);

            while(true) {
                try {
                    updateServer();
                    updateClients("client #"+ count + " has chosen a number!");
                    if(serverInfo.getpNum() == 1){
                        callback.accept("client: " + count + " chooses: " + morraInfo.getP1Plays());
                    }
                    else if(serverInfo.getpNum() == 2){
                        callback.accept("client: " + count + " chooses: " + morraInfo.getP2Plays());
                    }
                    //determineWinner(morraInfo);
                }
                catch(Exception e) {
                    callback.accept("OOOOPPs...Something wrong with the socket from client: " + count + "....closing down!");
                    updateClients("Client #" + count +" has left the server!");
                    clients.remove(this);
                    break;
                }
            }
        }//end of run


    }//end of client thread
}
