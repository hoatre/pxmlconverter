package dtdParser;

import java.util.Hashtable;
import java.util.Vector;

public class Node {
	private String name;
	private boolean visited;
	private Vector<DTDElement> parents;
//	private Vector<Node> pars; //weet niet goed welke variabele ik ga gebruiken
	private Hashtable<String, DTDAttribute> atts;
	private Vector<Node> children;
	
	public Node(String name) {
		this.name = name;
		this.visited = false;
		this.parents = new Vector<DTDElement>();
		this.atts = new Hashtable<String, DTDAttribute>();
		this.children = new Vector<Node>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Vector<DTDElement> getParents() {
		return parents;
	}
	
	public void addParent(DTDElement parent) {
		this.parents.add(parent);
	}
	
	public static Node getNode(Vector<Node> v, String name) {
		Node result = null;
		for (int i = 0; result == null && i < v.size(); i++) {
			if (v.get(i).getName().equals(name)) {
				result = v.get(i);
			}
		}
		return result;
	}
	
	public void setAttributes(Hashtable<String, DTDAttribute> atts) {
		this.atts = atts;
	}
	
	public Hashtable<String, DTDAttribute> getAtts() {
		return this.atts;
	}

	public void addChild(Node childNode) {
		this.children.add(childNode);
	}

	public Vector<Node> getChildren() {
		return children;
	}

	public String toStringChildren() {
		String result = " {";
		for (Node child : children) {
			result += child + " ";
		}
		result += "}";
		return result;
	}
	
	public String toStringParents() {
		String result = " {";
		for (DTDElement par : parents) {
			result += par + " ";
		}
		result += "}";
		return result;
	}	
	
	public String toString() {
		return this.name;
	}

	public boolean isPCDATA() {
		return this.children.size() == 1 && this.children.firstElement().toString().equals("PCDATA");
	}
}
