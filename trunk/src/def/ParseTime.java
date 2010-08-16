package def;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Pattern;

public class ParseTime {
	public static final String START = "start";
	public static final String STOP = "end";
	
	public static FileReader fr;
	public static Scanner sc;
	public static DurationVector times;
	public static Comparator<Duration> comp;
	
	public static void main(String[] args) throws FileNotFoundException {
		fr = new FileReader("time.txt");
		sc = new Scanner(fr);
		times = new DurationVector();
		parse(sc);
		
		for(int i = 0; i <times.size(); i++ ){
			System.out.println(times.get(i));
		}
	}
	
	
	
	public static void parse(Scanner sc) {//10:29:55.90 start accel_1000
		while(sc.hasNext()) {
			if (sc.hasNext(Pattern.compile("\\d\\d:\\d\\d:\\d\\d.\\d\\d"))) {
				String time = sc.next();
				String status = sc.next();
				String description = sc.next();
				makeDuration(description, parseTime(time), status);
			} else {
				System.out.println(sc.next());
			}
		}
	}
	
	private static void makeDuration(String descr, Time time, String status) {

		if (status.equals(START)) {
			Duration dur = new Duration(descr);			
			dur.setBegin(time);
			times.add(0,dur);
		} else if (status.equals(STOP)) {
			Duration dur = times.firstElement();
			if (!dur.getSQLdesc().equals(descr)) {
				System.out.println("ERROR: descriptions komen niet overeen");
			}
			dur.setEnd(time);
		} else {
			System.out.println("Geen geldige status : " + status);
		}
	}

	public static Time parseTime(String time) {
		Scanner hsc = new Scanner(time);
		String uren = 		hsc.findInLine("\\d\\d");
							hsc.findInLine(":");
		String min = 		hsc.findInLine("\\d\\d");
							hsc.findInLine(":");
		String sec = 		hsc.findInLine("\\d\\d");
							hsc.findInLine(".");
		String msec = 		hsc.findInLine("\\d\\d");
		return new Time(Integer.parseInt(uren), Integer.parseInt(min), Integer.parseInt(sec), Integer.parseInt(msec));
	}
}


