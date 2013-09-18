(ns cloj-server.move-responder-spec
  (:import [java.util HashMap])
  (:require [speclj.core :refer :all ]
            [cloj-server.move-responder :refer :all ]
            [cloj-server.test-utility-methods :refer :all ]
            [cloj-server.game-string-builder :refer :all ]))

(defn adjust-player-string [string]
  (if (= "X" string) "player1" "player2"))

(describe "move-responder"

  (it "places move on board"
    (let [post-map (get-valid-move-hash)
          game (get-test-game)]

      (should= true (.runGameLoop game
                      (adjust-player-string (.get post-map "player"))
                      (read-string
                        (.get post-map "move"))
                      ))
      (should= "X" (aget (.getBoardArray (.getBoard game)) 1))))

  (it "gets game from game-atom"
    (let [game-atom (atom {:test-key "game proxy"})
          post-map (new HashMap {String String})]
      (.put post-map "board_id" "test-key")
      (should= "game proxy" (get-game-from-game-atom game-atom post-map))
      )))

