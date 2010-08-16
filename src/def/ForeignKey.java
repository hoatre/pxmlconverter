package def;

public class ForeignKey {
	private String column;
	private String otherTable;
	private String otherColumn;
	
	public ForeignKey(String column, String otherTable, String otherColumn) {
		this.column = column;
		this.otherColumn = otherColumn;
		this.otherTable = otherTable;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getOtherTable() {
		return otherTable;
	}

	public void setOtherTable(String otherTable) {
		this.otherTable = otherTable;
	}

	public String getOtherColumn() {
		return otherColumn;
	}

	public void setOtherColumn(String otherColumn) {
		this.otherColumn = otherColumn;
	}
	
	
}
