(ns advent-utils.grid
  (:require [clojure.string :as str]
            [advent-utils.core :as u]))

(defn adj-coords-2d
  "Coordinates of adjacent points. If include-diagonals is not set or false, 
   returns the four adjacent points (of the von Neumann neighborhood), 
   always in the order N W S E. If include-diagonals is set to true,
   return the eight adjacent coordinates (of the Moore neighborhood)"
  [[x y] & {:keys [include-diagonals]}]
  (if include-diagonals
    ;; including diagonals
    (->> (for [ny (range (dec y) (+ y 2))
               nx (range (dec x) (+ x 2))]
           [nx ny])
         (filter #(not= [x y] %)))
    ;; only directly adjacent
    [[x (dec y)] [(dec x) y] [x (inc y)] [(inc x) y]]))

(defprotocol Grid2D
  "A two-dimensional grid of values"
  (width [this] "The total width of the grid (number of cells in the horizontal direction)")
  (height [this] "The total height of the grid (number of cells in the vertical direction)")
  (value [this pos] "The value of the grid at position pos")
  (neighbors-4 [this pos] "The values of the four nearest (von Neumann) neighbors of position pos")
  (neighbors-8 [this [x y]] "The values of the eight nearest (Moore) neighbors, including diagonals, of position pos"))

;; An alternative implementation of Grid2D backed by vectors 
;; (defrecord VectorGrid2D [v]
;;   Grid2D
;;   (width [_] (count (first v)))
;;   (height [_] (count v))
;;   (value [_ [x y]] (get-in v [y x]))
;;   (neighbors-4 [_ pos] (map #(get-in v (-> % reverse vec)) (adj-coords-2d pos)))
;;   (neighbors-8 [_ pos] (map #(get-in v (-> % reverse vec)) (adj-coords-2d pos :include-diagonals true))))

(defn Grid2D->ascii
  "Convert a Grid2D into an ASCII-art string representation.
   
   charmap is a map where the keys are ASCII chars and
   the values are expected to be symbols to use in
   your application. Ex.: (def charmap {\\. :space \\# :wall})"
  [charmap grid2d]
  (let [chars (u/invert-map charmap)
        w (width grid2d)
        h (height grid2d)
        rep (partition w (for [y (range h)
                               x (range w)]
                           (chars (value grid2d [x y]))))]
    (str/join "\n" (mapv #(apply str %) rep))))