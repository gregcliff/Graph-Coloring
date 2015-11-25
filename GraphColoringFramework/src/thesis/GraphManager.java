package thesis;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import scala.Array;


public class GraphManager {
	public static GraphDatabaseService graphDb;
	public static Random random;
	private static boolean shutdownCalled = false; // so it can only be shut down once
	static int randomConfigInt = -1;
	static String gPath = "";
	static BaseGenerator graphGen;
	static ComplementGenerator dualGen;
	static ExecutionEngine engine;
	static BaseGraphConfiguration baseConfig = null;
	static GenerationConfiguration dualConfig = null;
	
	static ComplementGenerator getDual() {
		return dualGen;
	}
	
	static BaseGenerator graphGen() {
		return graphGen;
	}

	public GraphManager(GraphDatabaseService graphDb) {
		GraphManager.graphDb = graphDb;
		engine = new ExecutionEngine(graphDb);
		graphGen = new BaseGenerator(graphDb);
		graphGen.setEngine(engine);
		random = new Random();
	}

	public GraphManager(GraphDatabaseService graphDb, String path) { //need that to delete files
		GraphManager.graphDb = graphDb;
		gPath = path;
		engine = new ExecutionEngine(graphDb);
		graphGen = new BaseGenerator(graphDb);
		graphGen.setEngine(engine);
		random = new Random();
	}

	public void shutdownAndDelete() { // shutdown graphDb and delete its directory
		if(shutdownCalled) return;
		shutdownCalled = true;
		try {
			graphDb.shutdown();
			File file = new File(gPath);
			FileUtils.deleteDirectory(file);
			System.out.println("Database shutdown and folder deleted.");
		} catch (IOException e) {
			//throws unable to delete exception but is not problematic for operating the database
		}
	}

	// create dual
	// base must be created already
	public void initDual() {
		setNumNodes(baseConfig.numNodes);
		setNumRels(baseConfig.numRels);
		dualGen = new ComplementGenerator(graphGen);
		dualGen.setEngine(engine);
	}

	public int getRandomConfigInt() {
		randomConfigInt = baseConfig.rand;
		return randomConfigInt;
	}
	
	public Random getRandom() {
		return random;
	}

	public void displayAll() {
		System.out.println("Generated graph is:");
		displayAll(graphGen);
		System.out.println("Dual graph is:");
		displayAll(dualGen);
	}

	public void displayAll(Generator gen) {
		//show nodes in console
		ResourceIterator<Node> iterator = gen.getAllNodes(gen.getNodeLabel());
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

	public boolean isBaseConsistent() {
		//check it graph is colored appropriately using database
		ResourceIterator<Node> iterator = graphGen.getAllNodes(graphGen.getNodeLabel());
		while(iterator.hasNext()) {
			Node node = iterator.next();
			String color = node.getProperty("color").toString();
			for(Relationship rel: node.getRelationships(Direction.OUTGOING, Generator.Relationships.REL)) {
				String otherColor = rel.getEndNode().getProperty("color").toString();
				if(color.equals(otherColor) || color.equals("255255255") || otherColor.equals("255255255")) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void setSeed(int rand) {
		randomConfigInt = rand;
		random.setSeed(rand);
	}
	
	public void setNumNodes(int nodes) {
		graphGen.setNumberNodes(nodes);
	}
	
	public void setNumRels(int rels) {
		graphGen.setNumberRels(rels);
	}
	
	public void runBaseGenerator() {
		graphGen.run();
	}
	
	public void setBaseGenerationConfiguration(BaseGraphConfiguration b) {
		baseConfig = b;
		graphGen.setConfiguration(baseConfig);
	}
	
	public void setBaseGeneratorConfiguration(String baseGraphConfigurationName, Object baseProperties) {
		try{ 
			baseConfig = (BaseGraphConfiguration) Class.forName(baseGraphConfigurationName)
					.getConstructor(String[].class).newInstance(baseProperties);
			baseConfig.setRandom(random);
			graphGen.setConfiguration(baseConfig);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void generateDual(GenerationConfiguration g) {
		initDual();
		dualGen.setConfiguration(g);
		dualGen.run();
	}

	@Deprecated
	public void clearAll() {
		// use cypher to delete all nodes and relationships
		ExecutionResult e = engine.execute("MATCH (n) RETURN n");
		ResourceIterator<Node> iterator = e.columnAs("n");
		while(iterator.hasNext()) {
			Node node = iterator.next();
			for(Relationship rel: node.getRelationships()) {
				rel.delete();
			}
			node.delete();
		}
	}

	@Deprecated
	public int clearAll(Generator gen) {
		// use cypher to delete all nodes and relationships in a given graph generator
		int numRemoved = 0;
		ResourceIterator<Node> iterator = gen.getAllNodes(gen.getNodeLabel());
		while(iterator.hasNext()) {
			Node node = iterator.next();
			for(Relationship rel: node.getRelationships()) {
				rel.delete();
				numRemoved++;
			}
			node.delete();
			numRemoved++;
		}
		return numRemoved;
	}
}
