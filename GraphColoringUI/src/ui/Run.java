package ui;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;

import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import thesis.BaseGraphConfiguration;
import thesis.Main;
import thesis.RunInformation;

//sources:
//https://code.google.com/p/reflections/
//http://docs.oracle.com/javase/tutorial/uiswing/components/frame.html
//http://stackoverflow.com/questions/10450405/why-retrieving-system-info-return-null-point-of-exception
//http://docs.oracle.com/javase/tutorial/uiswing/concurrency/bound.html
//http://java.dzone.com/articles/how-annotations-work-java

//need to implement automatically detecting what "version" is next

public class Run {
	static Run run;
	static ColororInstance coloror;
	static JFrame frame;
	static JTextArea text;
	static JTextArea databasePathArea;
	static JScrollPane scroller;
	static JPanel runOptions;
	static JPanel configurationPanel;
	static JPanel addFilePanel;
	static JComboBox<ListData> dropdown;
	static JComboBox<ListData> dropdown2;
	static Checkbox c1;
	static Checkbox c2;
	static Checkbox c3;
	static JTextArea fileNameArea;
	static JTextArea testCountArea;
	static JPanel nameArea;
	static JPanel testArea;
	static ArrayList<JPanel> configurationPropertiesArea;
	static DefaultCaret caret;
	static JButton startButton;
	static JButton compileButton;
	static JButton cancelButton;
	static JButton addFileButton;
	static JFileChooser fileChooser;
	static String tags = "-";
	static String fileName = "results.txt";
	static String baseGraphName = "Random Configuration";
	static String graphColoringName = "Largest Degree First";
	static String pathToJar = null;
	static String graphDbHome = "C:\\Users\\Gregory\\Documents\\Neo4j\\graphColoring";
	static File projectDirectory;
	static File extensionDirectory;
	static File createdDirectory = null;
	static int testIncrement = 1;

	public Run() {
		File f;
		try {
			graphDbHome = new File(Run.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI().getPath()).getParent();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void startGUI() {
		configurationPropertiesArea = new ArrayList<JPanel>();
		frame = new JFrame("Graph Coloror");
		text = new JTextArea();
		databasePathArea = new JTextArea();
		databasePathArea.setBorder(BorderFactory.createLineBorder(Color.black));
		databasePathArea.setText(graphDbHome);
		text.setEditable(false);
		text.setBackground(new Color(0, 0, 0));
		text.setFont(new Font("Serif", 1, 10));
		text.setForeground(Color.WHITE);
		caret = (DefaultCaret)text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroller = new JScrollPane(text);
		scroller.setPreferredSize(new Dimension(400, 200));
		runOptions = new JPanel(new GridLayout(3, 1));
		c1 = new Checkbox("Background?");
		c2 = new Checkbox("Autosolve?");
		c3 = new Checkbox("Auto-shutdown?");
		nameArea = new JPanel(new BorderLayout());
		testArea = new JPanel(new BorderLayout());
		fileNameArea = new JTextArea(fileName);
		fileNameArea.setPreferredSize(new Dimension(80, 20));
		fileNameArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		nameArea.add(new JLabel("Output file: "), BorderLayout.WEST);
		nameArea.add(fileNameArea, BorderLayout.EAST);
		testCountArea = new JTextArea("1");
		testCountArea.setPreferredSize(new Dimension(80, 20));
		testCountArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		testArea.add(new JLabel("Test count: "), BorderLayout.WEST);
		testArea.add(testCountArea, BorderLayout.EAST);
		dropdown = new JComboBox<ListData>();
		dropdown.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() != ItemEvent.SELECTED) return;
				ListData data = (ListData)dropdown.getSelectedItem();
				String[] props = data.properties;
				configurationPropertiesArea.clear();
				if(props != null) {
					for(int i = 0; i < props.length; i++) {
						String name = props[i];
						String value = "";
						if(props[i].contains("=")) {
							name = props[i].substring(0, props[i].indexOf('='));
							value = props[i].substring(props[i].indexOf('=')+1);
						}
						JPanel panelArea = new JPanel(new BorderLayout());
						JTextArea textArea = new JTextArea(value);
						textArea.setPreferredSize(new Dimension(80, 20));
						textArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
						panelArea.add(new JLabel(name), BorderLayout.WEST);
						panelArea.add(textArea, BorderLayout.EAST);
						configurationPropertiesArea.add(panelArea);
					}
				}
				redisplayRunOptions();
				frame.repaint();	
				frame.pack();
			}
		});
		ClassPathScanner<thesis.BaseGraphConfiguration> configs = 
				run.new ClassPathScanner<thesis.BaseGraphConfiguration>
		(dropdown, thesis.BaseGraphConfiguration.class);
		configs.execute();
		dropdown.setSelectedItem(baseGraphName);
		dropdown.setPreferredSize(new Dimension(150, 24));
		dropdown2 = new JComboBox<ListData>();
		ClassPathScanner<thesis.GraphColoror> solutions = 
				run.new ClassPathScanner<thesis.GraphColoror>
		(dropdown2, thesis.GraphColoror.class);
		solutions.execute();
		dropdown2.setSelectedItem(graphColoringName);
		dropdown2.setPreferredSize(new Dimension(150, 24));
		redisplayRunOptions();
		JPanel startAndStopButtons = new JPanel(new BorderLayout());
		startButton = new JButton("Start");
		startButton.setEnabled(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cancelButton.setText("Cancel");
				testIncrement = Integer.valueOf(testCountArea.getText());
				databasePathArea.setEditable(false);
				String cmd = setCmd();
				if(cmd == null) {
					addText("Invalid database path!");
					databasePathArea.setEditable(true);
				} else {
					addText("Press 's' to toggle autosolve.");
					addText("Press 'g' to color a node.");
					addText("Press 'c' to look at the complement.");
					addText("Press 'd' to look at the default configuration.");
					coloror = run.new ColororInstance(cmd);
					coloror.execute();
					startButton.setEnabled(false);
				}
			}
		});
		cancelButton = new JButton("Clear");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				testIncrement = 0;
				if(coloror != null)	{
					coloror.cancel(false);
					addText("Coloring cancelled.");
				}
				graphDbHome = databasePathArea.getText();
				databasePathArea.setEditable(true);
				try {
					long sizeInKb = createdDirectory.getTotalSpace()/1000;
					if(sizeInKb > 5000) {
						addText("Folder is " + sizeInKb + " Kb.  Delete manually if needed.");
					} else {
						FileUtils.cleanDirectory(createdDirectory);
					}
				} catch (IOException e1) {
					addText(e1.getMessage());
				}
			}
		});
		startAndStopButtons.add(startButton, BorderLayout.WEST);
		startAndStopButtons.add(cancelButton, BorderLayout.EAST);
		/*addFileButton = new JButton("Add jar file");
		addFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MavenCommand c = run.new MavenCommand();
				c.execute();
			}
		});
		compileButton = new JButton("Compile");
		compileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				compileButton.setEnabled(false);
				startButton.setEnabled(false);
				cancelButton.setEnabled(false);
				run.new CompileInstance().execute();
			}
		});*/
		addFilePanel = new JPanel(new BorderLayout());
		//addFilePanel.add(addFileButton, BorderLayout.WEST);
		JPanel panelArea = new JPanel(new BorderLayout());
		JLabel dbHomeLabel = new JLabel("Graph DB Home");
		dbHomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panelArea.add(dbHomeLabel, BorderLayout.NORTH);
		panelArea.add(databasePathArea, BorderLayout.CENTER);
		addFilePanel.add(panelArea, BorderLayout.CENTER);
		//addFilePanel.add(compileButton, BorderLayout.EAST);
		configurationPanel = new JPanel(new BorderLayout());
		configurationPanel.add(runOptions, BorderLayout.WEST);
		configurationPanel.add(startAndStopButtons, BorderLayout.EAST);
		configurationPanel.add(addFilePanel, BorderLayout.NORTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(scroller, BorderLayout.CENTER);
		frame.getContentPane().add(configurationPanel, BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);
	}

	private static void redisplayRunOptions() {
		runOptions.removeAll();
		JPanel[] indices = new JPanel[3];
		for(int i = 0; i < indices.length; i++) {
			indices[i] = new JPanel(new FlowLayout());
			runOptions.add(indices[i]);
		}
		indices[0].add(c1);
		indices[0].add(c2);
		indices[0].add(c3);
		indices[0].add(nameArea);
		indices[0].add(testArea);
		for(int i = 0; i < configurationPropertiesArea.size(); i++) {
			indices[1].add(configurationPropertiesArea.get(i));
		}
		indices[2].add(dropdown);
		indices[2].add(dropdown2);
		frame.pack();
	}

	private static String[] getBaseConfigurationParameters() {
		String[] params = new String[configurationPropertiesArea.size()];
		for(int i = 0; i < configurationPropertiesArea.size(); i++) {
			Component[] c = configurationPropertiesArea.get(i).getComponents();
			for(int j = 0; j < c.length; j++) {
				if(c[j] instanceof JTextArea) {
					params[i] = (((JTextArea)c[j]).getText());
				}
			}
		}
		return params;
	}

	private static String arrayToString(String[] s, char delim) {
		String newString = "";
		for(int i = 0; i < s.length; i++) {
			newString += s[i] + delim;
		}
		return newString;
	}

	public static void main(String[] args) throws Exception {
		if(args.length == 0) throw new Exception("Error: Enter the project path as first parameter.");
		pathToJar = args[0];
		run = new Run();
		startGUI();
	}

	static String setCmd() {
		final String outputTag = "-outputTo=";
		final String baseTag = "-base=";
		final String colororTag= "-coloror=";
		final String databasePathTag = "-neo4j=";
		if(c1.getState()) tags = "-";
		else tags = "-g";
		if(c2.getState()) tags += "a";
		if(c3.getState()) tags += "s";
		fileName = fileNameArea.getText();
		if(!fileName.equals("")) {
			tags += "o";
		}
		if(!databasePathArea.getText().isEmpty()) {
			graphDbHome = databasePathArea.getText();
			createdDirectory = new File(graphDbHome + "\\graph-coloring");
			if(!isValidDatabasePath(createdDirectory)) {
				databasePathArea.requestFocusInWindow();
				return null;
			}
		} else {
			databasePathArea.requestFocusInWindow();
			return null;
		}
		baseGraphName = ((ListData)dropdown.getSelectedItem()).className;
		graphColoringName = ((ListData)dropdown2.getSelectedItem()).className;
		String[] baseParameters = getBaseConfigurationParameters();
		String cmd = "java -jar "
				+ pathToJar + " " + tags + " " 
				+ databasePathTag + createdDirectory.getAbsolutePath() + " "
				+ outputTag + fileName + " "
				+ baseTag + baseGraphName + " " 
				+ arrayToString(baseParameters, ' ')
				+ colororTag + graphColoringName;
		return cmd;
	}

	/*
	 * user interface for creating graphs
	 * graph formatting files
	 * why is cycle random?
	 * add type of graph into output file
	 * randomness in degree
	 * homogeniety of a graph: measurement? vector?
	 */

	private static boolean isValidDatabasePath(File file) {
		if(file.exists() && file.isDirectory()) return true;
		if(file.getParentFile() != null && !file.exists()) {
			boolean created = file.mkdir();
			if(created)
				addText("File created: " + file.getAbsolutePath());
			else {
				addText("Folder not created.");
				addText("Write: " + file.getParentFile().canWrite());
				return false;
			}
			return true;
		}
		return false;
	}

	private static void addText(String newText) {
		text.append(newText + "\n");
	}

	class ClassPathScanner <T> extends SwingWorker<JComboBox<ListData>, Void> {
		JComboBox<ListData> box;
		Class<T> _class;

		ClassPathScanner(JComboBox<ListData> box, Class<T> c) {
			this.box = box;
			this._class = c;
		}

		@Override
		protected void done() {
			try {
				box = get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			startButton.setEnabled(true);
			box.repaint();
			frame.pack();
		}

		@Override
		protected JComboBox<ListData> doInBackground() throws Exception {
			getChildClassesInComboBox(this._class);
			return box;
		}

		public void getChildClassesInComboBox(Class<T> c) {
			Reflections reflections;
			try {
				URLClassLoader urls = new URLClassLoader(
						new URL[] {new File(pathToJar).toURI().toURL(), new File(graphDbHome+"\\run.jar").toURI().toURL()});
				reflections = new Reflections(new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forClassLoader(urls)).addClassLoader(urls));
				Set<Class<? extends T>> subTypes = reflections.getSubTypesOf(c);
				addToBox(box, subTypes);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}

		public void addToBox(JComboBox<ListData> cBox, Set<Class<? extends T>> c) {
			Iterator<Class<? extends T>> it = c.iterator();

			while(it.hasNext()) {
				Class<?> o = it.next();
				ListData data;
				if(o.getAnnotation(RunInformation.class)==null) data = new ListData(o.getName());
				else {
					data = new ListData(o.getAnnotation(RunInformation.class).name(),
							o.getAnnotation(RunInformation.class).properties(),
							o.getName());
				}
				cBox.addItem(data);
			}
		}

	}

	class ColororInstance extends SwingWorker<Void, Void> {
		Process process;
		BufferedReader r;
		BufferedReader r2;
		String cmd;
		boolean timeoutDetected = false;
		long timeout = 60000;
		long start;
		long current;

		ColororInstance(String cmd) {
			databasePathArea.setEditable(false);
			this.cmd = cmd;
			addText(cmd);
			try {
				this.process = Runtime.getRuntime().exec(this.cmd);
				this.r = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
				this.r2 = new BufferedReader(new InputStreamReader(this.process.getErrorStream()));
			} catch (IOException e) {
				e.printStackTrace();
			} 
			start = System.currentTimeMillis();
		}

		@Override
		public void done() {
			try {
				if(isCancelled()) {
					process.destroy();
				}
				r.close();
				r2.close();
				testIncrement--;
				if(testIncrement > 0) {
					coloror = run.new ColororInstance(cmd);
					coloror.execute();
					startButton.setEnabled(false);
				} else {
					cancelButton.setText("Clear");
					startButton.setEnabled(true);
					databasePathArea.setEditable(true);
					coloror = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground() throws Exception {
			try {
				while(!isCancelled()) {
					current = System.currentTimeMillis();
					if((current - start) > timeout && !timeoutDetected) {
						timeoutDetected = true;
						addText("Program is taking unusually long.  "
								+ "Recommended cancellation and manual deletion of graph database files.");
					}
					String line = r.readLine();
					if(line == null) break;
					if(line != "") addText(line);
					if(r2.ready()) {
						String error = r2.readLine();
						while(error != null) {
							if(error.equals("")) break;
							addText(error);
							error = r2.readLine();
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return null;
		}
	}

	/*class CompileInstance extends SwingWorker<Void, Void> {
		Process process;
		BufferedReader r;
		String cmd = "cmd /c mvn compile package";

		CompileInstance() { 
			try {
				this.process = Runtime.getRuntime().exec(this.cmd);
				this.r = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}		

		@Override
		public void done() {
			try {
				r.close();
				startButton.setEnabled(true);
				compileButton.setEnabled(true);
				cancelButton.setEnabled(true);
				dropdown = new JComboBox<ListData>();
				ClassPathScanner<thesis.BaseGraphConfiguration> configs = 
						run.new ClassPathScanner<thesis.BaseGraphConfiguration>
				(dropdown, thesis.BaseGraphConfiguration.class);
				configs.execute();
				dropdown.setSelectedItem(baseGraphName);
				dropdown2 = new JComboBox<ListData>();
				ClassPathScanner<thesis.GraphColoror> solutions = 
						run.new ClassPathScanner<thesis.GraphColoror>
				(dropdown2, thesis.GraphColoror.class);
				solutions.execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground() throws Exception {
			try {
				while(true) {
					String line = r.readLine();
					if(line == null) break;
					if(line != "") addText(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return null;
		}
	}

	class MavenCommand extends SwingWorker<Void, Void> {
		Process process;
		BufferedReader r;
		String cmd;

		MavenCommand() {
			adder.show();
		}

		@Override
		protected void done() {
			try {
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected Void doInBackground() throws Exception {
			try {
				while(!adder.isAccepted() && !isCancelled()) {	
				}
				cmd = adder.getAddition().toString();
				try {
					this.process = Runtime.getRuntime().exec(this.cmd, null, projectDirectory);
					addText(cmd);
					addText("Added with version as " + adder.getAddition().getVersion());
					this.r = new BufferedReader(new InputStreamReader(this.process.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				} 
				while(!isCancelled()) {
					String line = r.readLine();
					if(line == null) break;
					if(line != "") addText(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} 
			return null;
		}
	}*/

	class ListData {
		String displayName = null;
		String properties[] = null;
		String className = null;

		@Override
		public boolean equals(Object obj) {
			if(this.displayName != null && this.displayName.equals(obj.toString())) {
				return true;
			}
			if(this.className.equals(obj.toString())) {
				return true;
			}
			return false;
		}

		@Override
		public String toString() {
			if(displayName != null) return displayName;
			return className;
		}

		ListData(String cName) {
			setClassName(cName);
		}

		ListData(String dName, String[] props, String cName) {
			setDisplayName(dName);
			setClassName(cName);
			setProperties(props);
		}

		void setDisplayName(String dName) {
			displayName = dName;
		}

		void setProperties(String[] props) {
			properties = new String[props.length];
			for(int i = 0; i < props.length; i++) properties[i] = props[i];
		}

		void setClassName(String cName) {
			className = cName;
		}
	}
}
