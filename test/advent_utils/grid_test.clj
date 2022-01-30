(ns advent-utils.grid-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.grid :as grid :refer [->MapGrid2D]]))

(deftest lists->MapGrid2D-test
  (testing "Transform a list-of-lists into a MapGrid2D"
    (is (= (->MapGrid2D 2 2 {[0 0] :a [1 0] :b [0 1] :c [1 1] :d})
           (grid/lists->MapGrid2D [[:a :b] [:c :d]])))))

(deftest adj-coords-2d-test
  (testing "Returns the directly adjacent (non-diagonal) coordinates to the given pos"
    (is (= [[0 -1] [-1 0] [0 1] [1 0]] (grid/adj-coords-2d [0 0])))
    (is (= [[-1 -1] [0 -1] [1 -1]
            [-1  0]        [1  0]
            [-1  1] [0  1] [1  1]]
           (grid/adj-coords-2d [0 0] :include-diagonals true)))))

(deftest ascii->MapGrid2D-test
  (testing "Successfully transforms ASCII art into a data structure"
    (is (= (->MapGrid2D
            3 2
            {[0 0] :space
             [1 0] :space
             [2 0] :wall
             [0 1] :space
             [1 1] :wall
             [2 1] :space})
           (grid/ascii->MapGrid2D
            {\. :space \# :wall}
            ["..#"
             ".#."])))))