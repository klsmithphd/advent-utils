(ns advent-utils.graph.map-graph
  (:require [clojure.math.combinatorics :as combo]
            [clojure.set :as set]
            [advent-utils.graph :as g :refer [Graph edges distance without-vertex]]))

(defrecord MapGraph [vs es]
  Graph
  (directed?
    [_]
    false)

  (vertices
    [_]
    (keys vs))

  (vertex
    [_ v]
    (get vs v))

  (indegree
    [_ v]
    (->> (keys es) (filter #(contains? % v)) count))

  (edges
    [_ v]
    (let [nodes (->> (keys es)
                     (filter #(contains? % v))
                     (apply set/union))]
      (seq (disj nodes v))))

  (distance
    [_ v1 v2]
    (get es #{v1 v2}))

  (without-vertex
    [g v]
    (assoc g
           :vs (dissoc vs v)
           :es (into {} (remove #(contains? (key %) v) es))))

  (rewired-without-vertex
    [g v]
    (let [neighbors (edges g v)
          all-pairs (combo/permuted-combinations neighbors 2)
          newedge-fn (fn [g [v1 v2]]
                       (assoc-in g [:es #{v1 v2}] (+ (distance g v1 v)
                                                     (distance g v v2))))]
      (without-vertex (reduce newedge-fn g all-pairs) v))))

(defn edgemap->MapGraph
  [es]
  (->MapGraph (zipmap (->> (keys es) (apply set/union) seq)
                      (repeat nil)) es))

