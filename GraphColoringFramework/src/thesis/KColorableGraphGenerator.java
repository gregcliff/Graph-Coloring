package thesis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.neo4j.graphdb.Node;

@RunInformation(name = "K Colorable Graph", properties = { "#Nodes=50", "#Edges=100", "K", "RandomSeed" })
public class KColorableGraphGenerator extends BaseGraphConfiguration{
	ArrayList<Integer> primes;
	final int RANDMAX = 1000;
	boolean output = false;
	int n;
	int e;
	int k;
	int a;
	int c;
	int m;
	int X0;
	int X[];
	int Y0;
	int Y[];
	int b[];
	BufferedReader br;

	public KColorableGraphGenerator(String[] properties) {
		super(properties);
		this.n = Integer.valueOf(properties[0]);
		super.numNodes = n;
		this.e = Integer.valueOf(properties[1]);
		super.numRels = e;
		this.k = Integer.valueOf(properties[2]);
		try {
			this.br = new BufferedReader(new FileReader("10000primes.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(properties.length < 4 || Integer.valueOf(properties[3]) < 0 || properties[3].equals("")) {
			long t = System.currentTimeMillis();
			double r = Math.random();
			rand = (int)((t*r)%10000);
		} else {
			rand = Integer.valueOf(properties[3]);
		}
	}

	public void initB() {
		this.b = new int[k-1];
		for(int i = 1; i < k - 1; i++) {
			b[i] = 0;
		}
		int index;
		b[0] = 1;
		int edgesRemaining = this.e - edgeAdditionsBasedOnIndex(0);		
		while(edgesRemaining > 0) {
			index = random.nextInt(k-2) + 1;
			if(edgeAdditionsBasedOnIndex(index) > edgesRemaining) continue;
			b[index]++;
			edgesRemaining -= edgeAdditionsBasedOnIndex(index);
		}
		if(output) {
			System.out.println();
			for(int i = 0; i < k - 1; i++) {
				System.out.print(b[i] + ", ");
			}
			System.out.println();
		}
	}

	public int edgeAdditionsBasedOnIndex(int index) {
		return summation(k-index);
	}

	public int summation(int k) {
		return (k*(k-1))/2;
	}

	public void initXandY() {
		X0 = random.nextInt(m);
		X = new int[m];
		Y = new int[m];
		X[0] = X0;
		Y[0] = X0%n;
		for(int i = 1; i < m; i++) {
			X[i] = (a*X[i-1] + c) % m;
			Y[i] = X[i]%n;
		}

		if(output) {
			for(int i = 0; i < m; i++) {
				System.out.print(X[i] + ", ");
			}
			System.out.println();
			for(int i = 0; i < m; i++) {
				System.out.print(Y[i] + ", ");
			}
		}
	}

	public void setOutput(boolean o) {
		output = o;
	}

	public void initACM() throws Exception {
		if(!divides(k,n)) {
			while(!divides(k, n)) {
				n++;
			}
			System.out.println("Changing n to " + n + " because k did not divide n.");
		}
		int tempA = a();
		int tempM = m(n);
		while(gcd(tempM, n) != k) {
			tempM = m(n);
		}
		this.primes = populatePrimesList(tempM);
		int tempC = c();
		while(gcd(tempC, tempM) != 1) {
			tempC = c();
		}
		while(!primesDividesMImpliesDividesA_1(primes, tempA, tempM)) {
			tempA = a();
		}
		if(checkACM(tempA, tempC, tempM, primes)) {
			a = tempA;
			c = tempC;
			m = tempM;
		} else {
			throw new Exception("Variables not correctly setup.");
		}
	}

	public void output() {
		System.out.println("n: " + n);
		System.out.println("m: " + m);
		System.out.println("a: " + a);
		System.out.println("c: " + c);
		System.out.println(checkACM(a, c, m, primes));
	}

	public ArrayList<Integer> populatePrimesList(int m) {
		ArrayList<Integer> primes = new ArrayList<Integer>();
		try {
			Integer p = nextNumber();
			while(p < m) {
				primes.add(p);
				p = nextNumber();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return primes;
	}

	private boolean primesDividesMImpliesDividesA_1(ArrayList<Integer> primes, int a, int m) {
		boolean condition4;
		boolean condition5;
		int a_1 = a - 1;	
		condition4 = true;
		for(int i = 0; i < primes.size(); i++) {
			int p = primes.get(i);
			if(p > a) break;
			if(divides(p, m)) {
				if(!divides(p, a_1)) {
					return false;
				}
			}
		}
		if(divides(4, m)) {
			if(divides(4, a_1)) {
				condition5 = true;
			} else {
				return false;
			}
		} else {
			return condition4;
		}

		if(condition4 && condition5) {
			return true;
		}
		return false;
	}

	private Integer nextNumber() throws IOException, Exception {
		String num = "";
		int space = (int)' ';
		int c = 0;
		boolean numberStarted = false;
		while(c > -1) {
			c = br.read();
			if(numberStarted) {
				if(c < '0' || c > '9') {
					break;
				} else num += (char)c;
			}
			if(c == space) continue;
			if(!numberStarted && c >= '0' && c <= '9') {
				numberStarted = true;				
				num += (char)c;
			}
		}
		return Integer.valueOf(num.trim());
	}

	public int m(int n) {
		int m = random.nextInt(RANDMAX);
		while((m-n) < (2*n) || m == 0) m = random.nextInt(RANDMAX);
		return m;
	}

	public int a() {
		int a = random.nextInt(RANDMAX);
		while(a <= 0) a = random.nextInt(RANDMAX);
		return a;
	}

	public int c() {
		int c = random.nextInt(RANDMAX);
		while(c <= 0) c = random.nextInt(RANDMAX);
		return c;
	}

	public boolean checkACM(int a, int c, int m, ArrayList<Integer> primes) {
		if(gcd(n, m) != k) return false;
		if(gcd(c, m) != 1) return false;
		if(!primesDividesMImpliesDividesA_1(primes, a, m)) return false;
		return true;
	}

	public boolean divides(int a, int d) {
		for(int i = (d/2)+1; i > 0; i--) {
			if(i*a==d) {
				//System.out.println(a + " * " + i + " = " + d);
				return true;
			}
		}
		return false;
	}

	public int gcd(int m, int n) {
		//ArrayList<Integer> qList = new ArrayList<Integer>();
		//ArrayList<Integer> rList = new ArrayList<Integer>();
		if(m <= 0 || n <= 0) return -1;
		if(m == n) return m;
		int gcd = 1;
		if(m < n) {
			int copy = m;
			m = n;
			n = copy;
		}

		int a = m;
		int q = a/n;
		int rn = n;
		int r = a%(rn*q);
		//m = n*q + r -> m/n = q and m%(n*q) == r 
		//n = r*q + r
		//printEquation(m, rn, q, r);
		while(r != 0) {
			a = rn;
			q = a/r;
			rn = r;
			r = a%(rn*q);
			//printEquation(a, rn, q, r);
		}
		gcd = rn;
		return gcd;
	}

	public void printEquation(int rn, int rn1, int q, int r) {
		System.out.println(rn + " = " + rn1 + "*" + q + " + " + r);
	}

	@Override
	public void generateNodes() {
		numNodes = n;
		for(int i = 0; i < n; i++) {
			Node node = super.createGraphColoringNode();
			node.setProperty("name", node.getId());
		}
	}

	@Override
	public void generateRels() {
		int edgesAdded = 0;
		int indexY = 0;
		int indexB = 0;
		int curK = this.k;
		for(int k = 0; k < b.length; k++) {
			for(int j = 0; j < b[k]; j++) {
				ArrayList<Node> nodes = new ArrayList<Node>();
				for(int i = 0; i < curK; i++) {
					int id = Y[i+indexY];
					Node node = graphDb.getNodeById(id);
					nodes.add(node);
				}
				indexY += curK;
				createClique(nodes);
				edgesAdded += edgeAdditionsBasedOnIndex(k);
			}
			curK--;
		}
	}

	public void createClique(ArrayList<Node> nodes) {
		Iterator<Node> nodeIt = nodes.iterator();
		Node node;
		while(nodeIt.hasNext()) {
			node = nodeIt.next();
			for(Node neighbor : nodes) {
				if(neighbor.equals(node)) continue;
				super.createGraphColoringRelationship(node, neighbor);
			}
			nodeIt.remove();
		}
	}

	@Override
	public void run() {
		try{
			this.initACM();
			this.initXandY();
			this.initB();
		} catch (Exception e) {
			System.out.println("Exception caught while initializing random uniform number sequence.");
			System.exit(-1);
		}
		generateNodes();
		generateRels();
	}

	public void setRandom(Random r) {
		random = r;
		random.setSeed(rand);
	}
}
