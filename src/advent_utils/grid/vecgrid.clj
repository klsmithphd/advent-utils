(ns advent-utils.grid.vecgrid
  (:require [advent-utils.grid :as grid :refer [Grid2D]]))

(defrecord VecGrid2D [v]
  Grid2D
  (width [_] (count (first v)))
  (height [_] (count v))
  (value [_ [x y]] (get-in v [y x]))
  (slice
    [_ dim idx]
    (->VecGrid2D
     (case dim
       :row (vector (get v idx))
       :col (mapv vector (map #(nth % idx) v)))))
  (neighbors-4
    [_ pos]
    (let [locs (grid/adj-coords-2d pos)]
      (zipmap locs (map #(get-in v (-> % reverse vec)) locs))))
  (neighbors-8
    [_ pos]
    (let [locs (grid/adj-coords-2d pos :include-diagonals true)]
      (zipmap locs (map #(get-in v (-> % reverse vec)) locs)))))

(defn ascii->VecGrid2D
  "Convert an ASCII represention of a 2D grid into
   a VecGrid2D.
   
   charmap is a map where the keys are ASCII chars and
   the values are expected to be symbols to use in
   your application. Ex.: (def codes {\\. :space \\# :wall})"
  [charmap lines]
  (->VecGrid2D (mapv #(mapv charmap %) lines)))
