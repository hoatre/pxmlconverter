package def;

public class FileOutputException extends Exception
{
	public FileOutputException() {
		super("OutputStream kon niet worden aangemaakt");
	}
}
