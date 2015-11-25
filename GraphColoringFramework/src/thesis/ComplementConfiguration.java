package thesis;
import java.util.ArrayList;
import java.util.Random;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;


public class ComplementConfiguration implements GenerationConfiguration {
	int numExistingNodes;
	int numExistingRels;
	int numNewRels;
	int firstNodeId;
	Generator graphGen;

	public ComplementConfiguration(Generator g) {
		graphGen = g;
		numExistingNodes = g.numNodes;
		numExistingRels = g.numRels;
		numNewRels = numExistingRels*(numExistingRels - 1)/2;
		firstNodeId = 0;
	}
	
	public ComplementConfiguration(Generator g, int firstNodeId) {
		graphGen = g;
		numExistingNodes = g.numNodes;
		numExistingRels = g.numRels;
		numNewRels = numExistingRels*(numExistingRels - 1)/2;
		setFirstNodeId(firstNodeId);
	}

	
	public void run() {
		createDualNodes();
		createDualRels();
	}	

	public void createDualNodes() {
		ResourceIterator<Node> allNodes = graphGen.getAllNodes(Generator.Labels.NODE);
		Node node;
		while(allNodes.hasNext()) {
			node = allNodes.next();
			String nodeList = node.getProperty("endNodeList").toString();
			String inverseNodeList = inverseNodeList(nodeList, String.valueOf(node.getId()));
			Node dualNode = Generator.graphDb.createNode(Generator.Labels.COMP_NODE);
			node.createRelationshipTo(dualNode, Generator.Relationships.COMP);
			dualNode.setProperty("endNodeList", inverseNodeList);
			dualNode.setProperty("relationshipCount", (numExistingNodes - Integer.parseInt(node.getProperty("relationshipCount").toString())));
			dualNode.setProperty("color", "255255255");
		}
	}

	public void createDualRels() throws NotFoundException {
		ResourceIterator<Node> dualNodes = graphGen.getAllNodes(Generator.Labels.COMP_NODE);
		Node dualNode;
		ArrayList<Integer> endNodeIds = new ArrayList<Integer>();
		while(dualNodes.hasNext()) { 
			dualNode = dualNodes.next();
			String nodeList = dualNode.getProperty("endNodeList").toString();
			String current = "";
			for(int i = 0; i < nodeList.length(); i++) {
				if(nodeList.charAt(i) == ';') {
					endNodeIds.add(Integer.parseInt(current));
					current = "";
					continue;
				}
				current += nodeList.charAt(i);
			}
			for(int i = 0; i < endNodeIds.size(); i++) {
				Relationship rel = Generator.graphDb.getNodeById(endNodeIds.get(i))
						.getSingleRelationship(Generator.Relationships.COMP, Direction.OUTGOING);
				if(rel == null) continue;
				Node endNode = rel.getEndNode();
				dualNode.createRelationshipTo(endNode, Generator.Relationships.COMP_REL);
			}
			//System.out.println(endNodeIds.size() + " rels added.");
			endNodeIds.clear();
		}
	}

	public String inverseNodeList(String nodeList, String id) {
		String inverse = "";
		String current = "";
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < nodeList.length(); i++) {
			current += nodeList.charAt(i);
			if(current.endsWith(";")) {
				list.add(current);
				current = "";
			} 
		}
		for(int i = 0; i < numExistingNodes; i++) {
			String add = String.valueOf(i) + ";";
			if(list.contains(add) || add.substring(0, add.length()-1).equals(id)) continue;
			inverse += add;
		}
		return inverse;
	}
	
	public void setFirstNodeId(int id) {
		firstNodeId = id;
	}

	public void setRandom(Random r) {
		
	}
}
