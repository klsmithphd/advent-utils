(ns advent-utils.mapgrid-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.mapgrid :as mg :refer [->MapGrid2D]]))

(deftest lists->MapGrid2D-test
  (testing "Transform a list-of-lists into a MapGrid2D"
    (is (= (->MapGrid2D 2 2 {[0 0] :a [1 0] :b [0 1] :c [1 1] :d})
           (mg/lists->MapGrid2D [[:a :b] [:c :d]])))))

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
           (mg/ascii->MapGrid2D
            {\. :space \# :wall}
            ["..#"
             ".#."])))))