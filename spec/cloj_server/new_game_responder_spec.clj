(ns cloj-server.new-game-responder-spec
  (:import [tttmiddleware.interfaces Game])
  (:require [speclj.core :refer :all ]
            [cloj-server.new-game-responder :refer :all ]
            [cloj-server.game-responder :refer :all]
            [cloj-server.test-utility-methods :refer :all ]))

(describe "new-game-responder"

  (it "creates a new game"
    (let [factory (ttt-factory)
          request (get-test-game-request)
          post-map (get-post-variables (read-in-form-data request))
          game (get-new-game post-map factory)]
      (should= 3 (.size post-map))
      (should= "human" (.getType (.getPlayer1 game)))
      (should= "human" (.getType (.getPlayer2 game)))
      (should= 3 (.getRowLength (.getBoard game)))))

  (it "adds games to atom"
    (let [game-atom (atom {})
          test-game (get-test-game)
          second-game (get-test-game)]
      (add-game-to-atom game-atom test-game)
      (should= 1 (count (deref game-atom)))
      (add-game-to-atom game-atom second-game)
      (should= 2 (count (deref game-atom)))
      )))