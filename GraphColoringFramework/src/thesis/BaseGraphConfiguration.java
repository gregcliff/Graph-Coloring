package thesis;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

@RunInformation(name = "", properties = { "" })
public abstract class BaseGraphConfiguration implements GenerationConfiguration {
	int numNodes = -1;
	boolean trackNumNodes = false;
	int numRels;
	int rand;
	Random random;
	
	public GraphDatabaseService graphDb = Generator.graphDb;
	
	public abstract void generateNodes();
	public abstract void generateRels();
	
	public void run() {
		if(numNodes < 0) {
			numNodes = 0;
			trackNumNodes = true;
		}
		generateNodes();
		generateRels();
	}
	
	public BaseGraphConfiguration(String[] properties) {
		rand = (int)(System.currentTimeMillis()*Math.random()%10000);
	}
	
	public void initRandomSeed(int r) {
		random = new Random(r);
		rand = r;
	}
	
	public int getRandomNumber() {
		return rand;
	}

	public Node createGraphColoringNode() {
		Node node = Generator.graphDb.createNode(Generator.Labels.NODE);
		node.setProperty("color", "255255255");
		node.setProperty("endNodeList", "");
		node.setProperty("relationshipCount", 0);
		node.setProperty("name", "");
		if(trackNumNodes) numNodes++;
		return node;
	}
	
	// create relationship with problem dependent properties
	public void createGraphColoringRelationship(Node node1, Node node2) {
		node1.createRelationshipTo(node2, Generator.Relationships.REL);
		updateProperties(node1, node2);
	}
	
	private void updateProperties(Node node1, Node node2) {
		long id1 = node1.getId();
		long id2 = node2.getId();
		updateEndNodeList(node1, id2);
		updateEndNodeList(node2, id1);
		addToEndNodeRelationshipCount(node1);
		addToEndNodeRelationshipCount(node2);
	}
	
	private String randomColor() {
		String color = "\'#";
		String values = "0123456789ABCDEF";
		for(int i = 0; i < 6; i++) {
			color += values.charAt((int)(Math.random()*16));
		}
		color += "\'";
		return color;
	}

	private void updateEndNodeList(Node node, long endID) {
		String endNodeList = node.getProperty("endNodeList").toString();
		if(endNodeList.contains(";"+String.valueOf(endID)+";") || endNodeList.startsWith(String.valueOf(endID)+";")) {
			return;
		}
		endNodeList += String.valueOf(endID) + ";";
		node.setProperty("endNodeList", endNodeList);
	}
	
	private void addToEndNodeRelationshipCount(Node node) {
		int rels = Integer.parseInt(node.getProperty("relationshipCount").toString());
		node.setProperty("relationshipCount", rels+1);
	}
	
	public void setRandom(Random r) {
		random = r;
		random.setSeed(rand);
	}
}
