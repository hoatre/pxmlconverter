package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;

/** Represents the possible values for an attribute declaration
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDDecl implements DTDOutput
{
    public static final DTDDecl FIXED = new DTDDecl(0, "FIXED");
    public static final DTDDecl REQUIRED = new DTDDecl(1, "REQUIRED");
    public static final DTDDecl IMPLIED = new DTDDecl(2, "IMPLIED");
    public static final DTDDecl VALUE = new DTDDecl(3, "VALUE");

    public int type;
    public String name;

    public DTDDecl(int aType, String aName)
    {
            type = aType;
            name = aName;
    }

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDDecl)) return false;

        DTDDecl other = (DTDDecl) ob;
        if (other.type == type) return true;
        return false;
    }

    public void write(PrintWriter out)
        throws IOException
    {
        if (this == FIXED)
        {
            out.print(" #FIXED");
        }
        else if (this == REQUIRED)
        {
            out.print(" #REQUIRED");
        }
        else if (this == IMPLIED)
        {
            out.print(" #IMPLIED");
        }
        // Don't do anything for value since there is no associated DTD keyword
    }
    
    public String toString()
    {
    	String result = "";
        if (this == FIXED)
        {
            result = ("#FIXED");
        }
        else if (this == REQUIRED)
        {
        	result = ("#REQUIRED");
        }
        else if (this == IMPLIED)
        {
        	result = ("#IMPLIED");
        }
        // Don't do anything for value since there is no associated DTD keyword
        
        return result;
    }

}
