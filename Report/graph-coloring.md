# An Exploration of Graph Coloring and Intractability

“The limits of the possible can only be defined by going beyond them into the impossible.”
-Arthur C. Clarke

# Table of Contents

1. Introduction

2. Algorithm - Largest Degree First

  A. Graph Setup
  
  B. Example
  
  C. Generate the complement
  
  D. Implementation
  
  E. Additional Functionality
  
3. Application

  A. Neo4j
  
  B. User Interface
  
  C. Visuals
  
  D. Object Oriented Framework
  
  E. Project Extension
  
4. Conclusion

  A. Testing and Future Work
  
  B. Final Analysis
  
5. Appendices

  A. Works Cited
  
  B. Packages
  
  C. Other
  
## Introduction

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

## Algorithm – Largest Degree First

The Largest Degree First algorithm is a best effort approximation algorithm.  With the potentially intractable problem of finding the chromatic number, we accept the idea that we currently cannot come up with an answer in polynomial time.  Keeping this in mind, we construct an approximation algorithm.  This concept takes correctness, time complexity, and optimality into consideration.  An approximation algorithm acknowledges that we cannot guarantee a best solution, so it tries to get a good one.  In this case, we sacrifice optimality for the sake of time complexity.  However, it is important that we never break the conditions of the problem.  Our goal is to generate a correct, but not necessarily optimal, coloring in polynomial time.

### A) Graph Setup

As is convention, the set of colors, K will be identified numerically by values {1, 2, …, k} with k being the number of unique colors used in the algorithm.  Note that K is the set of colors, and k is the chromatic number.  The rest of this section will explain the setup and implementation of the LDF algorithm.

Let graph G be a graph with a set of vertices V and a set of edges E.  This can be expressed by G = (V, E) and will be referred to as the base graph.  We will generate the complement of G, called H.  Note: H = (V’, E’).  Vertices in H are mathematically equal to vertices in G.  A relationship exists in H if and only if there was not a relationship between two nodes in G.

We will use the properties of H to perform the coloring on G.  The algorithm only uses nodes and edges from V’ and E’.  First, we will find the node that has the most relationships.2  We will color this node “1.”3  Then, consider all of the node’s neighbors.  The goal is to color as many of these neighbors as possible with “1.” Note that all “1” nodes must form a clique.  This is because a clique in H implies a lack of connection in G.  If a node will not form a clique, it cannot be colored with “1.”  Once all neighbors of the original node have been considered, we can remove all nodes and respective edges with color “1” from G’.  Then, repeat the same steps with the next color using the set of all uncolored nodes.  Continue coloring nodes in this fashion until the complement graph does not have uncolored nodes.  Once all complement nodes have been colored in V’, the corresponding equivalent nodes should be colored in G.

### B) Example

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

### C) Generate the complement

Assume graph G is already constructed with the following properties in each node: 

color – a 9-digit string representing the red, green, and blue component values of its color.  

endNodeList – a string of natural numbers representing the nodes to which it is related.  Numerical values are delimited by a “;”.  This will be treated as a set.

relationshipCount – a natural number that shows how many relationships this node has.  This property is also known as the degree of a node.  The relationshipCount can be calculated directly from the endNodeList.

When referencing these properties in nodes, we will treat them like functions.  For example, relationshipCount(Node 0) is the value of relationshipCount in Node 0.

Some other requirements for the algorithm are incremental node identification and knowledge of the number of nodes in the database.  For a base graph with N nodes, nodes must be uniquely identified with values 1-N, with no particular order.  This will be called the id of the node.  Knowing the number of nodes in the database is essential to constructing the endNodeList in the complementary nodes.  It is also important to note that the database will be constructed assuming the graph is the only thing living in it.  The application framework developed with this thesis relies on automatically generated node IDs’ within Neo4j, which will be explained in Chapter 3.4  

We must now programmatically construct H.  We will generate H in the same graph database as G.  When constructing the complement, the programmer must distinguish between base graph nodes and complementary nodes.  In the instance of this thesis, Neo4j stores its nodes with labels.  This allows nodes to be labelled as either “base” or “complementary,” and the database could be queried accordingly.  Unique relationship labels, if a database offers them, may offer convenience between a base and complementary node when traversing the graph in the future.  To begin, simply create a complementary node for each base node in G, and create a relationship between the nodes.  This relationship must be distinguishable compared to any other relationship coming from either node.  This is a constraint in the sense that a user must be able to label or distinguish relationship types.  If a graph database does not support this feature, base nodes should add a property that identifies its complementary node and vice-versa.  These nodes should be created with the same properties as the base node (unless the implementation relies on property names to distinguish between base and complementary nodes).  We have already constructed V’.



Once the vertices in H are created, we must form the relationships based on the content of G.  We will create these relationships by finding the complement of the endNodeList property in the base node.  The complement of the endNodeList will be defined as the string of all natural numbers less than or equal to N where any given number is not the base node id or is not contained in the base endNodeList.  For each complementary node, its endNodeList will be the complement of its base node’s endNodeList.  The relationshipCount property can be calculated by N – relationshipCount(base node).  The color should start as “255255255,” representing white, or uncolored.  Note that these properties can be set when the complementary node is created.  There is no need for two passes of the nodes up to this point, but all nodes must be created before any relationships can be created.



With V’ fully initialized, one must simply loop through each node and create relationships based on every complementary node’s endNodeList.  This will create the full complement of any graph with the given properties.



### D) Algorithm implementation

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

### E) Additional Functionality

As an approximation algorithm, LDF does not guarantee an optimal coloring.  There is one option that may offer better results for some graphs.  In its most basic form, the algorithm randomly selects adjacent nodes.  This can be seen in step 4 of the algorithm outline.  By randomly selecting the first neighbor node, we may or may not be coloring the most nodes possible with that color.  While the first neighbor we choose will always form a clique with the original node, it may be a choice that affects the rest of the coloring negatively.  The algorithm builds on the assumption that coloring nodes with more relationships first will ultimately lead to a better coloring.  If we sort the list of adjacent nodes by their degree, we can choose the neighbor that will most likely form a bigger clique.   Adding this extra step will make the algorithm slightly slower, but it does not change the time complexity in terms of n and e’.  It remains O(n*e’) because we are sorting once at the start of each round of coloring, as oppose to resorting at every node we consider.  This will improve the coloring for some graphs.  Essentially, a graph coloring that works well with the original heuristic will generally benefit from sorting the adjacent nodes.


