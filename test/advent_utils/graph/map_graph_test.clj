(ns advent-utils.graph.map-graph-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.graph :as g :refer
             [vertices vertex indegree edges distance without-vertex rewired-without-vertex]]
            [advent-utils.graph.map-graph :as mg :refer [->MapGraph]]))

(def t1 (mg/edgemap->MapGraph {#{:a :b} 1
                               #{:b :c} 2
                               #{:c :d} 3
                               #{:d :e} 1}))

(def t2 (mg/edgemap->MapGraph {#{:a :b} 1
                               #{:b :c} 2
                               #{:b :f} 4
                               #{:c :d} 3
                               #{:d :e} 1
                               #{:f :g} 1}))

(def t3 (mg/edgemap->MapGraph {#{:a :b} 7
                               #{:a :c} 14
                               #{:a :d} 9
                               #{:b :d} 10
                               #{:b :e} 15
                               #{:c :d} 2
                               #{:c :f} 9
                               #{:d :e} 11
                               #{:e :f} 6}))

(def t4 (->MapGraph {:a nil :b nil :c nil :d nil :j nil :k nil}
                    {#{:a :j} 3
                     #{:b :j} 2
                     #{:c :j} 4
                     #{:d :j} 6}))

(def t5 (->MapGraph {:a "A" :b "B" :c "C"}
                    {#{:a :b} 3
                     #{:a :c} 4
                     #{:b :c} 5}))

(deftest vertices-test
  (testing "Returns the vertices of a graph"
    (is (= #{:a :b :c :d :e}       (set (vertices t1))))
    (is (= #{:a :b :c :d :e :f :g} (set (vertices t2))))
    (is (= #{:a :b :c :d :e :f}    (set (vertices t3))))
    (is (= #{:a :b :c :d :j :k}    (set (vertices t4))))
    (is (= #{:a :b :c}             (set (vertices t5))))))

(deftest vertex-test
  (testing "Returns data about a vertex"
    (is (= "A" (vertex t5 :a)))))

(deftest indegree-test
  (testing "Calculates the indegree of a vertex"
    (is (= 2 (indegree t1 :b)))
    (is (= 1 (indegree t1 :a)))
    (is (= 4 (indegree t3 :d)))
    (is (= 4 (indegree t4 :j)))
    (is (= 0 (indegree t4 :k)))))

(deftest edges-test
  (testing "Returns the edges associated with a given vertex"
    (is (= #{:a :c}       (set (edges t1 :b))))
    (is (= #{:a :b :c :e} (set (edges t3 :d))))
    (is (= nil            (edges t4 :k)))
    (is (= #{:a :b}       (set (edges t5 :c))))))

(deftest distance-test
  (testing "Returns the distance from v1 to v2 in a given graph"
    (is (= 3   (distance t1 :c :d)))
    (is (= 10  (distance t3 :d :b)))
    (is (= nil (distance t2 :e :g)))
    (is (= nil (distance t4 :k :a)))))

(deftest without-vertex-test
  (testing "Can return a new graph with a vertex (and its corresponding edges) removed"
    (is (= (mg/edgemap->MapGraph {#{:b :c} 2 #{:c :d} 3 #{:d :e} 1})
           (without-vertex t1 :a)))))

(deftest rewired-without-vertex-test
  (testing "Can return a new graph with a vertex removed, preserving the transitive relationships"
    (is (= (->MapGraph {:a nil :b nil :c nil :d nil :k nil}
                       {#{:a :b} 5
                        #{:a :c} 7
                        #{:a :d} 9
                        #{:b :c} 6
                        #{:b :d} 8
                        #{:c :d} 10})
           (rewired-without-vertex t4 :j)))))
