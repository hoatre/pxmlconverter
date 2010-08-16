package dtdParser;

import java.util.Vector;

import def.Constants;
import def.Table;

import schemaless.*;

public class SQL extends def.SQL {
	public static final String SHAREDPREFIX = "shared_";
	
	public static Vector<Table> createSharedTables(Vector<Node> v, String inputFilename, String dir) {
		Vector<Table> result = new Vector<Table>();
		for (int i = 0; i < v.size(); i++) {
			result.add(createSharedTable(v.get(i), inputFilename, dir));
		}
		return result;
	}
	
	public static Table createSharedTable(Node n, String inputFilename, String dir) {
		Table nodeTable = new Table(SHAREDPREFIX , inputFilename, "_"+n.getName(), dir);
		nodeTable.createColumn(n.getName()+ID, INT);
		if (n.getParents().size() > 0) {
			nodeTable.createColumn(n.getName()+ ".parent." +ID, INT);
			//nodeTable.createColumn(n.getName()+ ".parentCODE", INT); //Snap nog steeds niet waarvoor deze column moet worden aangemaakt
		}
		
		for (String key : n.getAtts().keySet()) {
			DTDAttribute att = n.getAtts().get(key); //TODO ff nakijken wat voor types attribute allemaal kan zijn
			nodeTable.createColumn(key, STRING);
		}
		
		if (n.isPCDATA()) {
			nodeTable.createColumn("text", STRING);
		} else {
			for (Node child : n.getChildren()) {
				nodeTable.createColumn(n.getName()+".child_"+child.getName()+"."+ID, INT);
			}
		}
		
		SQL.createTable(nodeTable);
		
		return nodeTable;
	}
}
