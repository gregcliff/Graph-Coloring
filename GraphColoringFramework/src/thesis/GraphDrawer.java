package thesis;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;


public class GraphDrawer {
	//class used only for drawing the nodes on the screen
	Generator g;
	Map<Long, DrawNode> drawNodes;
	Map<Long, DrawNode> drawComplementNodes;
	ArrayList<DrawRel> complementRels;
	ArrayList<DrawRel> rels;
	Map<Long, DrawNode> currentColoringNodes;
	ArrayList<DrawRel> currentColoringRels;
	Map<Long, DrawNode> tryNodes;
	ArrayList<DrawRel> tryRels;
	Main m;
	DrawNode selectedNode = null;
	boolean baseGenerated = false;

	public GraphDrawer(Main m) {
		//looots of memory to allocate in this class
		drawNodes = new HashMap<Long, DrawNode>();
		drawComplementNodes = new HashMap<Long, DrawNode>();
		rels = new ArrayList<DrawRel>();
		complementRels = new ArrayList<DrawRel>();
		currentColoringNodes = new HashMap<Long, DrawNode>();
		currentColoringRels = new ArrayList<DrawRel>();
		tryNodes = new HashMap<Long, DrawNode>();
		tryRels = new ArrayList<DrawRel>();
		this.m = m;
	}

	public void populateNodeList(Generator g) {
		// set up draw nodes in memory
		ResourceIterator<Node> iterator = g.getAllNodes(g.getNodeLabel());
		while(iterator.hasNext()) {
			Node node = iterator.next();
			Node otherNode;
			DrawNode draw;
			if(g instanceof ComplementGenerator && baseGenerated) { 
				otherNode = node.getRelationships(Generator.Relationships.COMP)
						.iterator().next().getOtherNode(node);
				draw = drawNodes.get(otherNode.getId());
				draw.node = node;
				drawComplementNodes.put(node.getId(), draw);
			} else if(g instanceof BaseGenerator && !baseGenerated) {
				draw = new DrawNode(node);
				drawNodes.put(node.getId(), draw);
			}
		}
	}
	
	public void updateNodeLayout(Map<Long, float[]> nodeCoordinates) {
		Iterable<Long> ids = nodeCoordinates.keySet();
		for(Long id : ids) {
			DrawNode node = drawNodes.get(id);
			node.x = nodeCoordinates.get(id)[0];
			node.y = nodeCoordinates.get(id)[1];
		}
	}

	/*public void updateDebugList(Generator g, GraphColoror colorer) {
		// includes nodes that were accessed but not colored
		currentColoringNodes.clear();
		currentColoringRels.clear();
		tryRels.clear();
		tryNodes.clear();
		Iterator<Long> coloredNodes = colorer.currentColoring.iterator();
		Node node;
		DrawNode drawNode;
		while(coloredNodes.hasNext()) {
			Long id = coloredNodes.next();
			if((drawNode = drawComplementNodes.get(id)) == null) {
				node = g.graphDb.getNodeById(id);
				drawNode = new DrawNode(node);
			} 
			currentColoringNodes.put(id, drawNode);
		}
		for(Long id : currentColoringNodes.keySet()) {
			ArrayList<DrawRel> rels = getDrawRelBasedOnDrawNode(currentColoringNodes.get(id));
			for(DrawRel rel : rels) {
				if(currentColoringNodes.containsKey(rel.node1.node.getId()) && 
						currentColoringNodes.containsKey(rel.node2.node.getId()))
				currentColoringRels.add(rel);
			}
		}
		if(colorer instanceof DegreeBasedColoring) {
			DegreeBasedColoring dColorer = (DegreeBasedColoring)colorer;
			DrawNode tryDrawNode;
			for(Relationship tryRel : dColorer.tryRels) {
				Long[] id = getOrderedTryNodeIds(tryRel);
				if((tryDrawNode = drawComplementNodes.get(id[1])) == null) {
					System.out.println(id[1]);
					Node tryNode = g.graphDb.getNodeById(id[1]);
					drawNode = new DrawNode(tryNode);
				}
				tryRels.add(new DrawRel(currentColoringNodes.get(id[0]), tryDrawNode));
				tryNodes.put(id[1], tryDrawNode);
			}
		}
	}*/
	
	public Long[] getOrderedTryNodeIds(Relationship rel) {
		// return ids of relationship nodes
		Long[] id = new Long[2];
		Node[] nodes = rel.getNodes();
		for(Long i : currentColoringNodes.keySet()) {
			if(currentColoringNodes.get(i).node.equals(nodes[0])) {
				id[0] = i;
				id[1] = nodes[1].getId();
				break;
			}
			else if(currentColoringNodes.get(i).node.equals(nodes[1])) {
				id[1] = i;
				id[0] = nodes[1].getId();
				break;
			} 
		}
		return id;
	}

	public void populateRelList(Generator g) {
		// put draw relationships in memory
		if(g instanceof BaseGenerator) {
			for(Long id : drawNodes.keySet()) {
				DrawNode node = drawNodes.get(id);
				for(Relationship rel : node.node.getRelationships(Direction.OUTGOING, g.getRelationshipLabel())) {
					Node[] relNodes = rel.getNodes();
					DrawRel draw = new DrawRel(drawNodes.get(relNodes[0].getId()), drawNodes.get(relNodes[1].getId()));
					if(!rels.contains(draw)) {
						rels.add(draw);
					}
				}
			}
		} else if(g instanceof ComplementGenerator) {
			for(Long id : drawComplementNodes.keySet()) {
				DrawNode node = drawComplementNodes.get(id);
				for(Relationship rel : node.node.getRelationships()) {
					if(drawNodes.containsKey(rel.getStartNode().getId())) continue;
					Node[] relNodes = rel.getNodes();
					DrawRel draw = new DrawRel(drawComplementNodes.get(relNodes[0].getId()), drawComplementNodes.get(relNodes[1].getId()));
					if(!complementRels.contains(draw)) {
						complementRels.add(draw);
					} 
				}
			}
		}
	}
	
	public ArrayList<DrawRel> getDrawRelBasedOnDrawNode(DrawNode node) {
		//get the relationship based on its node (could be either start or end node)
		ArrayList<DrawRel> rels = new ArrayList<DrawRel>();
		for(DrawRel testRel : complementRels) {
			if(testRel.node1.node == node.node || testRel.node2.node == node.node) rels.add(testRel);
		}
		return rels;
	}

	public void setSelectedNode(DrawNode node) {
		//node that the user has clicked on
		if(selectedNode == null || node == null)
			selectedNode = node;		
	}

	public void init(Generator g) {
		setGenerator(g);
		populateNodeList(g);
		populateRelList(g);
		update();
		if(g instanceof BaseGenerator) baseGenerated = true;
	}

	public void nuke() {//probably should clear everything
		rels.clear();
		drawNodes.clear();
	}

	public void update() {
		// update node based on database
		for(Long id : drawNodes.keySet()) {
			drawNodes.get(id).updateColor();
		}
		for(Long id : drawComplementNodes.keySet()) {
			drawComplementNodes.get(id).updateColor();
		}
	}

	public void setGenerator(Generator g) {
		this.g = g;
		currentColoringNodes.clear();
		currentColoringRels.clear();
		tryNodes.clear();
		tryRels.clear();
	}

	public Map<Long, DrawNode> getNodeMap() {
		if(g instanceof BaseGenerator) {
			return drawNodes;
		} else if(g instanceof ComplementGenerator) {
			return drawComplementNodes;
		}
		return null;
	}

	public ArrayList<DrawRel> getRelMap() {
		if(g instanceof BaseGenerator) {
			return rels;
		} else if(g instanceof ComplementGenerator) {
			return complementRels;
		}
		return null;
	}

	public void randomize() {
		// give random drawing coordinates to each node
		if(g instanceof BaseGenerator) {
			for(Long id : drawNodes.keySet()) {
				DrawNode node = drawNodes.get(id);
				node.x = (int)(Math.random()*m.width);
				node.y = (int)(Math.random()*m.height);
			}
		} else if(g instanceof ComplementGenerator) {
			for(Long id : drawComplementNodes.keySet()) {
				DrawNode node = drawComplementNodes.get(id);
				node.x = (int)(Math.random()*m.width);
				node.y = (int)(Math.random()*m.height);
			}
		}
	}

	public class DrawNode {
		//node class that works with processing window
		boolean isSelected = false;
		float x;
		float y;
		float radius = 15;
		int numRels = 0;
		int r = 255;
		int g = 255;
		int b = 255;
		Node node;
		String name;

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof DrawNode) {
				if(this.node.equals(((DrawNode) obj).node));
			} else if(obj instanceof Node) {
				if(this.node.equals(obj)) return true;
			}
			return false;
		}

		public void setSelected(boolean is) {
			isSelected = is;
		}

		public DrawNode(Node graphDbNode) {
			node = graphDbNode;
			//color = node.getProperty("color").toString();
			x = (float)(Math.random()*m.width);
			y = (float)(Math.random()*m.height);
			numRels = node.getDegree(Generator.Relationships.COMP_REL, Direction.OUTGOING);
			if(node.hasProperty("name")) this.name = node.getProperty("name").toString();
		}

		public void updateColor() {
			String colorString = this.node.getProperty("color").toString();
			this.r = Integer.valueOf(colorString.substring(0, 3));
			this.g = Integer.valueOf(colorString.substring(3, 6));
			this.b = Integer.valueOf(colorString.substring(6, 9));
		}
	}

	public class DrawRel {
		//relationship class that works with processing window
		DrawNode node1;
		DrawNode node2;

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof DrawRel) {
				DrawRel rel = (DrawRel)obj;
				DrawNode[] otherNodes = new DrawNode[] { rel.node1, rel.node2 };
				if ((this.node1 == otherNodes[0] || this.node1 == otherNodes[1])
						&& (this.node2 == otherNodes[0] || this.node2 == otherNodes[1])){ 
					return true;
				}
			}
			return false;
		}

		public DrawRel(DrawNode n1, DrawNode n2) {
			node1 = n1;
			node2 = n2;
		}

	}
}
