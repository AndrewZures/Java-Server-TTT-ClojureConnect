package tttmiddleware.gameresponders;

import org.andrewzures.javaserver.PostParser;
import org.andrewzures.javaserver.request.Request;
import org.andrewzures.javaserver.responders.ResponderInterface;
import org.andrewzures.javaserver.response.Response;
import tttmiddleware.interfaces.Game;
import tttmiddleware.stringbuilders.GameStringBuilder;

import java.util.HashMap;

public class MoveResponder implements ResponderInterface {
    PostParser parser;
    HashMap<Integer, Game> gameMap;
    GameStringBuilder gameStringBuilder;

    public MoveResponder(HashMap<Integer, Game> gameMap, PostParser parser, GameStringBuilder gameStringBuilder) {
        this.gameMap = gameMap;
        this.parser = parser;
        this.gameStringBuilder = gameStringBuilder;
    }

    public Response respond(Request request) {
        String variables = parser.getFormBody(request);
        System.out.println("move responder variables = " + variables);
        HashMap<String, String> postMap = parser.parsePostHash(variables);
        String[] variableList = {"move", "player", "board_id"};
        if (!this.hasVariables(postMap, variableList)) {
            System.out.println("no variables");
            return null;
        }
        Game game = gameMap.get(Integer.parseInt(postMap.get("board_id")));
        if (game == null) return null;
        System.out.println(postMap.get("player")+" "+postMap.get("move"));

        String player = "";
        if(postMap.get("player").equalsIgnoreCase("X")) player = "player1";
        else player = "player2";

        game.runGameLoop(player, Integer.parseInt(postMap.get("move")));
        System.out.println(game.getBoard().getBoardArray().length);
        for(int i =0; i < game.getBoard().getBoardArray().length; i++){
            System.out.print(game.getBoard().getBoardArray()[i] + " ");
        }

        Response response = new Response();
        response.inputStream = gameStringBuilder.updateGame(game);
        if (response.inputStream == null) return null;
        response = this.populateHeader(response);
        return response;
    }

    public Response populateHeader(Response response) {
        response.method = "GET";
        response.path = "/move";
        response.statusCode = "200";
        response.statusText = "OK";
        response.httpType = "HTTP/1.1";
        response.contentType = "text/html";
        return response;
    }

    public boolean hasVariables(HashMap<String, String> postMap, String[] variableList) {
        for (int i = 0; i < variableList.length; i++) {
            if (!postMap.containsKey(variableList[i])) {
                return false;
            }
        }
        return true;
    }
}
