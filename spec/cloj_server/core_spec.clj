(ns cloj-server.core-test
  (:import [org.andrewzures.javaserver.responders DefaultInternalResponder ResponderInterface]
           [org.andrewzures.javaserver.server_and_sockets MyServerSocket]
           [org.andrewzures.javaserver Logger InputReader PostParser]
           [tttmiddleware.stringbuilders GameStringBuilder]
           [org.andrewzures.javaserver.request Request]
           [org.andrewzures.javaserver.test.socket_test MockSocket]
           [java.util HashMap]
           [java.lang.String]
           [tttmiddleware.interfaces Game]
           [org.andrewzures.javaserver.response Response])
  (:import [org.andrewzures.javaserver.server_and_sockets Server])

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



