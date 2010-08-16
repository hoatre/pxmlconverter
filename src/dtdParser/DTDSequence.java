package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/** Represents a sequence in an element's content.
 * A sequence is declared in the DTD as (value1,value2,value3,etc.)
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDSequence extends DTDContainer
{
    public DTDSequence()
    {
    }

/** Writes out a declaration for this sequence */
    public void write(PrintWriter out)
        throws IOException
    {
        out.print("(");

        Enumeration e = getItemsVec().elements();
        boolean isFirst = true;

        while (e.hasMoreElements())
        {
            if (!isFirst) out.print(",");
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
        if (!(ob instanceof DTDSequence)) return false;

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
            	result += (", " + e.toString()); 	
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
		DTDItem result;
		DTDSequence result1 = new DTDSequence();
		result1.setCardinal(this.cardinal);
		result1.transformCard();
		boolean zeroMany = result1.getCardinal().equals(DTDCardinal.ZEROMANY);
		if (zeroMany) {
			result1.setCardinal(DTDCardinal.NONE);
		}
		
		//Alle items binnen deze sequence transformeren
		for(int i = 0; i < items.size(); i++) {
			DTDItem transformed = items.get(i);
			//Cardinaliteit van sequence verwerken in eigen elementen
			if (zeroMany) {
				transformed.setCardinal(DTDCardinal.ZEROMANY);
			}
			transformed = transformed.transform();
			result1.add(transformed);
		}
		
		//sequences zonder cardinality samenvoegen
		for(int i = 0; i < result1.items.size(); i++) {
			if (result1.items.get(i).getCardinal().equals(DTDCardinal.NONE) && result1.items.get(i) instanceof DTDSequence) {
				DTDSequence seq = (DTDSequence) result1.items.get(i);
				result1.remove(seq);
				i--;
				for(int j = 0; j < seq.items.size(); j++) {
					result1.items.add(seq.items.get(j));
				}
			}
		}
		
		
		//samenvoegen van items
		//System.out.println(result1);
		for(int i = 0; i < result1.items.size(); i++) {
			for(int j = 0; j < i; j++) {
				DTDItem iItem = result1.items.get(i);
				DTDItem jItem = result1.items.get(j);
				if (iItem instanceof DTDName && jItem instanceof DTDName) {
					DTDName iName = (DTDName) iItem;
					DTDName jName = (DTDName) jItem;
					//System.out.println("Vergelijken: " + iName.getValue() + " met " + jName.getValue());
					if (iName.getValue().equals(jName.getValue())) {
						//System.out.println("Dubbel: " + iName.getValue());
						jItem.setCardinal(DTDCardinal.ZEROMANY);
						result1.items.remove(i);
						i--;
					}
				}
			}
		}

		//Geen sequence nodig, wanneer de sequence uit 1 element bestaat
		if (result1.items.size() == 1) {
			result = result1.items.firstElement();
		} else {
			result = result1;
		}
		return result;
	}
}
