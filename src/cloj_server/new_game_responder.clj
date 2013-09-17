(ns cloj-server.new-game-responder
  (:import [org.andrewzures.javaserver.responders ResponderInterface]
           [org.andrewzures.javaserver.response Response]
           [org.jruby Ruby]
           [java.io ByteArrayInputStream])
  (:require [cloj-server.game-responder :refer :all ]
            [cloj-server.game-string-builder :refer :all ])
  )

(defn ttt-factory [] (.evalScriptlet (Ruby/newInstance) "require 'jfactory'; JFactory.new"))

(defn add-game-to-hash [game-map game]
  (.put game-map (.getID game) game))

(defn run-first-game-loop [game]
  (.runGameLoop game "player1" -1))

(defn get-new-game [post-map factory]
  (.getGame factory
    (str (gensym))
    (.getBoard factory (.get post-map "game_type"))
    (.getPlayer factory (.get post-map "player1") "X")
    (.getPlayer factory (.get post-map "player2") "O")))

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