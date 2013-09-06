package tttmiddleware.gameresponders;
import org.andrewzures.javaserver.PostParser;

import org.andrewzures.javaserver.request.Request;
import org.andrewzures.javaserver.responders.ResponderInterface;
import org.andrewzures.javaserver.response.Response;

import org.andrewzures.javaserver.responders.ResponderInterface;
import tttmiddleware.interfaces.Game;
import tttmiddleware.interfaces.TTTFactory;
import tttmiddleware.stringbuilders.GameStringBuilder;

import java.util.HashMap;

public class NewGameResponder implements ResponderInterface {
    PostParser parser;
    TTTFactory factory;
    GameStringBuilder gameStringBuilder;
    HashMap<Integer, Game> gameMap;
    String gameType, player1, player2;
    int count = 0;

    public NewGameResponder(HashMap<Integer,
            Game> gameMap,
                            PostParser parser,
                            TTTFactory factory,
                            GameStringBuilder gameStringBuilder) {
        this.gameMap = gameMap;
        this.parser = parser;
        this.factory = factory;
        this.gameStringBuilder = gameStringBuilder;
    }

    public Response respond(Request request) {
        Game game = this.createNewGame(request);
        if (game == null) return null;
        game.runGameLoop("player1", -1);

        Response response = new Response();

        response.inputStream = gameStringBuilder.updateGame(game);
        if (response.inputStream == null) return null;
        response = this.populateHeader(response);
        return response;
    }

    public Response populateHeader(Response response) {
        response.method = "POST";
        response.path = "/move";
        response.statusCode = "200";
        response.statusText = "OK";
        response.httpType = "HTTP/1.1";
        response.contentType = "text/html";
        return response;
    }

    private Game createNewGame(Request request) {
        String variables = parser.getFormBody(request);
        System.out.println("game variables = " + variables);
        HashMap<String, String> postMap = parser.parsePostHash(variables);
        if (postMap.containsKey("player1")
                && postMap.containsKey("player2")
                && postMap.containsKey("game_type")) {
            player1 = postMap.get("player1");
            player2 = postMap.get("player2");
            gameType = postMap.get("game_type");
        }
        int id = getID();

        Game game = factory.getGame(id,
                factory.getBoard(gameType),
                factory.getPlayer(player1, "X"),
                factory.getPlayer(player2, "O"));

        this.printGame(game);
        gameMap.put(id, game);
        return game;
    }

    public void printGame(Game game) {
        System.out.println("id = " + game.getID());
        System.out.println("gametype >>" + gameType + "<<");
        System.out.println("player1 = "+ game.getPlayer1().getType());
        System.out.println("player2 = "+ game.getPlayer2().getType());
        System.out.println("board = " + game.getBoard().getBoardArray().length);
    }


    private int getID() {
        int newId = count;
        count++;
        return newId;
    }
}
