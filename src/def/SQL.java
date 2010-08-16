package def;
import java.util.Vector;

import schemaless.Import;

/**
 * SQL makes I-SQL-instructions.
 * @author Paul Stapersma
 */
public class SQL implements Constants {
    
    /**
     * Translates @param table in I-SQL instructions.
     * @requires: table must have at least one column 
     */
    public static void createTable(Table table) {
    	//check is filled with tableChecks
    	String check = "";
		if (table.getCheck(table.getColumns().get(0)) != null) {
			check = CHECK + SPACE + table.getCheck(table.getColumns().get(0));
		}
		
		//DROPTABLE ensures that the table can be created in the database 
		table.printlnToFile(DROPTABLE + IF + EXISTS + table + CASCADE +PUNTKOMMA);
    	table.printlnToFile(CREATETABLE + table);
    	table.printlnToFile(HAAKJEOPEN);
    	
    	//Column instructions
    	table.printlnToFile(table.getColumns().get(0).getName() + " " + table.getColumns().get(0).getType() + SPACE + check);
    	for(int i = 1; i < table.getColumns().size(); i++) {
    		check = "";
    		Column column = table.getColumns().get(i);
    		if (table.getCheck(column) != null) {
    			check = CHECK + SPACE + HAAKJEOPEN + column.getName() + table.getCheck(column) + HAAKJESLUITEN;
    		}
    		table.printlnToFile(KOMMA + column.getName() + SPACE + column.getType() + SPACE + check);
    	}
    	
    	//Primary key instructions
    	if (table.getPrimaryKey() != null)
    		table.printlnToFile(KOMMA + PRIMARYKEY + SPACE + HAAKJEOPEN + table.getPrimaryKey() + HAAKJESLUITEN);
    	
    	//Foreign key instructions
    	for (ForeignKey k: table.getForeignKeys()) {
    		table.printlnToFile(KOMMA + FOREIGNKEY + HAAKJEOPEN + k.getColumn() + HAAKJESLUITEN + SPACE + REFERENCES + SPACE + k.getOtherTable() + HAAKJEOPEN + k.getOtherColumn() + HAAKJESLUITEN);
    	}
    	table.printlnToFile(HAAKJESLUITEN + PUNTKOMMA);    	
    	
    	//puts "SELECT * FROM @table;" in the file "select.txt". This sentence can be omitted
    	Import.selectInsert(table); //TODO vies
    	
    }
    
    /**
     * Translates @node into I-SQL instructions and puts these instructions into @accel
     * @param accel
     * @param node
     */
    public static void insertNode(Table accel, Node node) {
    	accel.printlnToFile(INSERTINTO + accel);
    	accel.printlnToFile(VALUES);
    	accel.printlnToFile(HAAKJEOPEN 
    			+ node.getPre() + KOMMA 
    			+ node.getPost() + KOMMA
    			+ node.getPar() + KOMMA
    			+ node.getKind() + KOMMA
    			+ QUOTE + node.getTag() + QUOTE + KOMMA 
    			+ node.getLevel() + KOMMA
    			+ node.getPosscode()
    			+ HAAKJESLUITEN + PUNTKOMMA);
    }
    
    /**
     * Translates @param attr into I-SQL instructions. @param attr is handled like an XML node and @param value is handled like the node content of @param attr.
     * @param accel
     * @param content
     * @param attr
     * @param value
     */
    public static void insertAttribute(Table accel, Table content, Node attr, String value) {
		insertNode(accel, attr);
		insertAttrCDATA(content, attr, value);
    }
    
    /**
     * Stores textnodes into @param accel and the content of these textnodes into @param content.
     * @param accel
     * @param content
     * @param cdata
     * @param parent
     */
    public static void insertNodeCDATA(Table accel, Table content, String cdata, Node textNode) {
    	insertCDATA(content, textNode, cdata);
    	insertNode(accel, textNode);

    }
    
    /**
     * Stores the content of the attribute node @param attrParent into @param content
     * @param content
     * @param attrParent
     * @param cdata
     */
    public static void insertAttrCDATA(Table content, Node attrParent, String cdata) {
    	insertCDATA(content, attrParent, cdata);
    }
    
    /**
     * Puts content @param cdata into table @param content and puts a reference to the parentNode @param parant, which is of type Node.ATTRIBUTE or Node.TEXTNODE
     * @param content
     * @param parent
     * @param cdata
     */
    public static void insertCDATA(Table content, Node parent, String cdata) {
    	content.printlnToFile(INSERTINTO + SPACE + content);
    	content.printlnToFile(VALUES);
    	content.printlnToFile(HAAKJEOPEN + parent.getPre() + KOMMA + "E" + QUOTE + cdata + QUOTE + HAAKJESLUITEN + PUNTKOMMA);    	
    }
    
    /**
     * Translates @param poss into I-SQL instructions and stores these instructions into @param ucp
     * @param ucp
     * @param poss
     */
    public static void insertPoss(Table ucp, PossNode poss) {
    	ucp.printlnToFile(INSERTINTO + SPACE + ucp);
    	ucp.printlnToFile(VALUES);
    	ucp.printlnToFile(HAAKJEOPEN + poss.getId() + KOMMA + poss.getVal() + KOMMA + poss.getProb() + KOMMA + poss.getPre() + KOMMA + poss.getPost() + KOMMA + poss.getPhase() + HAAKJESLUITEN + PUNTKOMMA);
    }
    
    /**
     * Given a @param table with @param key and @param weight, @returns an initialized repairkey table
     * @param table
     * @param key
     * @param weight
     * @return initialized rapairkey table
     */
    public static Table repairKey(Table table, Column key, Column weight, String prefix) {
    	Table repairKey = table.getRKTableCopy(prefix);
    	repairKey.printlnToFile(DROPTABLE + IF + EXISTS + repairKey + CASCADE + PUNTKOMMA);
    	repairKey.printlnToFile("create table " + repairKey +  " as repair key "+ key.getName() +" in "+table+" weight by " + weight.getName() + PUNTKOMMA);
    	return repairKey;
    }
    
    /**
     * Adds an extra column to @param table for @param poss.
     * @deprecated
     * @param table
     * @param pos
     */
    public static void insertPossColumn(Table table, ProbNode pos) {
    	table.printlnToFile("ALTER TABLE "+ table +" ADD cp"+ pos.getCode() +" INT NULL" + PUNTKOMMA);
    	table.printlnToFile("ALTER TABLE "+ table +" ADD cp"+ pos.getCode() + PREFIX_PROBCOLUMN +" FLOAT NOT NULL DEFAULT 1" + PUNTKOMMA);
    }
    
    /**
     * Fills the PossColumns, made with insertPossColumn(table, pos)
     * @deprecated
     * @param table
     * @param pos
     * @param prob
     */
    public static void updatePossColumn (Table table, ProbNode pos, PossNode prob) {
    	table.printlnToFile("UPDATE "+ table +" SET cp" + pos.getCode() + " = 0" + 
    						" WHERE " + table + "." + "pre>=" + prob.getPre() + " AND " + table + "." + "post<=" + prob.getPost() + PUNTKOMMA);
    	table.printlnToFile("UPDATE "+ table +" SET cp" + pos.getCode() + PREFIX_PROBCOLUMN +" = " + prob.getProb() + 
							" WHERE " + table + "." + "pre>=" + prob.getPre() + " AND " + table + "." + "post<=" + prob.getPost() + PUNTKOMMA);    	
    	/*
    	accel.printlnToFile("UPDATE "+ accel +" SET " + pos.getCode() + " = " + "1" + 
							" WHERE " + accel + "." + "pre<" + prob.getPre() + " OR " + accel + "." + "post>" + prob.getPost() + PUNTKOMMA);
    	accel.printlnToFile("UPDATE "+ accel +" SET " + pos.getCode() + PROBCOLUMNPREFIX+ " = " + "1" + 
							" WHERE " + accel + "." + "pre<" + prob.getPre() + " OR " + accel + "." + "post>" + prob.getPost() + PUNTKOMMA);
		*/
    }
    
    /**
     * Given @param poss, gives the key for a repairKey-operation
     * @deprecated
     * @param poss
     * @return repairKey key
     */
    @SuppressWarnings("unused")
	private static String getRepairKeys(Vector<ProbNode> poss) {
    	String result = "";
    	if (poss.size() > 0) {
    		result += "cp"+poss.get(0).getCode(); 
    		for (int i = 1; i < poss.size(); i++) {
    			result += ",cp" + poss.get(i).getCode();
    		}
    	}
    	return result;
	}
    
    /**
     * Given @param poss, gives the weight for a repairKey-operation
     * @deprecated
     * @param poss
     * @return repairKey weight
     */
    @SuppressWarnings("unused")
	private static String getRepairProbs(Vector<ProbNode> poss) {
    	String result = "(";
    	if (poss.size() > 0) {
    		result += "cp"+poss.get(0).getCode() + PREFIX_PROBCOLUMN;
    		for (int i = 1; i < poss.size(); i++) {
    			result += "*cp" + poss.get(i).getCode() + PREFIX_PROBCOLUMN;
    		}
    	}
    	result+= ")";    	
    	return result;
	}
    
    /**
     * Create PhaseTables, given a magic query.
     * @param table
     * @param ucp
     * @param cp
     * @param phase
     * @return phaseTable
     */
    public static Table createPhaseTable(Table table, Table ucp, Table cp, int phase) {
    	Table phaseTable = table.getRKTableCopy(PREFIX_UACCEL + phase);
    	phaseTable.printlnToFile(
    	DROPTABLE + IF + EXISTS + phaseTable + CASCADE + PUNTKOMMA +
    	CREATETABLE + phaseTable + AS +
    	  HAAKJEOPEN +
    	  SELECT + table + ".*" +
    	  FROM + table + KOMMA + ucp +
    	  WHERE + table + DOT + PREORDER + ">" + ucp + DOT + PREORDER + 
    	    AND + table + DOT + POSTORDER + "<" + ucp + DOT + POSTORDER +
    	    AND + ucp + DOT + PHASE + "=" + phase +
    	  HAAKJESLUITEN +
    	  UNION + ALL +
    	  HAAKJEOPEN +
    	  SELECT + table + ".*" +
    	  FROM + table + KOMMA + ucp +
    	  WHERE + ucp + DOT + PHASE + "=0" +
    	    AND + NOT + EXISTS + HAAKJEOPEN + 
    	           SELECT + ASTERIX + FROM + cp + 									//hier stond eigenlijk cp
    	           WHERE + table + DOT + PREORDER + ">" + cp + DOT + PREORDER + 	//hier stond eigenlijk cp
    	           	 AND + table + DOT + POSTORDER + "<" + cp + DOT + POSTORDER +	//hier stond eigenlijk cp
    	             AND + cp + DOT + PHASE + "=" + phase +							//hier stond eigenlijk cp
    	        HAAKJESLUITEN +
    	  HAAKJESLUITEN + PUNTKOMMA
    	);
    	return phaseTable;
    }

	public static void insertNodeCDATA(Vector<Table> sharedTables,
			String cdata, Node textNode) {
		// TODO Auto-generated method stub
		
	}

	public static void insertNodeCDATA(Vector<Table> sharedTables,
			String cdata, Node textNode) {
		// TODO Auto-generated method stub
		
	}

	public static void insertNode(Vector<Table> sharedTables, Node elem) {
		// TODO Auto-generated method stub
		
	}

	public static void insertAttribute(Vector<Table> sharedTables, Node attr,
			String value) {
		// TODO Auto-generated method stub
		
	}
    
}
/*
SELECT p.tag,q.tag, conf()
FROM uaccel_2pxml AS p, uaccel_2pxml AS q
GROUP BY p.tag,q.tag
*/