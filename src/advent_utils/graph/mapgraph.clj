(ns advent-utils.graph.mapgraph
  (:require [clojure.math.combinatorics :as combo]
            [advent-utils.graph :as g :refer
             [Graph vertices vertex edges distance without-vertex rewired-without-vertex]]))

;; (defrecord MapGraph [graph]
;;   Graph
;;   (vertices
;;     [_]
;;     (keys graph))

;;   (edges
;;     [_ v]
;;     (keys (graph v)))

;;   (distance
;;     [_ v1 v2]
;;     (get-in graph [v1 v2]))

;;   (without-vertex
;;     [g v]
;;     (let [neighbors (edges g v)
;;           newgraph (-> (reduce #(update %1 %2 dissoc v) graph neighbors)
;;                        (dissoc v))]
;;       (assoc g :graph newgraph)))

;;   (rewired-without-vertex
;;     [g v]
;;     (let [neighbors (edges g v)
;;           all-pairs (combo/permuted-combinations neighbors 2)
;;           newedge-fn (fn [g [v1 v2]]
;;                        (update-in g [:graph v1] assoc v2 (+ (distance g v1 v)
;;                                                             (distance g v v2))))]
;;       (without-vertex (reduce newedge-fn g all-pairs) v))))

(defrecord MapGraph [vs es]
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

(defn edgemap->MapGraph
  [es]
  (->MapGraph (zipmap (keys es) (repeat nil)) es))

