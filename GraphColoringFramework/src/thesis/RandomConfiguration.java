package thesis;
import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;

@RunInformation(
		name = "Random Configuration", 
		properties = { "# Nodes=50", "# Relationships=100" , "Random Seed=-1"}
		)
public class RandomConfiguration extends BaseGraphConfiguration {
	public RandomConfiguration(String[] properties) {
		super(properties);
		super.numNodes = Integer.valueOf(properties[0]);
		super.numRels = Integer.valueOf(properties[1]);
		if(properties.length < 3 || properties[2].equals("") || Integer.valueOf(properties[2]) < 0) {
			rand = (int)(System.currentTimeMillis()*Math.random()%10000);
		} else {
			rand = Integer.valueOf(properties[2]);
		}
	}

	// generate however many nodes
	@Override
	public void generateNodes() {
		for(int i = 0; i < numNodes; i++) {
			Node node = super.createGraphColoringNode();
			node.setProperty("name", node.getId());
		}		
	}

	// generate relationships randomly between nodes
	@Override
	public void generateRels() throws NotFoundException{
		for(int i = 0; i < numRels; i++) {
			long nodeId1 = 0;
			long nodeId2 = 0;
			while(nodeId1 == nodeId2) {
				long rand1 = random.nextLong();
				long rand2 = random.nextLong();
				//System.out.println("Rand1 = " + rand1 + " and Rand2 = " + rand2);
				nodeId1 = (long)(Math.abs(rand1) % numNodes);
				nodeId2 = (long)(Math.abs(rand2) % numNodes);
			}
			Node node1 = Generator.graphDb.getNodeById(nodeId1), node2 = Generator.graphDb.getNodeById(nodeId2);
			createGraphColoringRelationship(node1, node2);			
		}		
	}

	public int getNumberRandomNodes() {
		return numNodes;
	}

	public void setNumberRandomNodes(int num) {
		this.numNodes = num;
	}

	public int getNumberRandomRels() {
		return numRels;
	}

	public void setNumberRandomRels(int num) {
		this.numRels = num;
	}

	public void setRandom(Random r) {
		random = r;
		random.setSeed(rand);
	}
}
