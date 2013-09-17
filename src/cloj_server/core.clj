(ns cloj-server.core
  (:import [org.andrewzures.javaserver.server_and_sockets Server MyServerSocket]
           [org.andrewzures.javaserver.responders DefaultInternalResponder ResponderInterface]
           [org.andrewzures.javaserver Logger PostParser ArgumentParser]
           [java.util HashMap]
           [java.lang.String])
  (:require [cloj-server.new-game-responder :refer :all]
            [cloj-server.move-responder :refer :all]
            [cloj-server.game-string-builder :refer :all]))

(defn -main [& args]
  (let [parser (new ArgumentParser (into-array String args))
        server (new Server (.getPort parser) (.getPath parser) (new MyServerSocket) (new Logger))
        map (new HashMap {String ResponderInterface})]

    (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html"))
    (.addRoute server "get" "/new_game" (DefaultInternalResponder. "introduction.html"))
    (.addRoute server "get" "/x_icon.png" (DefaultInternalResponder. "x_icon.png"))
    (.addRoute server "get" "/o_icon.png" (DefaultInternalResponder. "o_icon.png"))
    (.addRoute server "get" "/blank_icon.png" (DefaultInternalResponder. "blank_icon.png"))
    (.addRoute server "post" "/new_game" (new-game-handler map))
    (.addRoute server "post" "/move" (move-handler map))
    (.go server))
  )