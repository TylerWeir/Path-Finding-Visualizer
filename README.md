# Pathfinding Visualizer
This program allows users to visualize the the running patterns of different pathfinding algorithms.

### Description
This project was implemented with the goal of gaining a better intuitive understanding of how different pathfinding algorithms work.  It works by drawing an n by n grid on the screen.  Each square in the grid repesents a node in a graph.  There is an edge between each adjacent nodes.  Each pathfinding algorithm may be let loose on the graph at any given starter node.  As the algorithm 'visits' nodes, they turn red to show that they have been discoverd.  The algorithm will continue until all nodes have been discovered.  The algorithm is slowed down by pausing execution after visiting a new node.

I implemented this project in Java so that I could practice Java graphics as well as reuse the Graph and Priority Queue classes I made for my Algorithms course.

Currently the program is run via the main function under each pathfinding class.  These then call the App class which implements the GUI.  This program structure is clumbsy and will soon be replaced by a more robust UI from which users can run any of the avaliable pathfinding algorithms.  

### Algorithms
* Depthfirst Search
* Breadthfirst Search
* Dijkstra's Algorithm
* A-Star