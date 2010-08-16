package def;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.XMLReaderFactory;

import schemabased.DTDHandler;


public class OutlineXML extends XMLReaderSAX {
	public FileWriter fw = null;
	public int level = 0;
	public int pre = 0;
	public Vector<String> openingTags;
	public static final String PREFIX = "Outlined_";
	public static String outputFilename;
	public static String outputDirectory = "outlined/";
	
	public OutlineXML() {
		super();
	}
	
    public void startDocument ()
    {
    	//Zonder deze regel krijg ik geen root-element!
    	System.out.println("Start document");
    	this.openingTags = new Vector<String>();
    }

    public void endDocument ()
    {
    	//printlnToFile("End document");
    	System.out.println("End document, amount of elements: " + (pre) + " :: Level : " + level);
    	pre--;
    	for (int i = 0; i < openingTags.size(); i++) {
			level--;
			printlnToFile(makeWhitespace(level) + "</" + openingTags.get(i) + ">");
    	}
    }
    
    public void startElement (String uri, String name, String qName, Attributes atts)
	{
    	if ("".equals(uri)) {
			this.openingTags.add(0,qName);
			printlnToFile(makeWhitespace(level) + "<" + qName + getAttributes(atts) + ">");
			level++;
			pre++;
    	} else {
    		printlnToFile("<{" + uri + "}" + name + ">");
    	}
	}
	
	public void endElement (String uri, String name, String qName)
	{
		if ("".equals (uri)) {
			this.openingTags.remove(0);
			level--;
			printlnToFile(makeWhitespace(level) + "</" + qName + ">");
		} else {
			printlnToFile("</{" + uri + "}" + name + ">");
		}
	}
	
    public void value(String value) {
    	printlnToFile(makeWhitespace(level + 5) + value);
    }
    
    public String getAttributes(Attributes atts) {
    	String result = "";
    	for(int i = 0; i < atts.getLength(); i++) {
    		result += " ";
    		result += atts.getQName(i) + "=\"" + atts.getValue(i) + "\"";
    	}
		
    	return result;
    }
    
    //Functies voor printen naar een file
    public void printToFile(String str) {
    	try {
	    	if (fw == null) {
	    		new File(outputDirectory).mkdir();
				File file = new File(outputDirectory + outputFilename);
				fw = new FileWriter(file);
	    	}
    		if (!validate(str).equals("")) {
    			//System.out.println(validate(str));
    			fw.write(str);
    			fw.flush();
    		}
    	} catch (IOException e) {
    		System.out.println("Fout met schrijven naar file");
    		System.exit(0);
    	}
    }
    
    public void printlnToFile(String str) {
    	printToFile(str + "\n");
    }
    
    public String validate(String str) {
    	String result = str.replace("\\n","");
    	result = result.replace("\n","");
    	result = result.replace(" ","");
    	return result;
    }
    
    public String makeWhitespace(int spaces) {
    	String result = "";
    	for(int i = 0; i < spaces*2; i++) {
    		result = result + " ";
    	}
    	return result;
    }
    
    public static void input(File inputfile) 
    throws Exception {
    	
		outputFilename = PREFIX + inputfile.getName();//filename.substring(0, filename.lastIndexOf(Constants.DOT)) + ".txt";
		
		XMLReader xr = XMLReaderFactory.createXMLReader();
		//xr.setEntityResolver(new DummyEntityResolver());
		DTDHandler dtdh = new DTDHandler();
		OutlineXML handler = new OutlineXML();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
		handler.fw = null;
		FileReader r = new FileReader(inputfile);
	    xr.parse(new InputSource(r));
    }
    
    
	/**
	 * Given a directory, processes all the files in it.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		File dir = new File(args[0]); 
		importDir(dir);
	}
	
	public static void importDir(File dir) throws Exception {
		String[] children = dir.list();
		for (int i = 0; i < children.length; i++){
			File file = new File(dir.getPath() +"/"+children[i]);
			if (!file.isHidden() && file.isFile() && file.canRead() && !children[i].equals("")) {
				String filename = file.getName();
				String extention = filename.substring(filename.lastIndexOf(Constants.DOT), filename.length());
				if (extention.equals(".xml")) {
					input(file);
				}
			} else if (file.isDirectory() && !file.getName().equals("generated")) {
				importDir(file);
			}
		}
	}
}
