(ns advent-utils.graph-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.graph :as g]
            [advent-utils.graph.map-digraph :as mg]))

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

(def t5 (mg/edgemap->MapDiGraph {:a {:b 1 :c 1}
                                 :b {:d 1}
                                 :c {:d 1}
                                 :d {:e 1}}))

(deftest degree-test
  (testing "Computes the outdegree for various nodes"
    (is (= 2 (g/outdegree t1 :b)))
    (is (= 4 (g/outdegree t3 :d)))
    (is (= 4 (g/outdegree t4 :j)))
    (is (= 0 (g/outdegree t4 :k)))))

(deftest sink-source-test
  (testing "Determines whether a node is a sink or a source"
    (is (= true  (g/sink? t4 :k)))
    (is (= true  (g/source? t4 :k)))
    (is (= false (g/sink? t5 :a)))
    (is (= true  (g/source? t5 :a)))
    (is (= false (g/source? t5 :b)))
    (is (= false (g/source? t5 :c)))
    (is (= false (g/source? t5 :d)))
    (is (= false (g/source? t5 :e)))
    (is (= true  (g/sink? t5 :e)))))

(deftest single-path-test
  (testing "Can traverse a graph until its end or a junction is reached"
    (is (= [:a :b :c :d :e] (g/single-path t1 :a)))
    (is (= [:a :b]          (g/single-path t2 :a)))
    (is (= [:g :f :b]       (g/single-path t2 :g)))))

(deftest all-paths-test
  (testing "Can traverse a graph until its end or a junction is reached"
    (is (= [[:b :a] [:b :c :d :e] [:b :f :g]]
           (g/all-paths t2 :b)))
    (is (= [[:g :f :b]]
           (g/all-paths t2 :g)))))

(deftest dijkstra-test
  (testing "Can find the shortest path between two vertices"
    (is (= [:a :d :c :f] (g/dijkstra t3 :a :f)))))
