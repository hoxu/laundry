(ns laundry.digest-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [peridot.multipart]
            [ring.mock.request :as mock]

            [laundry.digest]
            [laundry.machines :as machines]
            [laundry.server :as server]))

(deftest api-digest-sha256
  (let [conf {:tools "."}
        api (machines/generate-apis conf)
        app (server/make-handler api conf)
        request (-> (mock/request :post "/digest/sha256")
                    (merge (peridot.multipart/build {:file (io/file (io/resource "public/favicon.ico"))})))
        response (app request)]
    (is (= 200 (:status response)))
    (is (= "12e6426dc2534871f8316c57e9df60ce5bf4bd25f72a3fc40b90aae39658f70c\n"
           (:body response)))))
