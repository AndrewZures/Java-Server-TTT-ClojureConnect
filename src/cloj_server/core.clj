(ns cloj-server.core
  (:import [org.andrewzures.javaserver.server_and_sockets Server MyServerSocket]
           [org.andrewzures.javaserver.responders DefaultInternalResponder ResponderInterface]
           [org.andrewzures.javaserver Logger PostParser ArgumentParser]
           [tttmiddleware.stringbuilders GameStringBuilder]
           [tttmiddleware.gameresponders NewGameResponder MoveResponder]
           [java.util HashMap]
           [java.lang.String])
  (:require [cloj-server.new-game-responder :refer :all])
  )


;(defn add-breaks-to-game-string [game game-string]
;  (let [row-length (.getRowLength (.getBoard game))
;        new-list (partition row-length game-string)]
;;    (apply #(conj % "<br />") new-list)
;    (#(conj % "<br />") new-list)
;    ))

(defn -main [& args]
  (let [parser (new ArgumentParser (into-array String args))
        server (new Server (.getPort parser) (.getPath parser) (new MyServerSocket) (new Logger))
        map (new HashMap {String ResponderInterface})
        ]

    (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html"))
    (.addRoute server "get" "/new_game" (DefaultInternalResponder. "introduction.html"))
    (.addRoute server "post" "/new_game" (new-game-handler map))
    (.addRoute server "post" "/move" (move-handler map))
    (.go server))
  )


;BELOW works but uses Java version of new responders
;(defn -main [& args]
;  (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))
;        factory (ttt-factory)
;        map (new HashMap {String ResponderInterface})
;        string-builder (new GameStringBuilder)
;        parser (new PostParser)
;        ]
;    (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html"))
;    (.addRoute server "get" "/new_game" (DefaultInternalResponder. "introduction.html"))
;    (.addRoute server "post" "/new_game" (new NewGameResponder map parser factory string-builder))
;    (.addRoute server "post" "/move" (new MoveResponder map parser string-builder))
;    (.go server))
;
;  )