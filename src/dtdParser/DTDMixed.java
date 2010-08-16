package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/** Represents a mixed Element content (PCDATA + choice/sequence).
 * Mixed Element can contain #PCDATA, or it can contain
 * #PCDATA followed by a list of pipe-separated names.
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDMixed extends DTDContainer
{
    public DTDMixed()
    {
    }

/** Writes out a declaration for mixed content */
    public void write(PrintWriter out)
        throws IOException
    {
        out.print("(");

        Enumeration e = getItemsVec().elements();
        boolean isFirst = true;

        while (e.hasMoreElements())
        {
            if (!isFirst) out.print(" | ");
            isFirst = false;

            DTDItem item = (DTDItem) e.nextElement();
            item.write(out);
        }
        out.print(")");
        cardinal.write(out);
    }

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDMixed)) return false;

        return super.equals(ob);
    }
    
    public String toString() {
    	String result = "";
    	result += ("(");
    	Vector<DTDItem> items = getItemsVec();
        boolean isFirst = true;

        for (DTDItem e : items)
        {
            if (!isFirst) { 
            	result += (" | " + e.toString()); 	
            } else {
            	result += (e.toString());
            	isFirst = false;
            }
        }
        result += (")");
    	return result;
    }
}
