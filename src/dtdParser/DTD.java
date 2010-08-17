package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

/** Represents a parsed Document Type Definition
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */


public class DTD implements DTDOutput
{
/** Contains all the elements defined in the DTD */
    private Hashtable<String, DTDElement> elements;

/** Contains all the entities defined in the DTD */
    public Hashtable<String, DTDEntity> entities; /* @Paul, niet ondersteund */

/** Contains all the notations defined in the DTD */
    public Hashtable<String, DTDNotation> notations; /* @Paul, niet ondersteund */

/** Contains parsed DTD's for any external entity DTD declarations */
    public Hashtable externalDTDs; /* @Paul, niet ondersteund */

/** Contains all the items defined in the DTD in their original order */
    private Vector<DTDElement> elementItems; //Onder items wordt bedoeld: alles wat tot nu toe is tegengekomen, dus {DTDElement*, AttrList*}
	private Vector<DTDAttlist> attlistItems;
	private Vector<DTDNotation> notationItems;
	private Vector<DTDEntity> entityItems;



/** Contains the element that is most likely the root element  or null
    if the root element can't be determined.  */
    public DTDElement rootElement;

/** Creates a new DTD */
    public DTD()
    {
        elements = new Hashtable<String, DTDElement>();
        entities = new Hashtable<String, DTDEntity>();
        notations = new Hashtable<String, DTDNotation>();
        externalDTDs = new Hashtable(); //Wanneer wordt deze Hashtable gebruikt?
        
        //Houdt volgorde van DTD aan
        elementItems = new Vector<DTDElement>();
        attlistItems = new Vector<DTDAttlist>();
        notationItems = new Vector<DTDNotation>();
        entityItems = new Vector<DTDEntity>();
    }

/** Writes the DTD to an output writer in standard DTD format (the format
 *  the parser normally reads).
 *  @param outWriter The writer where the DTD will be written
 */
    public void write(PrintWriter outWriter)
        throws IOException
    {
    	for(DTDElement e:this.elementItems) {
            e.write(outWriter);
        }
    	
    	for(DTDAttlist e:this.attlistItems) {
    		e.write(outWriter);
    	}
    	
    	for(DTDEntity e:this.entities.values()) {
    		e.write(outWriter);
    	}
    	
    	for(DTDNotation e:this.notationItems)
    	{
    		e.write(outWriter);
    	}
    }

/** Returns true if this object is equal to another: elements have to stay be in the same order */
    public boolean equals(Object ob)
    {
        if (this == ob) return true;

        if (!(ob instanceof DTD)) return false;

        DTD otherDTD = (DTD) ob;

        return 	this.getElementItems().equals(otherDTD.getElementItems()) &&
        		this.getAttlistItems().equals(otherDTD.getAttlistItems()); 
        //hoe zit het met volgordes van deze lijsten? Die maken bij mijn weten niet uit...
    }
    
    public Hashtable<String, DTDElement> getElements() {
    	return this.elements;
    }
    
    public Hashtable<String, DTDEntity> getEntities() {
    	return entities;
    }

    public Hashtable<String, DTDNotation> getNotations() {
    	return notations;
    }

    public Hashtable getExternalDTDs() {
    	return externalDTDs;
    }

    public Vector<DTDElement> getElementItems() {
    	return elementItems;
    }

    public Vector<DTDAttlist> getAttlistItems() {
    	return attlistItems;
    }

    public DTDElement getRootElement() {
    	return rootElement;
    }
    
    public Vector<DTDEntity> getEntityItems() {
		return entityItems;
	}

	public Vector<DTDNotation> getNotationItems() {
		return this.notationItems;
	}
    
    public String toString() {
    	String result = "";
    	for(DTDElement e:this.elementItems) {
    		result += ("Element: " + e + "\n");
    		
    		/*for (DTDName child : e.getChildren()) {
    			System.out.print(":::" + child.getValue());
    		}*/
    		
        	for(DTDAttribute f:e.attributes.values()) {
        		result += ("Attlist: " + f + "\n");
        	}
        }
    	
    	for(DTDEntity e:this.entities.values()) {
    		result += ("Entity " + e + "\n");
    	}
    	
    	for(DTDNotation e:this.notationItems)
    	{
    		result += ("Notation: " + e + "\n");
    	}
    	return result;
    }
    
    public DTD transformDTD() {
    	DTD result = new DTD();
    	result.elementItems = transformElems(this.elementItems);
    	result.attlistItems = this.attlistItems; //volgens mij wordt deze variabele niet gebruikt
    	result.entities = this.entities;
    	result.notationItems = this.notationItems;
    	return result;
    }

	private Vector<DTDElement> transformElems(Vector<DTDElement> v) {
		Vector<DTDElement> result = new Vector<DTDElement>();
		for(int i = 0; i < v.size(); i++) {
			//element transformeren
			DTDElement par = v.get(i).transform();
			

			//Het toevoegen van een getransformeerd DTDElement
			result.add(par);
		}
		return result;
	}
	
	//Maakt ook de parentrelatie aan
	public static Vector<DTDNode> getAllNodes(DTD dtd, String inputFilename, String dir) {
		Vector<DTDElement> v = dtd.getElementItems();
		Vector<DTDNode> nodes = new Vector<DTDNode>();
		for(int i = 0; i < v.size(); i++) {
			DTDElement par = v.get(i);
			//System.out.println("Parent: " + par.getName());

			DTDNode parNode = DTDNode.getOrMakeNode(nodes, par.getName());
			parNode.setInputFilenameAndDir(inputFilename, dir);
			parNode.setAttributes(par.attributes);
			
			Vector<DTDName> children = par.getChildren(); //TODO Type veranderen: DTDName naar Node (PCData is ook kind)
			//Alle kinderen aflopen
			//System.out.println(par.name + " heeft content: " + children);
			for(int j = 0; children != null && j < children.size(); j++) {
				DTDName child = children.get(j);
				//System.out.println("Child: " + child.getValue());
				String childName = child.getValue();
				//Per kind een key aanmaken en een lijst vullen met alle parent elementen
				DTDNode childNode = DTDNode.getOrMakeNode(nodes, childName);
				childNode.addParent(par);
				//INLINEN
				if(child.getCardinal().equals(DTDCardinal.NONE)) {
					parNode.addInlined(childNode);
				} else if (child.getCardinal().equals(DTDCardinal.ZEROMANY)) {
					parNode.addChild(childNode);
				} else { System.out.println("Deze cardinaliteit kan niet voorkomen: " + child.getCardinal()); }
			}
		}
		return nodes;
	}
	
	/*
	public static Vector<InlinedNode> inline(DTD dtd, Vector<Node> nodes) {
		Vector<DTDElement> v = dtd.getElementItems();
		Vector<InlinedNode> inlined = new Vector<InlinedNode>();
		for(int i = 0; i < v.size(); i++) {
			DTDElement elem = v.get(i);
			Node elemNode = Node.getNode(nodes, elem.getName());
			if (elemNode == null) {
				System.out.println("Fout opgetreden. Node "+elem.getName()+" bestaat niet in de DTD graaf");
			}
			
			InlinedNode inNode = InlinedNode.getNode(inlined, elemNode);
				
			if (inNode == null) {
				inNode = new InlinedNode();
			}
			if (!elemNode.isVisited()) {
				inlined.add(inNode);
				inNode.addInlinedNode(elemNode);
				//System.out.println("Ingelined: " + elemNode.getName());
			}
			
			//Process Children
			Vector<DTDName> children = elem.getChildren();
			for(int j = 0; children != null && j < children.size(); j++) {
				DTDName child = children.get(j);
				Node childNode = Node.getNode(nodes, child.getValue());
				if (childNode == null) {
					System.out.println("Fout opgetreden. Node "+child.getValue()+" bestaat niet in de DTD graaf");
				}
				if (!childNode.isVisited()) {
					if (child.getCardinal().equals(DTDCardinal.ZEROMANY) || childNode.getParents().size() > 1) {
						//Kan niet inlinen
						InlinedNode newInNode = new InlinedNode();
						newInNode.addInlinedNode(childNode);
						inlined.add(newInNode);
					} else if (child.getCardinal().equals(DTDCardinal.NONE)) {
						inNode.addInlinedNode(childNode);
					} else {
						System.out.println("Foute cardinaliteit!");
					}
				}
			}
		}
		return inlined;
	}
	*/
}
