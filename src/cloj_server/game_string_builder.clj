(ns cloj-server.game-string-builder
  (:import [org.andrewzures.javaserver PostParser]
           [org.andrewzures.javaserver.responders ResponderInterface]
           [org.andrewzures.javaserver.response Response]
           [org.jruby Ruby]
           [java.io ByteArrayInputStream]))

(defn get-board-array [game]
  (let [board (.getBoardArray (.getBoard game))]
    (for [x (range 0 (alength board))]
      (if (= "open" (aget board x))
        (str (format "<input type=\"image\" src=\"blank_icon.png\" name=\"move\" value=\"%s\" />" x))
        (if (= "X" (aget board x))
          (str (format "<input type=\"image\" src=\"x_icon.png\" name=\"move\" value=\"%s\" />" x))
          (str (format "<input type=\"image\" src=\"o_icon.png\" name=\"move\" value=\"%s\" />" x)))
        ))))

(defn add-game-over [game]
  (if (= true (.isGameOver (.getBoard game)))
    (clojure.string/join
      [(if (= "tie" (.checkBoardStatus (.getBoard game)))
        "Game has ended in a Tie"
        (format "<br>Winner is %s" (.checkBoardStatus (.getBoard game))))
       "<br><a href=\"\\\">New Game</a>" ""])
    "<br><a href=\"\\\">Back To Game Menu</a>"
    ))


(defn add-breaks [game-string row-length]
  (let [new-list (partition row-length game-string)]
    (apply concat (map #(concat % '("<br />")) new-list))))

(defn build-game [game]
  (let [size (.getRowLength (.getBoard game))]
    (clojure.string/join
      ["<html><body><form action =\"move\" method= \"post\">"
       (format "<input type=\"hidden\" name=\"player\" value=\"%s\"/>"
         (.getSymbol (.getCurrentPlayer game)))
       (format "<input type=\"hidden\" name=\"board_id\" value=\"%s\" />"
         (.getID game))
       (apply str (add-breaks (get-board-array game) size))
       "</form>"
       (add-game-over game)
       "</body></html>"
       ])))