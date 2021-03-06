(ns cloj-server.core-spec
  (:import [org.andrewzures.javaserver.responders DefaultInternalResponder ResponderInterface]
           [org.andrewzures.javaserver.server_and_sockets MyServerSocket]
           [org.andrewzures.javaserver Logger PostParser]
           [org.andrewzures.javaserver.test.socket_test MockSocket]
           [org.andrewzures.javaserver.server_and_sockets Server])
  (:require [speclj.core :refer :all ]
            [cloj-server.core :refer :all ]
            [cloj-server.new-game-responder :refer :all ]))

(describe "main"

  (it "adds route"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (should= true (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))))

  (it "does not add route when route already taken"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (should= true (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))
      (should= false (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))))

  (it "correctly adds servers"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html"))
      (.addRoute server "get" "/new_game" (DefaultInternalResponder. "introduction.html"))
      (should= 2 (.size (.getRoutes server))))
    ))



