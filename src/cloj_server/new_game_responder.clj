(ns cloj-server.new-game-responder
  (:import [org.andrewzures.javaserver PostParser]
           [org.andrewzures.javaserver.responders ResponderInterface]
           [org.andrewzures.javaserver.response Response]
           [org.jruby Ruby]
           [java.io ByteArrayInputStream]))

(defn ttt-factory [] (.evalScriptlet (Ruby/newInstance) "require 'jfactory'; JFactory.new"))

(defn get-post-variable-hash [post-variable-string]
  (.parsePostHash (new PostParser) post-variable-string))

(defn adjust-player-string [string]
  (if (= "X" string) "player1" "player2"))

(defn read-in-form-data [request]
  (.getFormBody (new PostParser) request))

(defn set-response-body [response body-string]
  (.setInputStream response (new ByteArrayInputStream (.getBytes body-string))))

(defn add-game-to-hash [game-map game]
  (.put game-map (Integer/toString (.getID game)) game))

(defn run-game-loop [post-map game]
  (.runGameLoop game
    (adjust-player-string (.get post-map "player"))
    (read-string (.get post-map "move"))))

(defn run-first-game-loop [game]
  (.runGameLoop game "player1" -1))

(defn get-board-array-string [game]
  (let [board (.getBoardArray (.getBoard game))]
    (for [x (range 0 (alength board))]
      (if (= "open" (aget board x))
        (str (format "<input type=\"submit\" name=\"move\" value=\"%s\" />" x))
        (str (aget board x))))))

(defn build-success-response [response]
  (.setMethod response "POST")
  (.setStatusCode response "200")
  (.setStatusText response "OK")
  (.setContentType response "text/html")
  (.setHttpType response "HTTP/1.1")
  (.setHttpType response "HTTP/1.1")
  response)

(defn get-game-from-hash [game-map post-map]
  (.get game-map (.get post-map "board_id")))

(defn record-player-choice [post-map game]
  (.recordChoice (.getBoard game)
    (read-string (.get post-map "move"))
    (.get post-map "player")))

(defn add-game-over-string [game]
  (if (= true (.isGameOver (.getBoard game)))
    (clojure.string/join
      [(format "<br>Winner is %s" (.checkBoardStatus (.getBoard game)))
       "<br><a href=\"new_game\">New Game</a>" ""])))

(defn get-new-game [post-map factory]
  (.getGame factory
    1
    (.getBoard factory (.get post-map "game_type"))
    (.getPlayer factory (.get post-map "player1") "X")
    (.getPlayer factory (.get post-map "player2") "O")))

(defn build-game-string [game]
  (let [size (.getRowLength (.getBoard game))]
    (clojure.string/join
      ["<html><body><form action =\"move\" method= \"post\">"
       (format "<input type=\"hidden\" name=\"player\" value=\"%s\"/>"
         (.getSymbol (.getCurrentPlayer game)))
       (format "<input type=\"hidden\" name=\"board_id\" value=\"%s\" />"
         (.getID game))
       (apply str (get-board-array-string game))
       ;       (apply str (add-breaks-to-game-string game (get-board-array-string game)))
       "</form>"
       (add-game-over-string game)
       "</body></html>"
       ])))

(defn new-game-handler [map]
  (reify
    ResponderInterface
    (respond [this request]
      (let [response (new Response)
            post-map (get-post-variable-hash (read-in-form-data request))
            factory (ttt-factory)
            game (get-new-game post-map factory)
            ]
        (add-game-to-hash map game)
        (run-first-game-loop game)
        (set-response-body response (build-game-string game))
        (build-success-response response)

        response))))

(defn move-handler [map]
  (reify
    ResponderInterface
    (respond [this request]
      (let [response (new Response)
            post-map (get-post-variable-hash (read-in-form-data request))
            game (get-game-from-hash map post-map)
            ]
        (run-game-loop post-map game)
        (set-response-body response (build-game-string game))
        (build-success-response response)
        response))))