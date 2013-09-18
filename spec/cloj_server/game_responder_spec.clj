(ns cloj-server.new-game-responder-spec
  (:import [org.andrewzures.javaserver.request Request]
           [org.andrewzures.javaserver.test.socket_test MockSocket]
           [org.andrewzures.javaserver Logger PostParser]
           [java.util HashMap]
           [tttmiddleware.interfaces Game]
           [org.andrewzures.javaserver.response Response])
  (:require [speclj.core :refer :all ]
            [cloj-server.game-responder :refer :all ]
            [cloj-server.test-utility-methods :refer :all]))

(describe "clojure ResponderInterface responders"

  (it "gets post variable string from request"
    (let [request (get-test-post-request)]
      (should= "name=andrew&day=wednesday" (read-in-form-data request))))

  (it "parses post string"
    (let [hash-result (get-post-variable-hash "name=andrew&day=wednesday")]
      (should= "andrew" (.get hash-result "name"))))

  (it "builds hashmap of post variables"
    (let [request (new Request)
          socket (new MockSocket)]
      (.setInputStream socket "name=andrew&day=wednesday")
      (.setSocket request socket)))

  (it "builds basic header"
    (let [response (build-success-response (new Response))]
      (should= "POST" (.getMethod response))
      (should= "200" (.getStatusCode response))
      (should= "OK" (.getStatusText response))
      (should= "text/html" (.getContentType response))
      (should= "HTTP/1.1" (.getHttpType response))))

  (it "sets response body"
    (let [response (new Response)]
      (set-response-body response "hello world")
      ;104 = "h" but trouble converting from string,int,char
      (should= (char 104) (char (.read (.getInputStream response))))
      (should= (char 101) (char (.read (.getInputStream response))))
      (should= (char 108) (char (.read (.getInputStream response))))
      (should= (char 108) (char (.read (.getInputStream response))))
      (should= (char 111) (char (.read (.getInputStream response)))))))