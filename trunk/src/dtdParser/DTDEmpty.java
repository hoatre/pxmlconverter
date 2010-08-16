package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/** Represents the EMPTY keyword in an Element's content spec
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDEmpty extends DTDItem
{
    public DTDEmpty()
    {
    }

/** Writes out the keyword "EMPTY" */
    public void write(PrintWriter out)
        throws IOException
    {
        out.print("EMPTY");
        cardinal.write(out);
    }

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDEmpty)) return false;
        return super.equals(ob);
    }
    
    public String toString() {
    	String result = "EMPTY";
    	return result;
    }

	@Override
	public DTDEmpty transform() {
		DTDEmpty result = new DTDEmpty();
		result.setCardinal(this.cardinal);
		result.transformCard();			
		return result;
	}

	@Override
	public Vector<DTDName> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
}
