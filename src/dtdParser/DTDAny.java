package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/** Represents the ANY keyword in an Element's content spec
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDAny extends DTDItem
{
    public DTDAny()
    {
    }

/** Writes "ANY" to a print writer */
    public void write(PrintWriter out)
        throws IOException
    {
        out.print("ANY");
        cardinal.write(out);
    }

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDAny)) return false;

        return super.equals(ob);
    }
    
    public String toString() {
    	String result = "ANY";
    	return result;
    }

	@Override
	public DTDAny transform() {
		DTDAny result = new DTDAny();
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
