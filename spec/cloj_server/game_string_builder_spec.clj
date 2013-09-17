(ns cloj-server.new-game-responder-spec
  (:import [org.andrewzures.javaserver.request Request]
           [org.andrewzures.javaserver.test.socket_test MockSocket]
           [org.andrewzures.javaserver Logger InputReader PostParser]
           [java.util HashMap]
           [tttmiddleware.interfaces Game]
           [org.andrewzures.javaserver.response Response]
           )
  (:require [speclj.core :refer :all ]
            [cloj-server.core :refer :all ]
            [cloj-server.game-string-builder :refer :all ]
            [cloj-server.test-utility-methods :refer :all]
             ))

(describe "game-string-builder"

  (it "has hidden player and board_id fields"
    (let [game (get-test-game)
          post-map (get-valid-move-hash)
          game-string (build-game-string game)]
      (should-contain
        (format "<input type=\"hidden\" name=\"board_id\" value=\"%s\" />" (.getID game))
        game-string)
      (should-contain
        "<input type=\"hidden\" name=\"player\" value=\"X\"/>"
        game-string)))

  (it "has player2 for currentplayer and updatedboard after first pl1 move"
    (let [game (get-test-game)
          post-map (get-valid-move-hash)]
      (should= true (.runGameLoop game
                      (adjust-player-string (.get post-map "player"))
                      (read-string
                        (.get post-map "move"))
                      ))
      (should-contain
        (format "<input type=\"hidden\" name=\"board_id\" value=\"%s\" />" (.getID game))
        (build-game-string game))
      (should-contain
        "<input type=\"hidden\" name=\"player\" value=\"O\"/>"
        (build-game-string game))
      (should-contain
        "<input type=\"submit\" name=\"move\" value=\"0\" />"
        (build-game-string game))
      (should-contain
        ">X<"
        (build-game-string game))))

  (it "has <br /> points after third element for 3x3 board"
    (let [game (get-test-game)
          post-map (get-valid-move-hash)]
      (should-contain
      "<input type=\"submit\" name=\"move\" value=\"2\" /><br />"
      (build-game-string game))
      ))

  (it "has <br /> points after fourth element for 4x4 board"
    (let [game (get-test-game)
          post-map (get-valid-move-hash)]
      (should-contain
        "<input type=\"submit\" name=\"move\" value=\"2\" /><br />"
        (build-game-string game))
      )))