package dtdParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/** Represents an item that may contain other items (such as a
 * DTDChoice or a DTDSequence)
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
public class DTDContainer extends DTDItem
{
    protected Vector<DTDItem> items;

/** Creates a new DTDContainer */
    public DTDContainer()
    {
        items = new Vector<DTDItem>();
    }

/** Adds an element to the container */
    public void add(DTDItem item)
    {
        items.addElement(item);
    }

/** Removes an element from the container */
    public void remove(DTDItem item)
    {
        items.removeElement(item);
    }

/** Returns the elements as a vector (not a clone!) */
    public Vector<DTDItem> getItemsVec()
    {
        return items;
    }

/** Returns the elements as an array of items */
    public DTDItem[] getItems()
    {
        DTDItem[] retval = new DTDItem[items.size()];
        items.copyInto(retval);
        return retval;
    }

    public boolean equals(Object ob)
    {
       if (ob == this) return true;
        if (!(ob instanceof DTDContainer)) return false;

        if (!super.equals(ob)) return false;

        DTDContainer other = (DTDContainer) ob;

        return items.equals(other.items);
    }

/** Stores items in the container */
    public void setItem(DTDItem[] newItems)
    {
        items = new Vector<DTDItem>(newItems.length);
        for (int i=0; i < newItems.length; i++)
        {
            items.addElement(newItems[i]);
        }
    }

/** Retrieves the items in the container */
    public DTDItem[] getItem()
    {
        DTDItem[] retval  = new DTDItem[items.size()];
        items.copyInto(retval);

        return retval;
    }

/** Stores an item in the container */
    public void setItem(DTDItem anItem, int i)
    {
        items.setElementAt(anItem, i);
    }

/** Retrieves an item from the container */
    public DTDItem getItem(int i)
    {
        return items.elementAt(i);
    }

	@Override
	public void write(PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
	}
	
    public String toString() {
    	String result = "";
    	result += ("(");
    	Vector<DTDItem> items = getItemsVec();

        for (DTDItem e : items)
        {
        	result += e; 	
        }
        result += (")");
    	return result;
    }

	@Override
	public DTDItem transform() {
		DTDItem result;
		DTDContainer result1 = new DTDContainer();
		result1.setCardinal(this.cardinal);
		result1.transformCard();
		for(int i = 0; i < items.size(); i++) {
			DTDItem transformed = items.get(i).transform();
			result1.add(transformed);
		}
		if (result1.items.size() == 1) {
			result = result1.items.firstElement();
		} else {
			result = result1;
		}
		return result;
	}

	@Override
	public Vector<DTDName> getChildren() {
		Vector<DTDName> result = new Vector<DTDName>();
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getChildren() != null) {
				result.addAll(items.get(i).getChildren());
			}
		}
		return result;	
	}
}
