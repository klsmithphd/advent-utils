(ns advent-utils.digest-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.digest :as d]))

(deftest md5-str-test
  (testing "Computes the md5 digest string"
    (is (= "5d41402abc4b2a76b9719d911017c592" (d/md5-str "hello")))))