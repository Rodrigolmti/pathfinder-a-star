# Pathfinder A*

This is a code challenge of pathfinder a* algorithm

The definition of the algorithm can be found here: https://en.wikipedia.org/wiki/A*_search_algorithm

There is two type of implmentations here:
- Diagonal search using euclidian heuristics 

![alt text](https://i.imgur.com/yrcIYJ2.png)

- Non Diagonal search using Manhattan Heuristic

![alt text](https://i.imgur.com/hlocBGx.png)

Both of the approaches use the Dijkstra's algorithm to find the best route. The best route is calculated with the 
following formula:

- f(n)=g(n)+h(n)

# Improvements
- Create obstacles in the path
- Find a better way to find the node neighbors
