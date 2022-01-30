(defproject advent-utils "0.1.4"
  :description "Helper utilities supporting Advent of Code solutions in Clojure"
  :url "https://github.com/Ken-2scientists/advent-utils"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.priority-map "0.0.10"]
                 [org.clojure/math.combinatorics "0.1.6"]]
  :repl-options {:init-ns advent-utils.core}
  :deploy-repositories [["releases"  {:sign-releases false :url "https://repo.clojars.org"}]
                        ["snapshots" {:sign-releases false :url "https://repo.clojars.org"}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "--no-sign"]
                  ["deploy"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push" "--tags"]])
