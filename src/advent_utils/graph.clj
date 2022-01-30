(ns advent-utils.graph
  (:require [clojure.data.priority-map :refer [priority-map]]
            [advent-utils.core :as u]))

(defprotocol Graph
  (directed? [this] "Whether this graph is directed")
  (vertices [this] "A collection of all the vertices in the graph")
  (vertex [this v] "Any data/information/label associated with the given vertex in the graph")
  (indegree [this v] "The number of incident edges to this vertex in the graph")
  (edges [this v] "A collection of the edges for the given vertex in the graph")
  (distance [this v1 v2] "The distance (or edge weight) between two vertices")
  (without-vertex [this v] "Produces a new graph with the vertex removed")
  (rewired-without-vertex [this v] "Produces a new graph, re-wired to preserve the transitive edges through the removed vertex"))

(defn outdegree
  "The outdegree of a vertex is the number of outgoing edges it has"
  [g v]
  (count (edges g v)))

(defn source?
  "Whether a vertex is a source vertex (a vertex with no incoming edges)"
  [g v]
  (zero? (indegree g v)))

(defn sink?
  "Whether a vertex is a sink vertex (a vertex with no outgoing edges)"
  [g v]
  (zero? (outdegree g v)))

(defn isolated?
  "Whether a vertex is isolated (has degree of zero)"
  [g v]
  (and (source? g v) (sink? g v)))

(defn leaf?
  "Whether a vertex is a leaf vertex (meaning that it has at most one edge)"
  [g v]
  (= 1 (indegree g v)))

(defn junction?
  "Whether a vertex is a junction (meaning that it has more than two edges)"
  [g v]
  (> (outdegree g v) 2))

(defn path-distance
  "Computes the distance along a path (an ordered collection of vertices)"
  [g path]
  (reduce + (map #(apply (partial distance g) %) (partition 2 1 path))))

(defn entries-in-set
  [s m]
  (filter (fn [[k _]] (s k)) m))

(defn entries-not-in-set
  [s m]
  (filter (fn [[k _]] ((complement s) k)) m))

(defn single-path
  "Return the only possible path traversal from the start vertex (presumed to be a leaf vertex)
   until reaching another leaf vertex or a vertex with more than one un-traversed edge"
  ([g v & {:keys [exclude]}]
   (loop [visited (if exclude [exclude v] [v])
          neighbors (if exclude (filter (complement #{exclude}) (edges g v)) (edges g v))]
     (if (or (> (count neighbors) 1) (= (count neighbors) 0))
       visited
       (recur (conj visited (first neighbors))
              (filter (complement (set visited)) (edges g (first neighbors))))))))

(defn single-path-2
  "Return the only possible path traversal from the start vertex (presumed to be a leaf vertex)
   until reaching another leaf vertex or a vertex with more than one un-traversed edge"
  ([g v s stop-at]
   (loop [visited [s v]
          neighbors (filter (complement #{s}) (edges g v))]
     (if (or (> (count neighbors) 1)
             (= (count neighbors) 0)
             (some? (stop-at (last visited))))
       visited
       (recur (conj visited (first neighbors))
              (filter (complement (set visited)) (edges g (first neighbors))))))))

(defn all-paths
  "Find all the paths from a vertex reaching a leaf vertex or a vertex with more than one
  untraversed edges"
  [g v & {:keys [excludes]}]
  (let [neighbors (edges g v)
        stop-at (if excludes (set excludes) #{})]
    (map #(single-path-2 g % v stop-at) neighbors)))

(defn dijkstra-update
  [graph vertex {:keys [dist prev queue] :as state} neighbor]
  (let [alt (+ (dist vertex) (distance graph vertex neighbor))]
    (if (or (nil? (dist neighbor)) (< alt (dist neighbor)))
      {:dist  (assoc dist neighbor alt)
       :queue (assoc queue neighbor alt)
       :prev  (assoc prev neighbor vertex)}
      state)))

(defn dijkstra-retrace
  [prev-steps finish]
  (loop [vertex finish chain []]
    (if (nil? vertex)
      chain
      (recur (prev-steps vertex) (conj chain vertex)))))

(defn dijkstra
  "Executes Dijkstra's algorithm to identify the shortest path between the start and finish vertices"
  [graph start finish & {:keys [limit]}]
  (let [max-search (or limit (count (vertices graph)))
        init-state {:dist {start 0} :prev {} :queue (priority-map start 0)}]
    (loop [visited #{}
           visited-count 1
           vertex start
           state init-state]
      (if (or (= max-search visited-count) (= vertex finish))
        (reverse (dijkstra-retrace (state :prev) finish))
        (let [neighbors (remove visited (edges graph vertex))
              new-state (-> (reduce (partial dijkstra-update graph vertex) state neighbors)
                            (update :queue dissoc vertex))]
          (recur
           (conj visited vertex)
           (if (visited vertex) visited-count (inc visited-count))
           (ffirst (:queue new-state))
           new-state))))))

(defn shortest-distance
  [graph start finish]
  (path-distance graph (dijkstra graph start finish)))

(defn pruned
  "Prunes the single branches from a graph, excluding any vertices in the exclude-set"
  [graph exclude-set]
  (loop [newgraph graph]
    (let [dead-end-pred (every-pred (partial leaf? newgraph) (complement exclude-set))
          dead-ends (filter dead-end-pred (vertices newgraph))]
      (if (= 0 (count dead-ends))
        newgraph
        (recur (reduce without-vertex newgraph dead-ends))))))

(defn summarize-path
  [g path]
  [(first path) {(last path) (path-distance g path)}])

(defn adjacencies
  [graph]
  (let [leaves (filter (partial leaf? graph) (vertices graph))
        junctions (filter (partial junction? graph) (vertices graph))
        nodes (concat leaves junctions)]
    (->> (mapcat (partial all-paths graph) nodes)
         (map (partial summarize-path graph))
         (group-by first)
         (u/fmap #(apply merge (map second %))))))

(defn reachable
  [graph start stop-cond]
  (loop [visited #{start} explore (edges graph start)]
    (let [next-neighbors (filter (complement visited) explore)]
      (if (zero? (count next-neighbors))
        (disj visited start)
        (let [node (first next-neighbors)]
          (recur (conj visited node)
                 (if (stop-cond node) explore (concat explore (edges graph node)))))))))
