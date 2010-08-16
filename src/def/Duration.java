package def;

public class Duration implements Comparable {
	private Time begin;
	private Time end;
	private String SQLdesc;
	
	public Duration(String SQLdesc) {
		this.SQLdesc = SQLdesc;
	}

	public String getSQLdesc() {
		return SQLdesc;
	}

	public Time getBegin() {
		return begin;
	}

	public void setBegin(Time begin) {
		this.begin = begin;
	}

	public Time getEnd() {
		return end;
	}

	public void setEnd(Time end) {
		this.end = end;
	}
	
	public String toString() {
		return this.SQLdesc + "\n::" + begin + "::" + end + "::" + end.getDuration(begin);
	}
	
	public int getDuration() {
		return end.getDuration(begin);
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
