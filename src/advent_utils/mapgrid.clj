(ns advent-utils.mapgrid
  (:require [advent-utils.grid :as grid :refer [Grid2D]]))

(defrecord MapGrid2D [width height grid]
  Grid2D
  (width [_] width)
  (height [_] height)
  (value [_ pos] (get grid pos))
  (slice
    [_ dim idx]
    (let [coord (case dim :col first  :row second)
          w     (case dim :col 1      :row width)
          h     (case dim :col height :row 1)]
      (->MapGrid2D w h (into (sorted-map) (filter #(= idx (-> % key coord)) grid)))))
  (neighbors-4
    [_ pos]
    (let [locs (grid/adj-coords-2d pos)]
      (zipmap locs (map (partial get grid) locs))))
  (neighbors-8
    [_ pos]
    (let [locs (grid/adj-coords-2d pos :include-diagonals true)]
      (zipmap locs (map (partial get grid) locs)))))

(defn lists->MapGrid2D
  "Index a 2D list-of-list-of-values with coordinates starting at [0 0]"
  [values]
  (let [width  (count (first values))
        height (count values)
        coords (for [y (range height)
                     x (range width)]
                 [x y])]
    (->MapGrid2D width height (zipmap coords (flatten values)))))

(defn ascii->MapGrid2D
  "Convert an ASCII represention of a 2D grid into
   a Grid2D.
   
   charmap is a map where the keys are ASCII chars and
   the values are expected to be symbols to use in
   your application. Ex.: (def codes {\\. :space \\# :wall})
   
   Output will be a MapGrid2D with keys width, height, and grid.
   grid will be a map where the keys are [x y] positions
   and values will be the symbols defined in charmap"
  [charmap lines]
  (let [height  (count lines)
        width   (count (first lines))
        symbols (mapcat #(map charmap %) lines)]
    (->MapGrid2D
     width
     height
     (zipmap (for [y (range height)
                   x (range width)]
               [x y])
             symbols))))
