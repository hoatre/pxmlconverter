package def;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class InsertProb {
	public static File file = null;
	public static DocumentBuilderFactory dbf = null;
	public static DocumentBuilder db = null;
	public static Document doc = null;
	public static int totalNodes;
	private static double chanceProb;
	private static FileWriter fw;
	public static final String PREFIX = "addProb_";
	public static String outputFilename;
	public static String outputDirectory;
	public static String chancePrefix;
	
	/**
	 * Initializes all handlers
	 * @throws Exception
	 */
    public static void startHandlers(File file) throws Exception {
    	dbf = DocumentBuilderFactory.newInstance();	
		db = dbf.newDocumentBuilder();
		doc = db.parse(file);	
    }
    
    /**
     * Processes a probabilistic XML file.
     * Because a XMLReader and MakeSQLDump can read more then one probabilistic XML file, it is required that handler.instantiateTables is called for every file.
     * @param file
     * @throws Exception 
     */
	public static void importFile(File file) throws Exception {
		startHandlers(file);
		String filename = file.getName();
    	filename = filename.replace('-', '_');
							//maakt DOM-documenten uit XML documenten
    	outputFilename = PREFIX + chancePrefix + filename.substring(0, filename.lastIndexOf(Constants.DOT)) + ".xml";
    	System.out.println(outputDirectory);
    	File outputfile = new File(outputDirectory + "/" + outputFilename);
		fw = new FileWriter(outputfile);
		System.out.println("Adding probability: " + filename);
		if (doc == null) {
			System.out.println("Afgesloten");
		} else {
			Node root = doc.getDocumentElement();
			totalNodes = getTotalDescendants(root);
			insertProbability2(root);
			fw.write(printDOM(doc));
			fw.flush();
			//OutlineXML.input(outputfile);
			System.out.println("Finished");
			//NodeList nodeLst = doc.getElementsByTagName("employee");
			//System.out.println("Information of all employees: " + nodeLst.toString());
		}
	}
/*
	private static void insertProbability1(Node node) {
		if (node.hasChildNodes()) {
			boolean insertProb = chanceProb >= Math.random();
			NodeList childNodes = node.getChildNodes();
			int totalChildren = getTotalChildren(node);
			System.out.println(chanceProb);
			if (insertProb && canAppend(node)) {
				
				Element prob = doc.createElement(Constants.PROBNODE);
				node.appendChild(prob);
				
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node child = childNodes.item(i);
					
					if ( 	canAppendPoss(node, child)
						) {
						insertProbability1(child);
						
						Element poss = doc.createElement(Constants.POSSNODE);
						poss.setAttribute(Constants.POSSATTR, Double.toString(1.0/totalChildren));
						prob.appendChild(poss);
						poss.appendChild(child);
						
						//System.out.println("NodeName: " + node.getNodeName());
						//System.out.println("ChildName: " + child.getNodeName());
						//node.removeChild(child);
						//node.replaceChild(prob, child);
					}
				}
			} else {
				//System.out.println("NIETS: " + node.getNodeName());
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node child = childNodes.item(i);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						insertProbability1(child);
						
					}
				}
			}
		}
	}
*/	
	//Overal wordt een probNode geplaatst, maar nu bepaald de kans of er een possNode wordt bijgeplaatst.
	private static void insertProbability2(Node node) {
		if (node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			int totalChildren = getTotalChildren(node);
			
			if (canAppend(node)) {
				
				Element prob = doc.createElement(Constants.PROBNODE);
				node.appendChild(prob);
				boolean first = false;
				
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node child = childNodes.item(i);
					
					if ( 	canAppendPoss(node, child) && (chanceProb >= Math.random() || !first)
						) {
						first = true;
						insertProbability2(child);
						
						Element poss = doc.createElement(Constants.POSSNODE);
						prob.appendChild(poss);
						poss.appendChild(child);
						
						//System.out.println("NodeName: " + node.getNodeName());
						//System.out.println("ChildName: " + child.getNodeName());
						//node.removeChild(child);
						//node.replaceChild(prob, child);
					}
				}
				normalize(prob.getChildNodes());
			} else {
				//System.out.println("NIETS: " + node.getNodeName());
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node child = childNodes.item(i);
					if (child.getNodeType() == Node.ELEMENT_NODE) {
						insertProbability2(child);
						
					}
				}
			}
		}
	}
	
	private static void normalize(NodeList possNodes) {		
		for (int i = 0; i < possNodes.getLength(); i++) {
			Node poss = possNodes.item(i);
			((Element) poss).setAttribute(Constants.POSSATTR, Double.toString(1.0/possNodes.getLength()));
		}
	}

	public static boolean canAppendPoss(Node parent, Node child) {
		return (child.getNodeType() == Node.ELEMENT_NODE && 
				!child.getNodeName().equals(Constants.PROBNODE) &&
				!parent.getNodeName().equals(Constants.PROBNODE) &&
				!parent.getNodeName().equals(Constants.POSSNODE) 
			);
	}

	private static boolean canAppend(Node node) {
		boolean result = false;
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength() && !result; i++) {
			Node child = childNodes.item(i);
			if (canAppendPoss(node, child))
			{
				result = true;
			}
		}
		return result;
	}

	private static int getTotalDescendants(Node node) {
		int result = 1; //node zelf
		if (node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					result += getTotalDescendants(child);
				}
			}
		}
		return result;
	}
	
	private static int getTotalChildren(Node node) {
		int result = 0; //node zelf
		if (node.hasChildNodes()) {
			NodeList childNodes = node.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE) {
					result += 1;
				}
			}
		}
		return result;
	}
	
	public static String printDOM(Document doc) {
		String result = "";
		try {
			TransformerFactory transfac = TransformerFactory.newInstance();
	        Transformer trans = transfac.newTransformer();
	        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        StringWriter sw = new StringWriter();
	        StreamResult res = new StreamResult(sw);
	        DOMSource source = new DOMSource(doc);
	        trans.transform(source, res);
	        result = sw.toString();
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		return result;
	}
	
	/**
	 * Given a directory, processes all the files in it.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		try {
			File dir = new File(args[0]);
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++){
				System.out.println("Child file: " + children[i]);
				File file = new File(args[0]+"/"+children[i]);
				if (!file.isHidden() && file.isFile() && file.canRead() && !children[i].equals("")) {
					for (int j = 1; j <=10; j++) {
						chanceProb = j * 0.1;
						outputDirectory = "testsets/prob" + j;
						new File(outputDirectory).mkdir();
						chancePrefix = (j*10) + "p_";
						importFile(file);
					}
				}
			}
		} catch (NullPointerException e) {
			System.out.println("Geef geldig pad op");
		}
	}
}