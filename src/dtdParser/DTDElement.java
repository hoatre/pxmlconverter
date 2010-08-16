package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

/** Represents an element defined with the ELEMENT DTD tag
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDElement implements DTDOutput
{
/** The name of the element */
    public String name;

/** The element's attributes */
    public Hashtable<String, DTDAttribute> attributes;

/** The element's content */
    private DTDItem content;

    public DTDElement()
    {
        attributes = new Hashtable<String, DTDAttribute>();
    }

    public DTDElement(String aName)
    {
        name = aName;

        attributes = new Hashtable<String, DTDAttribute>();
    }

/** Writes out an element declaration and an attlist declaration (if necessary)
    for this element */
    public void write(PrintWriter out)
        throws IOException
    {
        out.print("<!ELEMENT ");
        out.print(name);
        out.print(" ");
        if (content != null)
        {
            //content.write(out); //MAG EIGENLIJK NIET!
        }
        else
        {
            out.print("ANY");
        }
        out.println(">");
        out.println();
    }

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDElement)) return false;

        DTDElement other = (DTDElement) ob;

        if (name == null)
        {
            if (other.name != null) return false;
        }
        else
        {
            if (!name.equals(other.name)) return false;
        }

        if (attributes == null)
        {
            if (other.attributes != null) return false;
        }
        else
        {
            if (!attributes.equals(other.attributes)) return false;
        }

        if (content == null)
        {
            if (other.content != null) return false;
        }
        else
        {
            if (!content.equals(other.content)) return false;
        }

        return true;
    }

/** Sets the name of this element */
    public void setName(String aName)
    {
        name = aName;
    }

/** Returns the name of this element */
    public String getName()
    {
        return name;
    }

/** Stores an attribute in this element */
    public void setAttribute(String attrName, DTDAttribute attr)
    {
        attributes.put(attrName, attr);
    }

/** Gets an attribute for this element */
    public DTDAttribute getAttribute(String attrName)
    {
        return attributes.get(attrName);
    }

/** Sets the content type of this element */
    public void setContent(DTDItem theContent)
    {
        content = theContent;
    }

/** Returns the content type of this element */
    public DTDItem getContent()
    {
        return content;
    }
    
    public String toString() {
    	return this.name + " : " + this.content + "";
    }
    
    public DTDElement transform() {
    	DTDElement result = new DTDElement();
    	result.name = this.name;
    	result.attributes = this.attributes;
    	result.content = this.content.transform();
    	return result;
    }
    
    public Vector<DTDName> getChildren() {
    	return content.getChildren();
    }
}
