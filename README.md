# advent-utils

A Clojure library of helper utilities for solving
[Advent of Code](https://adventofcode.org) puzzles

## Usage

Add the following to your Leiningen `:dependencies`

```clojure
[advent-utils "0.1.1"]
```

### advent-utils.core
The `core` namespace contains the main helper functions for loading the
puzzle inputs (saved as files) plus a variety of small utility functions
that tend to be needed across multiple puzzles

```clojure
(ns foo
  (:require [advent-utils.core :as u]))

; Save your puzzle input to the `resources` directory of your project
; Returns a seq, with an element for each row in the input file
=> (def day01-input (u/puzzle-input "day01-input.txt"))
```

### advent-utils.ascii
The `ascii` namespace has a couple of helpers for dealing with 2D grid
inputs where characters denote different objects.

```clojure
(ns foo
  (:require [advent-utils.ascii :as ascii]))

; ascii->map converts ASCII-art grids into a data structure
; The keys of `:grid` are [x y] positions, indexed from the upper-left corner
=> (ascii/ascii->map {\. :space \# :wall} ["..#" ".#." "#..."]))
{:width 3 :height 3 :grid {[0 0] :space [1 0] :space [2 0] :wall
                           [0 1] :space [1 1] :wall  [2 1] :wall
                           [0 2] :wall  [1 2] :space [2 2] :space}}
```

### advent-utils.graph
The `graph` namespace provides a couple of data structures for representing
graphs (sets of nodes and edges), as well as classic graph algorithms like
Dijkstra's algorithm.

```clojure
(ns foo
  (:require [advent-utils.graph :as g]))
```

### advent-utils.math
The `math` namespace contains helper functions, especially for modular arithmetic

```clojure
(ns foo
  (:require [advent-utils.math :as math]))
```

### advent-utils.maze
The `maze` namespace has functions that help with path-finding in a maze.

```clojure
(ns foo
  (:require [advent-utils.math :as math]))
```

## License

Copyright © 2020 Ken Smith

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
