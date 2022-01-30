(ns advent-utils.grid.mapgrid-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.grid :refer [width height value slice neighbors-4 neighbors-8]]
            [advent-utils.grid.mapgrid :as mg :refer [->MapGrid2D]]))

(def sample
  (->MapGrid2D 3 2 {[0 0] 1 [1 0] 2 [2 0] 3
                    [0 1] 4 [1 1] 5 [2 1] 6}))

(deftest MapGrid2D-test
  (testing "Satisifies the Grid2D protocol"
    (is (= 3 (width sample)))
    (is (= 2 (height sample)))
    (is (= 5 (value sample [1 1])))
    (is (= (->MapGrid2D 3 1 {[0 1] 4 [1 1] 5 [2 1] 6})
           (slice sample :row 1)))
    (is (= (->MapGrid2D 1 2 {[2 0] 3 [2 1] 6})
           (slice sample :col 2)))
    (is (= {[1 0] 2 [0 1] 4 [1 2] nil [2 1] 6}
           (neighbors-4 sample [1 1])))
    (is (= {[0 -1] nil [1 -1] nil [2 -1] nil
            [0 0] 1               [2 0] 3
            [0 1] 4    [1 1] 5    [2 1] 6}
           (neighbors-8 sample [1 0])))))

(deftest lists->MapGrid2D-test
  (testing "Transform a list-of-lists into a MapGrid2D"
    (is (= (->MapGrid2D 2 2 {[0 0] :a [1 0] :b [0 1] :c [1 1] :d})
           (mg/lists->MapGrid2D [[:a :b] [:c :d]])))))

(deftest ascii->MapGrid2D-test
  (testing "Successfully transforms ASCII art into a MapGrid2D"
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