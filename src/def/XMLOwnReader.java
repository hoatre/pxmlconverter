package def;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class XMLOwnReader {

	public static void main(String[] args) {

		FileWriter fw = makeOutputDoc("Output.txt");
		if (fw == null) {
			System.out.println("OutputStream niet aan kunnen maken");
			System.exit(0);
		}
		
		if (args.length < 1 || !args[0].endsWith(".xml")) {
			System.out.println("Geef aub een geldige bestandsnaam op als argument");
			System.exit(0);
		}
		
		File file = new File(args[0]);
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String buffer = null;
	
		Pattern tagWithAtt = Pattern.compile("<[a-zA-Z1-9]*");
		Pattern tagWithoutAtt = Pattern.compile("<[a-zA-Z1-9]*>");
		Pattern tagWithoutAttPlus = Pattern.compile("<[a-zA-Z1-9]*>[a-zA-Z1-9]*");

		while(sc != null && sc.hasNext()) {
			//tag met attributen		
			if(sc.hasNext(tagWithAtt)) {
				buffer = (sc.next() + " is een tag met attributen");
				System.out.println(sc.next() + " is een tag met attributen");
			} else if (sc.hasNext(tagWithoutAttPlus)) {
				buffer = (sc.findInLine(tagWithoutAtt) + " is een tag zonder attributen");
				System.out.println(sc.findInLine(tagWithoutAtt) + " is een tag zonder attributen");
			} else {
				buffer = (sc.next() + " geen match");
				System.out.println(sc.next() + " geen match");
			}
			
			try {
				fw.write(buffer + "\n");
				buffer = "";
			} catch(IOException f) {
				
			}
		}
		System.out.println("Klaar");
	}
	
	public static void start() {
		
	}
	
	public static FileWriter makeOutputDoc(String fileName) {
		FileWriter fw = null;
		
		try {
			File file = new File(fileName);
			fw = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fw;
	}
}
