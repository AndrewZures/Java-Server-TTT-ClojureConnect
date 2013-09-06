package tttmiddleware;

import org.andrewzures.javaserver.Logger;
import org.andrewzures.javaserver.PostParser;
import org.andrewzures.javaserver.responders.DefaultInternalResponder;
import org.andrewzures.javaserver.server_and_sockets.MyServerSocket;
import org.andrewzures.javaserver.server_and_sockets.Server;
import org.andrewzures.javaserver.server_and_sockets.ServerSocketInterface;

import org.jruby.Ruby;
import tttmiddleware.gameresponders.MoveResponder;
import tttmiddleware.gameresponders.NewGameResponder;
import tttmiddleware.interfaces.Game;
import tttmiddleware.interfaces.TTTFactory;
import tttmiddleware.stringbuilders.GameStringBuilder;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.HashMap;

public class Driver {

    public static void main(String[] args) throws IOException, ScriptException {
        int port = 8189;
        TTTFactory factory = (TTTFactory) Ruby.newInstance().evalScriptlet("require 'jfactory'; JFactory.new");
        String startingPath = ".";
        ServerSocketInterface myServerSocket = new MyServerSocket();
        Logger logger = new Logger();
        Server server = new Server(
                port,
                startingPath,
                myServerSocket,
                logger
        );
        PostParser parser = new PostParser();
        HashMap<Integer, Game> gameMap = new HashMap<Integer, Game>();
        GameStringBuilder gameStringBuilder = new GameStringBuilder();
        MoveResponder moveResponder = new MoveResponder(gameMap, parser, gameStringBuilder);
        DefaultInternalResponder introResponder = new DefaultInternalResponder("introduction.html");
        NewGameResponder newGameResponder = new NewGameResponder(gameMap, parser, factory, gameStringBuilder);

        server.addRoute("POST", "/new_game", newGameResponder);
        server.addRoute("GET", "/new_game", introResponder);
        server.addRoute("POST", "/move", moveResponder);
        server.go();
    }
}
