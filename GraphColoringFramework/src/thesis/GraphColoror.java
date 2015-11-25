package thesis;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public abstract class GraphColoror {
	private static ArrayList<Color> colorSet;
	private static Map<String, Integer> colorCounts;
	private static int colorIndex;
	private static byte brighterCalled = 0;
	private static byte darkerCalled = 0;
	public Random random;
	public static double executionStartTime;
	
	public void setRandom(Random r) {
		random = r;
	}
	
	public static void startExecutionTime() {
		executionStartTime = System.currentTimeMillis();
	}
	
	public static double getExecutionTime() {
		return System.currentTimeMillis() - executionStartTime;
	}
	
	public Set<Long> currentColoring;
	{
		colorCounts = new HashMap<String, Integer>();
		colorSet = new ArrayList<Color>();
		colorSet.add(colorWithName(Color.WHITE, "white"));
		colorSet.add(colorWithName(Color.RED, "red"));
		colorSet.add(colorWithName(Color.GREEN, "green"));
		colorSet.add(colorWithName(Color.BLUE, "blue"));
		colorSet.add(colorWithName(Color.MAGENTA, "magenta"));
		colorSet.add(colorWithName(Color.YELLOW, "yellow"));
		colorSet.add(colorWithName(Color.CYAN, "cyan"));
		colorSet.add(colorWithName(Color.ORANGE, "orange"));
		colorIndex = 0;
		nextColor();
	}

	public abstract void colorNext();

	public void solve(int numNodes) {
		for(int i = 0; i < numNodes; i++) {
			colorNext();
		}
	}
	
	private static String RGBString(Color color) {
		String rgb = "";
		rgb = colorString(color.getRed()) + colorString(color.getGreen()) + colorString(color.getBlue());
		return rgb;
	}

	public String nextColor() {
		if(colorIndex == colorSet.size()) {
			addColorsByTint();
			//addColors();
		}
		String color = RGBString(colorSet.get(colorIndex));
		colorIndex++;
		//colorCounts.put(color, 1);
		return color;
	}

	public String currentColor() {
		String color = RGBString(colorSet.get(colorIndex-1));
		//colorCounts.put(color, colorCounts.get(color)+1);
		return color;
	}

	public void incrementColorCount(String color) {
		Integer count = colorCounts.get(color);
		if(count == null) 
			colorCounts.put(color, 1);
		else 
			colorCounts.put(color, count+1);
	}
	
	public static void addColorsByTint() {
		/*if(brighterCalled >= darkerCalled)
			addBrighterColors();
		else*/
		addDarkerColors();
	}
	
	public static int addBrighterColors() {
		int added = 0;
		brighterCalled++;
		for(int i = 1; i < 8; i++) {
			Color oldColor = colorSet.get(i);
			Color newColor = oldColor.brighter();
			for(int j = 1; j < brighterCalled; j++)
				newColor = newColor.brighter();
			if(!colorSet.contains(newColor)) {
				colorSet.add(newColor);
				added++;
			}
		}
		return added;
	}
	
	public Color colorWithName(Color oldColor, final String name) {
		final Color tempColor = oldColor;
		final Color newColor = new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue()) {
			@Override
			public String toString() {
				return name;
			}
		};
		return newColor;
	}
	
	public static int addDarkerColors() {
		int added = 0;
		darkerCalled++;
		for(int i = 1; i < 8; i++) {
			Color oldColor = colorSet.get(i);
			final String baseName = oldColor.toString();
			int j = 0;
			while(j < darkerCalled) {
				oldColor = oldColor.darker();
				j++;
			}
			final int k = j;
			final Color tempColor = oldColor;
			final Color newColor = new Color(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue()) {
				@Override
				public String toString() {
					String name = baseName;
					for(int m = 0; m < k; m++)
						name = "dark " + name;
					return name;
				}
			};
			if(!colorSet.contains(newColor)) {
				colorSet.add(newColor);
				added++;
			}
		}
		return added;
	}

	public static void addColors() {
		String colorString;
		String lastColor = RGBString(colorSet.get(colorSet.size()-1));
		int old = Integer.parseInt(lastColor.substring(6, 9));
		if((colorIndex-1)%12 > 6) {
			old = (old + (256-old)/2) % 256;
		}
		int newC = old/2;
		for(int i = 0; i < 6; i++) {
			int r = 0, g = 0, b = 0;
			switch(i) {
			case 0: {
				r = newC;
				g = old;
				b = old;
				break;
			}
			case 1: {
				r = old;
				g = newC;
				b = old;
				break;
			}
			case 2: {
				r = old;
				g = old;
				b = newC;
				break;
			}
			case 3: {
				r = newC;
				g = newC;
				b = old;
				break;
			}
			case 4: {
				r = newC;
				g = old;
				b = newC;
				break;
			}
			case 5: {
				r = old;
				g = newC;
				b = newC;
				break;
			}
			}
			String newR = colorString(String.valueOf(r));
			String newG = colorString(String.valueOf(g));
			String newB = colorString(String.valueOf(b));
			colorString =  newR + newG + newB;
			Color color = new Color(r, g, b);
			colorSet.add(color);
		}
	}

	public Map<String, Integer> getColorCounts() {
		return colorCounts;
	}
	
	public Map<String, Integer> getColorCountsAsNames() {
		HashMap<String, Integer> namedColorCounts = new HashMap<String, Integer>();
		Iterator<Color> colors = colorSet.iterator();
		while(colors.hasNext()) {
			Color color = colors.next();
			namedColorCounts.put(color.toString(), colorCounts.get(RGBString(color)));
		}
		return namedColorCounts;
	}

	public int getColorIndex() {
		return colorIndex;
	}

	private static String colorString(String string) {
		if(string.length() == 3) return string;
		else if(string.length() == 2) {
			string = "0" + string;
			return string;
		} else {
			string = "00" + string;
			return string;
		}
	}
	
	private static String colorString(int value) {
		String string = String.valueOf(value);
		if(string.length() == 3) return string;
		else if(string.length() == 2) {
			string = "0" + string;
			return string;
		} else {
			string = "00" + string;
			return string;
		}
	}
}
