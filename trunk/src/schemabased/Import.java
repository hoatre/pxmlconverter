package schemabased;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import def.Constants;
import def.Table;

public class Import implements Constants {
	
	/**
	 * Instance variables.
	 * this.xr is used for reading XML documents
	 * this.handler handles the parsing of probabilistic XML documents (extends SAX parser)
	 * this.makew creates a BAT-file for creating table in the MayBMS database
	 */
    public static XMLReader xr = null;
    public static MakeSQLDump handler = null;
	public static FileWriter makew = null;
	public static FileWriter selectw = null;
	
	/**
	 * Given a directory, processes all the files in it.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File dir = new File(args[0]); 
			importDir(dir);
		} catch (NullPointerException e) {
			System.out.println("Geef geldig pad op");
		}
	}
	
	public static void importDir(File dir) {
		String[] children = dir.list();
		for (int i = 0; i < children.length; i++){
			File file = new File(dir.getPath() +"/"+children[i]);
			if (!file.isHidden() && file.isFile() && file.canRead() && !children[i].equals("")) {
				importFile(file);
			} else if (file.isDirectory() && !file.getName().equals("generated")) {
				importDir(file);
			}
		}
	}
	
	/**
	 * Initializes all handlers
	 * @throws Exception
	 */
    public static void startHandlers() throws Exception {
    	if (xr == null || handler == null) {
			xr = XMLReaderFactory.createXMLReader();
			handler = new MakeSQLDump();
			DTDHandler dtdh = new DTDHandler(handler);
			xr.setDTDHandler(dtdh);
			xr.setProperty("http://xml.org/sax/properties/"
					+ "lexical-handler", dtdh);
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
    	}
    	if (makew == null) {
			File file = new File("make.bat");
			makew = new FileWriter(file);
			makew.write("@ECHO OFF\n");
    	}
    	if (selectw == null) {
			File file = new File("select.txt");
			selectw = new FileWriter(file);
    	}
    }
    
    /**
     * Processes a probabilistic XML file.
     * Because a XMLReader and MakeSQLDump can read more then one probabilistic XML file, it is required that handler.instantiateTables is called for every file.
     * @param file
     */
    public static void importFile(File file) {
    	try {
	    	startHandlers();
	    	String filename = file.getName();
	    	filename = filename.replace("-1.", "-1");
	    	filename = filename.replace("-0.", "-0");
	    	filename = filename.replace('-', '_');

			handler.instantiateTables(
					new Table("shared_" + PREFIX_CP, filename.substring(0, filename.lastIndexOf(DOT)),  "", file.getParent())
			);
			System.out.println("Converting: " + filename);
			
			FileReader r = new FileReader(file);
		    xr.parse(new InputSource(r));			
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }

    /**
     * Puts a SELECT I-SQL instruction in the file selectw writes in.
     * @param table
     */
    public static void selectInsert(Table table) {
    	try {
    		if (selectw != null) {
        		selectw.write("SELECT * FROM "+table+";\n");
        		selectw.flush();			
    		}
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
    
    /**
     * Calls the created I-SQL instuction files that are made for @param table with a call for a timestamp at the beginning and at the ending of the call.
     * @param table
     */
    public static void makeInsert(Table table) {
    	try {
    		if (makew != null) {
	    		makew.write("echo %time% start " + table.getTableName() + "\n");
	    		makew.write("bin\\psql template1 < \"Z:\\Documents\\workspace\\BachRef\\"+ (table.getOutputDir() ).replace('/', '\\') + table + Table.EXTENTION+"\" > "+table+"_output.txt\n");
	    		makew.write("echo %time% end " + table.getTableName() + "\n");
	    		makew.flush();
    		}
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }
}
