## PR1

## Author
- Cristóbal Sánchez Arisa  
- csanchezarisa@uoc.edu

## 
- `Utils` class created. It contains:
  - `anyMatch` function that allows to test a predicate looping an iterator
  - `find` using **Traversal** function that allows to find a position by using a predicate 
  - `find` using **Iterator** function that allows to find an element by using a predicate
  - `filter` function that allows to filter an **Iterator** and creates a new list containing the result
  - `count` function that counts the number of elements in an **Iterator** that fit a predicate
  - `toList` function that converts any data structure of DSLib into a List
- `GraphUtils` class created. Contains functionalities needed to manage and query graphs
  - `existConnection` checks if a route exist between two ports
  - `bestPortRoute` returns the best route based on the number of ports
  - `bestKmsRoute` returns the best route based on the distance between ports using `MinimumPaths` class algorithm
- `TestUtils` test class for Utils class