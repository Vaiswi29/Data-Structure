Spider-Verse Project

Overview

Welcome to my Spider-Verse project! This is all about solving problems using graphs and algorithms. Think of it like connecting different dimensions in the Spider-Verse. Pretty cool, right?

What's Inside?

Clusters.java: This file helps group things together using a special table.
Collider.java: This one builds connections between dimensions and places people where they belong.
TrackSpot.java: This tracks The Spot's path using Depth-First Search (DFS).
CollectAnomalies.java: This finds and brings anomalies back to the hub using Breadth-First Search (BFS).
GoHomeMachine.java: This uses Dijkstra’s algorithm to find the best routes for sending anomalies back home.
How to Use

Clusters
Compile: javac -d bin src/spiderman/*.java
Run: java -cp bin spiderman.Clusters dimension.in clusters.out
Collider
Compile: javac -d bin src/spiderman/*.java
Run: java -cp bin spiderman.Collider dimension.in spiderverse.in collider.out
TrackSpot
Compile: javac -d bin src/spiderman/*.java
Run: java -cp bin spiderman.TrackSpot dimension.in spiderverse.in spot.in trackspot.out
CollectAnomalies
Compile: javac -d bin src/spiderman/*.java
Run: java -cp bin spiderman.CollectAnomalies dimension.in spiderverse.in hub.in collected.out
GoHomeMachine
Compile: javac -d bin src/spiderman/*.java
Run: java -cp bin spiderman.GoHomeMachine dimension.in spiderverse.in hub.in anomalies.in gohome.out
Submitting Your Work

Zip up the whole SpiderVerse folder with all your Java files.
Make sure you keep the project structure the same.
Important Tips

Avoid using static variables.
Stick to the input/output formats given.
You can add new classes in src/spiderman if needed.
Don’t change package statements or use System.exit() in your code.
