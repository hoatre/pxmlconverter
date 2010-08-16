package def;

import java.util.Comparator;
import java.util.Vector;

public class DurationVector extends Vector<Duration> implements Comparator<Duration>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int compare(Duration o1, Duration o2) {
		if (o1.getDuration() < o2.getDuration())
			return 1;
		else if (o1.getDuration() == o2.getDuration())
			return 0;
		else
			return -1;
	}

}
