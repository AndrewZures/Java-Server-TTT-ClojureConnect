(ns cloj-server.new-game-responder-spec
  (:require [speclj.core :refer :all ]
            [cloj-server.move-responder :refer :all ]
            [cloj-server.test-utility-methods :refer :all ]))

(describe "move-responder"

  (it "places move on board"
    (let [post-map (get-valid-move-hash)
          game (get-test-game)]

      (should= true (.runGameLoop game
                      (adjust-player-string (.get post-map "player"))
                      (read-string
                        (.get post-map "move"))
                      ))
      (should= "X" (aget (.getBoardArray (.getBoard game)) 1)))))
