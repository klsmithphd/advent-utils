(ns advent-utils.binary-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-utils.binary :as b]))

(deftest bitstr->int-test
  (testing "Demonstration of bitstr->int: converts a bit string into an integer"
    (is (= 27 (b/bitstr->int "11011")))
    (is (= 2147483647 Integer/MAX_VALUE
           (b/bitstr->int "1111111111111111111111111111111")))
    (is (= 2147483648
           (b/bitstr->int "10000000000000000000000000000000")))
    (is (= 9223372036854775807 Long/MAX_VALUE
           (b/bitstr->int "111111111111111111111111111111111111111111111111111111111111111")))
    (is (= 9223372036854775808N
           (b/bitstr->int "1000000000000000000000000000000000000000000000000000000000000000")))))

(deftest int->bitstr-test
  (testing "Demonstration of int->bitstr: converts any int type into a string of bits"
    (is (= "11011" (b/int->bitstr 27)))
    (is (= "1111111111111111111111111111111" (b/int->bitstr 2147483647)))
    (is (= "10000000000000000000000000000000" (b/int->bitstr 2147483648)))
    (is (= "111111111111111111111111111111111111111111111111111111111111111"
           (b/int->bitstr 9223372036854775807)))
    (is (= "1000000000000000000000000000000000000000000000000000000000000000"
           (b/int->bitstr 9223372036854775808N)))))