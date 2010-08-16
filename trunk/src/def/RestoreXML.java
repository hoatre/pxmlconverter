package def;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class RestoreXML extends OutlineXML {
	public FileWriter fw = null;
	public int level = 0;
	public int pre = 0;
	public int maxPre;
	public Vector<String> openingTags;
	
	public RestoreXML(int maxPre) {
		super();
		this.maxPre = maxPre;
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
    		if (qName.equals(Constants.POSSNODE) || qName.equals(Constants.PROBNODE) || pre >= maxPre) 	{ }
    		else {
    			this.openingTags.add(0,qName);
    			printlnToFile(makeWhitespace(level) + "<" + qName + getAttributes(atts) + ">");
    			level++;
    			pre++;
    		}
    	} else {
    		printlnToFile("<{" + uri + "}" + name + ">");
    	}
	}
	
	public void endElement (String uri, String name, String qName)
	{
		if ("".equals (uri)) {
    		if (qName.equals(Constants.POSSNODE) || qName.equals(Constants.PROBNODE) || pre >= maxPre) 	{ }
    		else {
    			this.openingTags.remove(0);
				level--;
				printlnToFile(makeWhitespace(level) + "</" + qName + ">");
    		}

		} else {
			printlnToFile("</{" + uri + "}" + name + ">");
		}
	}
	
    public void value(String value) {
    	if (!(pre >= maxPre))
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
    
    public void setMaxPre(int maxPre) {
    	this.maxPre = maxPre;
    }
    
    //Functies voor printen naar een file
    public void printToFile(String str) {
    	try {
	    	if (fw == null) {
				File file = new File(maxPre + ".txt");
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
    
    public static void main (String args[])
	throws Exception
    {

				// Parse each file provided on the
				// command line.
		for (int i = 0; i < args.length; i++) {
			for(int j = 1; j <= 10; j++) {
				int elements = 5000 * j;
				XMLReader xr = XMLReaderFactory.createXMLReader();
				RestoreXML handler = new RestoreXML(elements);
				xr.setContentHandler(handler);
				xr.setErrorHandler(handler);
				handler.setMaxPre(elements);
				handler.fw = null;
				File file = new File(args[i]);
				FileReader r = new FileReader(file);
			    xr.parse(new InputSource(r));
			}
		}
    }
}
