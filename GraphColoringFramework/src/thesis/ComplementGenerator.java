package thesis;
import java.util.ArrayList;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;


public class ComplementGenerator extends Generator {
	
	public ComplementGenerator(BaseGenerator graphGen) {
		setNumberNodes(graphGen.getNumberNodes());
		int num = (graphGen.getNumberRels()*(graphGen.getNumberRels()-1))/2;
		setNumberRels(num);
	}
	
	public void run() {
		config.run();
	}
	
	@Override
	public Label getNodeLabel() {
		return Generator.Labels.COMP_NODE;
	}

	@Override
	public RelationshipType getRelationshipLabel() {
		return Generator.Relationships.COMP_REL;
	}
}
