(ns advent-utils.ascii-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.ascii :as ascii]))

(def sample
  ["..#"
   ".#."])

(deftest ascii->map
  (testing "Successfully transforms ASCII art into a data structure"
    (is (= {:height 2
            :width 3
            :grid {[0 0] :space
                   [1 0] :space
                   [2 0] :wall
                   [0 1] :space
                   [1 1] :wall
                   [2 1] :space}}
           (ascii/ascii->map {\. :space \# :wall} sample)))))