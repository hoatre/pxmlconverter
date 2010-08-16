package def;

import java.util.Vector;


public class ProbNode extends Node{
	/**
	 * Instance variables.
	 * this.possNodes contain all direct children of this. A ProbNode only has PossNode children
	 * this.id contains the id of this
	 * this.code contains the referring code for this 
	 */
	private Vector<PossNode> possNodes; //indexOf(possNodes.getElement()) == possNodes.getElement().getVal();
	private int id;
	private int code;
	
	/**
	 * Constructor of ProbNode
	 * @param pre
	 * @param post
	 * @param par
	 * @param kind
	 * @param tag
	 * @param level
	 * @param probcode
	 * @param posscode
	 */
	public ProbNode(int pre, int post, int par, int kind, String tag, int level, int probcode, int posscode) {
		super(pre, post, par, kind, tag, level, probcode, posscode);
		possNodes = new Vector<PossNode>();
	}
	
	/**
	 * Adds @param prob to the children collection
	 * @param prob
	 */
	public void addPossNode(PossNode prob) {
		//Possible to check if @param prob is a child of this.
		possNodes.add(prob);
	}
	
	public String toString() {
		String result = "ProbNode"; //super.toString()  + "\n";
		for (PossNode poss: possNodes) {
			result+=poss + "\n";
		}
		return result; 
	}
	
	/**
	 * Returns the total chance of all its children. This is in a normal case equal to one.
	 * @return forall PossNode poss:possNodes :: SUM poss.getProb()
	 */
	public double getTotalChance() {
		double result = 0;
		for(PossNode poss : possNodes) {
			if (!poss.getTag().equals(Constants.RESTPOSSNODE))
				result += poss.getProb(); 
		}
		return result;
	}
	
	/**
	 * Returns the restNode if it exists, otherwise null.
	 * @deprecated
	 * @return restNode
	 * @ensures getTotalChance() == 1
	 */
	public PossNode getRestPoss() {
		PossNode result = null;
		for(PossNode poss : possNodes) {
			if (poss.getTag().equals(Constants.RESTPOSSNODE)) {
				result = poss;
			}
		}
		return result;
	}
	
	/**
	 * Changes the probvalue of all its PossNode children with @param factor * child.getProb()
	 * @param factor
	 */
	public void vermenigvuldigFactor(double factor) {
		for(PossNode poss : possNodes) {
			if (!poss.getTag().equals(Constants.RESTPOSSNODE)) {
				double pos = poss.getProb();
				poss.setProb(pos * factor);
			}
		}
	}
	
	/**
	 * Getter for variable possNodes
	 * @return this.possNodes
	 */
	public Vector<PossNode> getPossNodes() {
		return possNodes;
	}

	/**
	 * Getter for variable code
	 * @return code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * Setter for variable this.code
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * Getter for variable id
	 * @return this.id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Setter for variable this.id
	 * @param id 
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the val-value of a possNode. If @param poss isChild of this, then @return >=0, otherwise @return -1
	 * @param poss
	 * @return this.getPossNodes.indexOf(poss);
	 */
	public int getVal(PossNode poss) {
		return possNodes.indexOf(poss);
	}
}
