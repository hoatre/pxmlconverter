//http://www.saxproject.org/
package def;
import org.xml.sax.helpers.DefaultHandler;

public class XMLReaderSAX extends DefaultHandler
{
	public XMLReaderSAX() {
		super();
	}
	
	/**
	 * Special characters are omitted
	 */
    public void characters (char ch[], int start, int length)
    {
    	String temp = "";
    	
    	for (int i = start; i < start + length; i++) {
		    switch (ch[i]) {
			    case '\\':
				temp += ("\\\\");
				break;
			    case '\'':
				temp += ("\\'");
				break;				
			    case '"':
			    temp += ("\\\"");
				break;
			    case '\n':
			    temp += ("");
				break;
			    case '\r':
			    temp += ("\\r");
				break;
			    case '\t':
			    temp += ("\\t");
				break;
			    case '&':
			    temp += ("&amp;");
				break;
			    default:
			    temp += (Character.toString(ch[i]));
				break;
		    }
    	}
    	value(temp);
    }
    
    public void value(String value) {
    }
}

