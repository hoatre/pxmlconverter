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
	
	public static void importFile(File file, MakeSQLDump handler) {
		importFile(file);
		//TODO handler voorzien van Vector<Nodes>, waarbij Nodes voorzien zijn van tabellen.
	}
	
	public static void importFile(File file)
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
        
        String filename = file.getName();
        Vector<DTDNode> z = DTD.getAllNodes(transformed, filename.substring(0, filename.lastIndexOf(Constants.DOT)), file.getParent());
        
        for (int y = 0; y < z.size(); y++) {
        	System.out.println("Node: " + z.get(y) + z.get(y).toStringParents() + z.get(y).toStringChildren());
        }
        
        //Nodes omzetten naar SQL
        SQL.createSharedTables(z);
        
        System.out.println("--------------------END FILE--------------------");
	}   
}
