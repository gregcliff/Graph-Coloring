package thesis;
import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;

public abstract class Generator {
	public static GraphDatabaseService graphDb = null;
	public ExecutionEngine engine;
	public GenerationConfiguration config;
	int numNodes;
	int numRels;

	public enum Labels implements Label {
		NODE,
		COMP_NODE;
	}

	public enum Relationships implements RelationshipType {
		REL,
		COMP,
		COMP_REL;
	}

	public void displayAll(Label label) {
		ResourceIterator<Node> iterator = getAllNodes(label);
		int i = 0;
		while(iterator.hasNext()) {
			Node node = iterator.next();
			long id = node.getId();
			for(Relationship rel: node.getRelationships(Direction.OUTGOING, Generator.Relationships.REL)) {
				long id2 = rel.getEndNode().getId();
				i++;
				System.out.println(i + " is " + id + "->" + id2);
			}
		}
	}

	public ResourceIterator<Node> getAllNodes(Label label) {
		String queryLabel = label.toString();
		ExecutionResult result = engine.execute("MATCH (n:" + queryLabel + ") RETURN n");
		return result.columnAs("n");

	}

	public int getNumberNodes() {
		return numNodes;
	}

	public int getNumberRels() {
		return numRels;
	}

	public void setNumberNodes(int num) {
		this.numNodes = num;
	}

	public void setNumberRels(int num) {
		this.numRels = num;
	}

	public void setEngine(ExecutionEngine eng) {
		engine = eng;
	}

	public void setConfiguration(GenerationConfiguration config) {
		this.config = config;
	}

	public abstract Label getNodeLabel();
	
	public abstract RelationshipType getRelationshipLabel();
}
