package def;


public class PossNode extends Node {
	/**
	 * Instance variables.
	 * this.id contains the id for this
	 * this.val contains the possibility for this
	 * this.prob contains the probability for this
	 * this.phase contains the phase for this
	 */
	private int id;
	private int val;	
	private double prob;
	private int code;
	private int phase;

	/**
	 * Constructor of PossNode
	 * @param pre
	 * @param post
	 * @param par
	 * @param kind
	 * @param tag
	 * @param level
	 * @param prob
	 * @param probcode
	 * @param posscode
	 * @param phase
	 */
	public PossNode(int pre, int post, int par, int kind, String tag, int level, double prob, int probcode, int posscode, int phase) {
		super(pre, post, par, kind, tag, level, probcode, posscode);
		this.prob = prob;
		this.phase = phase;
		this.id = Constants.NOVALUE;
		this.val = Constants.NOVALUE;
	}

	/**
	 * Getter for variable prob
	 * @return this.prob
	 */
	public double getProb() {
		return prob;
	}

	/**
	 * Setter for variable this.prob
	 * @param prob
	 */
	public void setProb(double prob) {
		this.prob = prob;
	}
	
	public String toString() {
		String result = super.toString()  + "\n";
		result+="\t" + "Prob: " + prob;
		
		return result; 
	}

	/**
	 * Getter for variable code
	 * @return this.code
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
	 * Getter for variable val
	 * @return this.val
	 */
	public int getVal() {
		return val;
	}

	/**
	 * Setter for variable this.val
	 * @param val
	 */
	public void setVal(int val) {
		this.val = val;
	}

	/**
	 * Getter for variable phase
	 * @return this.phase
	 */
	public int getPhase() {
		return phase;
	}

	/**
	 * Setter for variable this.phase
	 * @param phase 
	 */
	public void setPhase(int phase) {
		this.phase = phase;
	}
}
