package dtdParser;

import java.util.Vector;

import def.Constants;
import def.Table;

import schemaless.*;

public class SQL extends def.SQL {
	
	public static void createSharedTables(Vector<DTDNode> v) {
		for (int i = 0; i < v.size(); i++) {
			createSharedTable(v.get(i));
		}
	}
	
	public static final String PARENTPREFIX = "parent_";
	public static final String CONTENTPREFIX = "content_";
	public static final String CHILDPREFIX = "child_";
	public static final String TEMPPREFIX = "temp_";
	
	public static void createSharedTable(DTDNode n) {
		Table nodeTable = new Table(SHAREDPREFIX , n.getInputFilename(), "_"+n.getName(), n.getDir());
		createSharedColumns(n, nodeTable);
		SQL.createTable(nodeTable);
		n.addTable(nodeTable);
	}
	
	public static void createSharedColumns(DTDNode n, Table nodeTable) {
		nodeTable.createColumn(ID, INT);
		if (n.getParents().size() > 0) {
			nodeTable.createColumn(PARENTPREFIX + ID, INT);
			//nodeTable.createColumn(n.getName()+ ".parentCODE", INT); //Snap nog steeds niet waarvoor deze column moet worden aangemaakt
		}
		createSharedAttColumns(n, nodeTable);
		createSharedChildColumns(n, nodeTable);
	}
	
	public static void createSharedChildColumns (DTDNode n, Table nodeTable) {
		for (DTDNode child : n.getChildren()) {
			//if (n.isPCDATA()) {
			//	nodeTable.createColumn(CHILDPREFIX+CONTENTPREFIX+n.getName(), STRING);
			//} else {
			nodeTable.createColumn(CHILDPREFIX+child.getName()+ID, INT);
			//}
		}
		
		for (DTDNode child : n.getInlined()) {
			if (n.isPCDATA()) {
				nodeTable.createColumn(CHILDPREFIX+CONTENTPREFIX+n.getName(), STRING);
			} else {
				nodeTable.createColumn(CHILDPREFIX+CONTENTPREFIX+child.getName(), INT);
				createSharedChildColumns(child, nodeTable);
			}
		}
	}
	
	public static void createSharedAttColumns (DTDNode n, Table nodeTable) {
		for (String key : n.getAtts().keySet()) {
			DTDAttribute att = n.getAtts().get(key); //TODO ff nakijken wat voor types attribute allemaal kan zijn
			nodeTable.createColumn("att_" + key, STRING);
		}
	}
	
	public static void uninlineNode(DTDNode inlinedChild, DTDNode par) {
		//rekening houden met de inlinedChildren van inlinedChild
		
		//inlinedChild voorzien van tabel
		Table childTable = par.getTable().getInlineTableCopy(inlinedChild);
		createSharedColumns(inlinedChild, childTable);
		inlinedChild.addTable(childTable);
		
		//par voorzien van geupdate tabel
		Table parTable = par.getTable().getInlineTableCopy(par);
		createSharedColumns(par, parTable);
		inlinedChild.addTable(parTable);
		
		//SQL statements uitvoeren
		childTable.printlnToFile(DROPTABLE + IF + EXISTS + childTable + CASCADE +PUNTKOMMA);
		
		//TODO nog geen rekening gehouden met attributen en children van de childTable.
		/*
		 * CREATE TABLE year AS
		 * (SELECT id,  id as parent_id, year as year_content FROM movies);
		*/
		childTable.printlnToFile(CREATETABLE + childTable + AS);
		childTable.printlnToFile(HAAKJEOPEN);
		childTable.printlnToFile(SELECT + ID + KOMMA + ID + AS + PARENTPREFIX+ID + KOMMA + parTable.getColumn(inlinedChild.getName()) + FROM + parTable);
		childTable.printlnToFile(HAAKJESLUITEN + PUNTKOMMA);
		
		/*
		 * ALTER TABLE movies
		 * DROP COLUMN year;
		 */
		parTable.printlnToFile(ALTERTABLE + parTable);
		parTable.printlnToFile(DROPCOLUMN + parTable.getColumn(CONTENTPREFIX + inlinedChild.getName()) + PUNTKOMMA);
		
		/*
		 * CREATE TABLE movies_temp AS
		 * ( SELECT movies.*, year.id as year_id FROM movies LEFT JOIN year ON movies.id = year.parent_id);
		 */
		parTable.printlnToFile(CREATETABLE +TEMPPREFIX + parTable + AS);
		parTable.printlnToFile(HAAKJEOPEN + SELECT + parTable + DOT + ASTERIX + KOMMA + childTable + DOT + childTable.getColumn(ID) + AS + parTable.getColumn(CHILDPREFIX+inlinedChild.getName()+ID) + FROM + parTable);
		parTable.printlnToFile(LEFTJOIN + childTable + ON + parTable + DOT + ID + EQUALS + childTable + DOT + PARENTPREFIX + ID + PUNTKOMMA);
		
		/*
		 * DROP TABLE movies;
		 * ALTER TABLE movies_temp
		 * RENAME TO movies;
		 */
		parTable.printlnToFile(DROPTABLE + parTable + CASCADE); //TODO oude foreign keys wordt niets mee gedaan, evenals checks!
		parTable.printlnToFile(ALTERTABLE + TEMPPREFIX + parTable);
		parTable.printlnToFile(RENAMETO + parTable + PUNTKOMMA);
		
		/*
		 * ALTER TABLE year
		 * ADD PRIMARY KEY (id);
		 */
		childTable.printlnToFile(ALTERTABLE + childTable);
		childTable.printlnToFile(ADD + PRIMARYKEY + HAAKJEOPEN + ID + HAAKJESLUITEN + PUNTKOMMA);
		
		/*
		 * ALTER TABLE movies
		 * ADD FOREIGN KEY (year_id) REFERENCES year(id),
		 * ADD PRIMARY KEY (id);
		 */
		parTable.printlnToFile(ALTERTABLE + parTable);
		parTable.printlnToFile(ADD + FOREIGNKEY + HAAKJEOPEN + parTable.getColumn(CHILDPREFIX+inlinedChild.getName()+ID) + HAAKJESLUITEN);
		parTable.printlnToFile(REFERENCES + childTable + HAAKJEOPEN + childTable.getColumn(ID) + HAAKJESLUITEN + PUNTKOMMA);
		
		/*
		 * ALTER TABLE year
		 * ADD FOREIGN KEY (parent_id) REFERENCES movies(id);
		 */
		childTable.printlnToFile(ALTERTABLE + childTable);
		childTable.printlnToFile(ADD + FOREIGNKEY + HAAKJEOPEN + childTable.getColumn(PARENTPREFIX + ID) + HAAKJESLUITEN);
		childTable.printlnToFile(REFERENCES + parTable + HAAKJEOPEN + parTable.getColumn(ID) + HAAKJESLUITEN + PUNTKOMMA);
	}

	
	/*
CREATE TABLE year AS
(SELECT id,  id as parent_id, year as year_content FROM movies);

ALTER TABLE movies
DROP COLUMN year;

CREATE TABLE movies_temp AS
( SELECT movies.*, year.id as year_id FROM movies LEFT JOIN year ON movies.id = year.parent_id);

DROP TABLE movies;
ALTER TABLE movies_temp
RENAME TO movies;

ALTER TABLE year
ADD PRIMARY KEY (id);

ALTER TABLE movies
ADD FOREIGN KEY (year_id) REFERENCES year(id),
ADD PRIMARY KEY (id);

ALTER TABLE year
ADD FOREIGN KEY (parent_id) REFERENCES movies(id);

	 */
}
