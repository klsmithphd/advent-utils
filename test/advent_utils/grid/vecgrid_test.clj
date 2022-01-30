(ns advent-utils.grid.vecgrid-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.grid :refer [width height value slice neighbors-4 neighbors-8]]
            [advent-utils.grid.vecgrid :as vg :refer [->VecGrid2D]]))

(def sample
  (->VecGrid2D [[1 2 3]
                [4 5 6]]))

(deftest VecGrid2D-test
  (testing "Satisifies the Grid2D protocol"
    (is (= 3 (width sample)))
    (is (= 2 (height sample)))
    (is (= 5 (value sample [1 1])))
    (is (= (->VecGrid2D [[4 5 6]])
           (slice sample :row 1)))
    (is (= (->VecGrid2D [[3] [6]])
           (slice sample :col 2)))
    (is (= {[1 0] 2 [0 1] 4 [1 2] nil [2 1] 6}
           (neighbors-4 sample [1 1])))
    (is (= {[0 -1] nil [1 -1] nil [2 -1] nil
            [0 0] 1               [2 0] 3
            [0 1] 4    [1 1] 5    [2 1] 6}
           (neighbors-8 sample [1 0])))))

(deftest ascii->VecGrid2D-test
  (testing "Successfully transforms ASCII art into a VecGrid2D"
    (is (= (->VecGrid2D
            [[:space :space :wall]
             [:space :wall  :space]])
           (vg/ascii->VecGrid2D
            {\. :space \# :wall}
            ["..#"
             ".#."])))))