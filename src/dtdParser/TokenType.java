package dtdParser;

/** Enumerated value representing the type of a token
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
class TokenType
{
	public int value;
	public String name;

	public TokenType(int aValue, String aName)
	{
		value = aValue;
		name = aName;
	}

	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TokenType)) return false;

		TokenType other = (TokenType) o;
		if (other.value == value) return true;
		return false;
	}

	public int hashCode()
	{
		return name.hashCode();
	}
}
