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
            [cloj-server.core :refer :all ]))


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
    (.setInputStream socket "player1=unbeatable_ai&player2=human&game_type=three_by_three")
    (.setInputReader request reader)
    request))

(describe "main"

  (it "adds route"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (should= true (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))))

  (it "does not add route when route already taken"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (should= true (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))
      (should= false (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html")))))

  (it "correctly addes servers"
    (let [server (new Server 8192 "." (new MyServerSocket) (new Logger))]
      (.addRoute server "get" "/hello" (DefaultInternalResponder. "welcome.html"))
      (.addRoute server "get" "/new_game" (DefaultInternalResponder. "introduction.html"))
      (should= 2 (.size (.getRoutes server))))
    ))


(describe "get-new-game"

  (it "returns a new game"
    (let [game-map (new HashMap {String Game})
          factory (ttt-factory)
          request (get-test-game-request)
          post-map (get-post-variable-hash (read-in-form-data request))
          game (get-new-game post-map factory)]
      (should= 3 (.size post-map))
      (should= "ai" (.getType (.getPlayer1 game)))
      (should= "human" (.getType (.getPlayer2 game)))
      (should= 3 (.getRowLength (.getBoard game)))
      )
    )
  )

(describe "set-response-body"
  (it "should set body"
    (let [response (new Response)]
      (set-response-body response "hello world")
      ;104 = "h" but trouble converting from string,int,char
      (should= (char 104) (char (.read (.getInputStream response))))
      (should= (char 101) (char (.read (.getInputStream response))))
      (should= (char 108) (char (.read (.getInputStream response))))
      (should= (char 108) (char (.read (.getInputStream response))))
      (should= (char 111) (char (.read (.getInputStream response))))
      )))

(describe "new-game-handler"
  (it "should handle request"
    (let [request (get-test-post-request)
          game-map (new HashMap {String Game})]
      (should= "POST" (.getMethod (.respond (new-game-handler game-map) request)))))
)

(describe "create-new-game"

  (it "should get post variables from request"
    (let [request (get-test-post-request)]
      (should= "name=andrew&day=wednesday" (read-in-form-data request))
      ))

  (it "should parse post string"
    (let [hash-result (get-post-variable-hash "name=andrew&day=wednesday")]
      (should= "andrew" (.get hash-result "name")))
    )

  (it "should get hashmap of post variables"
    (let [request (new Request)
          socket (new MockSocket)
          reader (new InputReader socket)]
      (.setInputStream socket "name=andrew&day=wednesday")
      (.setInputReader request reader)

      )))

(describe "build-success-response"

  (it "should build basic header"
    (let [response (build-success-response (new Response))]
      (should= "POST" (.getMethod response))
      (should= "200" (.getStatusCode response))
      (should= "OK" (.getStatusText response))
      (should= "text/html" (.getContentType response))
      (should= "HTTP/1.1" (.getHttpType response))
      )))

