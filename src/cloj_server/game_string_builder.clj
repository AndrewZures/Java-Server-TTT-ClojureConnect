(ns cloj-server.game-string-builder
  (:import [org.andrewzures.javaserver PostParser]
           [org.andrewzures.javaserver.responders ResponderInterface]
           [org.andrewzures.javaserver.response Response]
           [org.jruby Ruby]
           [java.io ByteArrayInputStream]))

(defn get-board-array-string [game]
  (let [board (.getBoardArray (.getBoard game))]
    (for [x (range 0 (alength board))]
      (if (= "open" (aget board x))
        (str (format "<input type=\"submit\" name=\"move\" value=\"%s\" />" x))
        (str (aget board x))))))

(defn add-game-over-string [game]
  (if (= true (.isGameOver (.getBoard game)))
    (clojure.string/join
      [(format "<br>Winner is %s" (.checkBoardStatus (.getBoard game)))
       "<br><a href=\"new_game\">New Game</a>" ""])))

(defn add-breaks-to-game-string [game game-string]
  (let [row-length (.getRowLength (.getBoard game))
        new-list (partition row-length game-string)]
    (apply concat (map #(concat % '("<br />")) new-list))))

(defn build-game-string [game]
  (let [size (.getRowLength (.getBoard game))]
    (clojure.string/join
      ["<html><body><form action =\"move\" method= \"post\">"
       (format "<input type=\"hidden\" name=\"player\" value=\"%s\"/>"
         (.getSymbol (.getCurrentPlayer game)))
       (format "<input type=\"hidden\" name=\"board_id\" value=\"%s\" />"
         (.getID game))
       (apply str (add-breaks-to-game-string game (get-board-array-string game)))
       "</form>"
       (add-game-over-string game)
       "</body></html>"
       ])))