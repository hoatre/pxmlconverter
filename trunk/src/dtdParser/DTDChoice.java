package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/** Represents a choice of items.
 * A choice in a DTD looks like (option1 | option2 | option3)
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDChoice extends DTDContainer
{
    public DTDChoice()
    {
    }

/** Writes out the possible choices to a PrintWriter */
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
        if (!(ob instanceof DTDChoice)) return false;

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
        result += (this.getCardinal());
    	return result;
    }
    
	@Override
	public DTDItem transform() {
		DTDSequence result = new DTDSequence();
		result.setCardinal(this.cardinal);
		result.transformCard();
		for(int i = 0; i < items.size(); i++) {
			DTDItem transformed = items.get(i).transform();
			result.add(transformed);
		}
		return result.transform();
	}
	
}
