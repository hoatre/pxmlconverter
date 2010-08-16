package def;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import schemaless.Import;

public class Table {
	/**
	 * Instance variables
	 * Because more tables are made for processing one probabilistic XML document, table-prefixes are used.
	 * this.prefix contains the prefix for this
	 * this.inputfilename contains the filename this table is linked to
	 * this.fw contains the FileWriter this Table writes in. All instructions for copying the content of this Table-instance into MayBMS are handled by this FileWriter 
	 * this.primaryKey can contain the primary key for this
	 * this.foreignKeys can contain foreign keys for this
	 * this.checks can contain table-checks
	 * this.columns contain the columns of this
	 */
	private String prefix;
	private String postfix;
	private String inputfilename;
	private String dir;
	public static final String EXTENTION = ".xml";
	public static final String OUTPUTDIR = "/generated/";
	private FileWriter fw = null;
	private String primaryKey = null;
	private Vector<ForeignKey> foreignKeys = new Vector<ForeignKey>();
	private HashMap<Column, String> checks = new HashMap<Column, String>();
	private Vector<Column> columns = new Vector<Column>();
	
	/**
	 * Constructor of Table
	 * @param prefix
	 * @param inputfilename
	 */
	public Table(String prefix, String inputfilename, String postfix, String dir) {
		this.inputfilename = inputfilename;
		this.prefix = prefix;
		this.postfix = postfix;
		this.dir = dir;
	}
	
	/**
	 * Makes column for this Table
	 * @param columnName
	 * @param columnType
	 */
	public void createColumn (String columnName, String columnType) {
		this.columns.add(new Column(columnName, columnType));
	}
	
    /**
     * Writes to the outputfile associated with this Table
     * @param str
     */
    public void printToFile(String str) {
    	try {
	    	if (fw == null) {
	    		new File (this.getOutputDir()).mkdir();
	    		//System.out.println(dir + OUTPUTDIR + prefix + inputfilename + EXTENTION);
				File file = new File(dir + OUTPUTDIR + prefix + inputfilename + postfix + EXTENTION);
				fw = new FileWriter(file);
				Import.makeInsert(this); //TODO VIES, Zodra een fileInputreader is aangemaakt, mag de table worden opgenomen in de import van de database
	    	}
    		fw.write(str);
    		fw.flush();
    	} catch (IOException e) {
    		System.out.println("Fout met schrijven naar file");
    		System.exit(0);
    	}
    }
    
    /**
     * Writes a line to the outputfile associated with this Table
     * @param str
     */
    public void printlnToFile(String str) {
    	printToFile(str + "\n");
    }

    /**
     * Returns the name of this table.
     * @return
     */
	public String getTableName() {
		return prefix + inputfilename + postfix;
	}
	
	/**
	 * Returns the filename this table is associated with (without extention)
	 * @return
	 */
	public String getFileName() {
		return inputfilename;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Creates primary key for this table.
	 * @param primaryKey
	 */
	public void setPrimaryKey(Column primaryKey) {
		this.primaryKey = primaryKey.getName();
	}

	/**
	 * Returns all columns associated with this
	 * @return a Vector with all associated columns of this
	 */
	public Vector<Column> getColumns() {
		return columns;
	}
    
	/**
	 * Adds foreign key @param fk to this
	 * @param fk
	 */
    public void addForeignKey(ForeignKey fk) {
    	this.foreignKeys.add(fk);
    }

    /**
     * Returns the collection of all the foreign keys associated with this
     * @return
     */
	public Vector<ForeignKey> getForeignKeys() {
		return foreignKeys;
	}
	
	/**
	 * Returns the prefix of this
	 * @return this.prefix
	 */
	public String getPrefix() {
		return this.prefix;
	}
	
	/**
	 * Returns the postfix of this
	 * @return postfix
	 */
	public String getPostfix() {
		return this.postfix;
	}
	
	/**
	 * Sets a check @param constraint for @param column
	 * @param column
	 * @param constraint
	 */
	public void setCheck(String column, String constraint) {
		Column key = null;
		for (int i = 0; i < columns.size() && key == null; i++) {
			if (columns.get(i).getName().equals(column)) {
				key = columns.get(i);
			}
		}
		checks.put(key, constraint);
	}
	
	/**
	 * Gets column constraint given Column @param key
	 * @param key
	 * @return
	 */
	public String getCheck(Column key) {
		return checks.get(key);
	}
	
	/**
	 * Returns Column with result.getName() equals name. If no column exists with result.getName() equals name, then @result null
	 * @param name
	 * @return Column with result.getName() equals name. If no column exists with result.getName() equals name, then @result null
	 */
	public Column getColumn(String name) {
		Column result = null;
		for (int i = 0; i < this.columns.size() && result == null; i++) {
			Column column = columns.get(i);
			if (column.getName().equals(name)) {
				result = column;
			}
		}
		return result;
	}
	
	/**
	 * Returns this.getTableName();
	 */
	public String toString() {
		return this.getTableName();
	}
	
	/**
	 * Returns copy of this table for a repairKey operation
	 * @param prefix
	 * @return
	 */
	public Table getRKTableCopy(String prefix) {
		Table result = new Table(prefix, this.inputfilename, "", this.dir);
		result.primaryKey = this.primaryKey;
		result.foreignKeys = this.foreignKeys;
		result.checks = this.checks;
		result.columns = this.columns;
		return result;
	}

	public String getDir() {
		return dir;
	}
	
	public String getOutputDir() {
		return dir + OUTPUTDIR;
	}
}
