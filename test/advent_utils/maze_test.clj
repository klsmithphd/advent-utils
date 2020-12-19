(ns advent-utils.maze-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.maze :as m]))

(deftest relative-directions-test
  (testing "Can translate from cardinal to relative directions"
    (is (= (m/relative-directions :north) {:forward :north :left :west :backward :south :right :east}))
    (is (= (m/relative-directions :west)  {:forward :west :left :south :backward :east :right :north}))
    (is (= (m/relative-directions :south) {:forward :south :left :east :backward :north :right :west}))
    (is (= (m/relative-directions :east)  {:forward :east :left :north :backward :west :right :south}))))

(deftest one-step-test
  (testing "Can take one step in any direction"
    (is (= [0 -1] (m/one-step [0 0] :north)))
    (is (= [-1 0] (m/one-step [0 0] :west)))
    (is (= [0 1]  (m/one-step [0 0] :south)))
    (is (= [1 0]  (m/one-step [0 0] :east)))))
