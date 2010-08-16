package dtdParser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import schemabased.MakeSQLDump;

import def.Constants;
import def.Table;


/** Example program to read a DTD and print out its object model
 *
 * @author Mark Wutka
 * @version $Revision: 1.17 $ $Date: 2002/07/28 13:33:12 $ by $Author: wutka $
 */

public class DTDMain
{
	
	/**
	 * Given a directory, processes all the files in it.
	 * @param args
	 */
	public static void main(String[] args) {
		File dir = new File(args[0]); 
		importDir(dir);
	}
	
	public static void importDir(File dir) {
		String[] children = dir.list();
		for (int i = 0; i < children.length; i++){
			File file = new File(dir.getPath() +"/"+children[i]);
			if (!file.isHidden() && file.isFile() && file.canRead() && !children[i].equals("")) {
				String filename = file.getName();
				String extention = filename.substring(filename.lastIndexOf(Constants.DOT), filename.length());
				if (extention.equals(".dtd")) {
					importFile(file);
				}
			} else if (file.isDirectory() && !file.getName().equals("generated")) {
				importDir(file);
			}
		}
	}
	
	public static Vector<Table> importFile(File file, MakeSQLDump handler) {
		Vector<Table> sharedTables = importFile(file);
		return sharedTables;//TODO VIES!
	}
	
	public static Vector<Table> importFile(File file)
	{
		System.out.println("--------------------NEW FILE-------------------- " + file.getName());
        DTDParser parser;
        DTD dtd = null;
		try {
			parser = new DTDParser(file, true);
	        dtd = parser.parse(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

        if (dtd.rootElement != null)
        {
            System.out.println("\n\nRoot element is probably: "+dtd.rootElement.name);
        }
       
        System.out.println("DTD");
        System.out.println(dtd);
        
        System.out.println("TRANSFORMED");
        DTD transformed = dtd.transformDTD();
        System.out.println(transformed);
        
        Vector<Node> z = DTD.getAllNodes(transformed);
        for (int y = 0; y < z.size(); y++) {
        	System.out.println("Node: " + z.get(y) + z.get(y).toStringParents() + z.get(y).toStringChildren());
        }
        
        //Nodes omzetten naar SQL
        String filename = file.getName();
        Vector<Table> sharedTables = SQL.createSharedTables(z, filename.substring(0, filename.lastIndexOf(Constants.DOT)), file.getParent());
        
        
        /*
        Inlinen doen we niet in dit onderzoek
         
        Vector<InlinedNode> x = DTD.inline(transformed, DTD.getAllNodes(transformed));

        for (int i = 0; i < x.size(); i++) {
        	System.out.println(x.get(i));
        }
        */
        
        System.out.println("--------------------END FILE--------------------");
        
        return sharedTables; //TODO heel vies!!!
	}   
}
