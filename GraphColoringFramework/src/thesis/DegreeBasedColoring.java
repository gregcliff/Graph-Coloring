package thesis;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;

@RunInformation(name = "Largest Degree First", properties = { "" })
public class DegreeBasedColoring extends GraphColoror {
	GraphDatabaseService graphDb;
	ComplementGenerator complement;
	BaseGenerator base;
	ArrayList<SortNode> nodes;
	ArrayList<Relationship> currentNeighborRels;
	Node next;
	Node current;
	//ArrayList<Relationship> tryRels;
	int currentSortNodeIndex = 0;
	Relationship currentRel;
	int colored;
	boolean firstNode = true;

	public DegreeBasedColoring(ComplementGenerator dual) {
		this.complement = dual;
		nodes = new ArrayList<SortNode>();
		currentColoring = new HashSet<Long>();
		currentNeighborRels = new ArrayList<Relationship>();
		//tryRels = new ArrayList<Relationship>();
		scanForFirstNode();
	}

	public Node scanForFirstNode() {
		Iterator<Node> it = complement.getAllNodes(complement.getNodeLabel());
		int maxRels = 0;
		Node first = null;
		Node node;
		while(it.hasNext()) {
			node = it.next();
			String endNodeList = node.getProperty("endNodeList").toString();
			int degree = 0;
			for(int i = 0; i < endNodeList.length(); i++) {
				if(endNodeList.charAt(i)==';') degree++;
			}
			if(degree >= maxRels) {
				maxRels = degree;
				first = node;
			}
			nodes.add(new SortNode(node));
		}
		//Collections.sort(nodes);
		colored = 0;
		return first;
	}

	public boolean setNextNode(boolean setTry) {
		if(nodes.size() == 0) return false;
		boolean foundNext = false;
		/*
		Node node = nodes.get(colored);
		next = node;
		 */
		//if all currentNeighbors have been colored
		//sort, and
		//get 0 from nodes
		//otherwise
		//get an uncolored neighbor
		//make sure it connects to other colored neighbors
		if(currentNeighborRels.size() > 0) { 
			Node endNode;
			int maxDegree = 0;
			Iterator<Relationship> rels = currentNeighborRels.iterator();
			while(rels.hasNext()) {
				Relationship rel = rels.next();
				int coloringMatch = 0;
				if(rel.hasProperty("dead")) {
					rels.remove();
					continue;
				}
				endNode = rel.getOtherNode(current);
				String otherColor = endNode.getProperty("color").toString();
				if(!otherColor.equals("255255255")) {
					/*if(setTry && !otherColor.equals(currentColor())) {
						tryRels.add(rel);
					}*/
					rels.remove();
					continue;
				}
				int degree = 0; //should be handling the degree in the sort, not here
				Iterable<Relationship> otherRels = endNode.getRelationships(Generator.Relationships.COMP_REL, Direction.OUTGOING);
				for(Relationship otherRel : otherRels) {
					if(!otherRel.hasProperty("dead")) degree++;
					if(currentColoring.contains(otherRel.getOtherNode(endNode).getId())) coloringMatch++;
				}
				if(coloringMatch == currentColoring.size()) {
					if(degree > maxDegree) {
						maxDegree = degree;
						currentRel = rel;
						next = endNode;
						foundNext = true;
					} 
				} else {
					rels.remove();
				}
			}
			if(foundNext) return true;
			else return false;
		} else {
			currentColoring.clear();
			//tryRels.clear();
			try {
				Collections.sort(nodes);
			} catch(IllegalArgumentException e) {
				Main.getConsoleStream().println("Exception caught while sorting nodes.  Attempting to sort again.");
				try {
					Collections.sort(nodes);
				} catch(IllegalArgumentException e2) {
					Main.getConsoleStream().println("Sorting failed.  Defaulting to first node in list.");
				}
			}
			//figure out why this bug is happening and randomize first node selection
			if(firstNode) { current = nodes.get(nodes.size() - 1).node; firstNode = false; }
			else
			current = nodes.get(0).node;
			colorCurrent();
			Iterable<Relationship> rels = current.getRelationships(Generator.Relationships.COMP_REL);
			for(Relationship rel : rels) {
				currentNeighborRels.add(rel);
			}
			return false;
			//setNextNode();
		}
	}

	public boolean useCurrentColor() {
		Iterable<Relationship> rels = current.getRelationships(Generator.Relationships.COMP_REL);
		Node endNode;
		for(Relationship rel : rels) {
			endNode = rel.getOtherNode(current);
			if(currentColor().equals(endNode.getProperty("color").toString())) {
				return false;
			}
		}
		return true;
	}

	public void colorCurrent() {
		//start of a new color cluster
		String color = nextColor();
		current.setProperty("color", color);
		incrementColorCount(color);
		sync(current);
		currentColoring.add(current.getId());
		nodes.remove(getSortNodeByNode(current));
		colored++;
	}
	
	/*public ArrayList<Relationship> getTryNodes() {
		return tryRels;
	}*/

	public SortNode getSortNodeByNode(Node node) {
		for(int i = 0; i < nodes.size(); i++) {
			if(nodes.get(i).equals(node)) return nodes.get(i);
		}
		return null;
	}

	@Override
	public void colorNext() {
		if(!setNextNode(true)) return;
		String color = currentColor();
		next.setProperty("color", color);
		incrementColorCount(color);
		currentRel.setProperty("dead", currentColor());
		decrementRelationshipCount(currentRel.getStartNode());
		decrementRelationshipCount(currentRel.getEndNode());
		currentNeighborRels.remove(currentRel);
		sync(next);
		nodes.remove(getSortNodeByNode(next));
		currentColoring.add(next.getId());
		colored++;
	}
	
	public void colorNext(boolean setTry) {
		if(!setNextNode(setTry)) return;
		String color = currentColor();
		next.setProperty("color", color);
		incrementColorCount(color);
		currentRel.setProperty("dead", currentColor());
		decrementRelationshipCount(currentRel.getStartNode());
		decrementRelationshipCount(currentRel.getEndNode());
		currentNeighborRels.remove(currentRel);
		sync(next);
		nodes.remove(getSortNodeByNode(next));
		currentColoring.add(next.getId());
		colored++;
	}

	public void sync(Node dualNode) {
		Node baseNode = dualNode.getRelationships(Generator.Relationships.COMP).iterator().next().getOtherNode(dualNode);
		baseNode.setProperty("color", dualNode.getProperty("color"));
	}

	public void decrementRelationshipCount(Node node) {
		int count = Integer.parseInt(node.getProperty("relationshipCount").toString());
		node.setProperty("relationshipCount", count-1);
	}

	private class SortNode implements Comparable<SortNode> {
		public Node node;

		public SortNode(Node node) {
			this.node = node;
		}

		@Override
		public boolean equals(Object obj) {
			if((obj instanceof SortNode && this.node.equals(((SortNode)obj).node)
					|| obj instanceof Node && this.node.equals((Node)obj))) {
				return true;
			} else {
				return false;
			}
		}

		public int compareTo(SortNode otherNode) {
			int thisCount = Integer.parseInt(this.node.getProperty("relationshipCount").toString());
			int otherCount = Integer.parseInt(otherNode.node.getProperty("relationshipCount").toString());
			if(thisCount > otherCount) {
				return 1;
			} else if (thisCount < otherCount) {
				return -1;
			} else {
				try {
				if(random != null) {
					int yea = (int)((random.nextLong() * System.currentTimeMillis()) % 2);
					if(yea == 0) return -1;
					else return 1;
				} else
					return ((int)(Math.random() * System.currentTimeMillis()) % 2) - 1;
				} catch (Exception e) {
					System.out.println("Contract violation while sorting nodes.  Nodes will still be sorted, but results "
							+ "will not be \'completely\' random.");
					return 0;
				}
			}
		}
	}
}
