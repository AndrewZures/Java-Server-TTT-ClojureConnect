(ns cloj-server.move-responder
  (:import [org.andrewzures.javaserver.responders ResponderInterface]
           [org.andrewzures.javaserver.response Response])
  (:require [cloj-server.game-responder :refer :all ]
            [cloj-server.game-string-builder :refer :all ]))

(defn get-game-from-hash [game-map post-map]
  (.get game-map (.get post-map "board_id")))

(defn get-game-from-game-atom [game-atom post-map]
  (let [key (keyword (.get post-map "board_id"))]
    (get (deref game-atom) key)))

(defn record-player-choice [post-map game]
  (.recordChoice (.getBoard game)
    (read-string (.get post-map "move"))
    (.get post-map "player")))

(defn move-handler [game-atom]
  (reify
    ResponderInterface
    (respond [this request]
      (let [response (new Response)
            post-map (get-post-variable-hash (read-in-form-data request))
            game (get-game-from-game-atom game-atom post-map)
;            game (get-game-from-hash map post-map)
            ]
        (run-game-loop post-map game)
        (set-response-body response (build-game-string game))
        (build-success-response response)
        response))))