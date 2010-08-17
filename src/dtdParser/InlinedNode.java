package dtdParser;

import java.util.Vector;

import def.Constants;
import def.SQL;
import def.Table;


public class InlinedNode {
	public static final String PREFIX = "IN_";
	
	private Vector<InlinedNode> children;
	private Vector<DTDNode> inlinedNodes;
	private String tableName;
	private String directory;
	private String nodetype;
	
	public InlinedNode() {
		children = new Vector<InlinedNode>();
		inlinedNodes = new Vector<DTDNode>();
	}
	
	public void addInlinedNode(DTDNode node) {
		inlinedNodes.add(node);
		//node.setVisited(true);
	}
	
	public void addChild(InlinedNode node) {
		children.add(node);
	}
	
	public boolean containsNode(DTDNode node) {
		return inlinedNodes.contains(node);
	}
	
	public boolean containsNode(String node) {
		boolean result = false;
		for (int i = 0; i < inlinedNodes.size() && !result; i++) {
			result = (inlinedNodes.get(i).getName().equals(node));
		}
		return result;
	}
	
	public static InlinedNode getNode(Vector<InlinedNode> v, String name) {
		InlinedNode result = null;
		for (int i = 0; result == null && i < v.size(); i++) {
			if (v.get(i).containsNode(name)) {
				result = v.get(i);
			}
		}
		return result;
	}
	
	public static InlinedNode getNode(Vector<InlinedNode> v, DTDNode node) {
		return getNode(v, node.getName());
	}
	
	public String toString() {
		String result = "";
		for (int i = 0; i < inlinedNodes.size(); i++) {
			result += inlinedNodes.get(i).getName() + "_";
		}
		return result;
	}
	
	public Table getTable() {
 		Table result = new Table(PREFIX, tableName,  "", directory);
 		
 		result.createColumn(Constants.ID, Constants.INT);
 		
 		result.createColumn(nodetype, Constants.STRING); //TODO nodetype is niet gedefinieerd, gelijk aan rootelement
 		
 		for (int i = 0; i < inlinedNodes.size(); i++) {
 			DTDNode inlined = inlinedNodes.get(i);
 			for (String att:inlined.getAtts().keySet()) {
 				//TODO default value gebruiken
 				result.createColumn(inlined.getName() + "_" + att, Constants.INT); //in artikel staat dat dezelfde naam moet worden gebruikt als het attribute
 			}
 		}
 		
 		//TODO foreign keys
 		
 		result.setPrimaryKey(result.getColumn(Constants.ID));		
		SQL.createTable(result);
 		
 		return result;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
		
	}
	
	
}
