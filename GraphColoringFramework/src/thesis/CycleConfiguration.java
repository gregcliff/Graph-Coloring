package thesis;

import java.util.Random;

import org.neo4j.graphdb.Node;

@RunInformation(
		name = "Cycle Configuration",
		properties = {"# nodes", "displayNodeIds=true"})
public class CycleConfiguration extends BaseGraphConfiguration {
	int firstNodeId;
	boolean displayIds = false;
	
	public CycleConfiguration(String[] props) {
		super(props);
		numNodes = Integer.valueOf(props[0]);
		if(props[1].equals("true")) displayIds = true;
	}

	@Override
	public void generateNodes() {
		Node node = super.createGraphColoringNode();
		firstNodeId = (int) node.getId();
		if(displayIds) node.setProperty("name", firstNodeId);
		for(int i = 1; i < numNodes; i++) {
			Node n = super.createGraphColoringNode();
			if(displayIds) n.setProperty("name", n.getId());
		}
	}

	@Override
	public void generateRels() {
		Node startNode = null;
		Node endNode = null;
		for(int i = firstNodeId; i < (numNodes+firstNodeId) - 1; i++) {
			startNode = graphDb.getNodeById(i);
			endNode = graphDb.getNodeById(i + 1);
			createGraphColoringRelationship(startNode, endNode);
		}
		createGraphColoringRelationship(endNode, graphDb.getNodeById(firstNodeId));
	}

}
