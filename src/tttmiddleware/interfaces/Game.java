package tttmiddleware.interfaces;

public interface Game {
    public boolean runGameLoop(String player, int move);
    public int getID();
    public  Player getPlayer1();
    public Player getPlayer2();
    public Player getCurrentPlayer();
    public Board getBoard();

}
