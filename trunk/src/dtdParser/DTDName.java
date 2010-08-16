package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/** Represents a named item in the DTD
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDName extends DTDItem
{
    public String value;
    private Vector<DTDElement> parents;

    public DTDName()
    {
    }

    public DTDName(String aValue)
    {
        value = aValue;
    }

/** Writes out the value of this name */
    public void write(PrintWriter out)
        throws IOException
    {
        out.print(value);
        cardinal.write(out);
    }

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDName)) return false;
        if (!super.equals(ob)) return false;

        DTDName other = (DTDName) ob;

        if (value == null)
        {
            if (other.value != null) return false;
        }
        else
        {
            if (!value.equals(other.value)) return false;
        }
        return true;
    }

/** Sets the name value */
    public void setValue(String aValue)
    {
        value = aValue;
    }

/** Retrieves the name value */
    public String getValue()
    {
        return value;
    }
    
    public String toString() {
    	String result = "";
    	result += this.value + this.cardinal;
    	return result;
    }

	@Override
	public DTDName transform() {
		DTDName result = new DTDName(this.value);
		result.setCardinal(this.cardinal);
		result.transformCard();
		return result;
	}

	@Override
	public Vector<DTDName> getChildren() {
		Vector<DTDName> result = new Vector<DTDName>();
		result.add(this);
		return result;
	}
}
