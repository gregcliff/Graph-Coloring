package thesis;
//Greg Lee
//Senior Thesis

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class BaseGenerator extends Generator {
	public BaseGenerator(GraphDatabaseService graphDb) {
		initialize(graphDb);
		engine = new ExecutionEngine(graphDb);
	}

	public void run() {
		config.run();
		this.numNodes = ((BaseGraphConfiguration)config).numNodes;
		this.numRels = ((BaseGraphConfiguration)config).numRels;
	}

	public void initialize(GraphDatabaseService graphDb) {
		this.graphDb = graphDb;
	}
	
	@Override
	public Label getNodeLabel() {
		return Generator.Labels.NODE;
	}

	@Override
	public RelationshipType getRelationshipLabel() {
		return Generator.Relationships.REL;
	}
}
