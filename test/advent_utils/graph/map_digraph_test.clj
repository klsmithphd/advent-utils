(ns advent-utils.graph.map-digraph-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.graph :as g :refer
             [vertices vertex indegree edges distance without-vertex rewired-without-vertex]]
            [advent-utils.graph.map-digraph :as mg :refer [->MapDiGraph]]))

(def t1 (mg/edgemap->MapDiGraph {:a {:b 1}
                                 :b {:a 1 :c 2}
                                 :c {:b 2 :d 3}
                                 :d {:c 3 :e 1}
                                 :e {:d 1}}))

(def t2 (mg/edgemap->MapDiGraph {:a {:b 1}
                                 :b {:a 1 :c 2 :f 4}
                                 :c {:b 2 :d 3}
                                 :d {:c 3 :e 1}
                                 :e {:d 1}
                                 :f {:b 4 :g 1}
                                 :g {:f 1}}))

(def t3 (mg/edgemap->MapDiGraph {:a {:b 7 :c 14 :d 9}
                                 :b {:a 7 :d 10 :e 15}
                                 :c {:a 14 :d 2 :f 9}
                                 :d {:a 9 :b 10 :c 2 :e 11}
                                 :e {:b 15 :d 11 :f 6}
                                 :f {:c 9 :e 6}}))

(def t4 (mg/edgemap->MapDiGraph {:a {:j 3}
                                 :b {:j 2}
                                 :c {:j 4}
                                 :d {:j 6}
                                 :j {:a 3 :b 2 :c 4 :d 6}
                                 :k {}}))

(def t5 (->MapDiGraph {:a "A" :b "B" :c "C"}
                      {:a {:b 3 :c 4}
                       :b {:a 3 :c 5}
                       :c {:a 4 :b 5}}))

(deftest vertices-test
  (testing "Returns the vertices of a graph"
    (is (= '(:a :b :c :d :e)       (vertices t1)))
    (is (= '(:a :b :c :d :e :f :g) (vertices t2)))
    (is (= '(:a :b :c :d :e :f)    (vertices t3)))
    (is (= '(:a :b :c :d :j :k)    (vertices t4)))
    (is (= '(:a :b :c)             (vertices t5)))))

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
    (is (= '(:a :c)       (edges t1 :b)))
    (is (= '(:a :b :c :e) (edges t3 :d)))
    (is (= nil            (edges t4 :k)))
    (is (= '(:a :b)       (edges t5 :c)))))

(deftest distance-test
  (testing "Returns the distance from v1 to v2 in a given graph"
    (is (= 3   (distance t1 :c :d)))
    (is (= 10  (distance t3 :d :b)))
    (is (= nil (distance t2 :e :g)))
    (is (= nil (distance t4 :k :a)))))

(deftest without-vertex-test
  (testing "Can return a new graph with a vertex (and its corresponding edges) removed"
    (is (= (mg/edgemap->MapDiGraph {:b {:c 2}, :c {:b 2, :d 3}, :d {:c 3, :e 1}, :e {:d 1}})
           (without-vertex t1 :a)))))

(deftest rewired-without-vertex-test
  (testing "Can return a new graph with a vertex removed, preserving the transitive relationships"
    (is (= (mg/edgemap->MapDiGraph {:a {:b 5 :c 7 :d 9}
                                    :b {:a 5 :c 6 :d 8}
                                    :c {:a 7 :b 6 :d 10}
                                    :d {:a 9 :b 8 :c 10}
                                    :k {}})
           (rewired-without-vertex t4 :j)))))
