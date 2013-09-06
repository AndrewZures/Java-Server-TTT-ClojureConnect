package tttmiddleware.interfaces;

public interface Board {
    boolean isGameOver();
    boolean recordChoice(int move, String mark);
    int getRowLength();
    String[] getBoardArray();
    String checkBoardStatus();
}
