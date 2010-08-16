package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/** Represents any item in the DTD
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public abstract class DTDItem implements DTDOutput
{
/** Indicates how often the item may occur */
    public DTDCardinal cardinal;

    public DTDItem()
    {
        cardinal = DTDCardinal.NONE;
    }

    public DTDItem(DTDCardinal aCardinal)
    {
        cardinal = aCardinal;
    }

/** Writes out a declaration for this item */
    public abstract void write(PrintWriter out)
        throws IOException;

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDItem)) return false;

        DTDItem other = (DTDItem) ob;

        if (cardinal == null)
        {
            if (other.cardinal != null) return false;
        }
        else
        {
            if (!cardinal.equals(other.cardinal)) return false;
        }

        return true;
    }

/** Sets the cardinality of the item */
    public void setCardinal(DTDCardinal aCardinal)
    {
        cardinal = aCardinal;
    }

/** Retrieves the cardinality of the item */
    public DTDCardinal getCardinal()
    {
        return cardinal;
    }
    
    public abstract DTDItem transform();
    
    public void transformCard() {
    	if (this.getCardinal().equals(DTDCardinal.ONEMANY)) {
    		this.setCardinal(DTDCardinal.ZEROMANY);
    	}
    	if (this.getCardinal().equals(DTDCardinal.OPTIONAL)) {
    		this.setCardinal(DTDCardinal.NONE);
    	}
    }
    
    public abstract Vector<DTDName> getChildren();
}
