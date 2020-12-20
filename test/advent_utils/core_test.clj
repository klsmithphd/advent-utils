(ns advent-utils.core-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.core :as u]))


(deftest rotate-test
  (testing "Demonstration of rotate"
    (is (= '(0 1 2 3 4) (u/rotate 0 (range 5))))
    (is (= '(1 2 3 4 0) (u/rotate 1 (range 5))))
    (is (= '(2 3 4 0 1) (u/rotate 2 (range 5))))
    (is (= '(3 4 0 1 2) (u/rotate 3 (range 5))))
    (is (= '(4 0 1 2 3) (u/rotate 4 (range 5))))
    (is (= '(0 1 2 3 4) (u/rotate 5 (range 5))))
    (is (= '(4 0 1 2 3) (u/rotate -1 (range 5))))
    (is (= '(3 4 0 1 2) (u/rotate -2 (range 5))))
    (is (= '(2 3 4 0 1) (u/rotate -3 (range 5))))
    (is (= '(1 2 3 4 0) (u/rotate -4 (range 5))))))