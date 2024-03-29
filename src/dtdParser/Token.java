package dtdParser;

/** Token returned by the lexical scanner
 *
 * @author Mark Wutka
 * @version $Revision: 1.16 $ $Date: 2002/07/19 01:20:11 $ by $Author: wutka $
 */
class Token
{
	public TokenType type;
	public String value;

	public Token(TokenType aType)
	{
		type = aType;
		value = null;
	}

	public Token(TokenType aType, String aValue)
	{
		type = aType;
		value = aValue;
	}
}
