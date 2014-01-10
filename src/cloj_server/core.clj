(ns cloj-server.core
  (:import [org.andrewzures.javaserver.server_and_sockets Server MyServerSocket]
           [org.andrewzures.javaserver.responders
            DefaultInternalResponder
            ResponderInterface
            File404Responder]
           [org.andrewzures.javaserver Logger PostParser ArgumentParser]
           [java.lang.String])
  (:require [cloj-server.new-game-responder :refer :all ]
            [cloj-server.move-responder :refer :all ]
            [cloj-server.game-string-builder :refer :all ]))

(defn -main [& args]
  (let [parser (new ArgumentParser (into-array String args))
        server (new Server (.getPort parser) (.getPath parser) (new MyServerSocket) (new Logger))
        game-atom (atom {})]

    (.add404Responder server (File404Responder.))
;    (.addRoute server "get" "/new_game" (DefaultInternalResponder. "introduction.html"))
    (.addRoute server "get" "/" (DefaultInternalResponder. "introduction.html"))
    (.addRoute server "get" "/ttt.css" (DefaultInternalResponder. "css/ttt.css"))
    (.addRoute server "get" "/x_icon.png" (DefaultInternalResponder. "images/x_icon.png"))
    (.addRoute server "get" "/o_icon.png" (DefaultInternalResponder. "images/o_icon.png"))
    (.addRoute server "get" "/blank_icon.png" (DefaultInternalResponder. "images/blank_icon.png"))
    (.addRoute server "post" "/new_game" (new-game-handler game-atom))
    (.addRoute server "post" "/move" (move-handler game-atom))
    (.go server)))