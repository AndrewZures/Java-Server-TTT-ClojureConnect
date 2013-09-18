(ns cloj-server.test-utility-methods
  (:import [org.andrewzures.javaserver.request Request]
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
    (.getPlayer factory "human" "X" "O" "player1")
    (.getPlayer factory "human" "O" "X" "player2")))

(defn get-test-post-request []
  (let [request (new Request)
        socket (new MockSocket)
        test-content "name=andrew&day=wednesday"]
    (.setInputStream socket test-content)
    (.setSocket request socket)
    (.setContentLength request (count test-content))
    request))

(defn get-test-game-request []
  (let [request (new Request)
        socket (new MockSocket)
        test-content "player1=human&player2=human&game_type=three_by_three"]
    (.setInputStream socket test-content)
    (.setSocket request socket)
    (.setContentLength request (count test-content))
    request))

(defn get-4x4-test-game-request []
  (let [request (new Request)
        socket (new MockSocket)
        test-content "player1=human&player2=human&game_type=four_by_four"]
    (.setInputStream socket test-content)
    (.setSocket request socket)
    (.setContentLength request (count test-content))
    request))

(defn get-test-move-request []
  (let [request (new Request)
        socket (new MockSocket)]
    (.setInputStream socket "move=1&player=X&board_id=1")
    (.setSocket request socket)
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
