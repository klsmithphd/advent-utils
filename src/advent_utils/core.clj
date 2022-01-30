(ns advent-utils.core
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(defn puzzle-input
  "Load a puzzle input (from the resources directory in your project) as
   a seq, with each line in the input as an element in the seq"
  [filename]
  (->> filename
       io/resource
       io/reader
       line-seq))

(defn split-at-blankline
  "Splits a seq of lines (e.g. produced by `puzzle-input`) into
   a collection of chunks, each chunk with a seq of lines"
  [input]
  (let [chunks (-> (str/join "\n" input)
                   (str/split #"\n\n"))]
    (map #(str/split % #"\n") chunks)))

(defn fmap
  "Applies the function f to the values of the map m"
  [f m]
  (zipmap (keys m) (map f (vals m))))

(defn kmap
  "Applies the function f to the keys of the map m"
  [f m]
  (zipmap (map f (keys m)) (vals m)))

(defn without-keys
  "Returns a map with only the entries in map whose key isn't in keyseq"
  [map keyseq]
  (select-keys map (filter (complement (set keyseq)) (keys map))))

(defn invert-map
  "Swap keys and values for map m. Unlikely to do what you want if there
   isn't a 1-1 mapping"
  [m]
  (zipmap (vals m) (keys m)))

(defn rotate
  "Rotate the collection by n. Positive values of n rotate to the left,
   meaning that values are taken from the beginning of coll and moved to
   the end.  Negative values of n rotate to the right, meaning values
   are taken from the end of coll and moved to the front"
  [n coll]
  (let [size (count coll)]
    (take size (drop (mod n size) (cycle coll)))))

(defn index-of
  "Find the index position of x within coll. Only returns the
   first match, even when there are multiple matches. Returns nil
   if x is not found"
  [x coll]
  (ffirst (filter #(= x (second %)) (map-indexed vector coll))))

(defn count-if
  "Find the count of items in coll that satisfy predicate pred"
  [coll pred]
  (count (filter pred coll)))