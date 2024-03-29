package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;

/** Represents a comment in the DTD
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDComment implements DTDOutput
{
/** The comment text */
    public String text;

    public DTDComment()
    {
    }

    public DTDComment(String theText)
    {
        text = theText;
    }
    
    public String toString()
    {
	    return text;
    }

    public void write(PrintWriter out)
        throws IOException
    {
        out.print("<!--");
        out.print(text);
        out.println("-->");
    }

    public boolean equals(Object ob)
    {
        if (ob == this) return true;
        if (!(ob instanceof DTDComment)) return false;

        DTDComment other = (DTDComment) ob;
        if ((text == null) && (other.text != null)) return false;
        if ((text != null) && !text.equals(other.text)) return false;

        return true;
    }

/** Sets the comment text */
    public void setText(String theText)
    {
        text = theText;
    }

/** Returns the comment text */
    public String getText()
    {
        return text;
    }
}
