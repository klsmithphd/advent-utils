# advent-utils

A Clojure library of helper utilities for solving
[Advent of Code](https://adventofcode.org) puzzles

## Usage

Add the following to your Leiningen `:dependencies`

```clojure
[advent-utils "0.1.4"]
```

### advent-utils.core
The `core` namespace contains the main helper functions for loading the
puzzle inputs (saved as files) plus a variety of small utility functions
 that tend to be needed across multiple puzzles

#### Managing puzzle inputs
```clojure
(ns foo
  (:require [advent-utils.core :as u]))

; Save your puzzle input to the `resources` directory of your project and then
; `puzzle-input` will return a seq, with an element for each row in the input file
=> (def day01-input (u/puzzle-input "day01-input.txt"))

; Sometimes puzzle inputs will consist of multiple "chunks", with each chunk
; separated by a blank line. For those cases, use `split-at-blankline`
=> (u/split-at-blankline ["Chunk 1"
                          "Some values"
                          ""
                          "Chunk 2"
                          "Some other values"])
[["Chunk 1" "Some values"]
 ["Chunk 2" "Some other values"]]
```

#### Helper/utility functions
```clojure
; Apply a function to the values of a map
=> (u/fmap inc {:a 1 :b 2 :c 3})
{:a 2 :b 3 :c 4}

; ...or the keys of a map
=> (u/kmap inc {0 :a 1 :b 2 :c})
{1 :a 2 :b 3 :c}

; Return a map with certain keys excluded
=> (u/without-keys {:a 1 :b 2 :c 3 :d 4} [:a :d])
{:b 2 :c 3}

; Swap the keys/vals of a map, for a 1:1 mapping
=> (u/invert-map {:a 0 :b 1 :c 2})
{0 :a 1 :b 2 :c}

; Rotate the elements of a collection, keeping the same size.
; Positive shifts rotate "left". Negative shifts rotate "right"
=> (u/rotate 1 '(0 1 2 3 4))
(1 2 3 4 0)
=> (u/rotate -1 '(0 1 2 3 4))
(4 0 1 2 3)

; Find the position in a collection of the first occurrence of x
=> (u/index-of 8 [1 2 4 8 16])
3

; Count the elements of a collection that satisfy a predicate
=> (u/count-if (range 10) odd?)
5
```

### advent-utils.binary
The `binary` namespace contains the helper functions for converting to-and-from binary repesentations of numbers

```clojure
(ns foo
  (:require [advent-utils.binary :as b]))

; Convert an int value into a binary string representation of 0s and 1s
=> (u/int->bitstr 2147483647)
"1111111111111111111111111111111"
=> (u/int->bitstr 9223372036854775808N)
"1000000000000000000000000000000000000000000000000000000000000000"

; And convert the binary string representation back to a value
=> (u/bitstr->int "11011")
27
```

### advent-utils.digest
The `digest` namespace contains the helper functions for working with message digests (e.g. MD5)

```clojure
(ns foo
  (:require [advent-utils.digest :as d]))

=> (d/md5-str "hello")
"5d41402abc4b2a76b9719d911017c592"
```

### advent-utils.graph
The `graph` namespace provides a couple of data structures for representing
graphs (sets of nodes and edges), as well as classic graph algorithms like
Dijkstra's algorithm.

```clojure
(ns foo
  (:require [advent-utils.graph :as graph]))
```

TODO: Add more documentation

### advent-utils.grid
The `grid` namespace has helpers for dealing with values on a regular
2D grid of values, i.e. where there are values/objects at given `[x y]`
coordinates. 

The namespace defines a protocol `Grid2D`, for which there
are two implementations. 

* `MapGrid2D` uses a map as its underlying
data structure, with the `[x y]` positions as keys. This implementation
is particularly useful if the grid size does not remain static

* `VecGrid2D` uses a vector of vectors as its underlying structure.

```clojure
(ns foo
  (:require [advent-utils.grid :as grid :refer [width height value neighbors-4 neighbors-8]]
            [advent-utils.grid.mapgrid :as mg]
            [advent-utils.grid.vecgrid :as vg]))

; Compute the adjacent coordinates for a given position
=> (grid/adj-coords-2d [0 0])
[[0 -1] [-1 0] [0 1] [1 0]]

; Can specify :include-diagonals true to get the 8 adjacent coordinates
=> (grid/adj-coords-2d [0 0] :include-diagonals true)
[[-1 -1] [0 -1] [1 -1] [-1  0] [1  0] [-1  1] [0  1] [1  1]]
            
; ascii->MapGrid2D converts ASCII-art grids into a data structure
; The keys of `:grid` are [x y] positions, indexed from the upper-left corner
=> (mg/ascii->MapGrid2D {\. :space \# :wall} ["..#" ".#." "#.."]))
#MapGrid2D{:width 3 :height 3 :grid {[0 0] :space [1 0] :space [2 0] :wall
                                     [0 1] :space [1 1] :wall  [2 1] :wall
                                     [0 2] :wall  [1 2] :space [2 2] :space}}

; Alternatively, ascii->VecGrid2D converts the same ASCII grids into the
; VecGrid2D structure
=> (def foo (vg/ascii->VecGrid2D {\. :space \# :wall} ["..#" ".#." "#.."])
#VecGrid2D{:v [[:space :space :wall] 
               [:space :wall :space] 
               [:wall :space :space]]}

; Either implementation supports all the functions defined in the `Grid2D` protocol
=> (width foo)
3

=> (height foo)
3

=> (value foo [1 0])
:space

=> (neighbors-4 foo [1 1])
{[1 0] :space, [0 1] :space, [1 2] :space, [2 1] :space}

=> (neighbors-8 foo [1 1])
{[0 0] :space [1 0] :space [2 0] :wall 
 [0 1] :space              [2 1] :space
 [0 2] :wall  [1 2] :space [2 2] :space}
```

### advent-utils.math
The `math` namespace contains helper functions, especially for modular arithmetic

```clojure
(ns foo
  (:require [advent-utils.math :as math]))
```

TODO: Add more documentation

### advent-utils.maze
The `maze` namespace has functions that help with path-finding in a maze.

```clojure
(ns foo
  (:require [advent-utils.math :as math]))
```

TODO: Add more documentation

## License

Copyright © 2022 Ken Smith

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
