package def;

public class Node {
	/** Node kinds */
	public static int NODE = 0;
	public static int ATTRIBUTE = 1;
	public static int TEXTNODE = 2;
	public static int PROBNODE = 3;
	public static int POSNODE = 4;
	
	/**
	 * Instance variables.
	 * this.pre contains the preorder of this
	 * this.post contains the postorder of this
	 * this.par contains the preorder of this.parentNode
	 * this.level contains the level of this in the XML tree
	 * this.tag contains the XML element or XML attribute nametag
	 * this.kind contains the Node-kinds
	 * this.probcode contains the probcode, equal to direct descendant probNode.getCode()
	 * this.probcode contains the posscode, equal to direct descendant possNode.getCode()
	 */
	private int pre;
	private int post;
	private int par;
	private int level;	
	private String tag;
	private int kind;
	private int probcode;
	private int posscode;

	/**
	 * Constructor of Node.
	 * @param pre
	 * @param post
	 * @param par
	 * @param kind
	 * @param tag
	 * @param level
	 * @param probcode
	 * @param posscode
	 */
	public Node(int pre, int post, int par, int kind, String tag, int level, int probcode, int posscode) {
		this.pre = pre;
		this.post = post;
		this.par = par;
		this.kind = kind;
		this.tag = tag;
		this.level = level;
		this.probcode = probcode;
		this.posscode = posscode;
	}
	
	/**
	 * Getter for variable pre
	 * @return this.pre
	 */
	public int getPre() {
		return pre;
	}

	/**
	 * Setter for variable this.pre
	 * @param pre
	 */
	public void setPre(int pre) {
		this.pre = pre;
	}

	/**
	 * Getter for variable post 
	 * @return this.post
	 */
	public int getPost() {
		return post;
	}

	/**
	 * Setter for variable this.post
	 * @param post
	 */
	public void setPost(int post) {
		this.post = post;
	}

	/**
	 * Getter for variable par 
	 * @return this.par
	 */
	public int getPar() {
		return par;
	}

	/**
	 * Setter for variable this.par
	 * @param par
	 */
	public void setPar(int par) {
		this.par = par;
	}

	/**
	 * Getter for variable tag
	 * @return this.tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Setter for variable this.tag
	 * @param tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	/**
	 * Getter for variable level
	 * @return this.level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Setter for variable this.level
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * Getter for variable kind 
	 * @return this.kind
	 */
	public int getKind() {
		return kind;
	}

	/**
	 * Setter for variable this.kind
	 * @param kind
	 */
	public void setKind(int kind) {
		this.kind = kind;
	}
	
	public String toString() {
		return 	"\n" + 
		"\tTag: " + getTag() + "\n" +
		"\tPre: " + getPre() + "\n" +
		"\tPost: " + getPost() + "\n" +
		"\tKind: " + getKind() + "\n" +
		"\tLevel: " + getLevel();
	}
	
	/**
	 * Returns of @param node is a descendant of this
	 * @param node
	 */
	public boolean isDescendant(Node node) {
		return pre > node.getPre() && post < node.getPost();
	}

	/**
	 * Getter for variable probcode
	 * @return this.probcode
	 */
	public int getProbcode() {
		return probcode;
	}
	/**
	 * Setter for variable this.probcode
	 * @param probcode
	 */
	public void setProbcode(int probcode) {
		this.probcode = probcode;
	}

	/**
	 * Getter for variable posscode 
	 * @return this.posscode
	 */
	public int getPosscode() {
		return posscode;
	}

	/**
	 * Setter for variable this.posscode
	 * @param posscode
	 */
	public void setPosscode(int posscode) {
		this.posscode = posscode;
	}
}
