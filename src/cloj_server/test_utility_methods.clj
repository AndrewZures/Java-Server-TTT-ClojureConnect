(ns cloj-server.test-utility-methods
  (:import [org.andrewzures.javaserver InputReader]
           [org.andrewzures.javaserver.request Request]
           [org.andrewzures.javaserver.test.socket_test MockSocket]
           [java.util HashMap]
           [org.jruby Ruby]
           [java.lang.String]
           [tttmiddleware.interfaces Game])
  (:require [cloj-server.game-responder :refer :all ]))

(defn test-ttt-factory [] (.evalScriptlet (Ruby/newInstance) "require 'jfactory'; JFactory.new"))

(defn test-get-new-game [post-map factory]
  (.getGame factory
    (str (gensym))
    (.getBoard factory (.get post-map "game_type"))
;    (.getPlayer factory (.get post-map "player1") "X")
;    (.getPlayer factory (.get post-map "player2") "O")))
    (.getPlayer factory "human" "X" "O" "player1")
    (.getPlayer factory "human" "O" "X" "player2")))

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

(defn get-4x4-test-game-request []
  (let [request (new Request)
        socket (new MockSocket)
        reader (new InputReader socket)]
    (.setInputStream socket "player1=human&player2=human&game_type=four_by_four")
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
        factory (test-ttt-factory)
        request (get-test-game-request)
        post-map (get-post-variable-hash (read-in-form-data request))
        game (test-get-new-game post-map factory)]
    game))

(defn get-4x4-test-game []
  (let [game-map (new HashMap {String Game})
        factory (test-ttt-factory)
        request (get-4x4-test-game-request)
        post-map (get-post-variable-hash (read-in-form-data request))
        game (test-get-new-game post-map factory)]
    game))
