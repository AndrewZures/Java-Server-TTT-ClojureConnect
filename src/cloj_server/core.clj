(ns cloj-server.core
  (:import [org.andrewzures.javaserver.response Response]
           [org.andrewzures.javaserver.server_and_sockets MyServerSocket]
           [org.andrewzures.javaserver.responders DefaultInternalResponder ResponderInterface]
           [tttmiddleware.stringbuilders GameStringBuilder]
           [org.andrewzures.javaserver PostParser]
           [tttmiddleware.gameresponders NewGameResponder MoveResponder]
           [java.util HashMap]
           [org.andrewzures.javaserver.request Request]
           [java.io ByteArrayInputStream])
  (:import [org.andrewzures.javaserver Logger])
  (:import [org.andrewzures.javaserver.server_and_sockets Server])
  (:import [org.jruby Ruby])
  (:import [Response])
  (:import [java.lang.String])
  )

(defn ttt-factory [] (.evalScriptlet (Ruby/newInstance) "require 'jfactory'; JFactory.new"))


(defn read-in-form-data [request]
  (.getFormBody (new PostParser) request)
  )

(defn get-post-variable-hash [post-variable-string]
  (.parsePostHash (new PostParser) post-variable-string)
  )

(defn set-response-body [response body-string]
  (.setInputStream response (new ByteArrayInputStream (.getBytes body-string)))

  )

(defn get-new-game [post-map factory]
  (.getGame factory
    1
    (.getBoard factory (.get post-map "game_type"))
    (.getPlayer factory (.get post-map "player1") "X")
    (.getPlayer factory (.get post-map "player2") "O")))





(defn build-success-response [response]
  (.setMethod response "POST")
  (.setStatusCode response "200")
  (.setStatusText response "OK")
  (.setContentType response "text/html")
  (.setHttpType response "HTTP/1.1")
  response
  )

(defn new-game-handler [map]
  (reify
    ResponderInterface
    (respond [this request]
      (let [response (new Response)
            post-map (get-post-variable-hash (read-in-form-data request))
            factory (ttt-factory)
            ]
        (set-response-body response "hello world")
        (build-success-response response)

        response))
    ))


(defn -main [& args]
  (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))
        factory (ttt-factory)
        map (new HashMap {String ResponderInterface})
        string-builder (new GameStringBuilder)
        parser (new PostParser)
       ]

    (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html"))

    (.addRoute server "get" "/new_game" (DefaultInternalResponder. "introduction.html"))
    ;(.addRoute server "post" "/new_game" (new NewGameResponder map parser factory string-builder))
    (.addRoute server "post" "/new_game" (new-game-handler map))
    (.addRoute server "post" "/move" (new MoveResponder map parser string-builder))
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




