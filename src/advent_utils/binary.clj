(ns advent-utils.binary)

(defn bitstr->int
  "Converts a bit string (e.g. '1101') to the corresponding integer (may be a long or bigint)"
  [s]
  (->> s (str "2r") read-string))

(defn int->bitstr
  "Converts any integer (or long or bigint) to a bit string"
  [x]
  (let [bi (biginteger x)]
    (.toString bi 2)))