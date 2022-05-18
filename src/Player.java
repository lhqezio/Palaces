import java.util.ArrayList;
import java.util.Collections;

public class Player {

    private final ArrayList<Integer> players;
    private final boolean[] isHuman;

    Player(int playerNum, int botNum) {
        players = new ArrayList<>();
        for (int i = 0; i < (playerNum + botNum); i++) {
            players.add(i);
        }
        isHuman = new boolean[playerNum + botNum];
        for (int i = 0; i < isHuman.length; i++) {
            isHuman[i] = i < playerNum;
        }
    }

    public boolean isHuman(int turn) {
        return isHuman[players.get(turn)];
    }

    public String whoGoFirst() {
        Collections.shuffle(players);
        StringBuilder string = new StringBuilder();
        for (Integer player : players) {
            string.append("Player ").append(player + 1).append("   ");
        }
        return string.toString();
    }

    public int currentPlayer(int turn) {
        return players.get(turn);
    }
}
