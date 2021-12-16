# Pathfinding Visualizer
This program allows users to visualize the the running patterns of different pathfinding algorithms.

### Description
This project was implemented with the goal of gaining a better intuitive understanding of how different pathfinding algorithms work.  It works by drawing an n by n grid on the screen.  Each square in the grid repesents a node in a graph.  There is an edge between each adjacent nodes.  Each pathfinding algorithm may be let loose on the graph at any given starter node.  As the algorithm 'visits' nodes, they turn red to show that they have been discoverd.  The algorithm will continue until all nodes have been discovered.  The algorithm is slowed down by pausing execution after visiting a new node.

I implemented this project in Java so that I could practice Java graphics as well as reuse the Graph and Priority Queue classes I made for my Algorithms course.

### Algorithms
* Depth First Search
* Breadth First Search
* Dijkstra's Algorithm
