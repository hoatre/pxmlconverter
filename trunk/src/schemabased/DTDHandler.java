package schemabased;

import java.io.File;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import def.Table;

import dtdParser.DTDMain;


public class DTDHandler extends DefaultHandler2 {
	
	private MakeSQLDump handler;
	
	public DTDHandler(MakeSQLDump handler) {
		super();
		this.handler = handler;
	}
	
	public void startDTD(String name, String publicId, String systemId) throws SAXException {
		//System.out.println("DTD Info: " + name + ", " + publicId + ", " + systemId);
		DTDMain.importFile(new File(systemId), handler); 
	}
}
