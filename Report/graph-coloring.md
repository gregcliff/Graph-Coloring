# An Exploration of Graph Coloring and Intractability

“The limits of the possible can only be defined by going beyond them into the impossible.”
-Arthur C. Clarke

[Introduction](##introduction)

[Algorithm](##Algorithm)

  [Graph Setup](###Graph-Setup)
  
  [Example](###Example)
  
  [Generate the complement](###Generate-the-complement)
  
  [Implementation](###Implementation)
  
  [Additional Functionality](###Additional-Functionality)
  
[Application](##Application)

  [Neo4j](###Neo4j)
  
  [User Interface](###User-Interface)
  
  [Visuals](###Visuals)
  
  [Object Oriented Framework](###Object-Oriented_Framework)
  
  [Project Extension](###Project-Extension)
  
[Conclusion](##Conclusion)

  [Testing and Future Work](###Testing-and-Future-Work)
  
  [Final Analysis](###Final-Analysis)
  
[Appendices]

  [Works Cited](###Works-Cited)
  
  [Packages](###Packages)
  
  [Other](###Other)
  
# Introduction

Computers seem like they can do anything.  In the modern world of technology, complicated tasks have been simplified to the single click of a button or sometimes have even been completely automated.  Most people recognize that some advanced software is driving this functionality.  Some could go as far as to say there are “algorithms” involved in this software.  But what does the word algorithms really mean to the common technology user?   It can seem abstract, mystical, or sometimes even generalized to the point of unimportance.   The formal definition of an algorithm in The Merriam-Webster Dictionary is “a rule of procedure for solving a problem (as in mathematics) that frequently involves repetition of an operation.”  This brings the idea of algorithms down to Earth, but we are still missing quite a bit of information in order to have an understanding of algorithms.  An important, often overlooked aspect of algorithms is the classification of them.  Some algorithms give the first acceptable answer they find.  Other algorithms simply say “yes” or “no.”  Some algorithms consistently give the best possible solution to a problem.  Other algorithms can only make a guess.  This introduces an important question: what makes a good algorithm?

There are many ways to analyze an algorithm.  In Computer Algorithms by Sara Baase and Allen Van Gelder, the authors outline the basic characteristics of an algorithm.  The most obvious way to analyze an algorithm is correctness.  The word algorithm may seem to imply correctness.  However, we will see that sometimes incorrectness is embedded in the design of an algorithm.  Another defining trait is the amount of work required by the complexity of an algorithm’s design.  This concept, known as time complexity, can distinguish a well-designed algorithm from an unusable one.  The third consideration is the amount of space used by an algorithm.  In some cases, algorithms need to be very cautious with space constraints.  However, in a world with 16 gigabyte DDR3 RAM and terabytes of data, the consideration of space is not as sensitive as it used to be.  The fourth trait is simplicity.  Clarity of design and implementation are important traits for future study or improvement of the algorithm.  Though this trait is important, it is somewhat arbitrary and difficult to measure.  The last but certainly not least trait is optimality.  Does the algorithm give a solution, or does it give the best solution?  In certain situations, it may be easy to offer an answer, but this does not always imply the answer is optimal.  

With these considerations in mind, we can begin evaluating algorithms.  We can form arguments about when to use a certain algorithm.  For example, consider the problem of solving 25x = 2500.  One may confidently claim that performing elementary “long division” is better than multiplying integers by 25 until we get 2500.  But what happens when the algorithms become more difficult?  Is one algorithm really better than another if they both take decades to finish?  In order to fully understand this question and its implications, we will need to look more closely at time complexity.

The conventional factor in evaluating algorithms is time complexity.  The reason this trait is so important is because it takes problems of different sizes into account.  Time complexity, the amount of work, is represented by a “big” O, called the order.  The order of an algorithm is essentially a representation of the worst case amount of work an algorithm performs.  We then say n represents the size of the input for the algorithm.  So, an algorithm with O(n) time complexity will perform some “work” at most n times.  Another way to say this is the algorithm will perform at most n units of work.  If there are 5 inputs, the algorithm will perform at most “5 units of work.”  If there are 100 inputs, the algorithm will perform at most “100 units of work.”  But consider an more complex algorithm, say with order of n3, or O(n3).  With 2 inputs, the algorithm performs just 8 units of work.  With 10 inputs, the algorithm must perform 1000 units of work.

Let us make this example a bit more concrete.  Say we want to determine if some Person A knows Person B, and pretend we can only ask Person B.  The input n in this case would be the number of people that B knows.  We must consider at most every person that Person B knows.  Once we have considered everyone B knows, we will know whether or not A knows B.  So, this pseudo-algorithm is O(n).  Now let us make the algorithm more complex.  We want to know if Person A knows Person B, if person A knows someone Person B knows, or if Person A knows someone that knows someone that knows Person B.  This is a much more involved algorithm.  One can imagine the social web expanding as you try to answer this question.  This algorithm, assuming everyone has n friends, is O(n3).  If everyone only knows 2 people, the algorithm must only check 8 people, but if everyone knows 10 people, the algorithm must check 1000.  This is why we must distinguish algorithms with time complexity.  On the other hand, it is even more important to classify the problems the algorithms solve.

Decades ago, as computational theory became more advanced, computer scientists and mathematicians began classifying problems based on computation.  Problems are divided into classes based on the algorithms we can use to solve them.  With this in mind, there is one particular distinction that has defined the study of algorithms for years.  The first set of problems is the problems that can be solved with algorithms of polynomial order.   The next, we suspect, is the set of problems whose answers can only verified with algorithms of polynomial order.

It is fair to consider problems that can be solved with algorithms of polynomial order to be the “easy” algorithms.  So, the examples of Person A and Person B are considered easy because they can be solved in polynomial time.  We define these “easy” problems to be in the set P, for “polynomial.”  Though we saw different polynomial time complexities can be drastically different in execution, it is appropriate to consider problems of this nature effortless.

Theorists infer that there is another set of problems that must be solved in a different way.  The accepted way to “answer” these problems is by guessing the answer to a question and verifying it with either a “yes” or a “no.”  This more complicated set of problems is the set NP.  We currently suspect that these problems are different than the problems in P in a very particular way.  In a simplified version of Baase and Gelder’s explanation of NP problems, we will break the “solution” of NP problems into two parts: 1) guessing and 2) checking.  We will consider the guess to be randomized.  The check is the important part.  With this class of problems, we can check the guess in polynomial time.  If our guess is correct, we have answered the question: “yes!”  However, there is an inherent difficulty of this design.  A “no” does not imply a correct guess does not exist.

The basic issue with these problems is that a “no” does not really help solve the algorithm.  All we can really do is continue to guess.  Consider a situation where Person A asks Person B to guess what number he is thinking.  Person B guesses 5, and Person A simply says “no.”  Person B knows it is not a 5, but that is just about the only information he gains.  His only option is to guess again.  This problem is not NP because, the guesses do not come from a finite set, but it helps illustrate the type of difficulty that NP problems present.  

If we think back to P problems, we can note that P is a subset of NP.  



This is because any problem in P can be verified in the same “yes” or “no” fashion as NP problems.  It is important to note that this diagram is not necessarily accurate because we do not yet know P ≠ NP.  We define a problem as intractable if it is “so hard that no polynomial time algorithm can possibly solve it” (Garey and Johnson, 8).  That means if P ≠ NP, the problems in NP – P are intractable.  Furthermore, it is interesting to note that if P = NP, there are no intractable problems in NP.

In order to fully understand the complexity of the problem in this thesis, we must define two more concepts.  Reduction is the transformation of one problem into another.  A thorough understanding of reduction is not completely necessary for this thesis.  The point of reducibility is to say one problem is at least as hard as another problem (Baase and Gelder, 561).  A problem can be reduced to something that is equally as hard as or harder than the original problem.  NP-Hard is the set of problems that all NP problems can be reduced to.  Recall that a problem cannot be reduced to an easier problem; it can only be reduced to be equally hard or harder.  But as a reminder, easier problems can be reduced to more difficult problems, such as the reduction of a P problem to an NP-P problem.1  This shows us that NP and NP-Hard intersect at the set of problems that are the hardest in NP.  This menacing set of problems is known as NP-Complete.



The problem that this thesis attempts to tackle is an NP-Complete problem.  For this thesis, it will suffice to know that these are the most difficult problems to solve, and we cannot currently solve them in reasonable time.  Unfortunately, some extremely useful problems are NP-Complete.  For example, how do you optimize simultaneous green lights at a traffic intersection?  How do you find out the minimal number of tables to seat a group of guests, some of which cannot sit at the same table?  How do you figure out a final exam schedule that accommodates all students at a college university? These are all examples of NP-Complete scheduling problems, and they can be answered with graph coloring.

We define a graph as a set of nodes and edges.  In the example to the right, we have a set of nodes that is {0, 1, 2, 3, 4}. We also define the unordered pairs of edges between them as {{1,2}, {2,3}, {3,4}, {0,4}, {4,1}}.  In graph theory, these edges represent adjacency.  These sets are a mathematical representation of this particular graph.  

Graph coloring is a problem that assigns values to each node.  These values are referred to as colors.  The restriction on these colors is that adjacent nodes may not share the same color.  The number of nodes used to color the graph is typically considered the “solution” in graph coloring problems.  This number, when it is as low as possible, is called the chromatic number.  Graph coloring is formally defined in “Computers and Intractability: A Guide to the Theory of NP-Completeness” like so:

“INSTANCE: Graph G = (V, E), positive integer K ≤|V|.

QUESTION: Is G K-colorable, i.e., does there exist a function f: V  {1, 2, …, K} such that f(u) ≠ f(v) whenever {u, v} belongs to E?” (Garey and Johnson 191).

Garey and Johnson present the problem in the form that we can approach with a “guess and check” method.  With this method, we have to provide some K.  We could then guess a solution and verify if it is correct in polynomial time.  However, finding the optimal K, such that K is the chromatic number is an NP-Complete problem.  Optimality is one of our defining traits of algorithms, but we now know not every algorithm can guarantee it.  Getting as close to the chromatic number as possible is the goal of this thesis.

## Algorithm

The Largest Degree First algorithm is a best effort approximation algorithm.  With the potentially intractable problem of finding the chromatic number, we accept the idea that we currently cannot come up with an answer in polynomial time.  Keeping this in mind, we construct an approximation algorithm.  This concept takes correctness, time complexity, and optimality into consideration.  An approximation algorithm acknowledges that we cannot guarantee a best solution, so it tries to get a good one.  In this case, we sacrifice optimality for the sake of time complexity.  However, it is important that we never break the conditions of the problem.  Our goal is to generate a correct, but not necessarily optimal, coloring in polynomial time.

### Graph Setup

As is convention, the set of colors, K will be identified numerically by values {1, 2, …, k} with k being the number of unique colors used in the algorithm.  Note that K is the set of colors, and k is the chromatic number.  The rest of this section will explain the setup and implementation of the LDF algorithm.

Let graph G be a graph with a set of vertices V and a set of edges E.  This can be expressed by G = (V, E) and will be referred to as the base graph.  We will generate the complement of G, called H.  Note: H = (V’, E’).  Vertices in H are mathematically equal to vertices in G.  A relationship exists in H if and only if there was not a relationship between two nodes in G.

We will use the properties of H to perform the coloring on G.  The algorithm only uses nodes and edges from V’ and E’.  First, we will find the node that has the most relationships.2  We will color this node “1.”3  Then, consider all of the node’s neighbors.  The goal is to color as many of these neighbors as possible with “1.” Note that all “1” nodes must form a clique.  This is because a clique in H implies a lack of connection in G.  If a node will not form a clique, it cannot be colored with “1.”  Once all neighbors of the original node have been considered, we can remove all nodes and respective edges with color “1” from G’.  Then, repeat the same steps with the next color using the set of all uncolored nodes.  Continue coloring nodes in this fashion until the complement graph does not have uncolored nodes.  Once all complement nodes have been colored in V’, the corresponding equivalent nodes should be colored in G.

### Example

We will go through an example of a graph with 5 nodes and 5 relationships:



This is a small graph.  We know that the chromatic number of this graph is 2 or greater because there is a 2-clique in the graph.  After a few attempts, one can easily see that we can color the graph with 2 colors.  The LDF algorithm achieves the optimal coloring for this example.

The first step is to generate the complement of this graph like so:



We start the coloring by finding the node with the most relationships.  Node 0 has 3 relationships, so that is the first node we color.  The first color we will use is red.



We then consider all neighbors of Node 0.  In the most basic design of the algorithm, neighboring nodes are considered without any particular order.  We will consider them in numerical order.  The first node we consider is Node 1.  Node 0 and Node 1 form a 2-clique, so we color Node 1 red.  When we consider Node 2, notice that Node 2 does not form a clique with the nodes that we have already colored red.  This means we drop Node 2.  Node 3 is the final node that we consider.  It forms a 3-clique with Node 0 and Node 1, so we color Node 3 red.  There are no more neighbors of 0, so we are done this round of coloring.



Once we finish coloring with red, we need to choose another node for the next round of coloring.  We no longer recognize colored nodes or relationships coming from colored nodes.  Though the complement graph is still shown, it is acceptable to remove the colored nodes and their relationships.  The edge between Node 0 and Node 2 is no longer recognized, so Node 2 and Node 4 both have one relationship.  We randomly choose one to start with, and color it green.  Then we see that they form a 2-clique, so we color the final node green as well.  Once all of the complement nodes are colored, we color their corresponding base graph nodes.



This is an example of a coloring using the LDF algorithm.

The algorithm is easy to describe on a high level: color nodes in the complement with the highest degree such that they form a clique with all other nodes of the current color.  

We will now outline the execution of the coloring using the components we have initialized.  The following algorithm is designed to be generalized in what will be considered non-technical terms: 

1. Remove any colored nodes and their relationships from V’

2. Find node v in V’ such that degree(v) is the highest degree in V’.  

3. Color v with unused color c

4. Find a node adjacent to of v, called u, such that u forms a clique with nodes colored c

5. Color u with c

6. Repeat 4 and 5 until no adjacent node forms a clique

7. Go back to step 1 until every node is colored.

This is a verbal explanation of the algorithm.  The next two sections will focus on technical implementation.

### Generate the complement

Assume graph G is already constructed with the following properties in each node: 

color – a 9-digit string representing the red, green, and blue component values of its color.  

endNodeList – a string of natural numbers representing the nodes to which it is related.  Numerical values are delimited by a “;”.  This will be treated as a set.

relationshipCount – a natural number that shows how many relationships this node has.  This property is also known as the degree of a node.  The relationshipCount can be calculated directly from the endNodeList.

When referencing these properties in nodes, we will treat them like functions.  For example, relationshipCount(Node 0) is the value of relationshipCount in Node 0.

Some other requirements for the algorithm are incremental node identification and knowledge of the number of nodes in the database.  For a base graph with N nodes, nodes must be uniquely identified with values 1-N, with no particular order.  This will be called the id of the node.  Knowing the number of nodes in the database is essential to constructing the endNodeList in the complementary nodes.  It is also important to note that the database will be constructed assuming the graph is the only thing living in it.  The application framework developed with this thesis relies on automatically generated node IDs’ within Neo4j, which will be explained in Chapter 3.4  

We must now programmatically construct H.  We will generate H in the same graph database as G.  When constructing the complement, the programmer must distinguish between base graph nodes and complementary nodes.  In the instance of this thesis, Neo4j stores its nodes with labels.  This allows nodes to be labelled as either “base” or “complementary,” and the database could be queried accordingly.  Unique relationship labels, if a database offers them, may offer convenience between a base and complementary node when traversing the graph in the future.  To begin, simply create a complementary node for each base node in G, and create a relationship between the nodes.  This relationship must be distinguishable compared to any other relationship coming from either node.  This is a constraint in the sense that a user must be able to label or distinguish relationship types.  If a graph database does not support this feature, base nodes should add a property that identifies its complementary node and vice-versa.  These nodes should be created with the same properties as the base node (unless the implementation relies on property names to distinguish between base and complementary nodes).  We have already constructed V’.



Once the vertices in H are created, we must form the relationships based on the content of G.  We will create these relationships by finding the complement of the endNodeList property in the base node.  The complement of the endNodeList will be defined as the string of all natural numbers less than or equal to N where any given number is not the base node id or is not contained in the base endNodeList.  For each complementary node, its endNodeList will be the complement of its base node’s endNodeList.  The relationshipCount property can be calculated by N – relationshipCount(base node).  The color should start as “255255255,” representing white, or uncolored.  Note that these properties can be set when the complementary node is created.  There is no need for two passes of the nodes up to this point, but all nodes must be created before any relationships can be created.



With V’ fully initialized, one must simply loop through each node and create relationships based on every complementary node’s endNodeList.  This will create the full complement of any graph with the given properties.



### Implementation

Before beginning implementation of this algorithm, it is important to recognize a few concepts involved in the programming.  First, note the convenience of generating H.  In G, nodes without a common edge must traverse the graph to “find” each other.  The advantage of using H to perform the coloring is that the programmer gets this connection, or lack thereof, without any traversal.  Though it is possible to do this coloring relying on strings and retrieval by node ID, this method offers the advantage of programmatically retrieving neighbors without having to run additional queries.  That is, the database can return a set of neighbors in a single result rather than separately retrieving neighbors by their unique IDs’.

Generating H in the same space as G is useful because it gives the programmer and algorithm easy access from complementary node to base node.  Again, this could be accomplished with a number of other methods, but a simple relationship offers elegant convenience.

A third concept, more related to the coloring algorithm itself, is the coloring string.  The coloring string is a string that will be treated as a set in this writing.  It represents all the nodes that have color Ki when i is less than or equal to k and greater than 0.  When coloring this graph, each additional node to be colored Ki must form a complete graph with all nodes in the coloring string.  It is crucial to understand that a complete graph in H implies an edge graph in G, which implies satisfactory coloring.  In this particular algorithm, the programmer need only keep track of the ith coloring string until no node can be added as part of the given clique.  Any node coloring is final in this best effort approximation algorithm.

We will now dive into a more specific implementation in pseudo code.  We will still use the coloring string in the same way.  The algorithm will be generalized to work with any language.  It is recommended that the programmer query the graph database for every node in V’ and store the resulting set.  This set will be referred to as V’’, and it will be treated as an array of nodes.  This will allow the user to remove nodes from V’’ without actually removing them from the database.  Some API’s may not be able to store an instance of a node class in memory.  If this is the case, a set of the node IDs or string identities could be stored as a substitute.  It is also important to note that the property endNodeList will be treated as function is this pseudo code snippet.  This simply means the endNodeList property of the given node.



Input: Set of vertices V’’ that is a representation in memory of V’.  

Output: The algorithm will color the nodes in the graph database using node representations from V’’.

LDF(Set V’’) 

int k = 0

while (|V’’| > 0)

String coloringString = NULL

sort (V’’ )5

Node current = V’’[0]

for each (Node neighbor in endNodeList(current))

if (endNodeList(neighbor) !contain every element in coloringString)

skip neighbor

else

color neighbor with k

add neighbor to coloringString

remove neighbor from V’’

	remove current from V’’

	k = k + 1



Basic operation: Best effort approximation algorithm for graph coloring.

Analysis:  The LDF algorithm executes in reasonable polynomial time complexity.  The most basic form of the algorithm is O(n*e’).  Here, n is the number of nodes and e’ is the average number of edges per node in the complement.  This is because the algorithm must check every node and check every complementary neighbor of the node.  Another way to write this is with respect to the average number of relationships per node in the base, called e.  This is O(n*(n-1-e)).  We can write this because for graphs with nodes that do not relate to themselves, e + e’ = n – 1.  Hence, e’ = n – 1 – e.

It is important to note that there is another step in the algorithm.  Recall that for every node that we attempt to color, we must check if it is adjacent to every node in the coloringString, as defined previously.  This mini-algorithm inside of LDF is O(c), where c is the length of the coloringString.  The coloringString changes depending on each step of the algorithm.  So we define its time complexity separately but recognize that it is a part of LDF.

### Additional Functionality

As an approximation algorithm, LDF does not guarantee an optimal coloring.  There is one option that may offer better results for some graphs.  In its most basic form, the algorithm randomly selects adjacent nodes.  This can be seen in step 4 of the algorithm outline.  By randomly selecting the first neighbor node, we may or may not be coloring the most nodes possible with that color.  While the first neighbor we choose will always form a clique with the original node, it may be a choice that affects the rest of the coloring negatively.  The algorithm builds on the assumption that coloring nodes with more relationships first will ultimately lead to a better coloring.  If we sort the list of adjacent nodes by their degree, we can choose the neighbor that will most likely form a bigger clique.   Adding this extra step will make the algorithm slightly slower, but it does not change the time complexity in terms of n and e’.  It remains O(n*e’) because we are sorting once at the start of each round of coloring, as oppose to resorting at every node we consider.  This will improve the coloring for some graphs.  Essentially, a graph coloring that works well with the original heuristic will generally benefit from sorting the adjacent nodes.


## Application


This chapter will discuss the design of the graph coloring program.  The language of Chapter 3 requires an understanding of Java, polymorphism, inheritance, and Maven.  First, we will explore Neo4j.  Neo4j is a graph database.  It can be used to create and manipulate representations of graphs on a machine that has Java.  According to neo4j.com, some use cases of Neo4j include Network and IT Management, Fraud Detection, and Social Networking.  This thesis uses the Neo4j API to manage nodes and relationships.  The next section will explore the design of the user interface of the program.  This user interface (UI) was developed with the Swing package in Java.  The third section describes the visual representation of graphs.  This portion of the project was created with the free graphics library, Processing.  Section four explains the object oriented design of the framework.  We will explore the idea of creating different graphs and different algorithms as objects.  The final section of this chapter delves into project expansion, as well as the tools used to allow future developers to add to the framework.

### Neo4j

The graph coloring application uses Neo4j as a backend graph database.  Neo4j has an extensive API with documentation and support.  It handles node and relationship management well because it provides almost any methods necessary for using a graph database.  One useful component for this particular project was Neo4j’s node auto-numbering feature.  Every node is assigned an ID that corresponds to the order it was inserted.  This is useful for the LDF algorithm, as seen above.  

Neo4j offers a few different ways to use the database.  This program uses the “embedded” graph database.  An embedded graph database lives and dies with the application that creates it.  There are a few advantages of the embedded database.  This use of Neo4j does not require any configuration files.   Likewise, there is virtually no security risk.  The embedded database is also slightly faster with certain transactions.  However, it does not continue to run once the application has terminated.  This means the embedded database is essentially a “one and done” use of the database.  There is no easy way to connect to it once the application shuts down.  Furthermore, the embedded graph database must be shut down once and only once within the application.  This introduces a problem for the application.

The problem with the embedded graph database stems from the dependency on the auto numbering feature.  The software assumes that the first node will have the ID 1.  If it does not, forming the complement will fall apart.  Meanwhile, there is no way to restart the auto numbering that Neo4j offers.  Even if a programmer deletes all of the nodes in the database, the auto-numbering will resume where the last added node left off.  So, when the user wants to run two tests, he must run the application two times.  This can be a bit cumbersome and is virtually unusable for large scale testing.  We can solve this issue by wrapping the graph coloring application in another application.



### User interface 

The user interface for this project is designed to be simple enough to use without a guide and thorough enough to perform advanced testing.  With the following screen capture as a reference, this section will outline the functions of each area of the user interface.



1. At the top of the interface, there is a text area for the Neo4j graph database location.  By default, this location is the directory of the executed jar file.  The program will create a folder in this directory for all of the Neo4j files and output files.

2. The check boxes are “universal” Boolean values for graph coloring instances.  Tests can be run with the following options:

autosolve – run the selected coloring algorithm without user interaction

autoshutdown – close the window and shutdown the database when coloring is complete

background  - choose to run the graph coloring in the background, with no display6

The main function of these checkboxes is to allow testing with flexibility functionality.  The file output box is the name of the file that will be created for output.  By default, the framework writes all configuration parameters and values to a text and csv file.  The test increment box allows the user to run multiple, consecutive instances of the program without having to start each one individually.  

3. Each graph configuration can have different parameters for the user to adjust.  These parameters will populate the center of the user interface.  Configuring these parameters will be covered in the “Annotations” header of the next section.  

4. As mentioned in section A, there are two dropdown menus at the bottom of the user interface.  The menu on the left contains the subclasses of BaseGraphConfiguration.java, and the menu on the right contains subclasses of GraphColoror.java.  By default, the Largest Degree First algorithm is the only option for the latter. 

5. There is an output console that displays coloring information or errors that the framework detects.  

6. The “Start” and “Clear” buttons perform their appropriate functions of the coloring instance based on the configuration options.  The “Clear” button will become a “Cancel” button when a coloring is running.  These are the main components of the user interface.  Once a coloring is instantiated, an additional window is created for the graph.

### Visuals

The visual representation of a graph in this project uses Processing, a free graphics library.  This library is relatively straightforward and easy to use.  The Main.java class handles all of the Processing API calls, while other classes pass the necessary data to the class to display.  GraphDrawer.java handles the representation of nodes and edges in the problem.  Nodes are simply drawn as a circle, and edges are drawn as a line.  The colors of the nodes are properties in the database, but GraphDrawer.java interprets their values and displays the appropriate color.  If the base configuration sets a name property for the nodes, it will be displayed next to the node.  As explained above, the GraphColoror.java class maintains the distributions of the colors.  The drawer primarily handles their location and size on the screen.  This class also allows the user to view either the base or complement graph.  Because the nodes of each graph are mathematically equivalent, it only stores one set of nodes in memory.  However, the relationships are different within the base and complement, so this class maintains a mapping of each set, V and the V’.  The graph illustration window has a red rectangle around it.  This symbolizes a graph that is not completely or correctly colored.  It will turn green once every node is colored and consistent with the conditions of the problem.

### Object oriented framework 

This project is a framework for problem creation and solution.  There are a few key components to the framework that drive the program.  The two most important classes are the BaseGraphConfiguration.java and GraphColoror.java classes.  The Base Graph Configuration is the setup of the original graph.  It is an abstract class with a few variables and methods already defined.  A developer can extend this class to include new graph setups when the program runs.  This is useful because it allows users to test the LDF algorithm with different types of graphs.  The methods in BaseGraphConfiguration.java allow a derived class to create nodes and relationships.  These inherited methods work smoothly with the existing framework.  On the other hand, there are a few abstract methods in BaseConfiguration.java that must be implemented in its child classes.  These methods are generateNodes() and generateRels().  Any base configuration must set up vertices before it sets up relationships.  For this reason, the classes that drive the program will call generateNodes() first and generateRels() second.  The programmer uses the methods provided by the parent class, called createGraphColoringNode() and createGraphColoringRelationship(), to ensure the graph is created appropriately.  As seen in Chapter 2, there are some node properties and labels that are required for the algorithm.  Using these methods allows the framework to run smoothly.  It will create the complement graph automatically and keep track of how many nodes are created.  

The second important class is the GraphColoror.java class.  This class handles the coloring of the given base graph.  This thesis focuses on the LDF algorithm, but there are many approximation algorithms for graph coloring problems.  The program recognizes the possibility of future improvements for graph coloring, so this class is designed to be expanded with that in mind.  The only method that a subclass must define is the colorNext() method.  This is the method that the framework calls to perform the graph coloring.  This class provides convenience methods for the coloring process.  The currentColor() method returns the string of the color that is currently used.  This was designed for the LDF algorithm, which only uses each color once.  The nextColor() method was designed for the LDF algorithm as well.  Once this method is called, the current color will increment, and the value returned by currentColor() will change.  The class uses Java’s javax package colors, and it can produce about 30 noticeably different colors.  Each color has been automatically overridden to return a unique name with the toString() method.

### Project Extension

This section explores the ability to add extensions to the program.  Understanding this process requires a basic knowledge of three concepts: reflection, Maven, and annotations.  We will first discuss these three topics and put everything together at the end of the section.  The “Reflection” subsection is important to understanding why this is a framework and not just an application.  The Reflections API is a complicated but incredibly powerful tool that plays a crucial role in project extension.  In Subsection 2, I will explain how to extend the project using Maven.  Java users will recognize that there are alternatives to using Maven, namely, adding the appropriate jar file to the build path.  However, that subsection acts as an explanation of my use of Maven as well as the hypothetical extension of a large scale software project.  Part 3 of this section outlines the use of Java Annotations in my project.  Finally, Subsection 4, “Running the Executable” describes how the whole project comes together into a single, executable file.

#### Reflection

The Java Reflections API proved very useful for this thesis.  The Reflections API is a tool that scans the classpath of a Java application at runtime.  It analyzes the manifest file and can programmatically determine superclasses, subclasses, annotations, variable names, and method names.  Determining this information at runtime is very useful for instantiating classes of which the programmer does not know the name.  

This thesis used the Reflections API to find all subclasses of the BaseGraphConfiguration.java and GraphColoror.java classes.  It is easy to do this within a single, local project.  However, it took some more work to achieve this for an expandable project that is actually composed of two jar files.  Using the URIClassLoaders class, the framework actually fetches the class loaders of each jar file, and programmatically adds those class loaders to the configuration of the Reflections API.  These class loaders allow the running jar file to have access to the constructors of the respective classes.  Without them, the framework would be able to detect the name of the subclasses, but it would not know how to instantiate them.  The UI then presents these classes in a dropdown menu, and the user can select which class to use.  Once the user selects the desired classes, the program can instantiate the appropriate classes based solely on their names.  There are a wide variety of uses the Reflections API can offer.

#### Maven

Another tool that makes project extension possible is Apache Maven.  In a development environment like Eclipse, a user can simply add a jar file to the build path of a project.  This is useful for very small projects, but it becomes very difficult to manage working with multiple versions of software or a development team.  For this reason, this thesis used Maven to manage dependencies.  The software made acquiring external jar files easy and manageable.   The easiest way to add a dependency is to include the appropriate tag in Maven’s pom.xml file.  This tag is essentially an identifier that Maven can use to find the appropriate jar file.  With respect to the program’s convenience, this section will explain how to extend the project using Maven rather than simply adding the jar to the build path. 

 A user should have Maven installed on his or her computer with an environmental variable that can run the software.  We will use mvn in accordance with Apache’s tutorials.  It is also necessary to have a copy of the jar file main-file.jar.  Furthermore, the potential future developer should be developing in a Maven project.  The first step is to add the existing graph coloring jar file, main-file.jar, to the local repository.7  Execute the following command to add the jar file to the local repository with the given groupId, artifactId, and version:



If a user observed the local repository, he or she would be able to find a copy of the jar file following the relative path .m2\repository\graph-coloring\framework\1.0.  The next step is to add the dependency to the pom.xml file.  Assuming one has executed the command above, the tag that he or she adds between the <dependencies> tags is:

 

This will include the graph coloring framework in the Maven project.  An IDE like Eclipse will now recognize dependencies and contracts embedded in the framework.  If a user extends the BaseConfiguration or GraphColoror class, the extension will appear on the user interface of the graph coloring program.  Note that it is also possible to add external compiled jar files to the pom.xml file using the same method.  The reflections API will still detect these files, and add them as viable run options.  This allows multiple extensions of the software to run under the same framework.  

#### Annotations

Annotations proved useful for this thesis, particularly as a part of the user interface.  The annotation used in this program was the @RunInformation annotation.  With an understanding of Reflection and Maven, the purpose of this annotation becomes clearer.  When the Reflections API scans the classpath, it finds all subclasses of BaseGraphConfiguration.java.  It returns these subclasses as strings with their full package description.  Consider the class CycleConfiguration.java in the package “thesis.”  The Reflections API would display “thesis.CycleConfiguration.”  Likewise, a class from another package included with Maven will have additional text before the class name.  This is not a very user friendly format.  

The @RunInformation annotation allows a programmer to define a name that will be displayed as part of the interface’s dropdown menu.  The other component of this annotation is a string array called properties.  This represents the parameters that a base graph configuration uses.  When a user selects a particular base configuration from the drop down menu, the program will retrieve the class’ annotations.  It will then display the appropriate text areas in order on the interface.  The text areas will be labeled according to the value of their index in the array, and one can set a default value by including “=*someValue*” after the string.  Consider the following annotation:



This annotation will show “Cycle Configuration” in the drop down menu and display two user adjustable text areas.  The “displayNodeIds” text area will be created with “true” in it like so:



#### Running the executable

The application has a few files in its directory.  There are two jar files, a DOS script, a text file, and two folders if the user has run tests.  As mentioned above, the software runs as multiple, separate instances of a java application wrapped in another java application.  The complicated nature of this design is simplified by a single DOS script, or batch file.  The file is only one line by default:



Java users will recognize immediately that the run.jar file is the executable, while the main-file.jar is a parameter.  This parameter is the path to the child java application, i.e. the one that is wrapped inside run.jar.  Once a user has added the desired base configurations or coloring methods, he or she can compile his code and substitute the new jar file into this parameter.  A future developer need not change anything other than this “main-file.jar” parameter.  It is easiest to add the jar to the same directory as run.jar, but an absolute path will work as well.
