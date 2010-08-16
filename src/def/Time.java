package def;

public class Time {
	private int uren;
	private int min;
	private int sec;
	private int msec;
	
	public Time(int uren, int min, int sec, int msec) {
		this.uren = uren;
		this.min = min;
		this.sec = sec;
		this.msec = msec;
	}

	public int getUren() {
		return uren;
	}

	public int getMin() {
		return min;
	}

	public int getSec() {
		return sec;
	}

	public int getMsec() {
		return msec;
	}
	
	public String toString() {
		return uren + ":" + min + ":" + sec + "." + msec;
	}
	
	public int getDuration(Time earlier) {
		return  (
					(this.getUren() - earlier.getUren()) * 360000 +
					(this.getMin() - earlier.getMin()) * 6000 +
					(this.getSec() - earlier.getSec()) * 100 +
					(this.getMsec() - earlier.getMsec())
				);
	}
	
	public static void main(String[] args) {
		Time begin = new Time(10,29,55,90);
		Time end = new Time(10,29,56,93);
		System.out.println(begin.getDuration(end));
	}
}
