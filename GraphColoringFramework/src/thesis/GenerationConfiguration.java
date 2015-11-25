package thesis;
import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;

public interface GenerationConfiguration {
	public void run();
	
	public void setRandom(Random r);
}
