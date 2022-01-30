(ns advent-utils.digest
  (:import java.security.MessageDigest))

(def md5-alg (MessageDigest/getInstance "MD5"))

(defn md5-bytes
  "Computes the MD5 digest of s as a byte array"
  [^String s]
  (.digest md5-alg (.getBytes s)))

(defn md5-str
  "Computes the string representation of the MD5 digest of s"
  [^String s]
  (let [raw (.toString (BigInteger. 1 (md5-bytes s)) 16)
        padding (apply str (repeat (- 32 (count raw)) "0"))]
    (str padding raw)))

(type (md5-bytes "hello"))