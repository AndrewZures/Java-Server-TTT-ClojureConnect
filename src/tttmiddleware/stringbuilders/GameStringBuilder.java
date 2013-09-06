package tttmiddleware.stringbuilders;


import tttmiddleware.interfaces.Board;
import tttmiddleware.interfaces.Game;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

public class GameStringBuilder {

    public InputStream updateGame(Game game) {
        String stringResult = this.buildHTML(game);
        return new ByteArrayInputStream(stringResult.getBytes());
    }

    public String getGameOverString() {

        String gameOverString = "";
        gameOverString += "<br>";
        gameOverString += "<a href=\"new_game\">New</a>";
        return gameOverString;
    }

    public String buildHTML(Game game) {
        Board board = game.getBoard();

        String responseString = "<html><body>";
        responseString += "<form action =\"move\" method= \"post\">";
        responseString += "<input type=\"hidden\" name=\"player\" value=\"" + game.getCurrentPlayer().getSymbol() + "\"/>";
        responseString += "<input type=\"hidden\" name=\"board_id\" value=\"" + game.getID() + "\" />";

        int count = 0;
        for (int i = 0; i < board.getBoardArray().length; i++) {
            if (board.getBoardArray()[i].equalsIgnoreCase("open")) {
                responseString += "<input type=\"submit\" name=\"move\" value=\"" + i + "\" />";
            } else {
                responseString += board.getBoardArray()[i];
            }
            if (count == board.getRowLength() - 1) {
                responseString += "<br />";
                count = 0;
            } else {
                count++;
            }
        }
        responseString += "</form>";

        if (game.getBoard().isGameOver()) {
            responseString += getGameOverString();
        }

        responseString += "</body></html>";
        return responseString;
    }
}
