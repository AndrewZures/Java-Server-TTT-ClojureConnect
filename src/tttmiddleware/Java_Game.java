package tttmiddleware;

import tttmiddleware.interfaces.Board;
import tttmiddleware.interfaces.Game;
import tttmiddleware.interfaces.Player;

import java.util.Arrays;

public class Java_Game implements Game {
    Board board;
    Player player1, player2, currentPlayer;
    int ID = -1;

    public Java_Game(int ID, Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.ID = ID;
        this.currentPlayer = player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void toggleCurrentPlayer() {
        if (currentPlayer == player1)
            currentPlayer = player2;
        else currentPlayer = player1;
    }

    public boolean isCorrectlySetUp() {
        if (board != null
                && player1 != null
                && player2 != null
                && ID != -1) return true;
        else return false;
    }

    public boolean runGameLoop(String player, int move) {
        Board board = this.getBoard();
        System.out.println("currentplayer = " + this.getCurrentPlayer().getSymbol() + " - " + this.getCurrentPlayer().getType());
        if (player.equalsIgnoreCase(this.getCurrentPlayer().getSymbol())) {
            if (this.getCurrentPlayer().getType().equalsIgnoreCase("human")) {
                boolean result = board.recordChoice(move, this.getCurrentPlayer().getSymbol());
                if (!result) return false;
                this.toggleCurrentPlayer();
            }
            this.makeAIMoves();
        }
        System.out.println("board = " + Arrays.toString(board.getBoardArray()));
        return true;
    }

    public void makeAIMoves() {
        while (this.getCurrentPlayer().getType() != "human") {
            if (this.getBoard().isGameOver()) {
                break;
            }
            int move = this.getCurrentPlayer().makeMove(this.getBoard());
            System.out.println("AI turn = " + this.getCurrentPlayer().getType() + " move: " + move);
            this.getBoard().recordChoice(move, this.getCurrentPlayer().getSymbol());
            this.toggleCurrentPlayer();
        }
    }

    public int getID() {
        return ID;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

}
