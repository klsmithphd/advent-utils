(ns advent-utils.grid-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.grid :as grid]))

(deftest adj-coords-2d-test
  (testing "Returns the directly adjacent (non-diagonal) coordinates to the given pos"
    (is (= [[0 -1] [-1 0] [0 1] [1 0]] (grid/adj-coords-2d [0 0])))
    (is (= [[-1 -1] [0 -1] [1 -1]
            [-1  0]        [1  0]
            [-1  1] [0  1] [1  1]]
           (grid/adj-coords-2d [0 0] :include-diagonals true)))))