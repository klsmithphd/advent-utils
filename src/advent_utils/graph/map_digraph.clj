(ns advent-utils.graph.map-digraph
  (:require [clojure.math.combinatorics :as combo]
            [advent-utils.graph :as g :refer [Graph edges distance without-vertex]]))

(defrecord MapDiGraph [vs es]
  Graph
  (vertices
    [_]
    (keys vs))

  (vertex
    [_ v]
    (get vs v))

  (edges
    [_ v]
    (keys (get es v)))

  (distance
    [_ v1 v2]
    (get-in es [v1 v2]))

  (without-vertex
    [g v]
    (let [neighbors (edges g v)
          newedges (-> (reduce #(update %1 %2 dissoc v) es neighbors)
                       (dissoc v))]
      (assoc g :vs (dissoc vs v) :es newedges)))

  (rewired-without-vertex
    [g v]
    (let [neighbors (edges g v)
          all-pairs (combo/permuted-combinations neighbors 2)
          newedge-fn (fn [g [v1 v2]]
                       (update-in g [:es v1] assoc v2 (+ (distance g v1 v)
                                                         (distance g v v2))))]
      (without-vertex (reduce newedge-fn g all-pairs) v))))

(defn edgemap->MapDiGraph
  [es]
  (->MapDiGraph (zipmap (keys es) (repeat nil)) es))

