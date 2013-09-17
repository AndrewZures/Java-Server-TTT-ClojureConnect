(ns cloj-server.new-game-responder-spec
  (:import [org.andrewzures.javaserver.test.socket_test MockSocket]
           [org.andrewzures.javaserver Logger InputReader PostParser]
           [java.util HashMap]
           [tttmiddleware.interfaces Game])
  (:require [speclj.core :refer :all ]
            [cloj-server.new-game-responder :refer :all ]
            [cloj-server.test-utility-methods :refer :all ]))

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
      (should= 3 (.getRowLength (.getBoard game)))))

  (it "adds game to hash"
    (let [map (new HashMap {String Game})
          game (get-test-game)]
      (should= 1 (.size map))
      (add-game-to-hash map game)
      (should= true (.containsKey map (.getID game)))
      (should= 2 (.size map)))))