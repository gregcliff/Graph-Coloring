package thesis;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import processing.core.PApplet;

public class Main extends PApplet {
	//path of neo4j database
	private static String path;
	//default test file information
	private static String fileNamePrefix = "C:\\Users\\Gregory\\Documents\\Thesis\\TestResults\\";
	private static String fileName = "results.txt";
	//graph setup data
	private static String baseProperties[];
	private static PrintStream filePrintStream;
	private static PrintStream consoleStream;
	private static GraphManager g;
	private static GraphDrawer draw;
	private static GraphDatabaseService graphDb;
	private static GraphColoror coloring;
	private static String graphColororName = "thesis.DegreeBasedColoring";
	private static String baseConfigurationName = "thesis.RandomConfiguration";
	/*
	 * global variables for functionality
	 */
	private boolean pKeyPressed;
	private boolean pMousePressed;
	private static boolean active = false; //whether or not the graphDb is active (processing loop see
	private static boolean solved = false; //is graph solved
	/*
	 * global variables for run settings
	 */
	public static boolean autoShutdown = false; // -s 
	public static boolean autoSolve = false; // -a
	public static boolean debug = false; //-d
	public static boolean displayGUI = false; //-g
	public static boolean outputToFile = false;

	public static PrintStream getFileStream() {
		return filePrintStream;
	}

	public static PrintStream getConsoleStream() {
		return consoleStream;
	}

	public static void runWithoutGUI() {
		setupGraph();
		while(!g.isBaseConsistent()) {
			coloring.colorNext();
		}
		outputToFile();
	}

	public static void setGraphColoror(String colororString) {
		try {
			coloring = (GraphColoror) Class.forName(colororString)
					.getConstructor(ComplementGenerator.class).newInstance(GraphManager.dualGen);
			coloring.setRandom(g.getRandom());
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

	public void setup() {
		size(640, 640);
		draw = new GraphDrawer(this);
		setupGraph();
	}

	public static void setupGraph() {
		/*
		 * set file name, if name already exists, increment and add a value at the end
		 */

		if(outputToFile) {
			File projectFolder = new File(new File(Main.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath()).getParent() + "\\TestResults");
			if(!projectFolder.exists()) projectFolder.mkdir();
			fileNamePrefix = projectFolder.getAbsolutePath() + "\\";
			consoleStream = System.out;
			String fullFileName = fileNamePrefix + fileName;
			File file = new File(fullFileName);
			int i = 1;
			String oFileName = fullFileName.substring(0, fullFileName.lastIndexOf(".txt"));
			while(file.exists()) {
				fullFileName = oFileName + String.valueOf(i) + ".txt";
				file = new File(fullFileName);
				i++;
			}
			fileName = fullFileName;
			consoleStream.println(fullFileName);
			try {
				filePrintStream = new PrintStream(file);
			} catch (IOException e) {
				filePrintStream.println("File did not successfully open.");
				outputToFile = false;
			}
		}
		/*
		 * processing setup and database setup
		 */

		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(path);
		active = true;
		Runtime.getRuntime().addShutdownHook(new Thread() {
			/*
			 * if user exits program before completion, still run a clean shutdown
			 */
			public synchronized void run() {
				if(outputToFile) filePrintStream.close();
				active = false;
				g.shutdownAndDelete();
			}
		});
		formGraph();
	}

	public static void formGraph() {
		/*
		 * graph initialization
		 */
		g = new GraphManager(graphDb, path);
		Transaction tx = graphDb.beginTx();
		try {
			g.setBaseGeneratorConfiguration(baseConfigurationName, baseProperties);
			g.runBaseGenerator();
			consoleStream.println("Random number parameter is: " + g.getRandomConfigInt());
			consoleStream.println(baseConfigurationName + " generated.  Generating complement.");
			g.generateDual(new ComplementConfiguration(GraphManager.graphGen));
			consoleStream.println("Complement generated.");
			//after base graph is generated, create its complement
			if(displayGUI) {
				draw.init(GraphManager.graphGen); //load drawing data in drawing class
				draw.init(GraphManager.dualGen); //ditto
				draw.setGenerator(GraphManager.graphGen); //by default start by viewing the base
			}
			setGraphColoror(graphColororName);
			consoleStream.println("Coloror: " + graphColororName);
			tx.success();
		} finally {
			tx.close();
		}
	}

	public synchronized void draw() {
		if(!active) return; // throws exception without this
		background(170);
		Transaction tx = graphDb.beginTx();
		try {
			draw.update(); // update the graph drawer using the database
			if(autoSolve && !solved) {
				coloring.colorNext(); // automatically color the next node
			}
			if(g.isBaseConsistent()) {
				/*
				 * coloring complete at this point
				 */
				if(!solved) {
					solved = true;
					draw.setGenerator(GraphManager.graphGen);
					debug = false;
					if(outputToFile) {
						outputToFile();
					} 
					consoleStream.println("Coloring complete. Color count: " + (coloring.getColorIndex()-1));
					Map<String, Integer> map = coloring.getColorCountsAsNames();
					for(String color : map.keySet()) {
						if(color.equals("white") || map.get(color) == null) continue;
						consoleStream.println(color + " has " + (map.get(color)));
					}
					if(autoShutdown) {
						exit();
						active = false;
					}
				}
				stroke(0, 255, 0);
			} else {
				stroke(255, 0, 0);				
			}
			fill(170);
			rect(5, 5, width-10, height-10);
			// draw red box if unsolved, green if solved
			tx.success();
		} finally {
			tx.close();
		}
		stroke(0);
		if(!debug) {
			/*
			 * draw nodes and relationships stored in graphdrawer class
			 */
			for(GraphDrawer.DrawRel rel : draw.getRelMap()) {
				line(rel.node1.x, rel.node1.y, rel.node2.x, rel.node2.y);
			}
			for(Long i : draw.getNodeMap().keySet()) {
				GraphDrawer.DrawNode node = draw.getNodeMap().get(i);
				fill(node.r, node.g, node.b);
				ellipse(node.x, node.y, node.radius, node.radius);
				if(node.name != null) {
					fill(0);
					textSize(16);
					text(node.name, node.x + 15.0f, node.y);
				}
			}
		}

		/*
		 * some key functions
		 */
		if(keyPressed && !pKeyPressed && key == 'c') {
			draw.setGenerator(GraphManager.dualGen);
			debug = false;
		} else if(keyPressed && !pKeyPressed && key == 'd') {
			draw.setGenerator(GraphManager.graphGen);
			debug = false;
		} else if(keyPressed && !pKeyPressed && key == 'r') {
			draw.randomize();
		} else if(keyPressed && !pKeyPressed && key == 'g' && !solved) {
			tx = graphDb.beginTx();
			try {
				coloring.colorNext();
				tx.success();
			} finally {
				tx.close();
			}
		} else if(keyPressed && !pKeyPressed && key == 's' && !solved) {
			autoShutdown = false;
			autoSolve = !autoSolve;
		} 

		// click and drag nodes around
		if(mousePressed) {
			int x = mouseX;
			int y = mouseY;
			if(draw.selectedNode != null) {
				draw.selectedNode.x = x;
				draw.selectedNode.y = y;
			}
			if(!pMousePressed) {
				for(Long i : draw.getNodeMap().keySet()) {
					GraphDrawer.DrawNode node = draw.getNodeMap().get(i);
					if(node.radius >= Math.sqrt((x - node.x)*(x - node.x) + (y - node.y)*(y - node.y))) {
						draw.setSelectedNode(node);
					}
				}
			}
		}

		if(pMousePressed && !mousePressed) {
			draw.setSelectedNode(null);
		}
		pMousePressed = mousePressed;
		pKeyPressed = keyPressed;

	}

	// write test information to file
	public static void outputToFile() { 
		consoleStream.println("Writing to " + fileName);
		filePrintStream.println("Base: " + baseConfigurationName);
		filePrintStream.println("Coloror: " + graphColororName);
		try {
			String[] props = Class.forName(baseConfigurationName)
					.getAnnotation(RunInformation.class).properties();
			for(int i = 0; i < baseProperties.length; i++) {
				filePrintStream.println(props[i] + ": " + baseProperties[i]);
			}
		} catch(Exception e) {
			filePrintStream.println("Error reading class annotation.");
		}
		filePrintStream.println("Random number parameter is: " + g.getRandomConfigInt());
		filePrintStream.println("Execution time: " + "");
		filePrintStream.println("Color count: " + (coloring.getColorIndex()-1));
		Map<String, Integer> count = coloring.getColorCountsAsNames();
		for(String color : count.keySet()) {
			if(color.equals("white") || count.get(color) == null) continue;
			filePrintStream.println(color + " has " + (count.get(color)));
		}
		File csvFile = new File(fileName.replace(".txt", ".csv"));
		consoleStream.println("Writing csv file " + csvFile);
		boolean writeToCsv = true;
		try {
			filePrintStream = new PrintStream(csvFile);
		} catch (FileNotFoundException e1) {
			writeToCsv = false;
			consoleStream.println("csv file not successfully created.");
		}

		if(writeToCsv) {
			filePrintStream.print("base, coloror, random, ");
			boolean hasAnnotation = true;
			try {
				String[] props = Class.forName(baseConfigurationName)
						.getAnnotation(RunInformation.class).properties();
				for(int i = 0; i < baseProperties.length; i++) {
					filePrintStream.print(props[i] + ", ");
				}
			} catch(Exception e) {
				filePrintStream.println("noAnnotation, ");
				hasAnnotation = false;
			}	
			filePrintStream.print("#nodes, #relationships, colorCount");
			filePrintStream.println();
			filePrintStream.print(baseConfigurationName + ", "  + graphColororName + ", "  + g.randomConfigInt + ", ");
			if(hasAnnotation) {
				for(int i = 0; i < baseProperties.length; i++) {
					filePrintStream.print(baseProperties[i] + ", ");
				}
			}
			filePrintStream.print(g.graphGen.numNodes + ", " + g.graphGen.numRels + ", " + (coloring.getColorIndex()-1));
			filePrintStream.println();
		}
	}

	public static void main(String[] args) {
		final String baseTag = "-base=";
		final String colororTag = "-coloror=";
		final String outputTag = "-outputTo=";
		final String databasePathTag = "-neo4j=";
		int BASE_PROPERTIES_SIZE = -1;
		int COLOROR_PROPERTIES_SIZE = -1;
		boolean readBase = false;
		setGlobalVariables(args[0]);
		for(int i = 0; i < args.length; i++) {
			if(args[i].startsWith(outputTag)) {
				fileName = args[i].substring(outputTag.length());
			}
			if(args[i].startsWith(databasePathTag)) {
				path = args[i].substring(databasePathTag.length());
			}
			if(!readBase) {
				if(BASE_PROPERTIES_SIZE > -1) {
					if(!args[i].startsWith(colororTag)) {
						BASE_PROPERTIES_SIZE++;
					} else {
						readBase = true;
					}
				}
				if(args[i].startsWith(baseTag)) {
					baseConfigurationName = args[i].substring(baseTag.length());
					BASE_PROPERTIES_SIZE++;
				}
			} 
			if(readBase) {
				if(COLOROR_PROPERTIES_SIZE > -1) {
					COLOROR_PROPERTIES_SIZE++;
				} else {
					baseProperties = new String[BASE_PROPERTIES_SIZE];
					int k = 0;
					for(int j = i - BASE_PROPERTIES_SIZE; j < i; j++) {
						baseProperties[k] = args[j];
						k++;
					}
				}
				if(args[i].startsWith(colororTag)) {
					graphColororName = args[i].substring(colororTag.length());
					COLOROR_PROPERTIES_SIZE++;
				}
			}
		}

		try {
			if(displayGUI)
				PApplet.main(new String[] {"thesis.Main"});
			else {
				runWithoutGUI();
			}
		} catch (Exception e) {
			e.printStackTrace(filePrintStream);
			e.printStackTrace(consoleStream);
			g.shutdownAndDelete();
		}
	}

	/*
	 * use cmd prompt tags to set variables
	 */
	public static void setGlobalVariables(String tags) {
		for(int i = 0; i < tags.length(); i++) {
			char tag = tags.charAt(i);
			switch(tag) {
			case 'a': { autoSolve = true; break; }
			case 'd': { debug = true; break; }
			case 's': { autoShutdown = true; break; }
			case 'g': { displayGUI = true; break; }
			case 'o': { outputToFile = true; break; }
			}
		}
	}
}
