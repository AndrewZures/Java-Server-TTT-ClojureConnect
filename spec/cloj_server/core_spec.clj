(ns cloj-server.core-test
  (:import [org.andrewzures.javaserver.responders DefaultInternalResponder ResponderInterface]
           [org.andrewzures.javaserver.server_and_sockets MyServerSocket]
           [org.andrewzures.javaserver Logger InputReader PostParser]
           [tttmiddleware.stringbuilders GameStringBuilder]
           [org.andrewzures.javaserver.request Request]
           [org.andrewzures.javaserver.test.socket_test MockSocket]
           [java.util HashMap]
           [java.lang.String]
           [tttmiddleware.interfaces Game]
           [org.andrewzures.javaserver.response Response])
  (:import [org.andrewzures.javaserver.server_and_sockets Server])

  (:require [speclj.core :refer :all ]
            [cloj-server.core :refer :all ]
            [cloj-server.new-game-responder :refer :all]))

;helper methods

(defn get-test-post-request []
  (let [request (new Request)
        socket (new MockSocket)
        reader (new InputReader socket)]
    (.setInputStream socket "name=andrew&day=wednesday")
    (.setInputReader request reader)
    request))


(defn get-test-game-request []
  (let [request (new Request)
        socket (new MockSocket)
        reader (new InputReader socket)]
    (.setInputStream socket "player1=human&player2=human&game_type=three_by_three")
    (.setInputReader request reader)
    request))


(defn get-test-move-request []
  (let [request (new Request)
        socket (new MockSocket)
        reader (new InputReader socket)]
    (.setInputStream socket "move=1&player=X&board_id=1")
    (.setInputReader request reader)
    request))

(defn get-valid-move-hash []
  (let [post-map (new HashMap {String String})]
    (.put post-map "move" "1")
    (.put post-map "board_id" "1")
    (.put post-map "player" "X")
    post-map))

(defn get-test-game []
  (let [game-map (new HashMap {String Game})
        factory (ttt-factory)
        request (get-test-game-request)
        post-map (get-post-variable-hash (read-in-form-data request))
        game (get-new-game post-map factory)]
    game))

;end helper methods, start tests

(describe "main"

  (it "adds route"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (should= true (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))))

  (it "does not add route when route already taken"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (should= true (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))
      (should= false (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))))

  (it "correctly adds servers"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html"))
      (.addRoute server "get" "/new_game" (DefaultInternalResponder. "introduction.html"))
      (should= 2 (.size (.getRoutes server))))
    ))

(describe "clojure ResponderInterface responders"

  (it "gets post variable string from request"
    (let [request (get-test-post-request)]
      (should= "name=andrew&day=wednesday" (read-in-form-data request))
      ))

  (it "parses post string"
    (let [hash-result (get-post-variable-hash "name=andrew&day=wednesday")]
      (should= "andrew" (.get hash-result "name")))
    )

  (it "builds hashmap of post variables"
    (let [request (new Request)
          socket (new MockSocket)
          reader (new InputReader socket)]
      (.setInputStream socket "name=andrew&day=wednesday")
      (.setInputReader request reader)

      ))

  (it "builds basic header"
    (let [response (build-success-response (new Response))]
      (should= "POST" (.getMethod response))
      (should= "200" (.getStatusCode response))
      (should= "OK" (.getStatusText response))
      (should= "text/html" (.getContentType response))
      (should= "HTTP/1.1" (.getHttpType response))
      ))

  (it "sets response body"
    (let [response (new Response)]
      (set-response-body response "hello world")
      ;104 = "h" but trouble converting from string,int,char
      (should= (char 104) (char (.read (.getInputStream response))))
      (should= (char 101) (char (.read (.getInputStream response))))
      (should= (char 108) (char (.read (.getInputStream response))))
      (should= (char 108) (char (.read (.getInputStream response))))
      (should= (char 111) (char (.read (.getInputStream response))))
      )))

(describe "new-game-responder"

  (it "creates a new game"
    (let [game-map (new HashMap {String Game})
          factory (ttt-factory)
          request (get-test-game-request)
          post-map (get-post-variable-hash (read-in-form-data request))
          game (get-new-game post-map factory)]
      (should= 3 (.size post-map))
      (should= "human" (.getType (.getPlayer1 game)))
      (should= "human" (.getType (.getPlayer2 game)))
      (should= 3 (.getRowLength (.getBoard game)))
      ))

  (it "adds game to hash"
    (let [map (new HashMap {String Game})]
      (should= 1 (.size map))
      (add-game-to-hash map (get-test-game))
      (should= true (.containsKey map "1"))
      (should= 2 (.size map)

        ))))

(describe "move-handler"

  (it "places move on board"
    (let [post-map (get-valid-move-hash)
          game (get-test-game)]

      (should= true (.runGameLoop game
                      (adjust-player-string (.get post-map "player"))
                      (read-string
                        (.get post-map "move"))
                      ))
      (should= "X" (aget (.getBoardArray (.getBoard game)) 1))
      )))

(describe "game-string-builder"
  (it "has hidden player and board_id fields"
    (let [game (get-test-game)
          post-map (get-valid-move-hash)
          game-string (build-game-string game)]
      (should-contain
        "<input type=\"hidden\" name=\"board_id\" value=\"1\" />"
        game-string)
      (should-contain
        "<input type=\"hidden\" name=\"player\" value=\"X\"/>"
        game-string)

      ))

  (it "has player2 for currentplayer and updatedboard after first pl1 move"
    (let [game (get-test-game)
          post-map (get-valid-move-hash)]
      (should= true (.runGameLoop game
        (adjust-player-string (.get post-map "player"))
        (read-string
          (.get post-map "move"))
        ))
      (should-contain
        "<input type=\"hidden\" name=\"board_id\" value=\"1\" />"
        (build-game-string game))
      (should-contain
        "<input type=\"hidden\" name=\"player\" value=\"O\"/>"
        (build-game-string game))
      (should-contain
        "<input type=\"submit\" name=\"move\" value=\"0\" />"
        (build-game-string game))
      (should-contain
        ">X<"
        (build-game-string game))

      ))

  )


