import java.io.IOException;
import java.io.Serializable;

public class MorraInfo implements Serializable{

    private int p1Points; //points for player 1
    private int p2Points; //points for player 2
    private String p1Plays; //player 1 fingers and guess
    private String p2Plays; //player 2 plays fingers and guess
    private int pNum; //tells which player they are
    private String playerString;
    private boolean twoPlayers;
    private boolean guessing;

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

    public void determineWinner() throws IOException {
        int win = morraGameLogic(Integer.parseInt(morraInfo.getP1Plays()), Integer.parseInt(morraInfo.getP2Plays()));
        if(win == 1){
            morraInfo.setP1Points(morraInfo.getP1Points() + 1);
            morraInfo.setPlayerString("client #1 has won this round!");
        }
        else if(win == 2){
            morraInfo.setP2Points(morraInfo.getP2Points() + 1);
            morraInfo.setPlayerString("client #2 has won this round!");
        }
        else{
            morraInfo.setPlayerString("No one won game is tied!");
        }
        updateClients();
    }

    public boolean isGuessing() {
        return guessing;
    }

    public void setGuessing(boolean guessing) {
        this.guessing = guessing;
    }

    public boolean isTwoPlayers() {
        return twoPlayers;
    }

    public void setTwoPlayers(boolean have2Players) {
        this.twoPlayers = have2Players;
    }

    public String getPlayerString() {
        return playerString;
    }

    public void setPlayerString(String playerString) {
        this.playerString = playerString;
    }

    public int getpNum() {
        return pNum;
    }

    public void setpNum(int pNum) {
        this.pNum = pNum;
    }

    public int getP1Points() {
        return p1Points;
    }

    public void setP1Points(int p1Points) {
        this.p1Points = p1Points;
    }

    public int getP2Points() {
        return p2Points;
    }

    public void setP2Points(int p2Points) {
        this.p2Points = p2Points;
    }

    public String getP1Plays() {
        return p1Plays;
    }

    public void setP1Plays(String p1Plays) {
        this.p1Plays = p1Plays;
    }

    public String getP2Plays() {
        return p2Plays;
    }

    public void setP2Plays(String p2Plays) {
        this.p2Plays = p2Plays;
    }

}
