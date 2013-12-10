(ns cloj-server.game-responder
  (:import [org.andrewzures.javaserver PostParser]
           [java.io ByteArrayInputStream]))

(defn adjust-player-string [string]
  (if (= "X" string) "player1" "player2"))

(defn run-game-loop [post-map game]
  (.runGameLoop game
    (adjust-player-string (.get post-map "player"))
    (read-string (.get post-map "move"))))

(defn read-in-form-data [request]
  (.getFormBody (new PostParser) request))

(defn get-post-variables [post-variable-string]
  (.parsePostHash (new PostParser) post-variable-string))

(defn set-response-body [response body-string]
  (.setInputStream response (new ByteArrayInputStream (.getBytes body-string))))

(defn build-success-response [response]
  (.setMethod response "POST")
  (.setStatusCode response "200")
  (.setStatusText response "OK")
  (.setContentType response "text/html")
  (.setHttpType response "HTTP/1.1")
  (.setHttpType response "HTTP/1.1")
  response)