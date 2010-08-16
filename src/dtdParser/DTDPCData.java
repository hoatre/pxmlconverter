package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/** Represents the #PCDATA keyword in an Element's content spec
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDPCData extends DTDItem
{
    public DTDPCData()
    {
    }

/** Writes out the #PCDATA keyword */
    public void write(PrintWriter out)
        throws IOException
    {
        out.print("#PCDATA");
        cardinal.write(out);
    }

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDPCData)) return false;

        return super.equals(ob);
    }
    
    public String toString() {
    	String result = "#PCDATA";
    	return result;
    }

	@Override
	public DTDPCData transform() {
		DTDPCData result = new DTDPCData();
		result.setCardinal(this.cardinal);
		result.transformCard();
		return result;
	}

	@Override
	public Vector<DTDName> getChildren() {
		Vector<DTDName> result = new Vector<DTDName>();
		DTDName temp = new DTDName("PCDATA"); //PCData is ook een kind!
		result.add(temp);
		return result;
	}
}
