(defproject advent-utils "0.1.0-SNAPSHOT"
  :description "Helper utilities supporting Advent of Code solutions"
  :url "https://github.com/Ken-2scientists/advent-utils"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.priority-map "0.0.10"]
                 [org.clojure/math.combinatorics "0.1.6"]]
  :repl-options {:init-ns advent-utils.core}
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]])