/*
 * Wanneer ik ucp ga joinen met de Tables, dan heb ik niet 1 table om rekening mee te houden, maar een hele hoop.
 * Manier waarop ik te werk ga:
 * - Alle Tables zijn gerelateerd aan nodes. Zodra een table is aangemaakt, sla ik deze op in de nodes.
 * - Zodra ik per Phase ga joinen, roep ik de laatst aangemaakte table aan, omdat ik per phase waar een XML element zit een join moet uitvoeren met de laatst aangemaakte table.
 * - Probleem is dat alle Descendants moeten worden gejoind. Waarschijnlijk ga ik hier ook Nodes voor gebruiken, omdat deze child attributes bijhouden van type Node die de tables bevatten die ik wil ga joinen.
 * - - Nieuw probleem, per table moet worden uitgezocht welke entries descendant zijn met de prob die ik wil gaan joinen. HOE?
 * - WERKT NIET: Andere manier is alle Tables gezamelijk opslaan, gerelateerd aan pre en post, waardoor ik met Java kan bepalen welke tables moeten worden gejoind met de root poss of met de phase poss
 * - OMDAT: entries in tables verschillende parents kunnen hebben
 */

package schemabased;
import java.util.Vector;
import org.xml.sax.Attributes;

import def.Constants;
import def.ForeignKey;
import def.Node;
import def.PossNode;
import def.ProbNode;
import def.SQL;
import def.Table;
import def.XMLReaderSAX;
/**
 * MakeSQLDump is used for handling SAX events for probabilistic XML documents.
 * @author Paul Stapersma
 */

public class MakeSQLDump extends XMLReaderSAX implements Constants {
	/**
	 * Instance variables.
	 * this.level is used for the level the tree has. Take in account that the level of the file is +2, as a result of the extra probnode with possnode.
	 * this.preorder keeps track of the preorder of XML-nodes. Probnodes don't have a preorder. The getParent.getPre() is used.
	 * this.postorder keeps track of the postorder of XML-nodes. Probnodes don't have a postorder. The getParent.getPost() is used.
	 * this.phase keeps track of the possnodes that can be processed in parallel.
	 * this.maxPhase equals max(this.phase)
	 * this.id equals the probs.size()
	 * this.poss equals the number of encountered PossNodes when processing a ProbNode.
	 * this.lastProbPreOrder is used when a Node is instantiated. It has the preorder of the probnode the node is the direct descendant of.
	 * this.lastPossPreOrder is used when a Node is instantiated. It has the preorder of the possnode the node is the direct descendant of.
	 * this.accel, this.content, this.cp store the complete structure of the processed probabilistic XML document.
	 * this.stack is the stack of the XML-nodes. A node is inserted when openingtags are processed and deleted when closingtags are processed.
	 * this.probs stores all the processed ProbNodes.
	 */
	
	private int level;
	private int preorder;
	private int postorder;
	private int phase;
	private int maxPhase;
	private int id; 				
	private int lastProbPreOrder; 	//This administration variable isn't used and can therefore be omitted
	private int lastPossPreOrder; 	//This administration variable isn't used and can therefore be omitted
	private Table cp;
	private Vector<Node> stack; //Make this variable private and don't use getters and setters.
	private Vector<ProbNode> probs;
	private Vector<Table> sharedTables;
	
	/**
	 * Constructor of MakeSQLDump. Calls the XMLReaderSAX constructor, an event-based SAX-parser
	 */
	public MakeSQLDump() {
		super();
	}
	
	/**
	 * Processing an probabilistic XML file, requires an accel, content and choicepoint table.
	 * Needs to be called before the parsing the probabilistic XML document.
	 * @param accel, stores all XML nodes, without ProbNodes and PossNodes
	 * @param content, stores the content of XML nodes
	 * @param cp, stores the ProbNodes and PossNodes
	 */
	public void instantiateTables(Table cp) {
		this.cp = cp;
	}
	
	/**
	 * Resets all variables of the SAX handler and defines the columns of the tables accel, content and cp.
	 * @require the call of instantiateTables()
	 */

    public void startDocument ()
    {
    	System.out.println("Start document");

    	/*
    	 * Instantiating variables
    	 */
    	level 				= INSTANTIATE_LEVEL;
    	preorder 			= INSTANTIATE_PREORDER;
    	postorder 			= INSTANTIATE_POSTORDER;
    	phase 				= INSTANTIATE_PHASE; // 0 is gereserved for the rootNode
    	id					= START_ID;
    	lastProbPreOrder 	= ROOTPROBPRE;
    	lastPossPreOrder 	= ROOTPOSSPRE;
    	stack 				= new Vector<Node>();
    	probs 				= new Vector<ProbNode>();
		
		cp.createColumn(ID, INT);
		cp.createColumn(PROB, FLOAT);
		cp.createColumn(PREORDER, INT);
		cp.createColumn(POSTORDER, INT);
		cp.createColumn(PHASE, INT);
		cp.setCheck(PROB, ">=0");
		//ucp.makeColumn(CODE, INT);
		SQL.createTable(cp);
		
		startRootNode(); // Initialiseert een PossNode en een ProbNode waaronder de het hele probabilistic XML document wordt gehangen.
    }
    
    /**
     * Initializes a ProbNode and a PossNode with prob=1 as root of the probabilistic XML document.
     */
    public void startRootNode() {
    	startProbNode(ROOTNODE, null);
    	startPossNode(POSSNODE, "1");
    }
    
    /**
     * Properly ends the ProbNode and PossNode, initialized with the function startRootNode()
     */
    public void endRootNode() {
    	endPossNode(POSSNODE);
    	endProbNode(ROOTNODE);
    }
    
    /**
     * Every processed XML starttag results in a call to startElement and sorts these tags with isProbNode, isPossNode, isSomethingElse.
     * @param uri is not supported
     * @param name is not supported
     * @param qName, contains the elementnametag
     * @param attrs, contains the attributes of this element
     */
    public void startElement (String uri, String name, String qName, Attributes attrs)
	{
    	//(tagname, isNotAttr -> false, listAttributeNames or Null)
    	if ("".equals(uri)) {    		
    		if (qName.equals(POSSNODE)) 		{ this.startPossNode(qName, attrs); }
    		else if (qName.equals(PROBNODE)) 	{ this.startProbNode(qName, attrs); }
    		else 								{ this.startNormalNode(qName, attrs); }
    	} else {
    		//printlnToFile("<{" + uri + "}" + name + ">");
    		System.out.println("<{" + uri + "}" + name + ">");
    	}
	}
	
    /**
     * Every processed XML endtag result in a call to endElement and sorts these tags with isProbNode, isPossNode, isSomethingElse.
     * @param uri is not supported
     * @param name is not supported
     * @param qName, contains the elementnametag
     */
	public void endElement (String uri, String name, String qName)
	{
		if ("".equals (uri)) {
    		if (qName.equals(POSSNODE)) 		{ this.endPossNode(qName); }
    		else if (qName.equals(PROBNODE)) 	{ this.endProbNode(qName); }
    		else 								{ this.endNormalNode(qName); }
		} else {
			System.out.println("<{" + uri + "}" + name + ">");
		}
	}
	
	/**
	 * Initializes a ProbNode and adds it to the beginning of the probs-Vector
	 * @param qName, equals Constant.PROBNODE
	 * @param attrs, equals null (this is not checked in the function)
	 */
	/*
	 * Volgens mij kan een Pos-node geen attrs hebben.
	 * Een posNode wordt niet genummerd. Hij wordt krijgt daarom de gegevens van zn pappie
	 */
	public void startProbNode(String qName, Attributes attrs) 
	{ 
		ProbNode probNode = null;
		if (stack.size() == 0) {
			lastProbPreOrder = 0;
			probNode = new ProbNode(-1, -1, -1, Node.PROBNODE, "root", -1, ROOTPROBPRE, ROOTPOSSPRE);
			probNode.setCode(0); // volgens mij mag deze regel weg
		}
		else
		{
			lastProbPreOrder = stack.firstElement().getPre();
			probNode = new ProbNode(stack.firstElement().getPre(), stack.firstElement().getPost(), stack.firstElement().getPar(), Node.PROBNODE, qName, level, lastProbPreOrder, lastPossPreOrder);
		}
		
		probNode.setId(id);
		this.id++;
		
		this.stack.add(0,probNode); //is added at the beginning of the stack, so the postorder can be set
		this.probs.add(0,probNode);
	}
	
	/**
	 * Properly handles the endtag of the processed ProbNode
	 * The ProbNode is set at the end of the probs-Vector and the postorder is set.
	 * @param qName equals Constants.PROBNODE
	 */
	public void endProbNode(String qName) {
		ProbNode firstProb = probs.firstElement(); // hoeft niet uit de lijst verwijderd te worden en staat ook niet op de stack.
		stack.remove(stack.firstElement());
		
		/*
		 * Toevoegen van een extra Probnode voor de resterende kans. Als het goed is is dit altijd 0
		if (firstProb != null && firstProb.getLevel() != -1) { //Rootnode hoeft niet gecontroleerd te worden.
			//possnode toevoegen met daarin de restprob
			startPossNode(RESTPOSSNODE, NOPROB.toString());
			endPossNode(RESTPOSSNODE);
		}
		*/
		
		firstProb.setPost(postorder);
		probs.remove(firstProb);
		probs.add(firstProb); //firstProb wordt achteraan de lijst geplaatst
	}

	/**
	 * Destilates the probabilityvalue of the PossNode
	 * @param qName
	 * @param attrs
	 */
	public void startPossNode(String qName, Attributes attrs)
	{
		boolean foundPos = false;
		String attrValue = "0.0";
    	for(int i = 0; attrs != null && i < attrs.getLength() && !foundPos; i++) {
    		if (attrs.getQName(i).equals(POSSATTR)) {
    			foundPos = true;
    			attrValue = attrs.getValue(i);
    		}
    	}
    	
    	if(!foundPos) {
    		System.out.println("FOUT: PossNode heeft geen pos-attr");
    	}
    	startPossNode(qName, attrValue);
	}

	/**
	 * Initializes a PossNode and adds it to the last processed ProbNode (it's parent).
	 * @param qName, equals Constant.PROBNODE
	 * @param attrs, equals null (this is not checked in the function)
	 */
	public void startPossNode(String qName, String attrValue)
	{
		ProbNode parentProb = probs.firstElement();
		lastPossPreOrder = preorder;
		PossNode elem = new PossNode(preorder, NOVALUE, NOVALUE, Node.NODE, qName, level, Double.parseDouble(attrValue), lastProbPreOrder, lastPossPreOrder, phase);
		stack.add(0,elem); //is added at the beginning of the stack, so the postorder can be set
		parentProb.addPossNode(elem);
		elem.setId(parentProb.getId());
		elem.setVal(parentProb.getVal(elem));
		preorder++;
		level++;
		if (phase > maxPhase) { //maxPhase is used for walking through the phases 
			maxPhase = phase; 
		}
		phase++; //Everytime a starttag of a PossNode is seen: phase++, otherwise this PossNode would be processed at the same time as it's direct preceding PossNode
		//SQL.insertAttribute(accel, content, attrName, attrValue, lastProbPreOrder, lastPossPreOrder); //the prob-attribute is not stored without this sentence
	}

	/**
	 * Properly handles the endtag of the processed PossNode
	 * @param qName equals Constants.PROBNODE
	 */
	public void endPossNode(String qName) {
		//endNormalNode(qName); //probNode wordt uit stack verwijderd, maar niet uit poss!!!
		level--;
		PossNode possNode = (PossNode) stack.firstElement();
		stack.remove(stack.firstElement());
		possNode.setPost(postorder);
		if (stack != null && stack.size() > 0)
			possNode.setPar(stack.firstElement().getPre());
		postorder++;
		phase--;
		SQL.insertPoss(cp, possNode);
	}
	
	/**
	 * Initializes a normal XML element and inserts the associated attributes
	 * @param qName isn't equal to Constants.POSSNODE and Constants.PROBNODE
	 * @param attrs
	 */
	public void startNormalNode(String qName, Attributes attrs)
	{
		Node elem = new Node(preorder, NOVALUE, NOVALUE, Node.NODE, qName, level, lastProbPreOrder, lastPossPreOrder);
		stack.add(0,elem);
		preorder++;
		level++;
		
		//Attribute processing
    	for(int i = 0; i < attrs.getLength(); i++) {
    		Node attr = new Node(preorder, postorder, elem.getPre(), Node.ATTRIBUTE, attrs.getQName(i), level, elem.getProbcode(), elem.getPosscode());
    		preorder++;
    		postorder++;
    		SQL.insertAttribute(sharedTables, attr, attrs.getValue(i));
    	}
	}
	
	/**
	 * Properly handles the endtag of the processed XML element
	 * @param qName isn't equal to Constants.POSSNODE and Constants.PROBNODE
	 */
	public void endNormalNode(String qName) {
		level--;
		Node elem = stack.firstElement();
		stack.remove(stack.firstElement());
		elem.setPost(postorder);
		if (stack != null && stack.size() > 0)
			elem.setPar(stack.firstElement().getPre());
		SQL.insertNode(sharedTables, elem);
		postorder++;
	}
	
	/**
	 * Stores the elementcontent if it contains any non-whitespace
	 * @param cdata contains the elements content
	 */
    public void value(String cdata) {
		if (!validate(cdata).equals("")) {
			Node textNode = new Node(preorder, postorder, stack.firstElement().getPre(), Node.TEXTNODE, PREFIX_TEXTNODE + stack.firstElement().getTag(), level, lastProbPreOrder, lastPossPreOrder);
			preorder++;
			postorder++;
			SQL.insertNodeCDATA(sharedTables, cdata, textNode);
		}
    }

    /**
     * Returns str without whitespace
     * @param str
     * @return
     */
    public static String validate(String str) {
    	String result = str.replace("\\n","");
    	result = result.replace("\n","");
    	result = result.replace("\\t","");
    	result = result.replace(" ","");
    	return result;
    }
    
    /**
     * For every phase, a repairkey table is initialized with key = phase and value is prob of the possNodes that have the same phase as the current phase. Other possNodes get prob = 1
     * @return Vector with repairkey tables, based on the phase value of the PossNodes.
     */
	public Vector<Table> createPhaseTables() {
		Vector<Table> phaseTables = new Vector<Table>();
		phaseTables.add(accel);
		Table ucp = SQL.repairKey(cp, cp.getColumn(ID), cp.getColumn(PROB), PREFIX_UCP);
		for (int i = 1; i <= maxPhase; i++) { //Root hoeft niet te worden meegenomen
			Table phaseTable = SQL.createPhaseTable(phaseTables.lastElement(), ucp, cp, i);
			phaseTables.add(phaseTable);			
		}
		return phaseTables;
	}
    
	/**
	 * As soon as the probabilisic XML document is processed, the rootNode can be probertly closed and the phaseTables can be made. 
	 */
	public void endDocument ()
    {
		endRootNode();
		
    	/*
    	 * Methode 1 : Failed
    	 * Probnodes krijgen een code en aan de tabel accel wordt per probnode een extra kolom toegevoegd.
    	 * Als laatste wordt een repairkey uitgevoerd op al deze kolommen.
    	 * 
    	 * codeerProbs();
    	 * repairKey(accel, poss);
    	 */
		
		/*
		 * Methode 2 : Failed
		 * UCP tabel maken met genormaliseerde poss-nodes, repair key op uitvoeren en joinen met accel
		 * 
		 * chanceNormalize();
		 * fillUCP();
		 * insertUCP();
		 * SQL.repairKeyUCP(ucp, probs);
		 */
		
		/*
		 * Methode 3 : Ready!
		 * Partities maken van nodes die moeten worden gejoind en nodes die niet hoeven worden gejoined.
		 */
		Vector<Table> phaseTables = createPhaseTables();
		for (int i = 1; i < phaseTables.size(); i++) {
			Table table = phaseTables.get(i);
			Import.selectInsert(table);
		}
		
    	
    	//Output status printen
    	for (int i = 0; i < probs.size(); i++) {
    		ProbNode prob = probs.get(i);
    		Vector<PossNode> posss = prob.getPossNodes();
    		System.out.println("VAL: " + prob.getId());
    		for (int j = 0; j < posss.size(); j++) {
    			PossNode poss = posss.get(j);
    			System.out.println(poss.getId() + PUNTKOMMA + poss.getVal() + PUNTKOMMA + poss.getProb() + PUNTKOMMA + HAAKJEOPEN + poss.getPre() + KOMMA + poss.getPost() + HAAKJESLUITEN + poss.getPhase());
    		}
    	}
    	System.out.println("End document");
    	System.out.println("------------------------------------\n");
    }

	public Vector<Table> getSharedTables() {
		return sharedTables;
	}

	public void setSharedTables(Vector<Table> sharedTables) {
		this.sharedTables = sharedTables;
	}
	
	
}
