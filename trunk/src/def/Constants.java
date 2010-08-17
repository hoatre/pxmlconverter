package def;
/**
 * Constants stores all the constants used for processing probabilistic XML documents.
 * @author Paul Stapersma
 */
public interface Constants {
	/* SQL language keywords */
	public static final String INT = " int ";
	public static final String STRING = " varchar(255) ";
	public static final String LONGTEXT = " text "; // MayBMS ondersteund geen LongText
	public static final String FLOAT = " float ";	
	public static final String HAAKJEOPEN = " ( ";
	public static final String HAAKJESLUITEN = " ) ";
	public static final String KOMMA = " , ";
	public static final String QUOTE = "'";
	public static final String SPACE = " ";
	public static final String PUNTKOMMA = ";";
	public static final String FOREIGNKEY = " FOREIGN KEY ";
	public static final String REFERENCES = " REFERENCES ";
	public static final String PRIMARYKEY = " PRIMARY KEY ";
	public static final String INSERTINTO = " INSERT INTO ";
	public static final String CHECK = " CHECK ";
	public static final String VALUES = " VALUES ";
	public static final String DOT = ".";	
	public static final String CREATETABLE = "CREATE TABLE ";
	public static final String DROPTABLE = "DROP TABLE ";
	public static final String DROPCOLUMN = "DROP COLUMN ";
	public static final String ALTERTABLE = "ALTER TABLE ";
	public static final String EXISTS = " EXISTS ";
	public static final String IF = " IF ";
	public static final String NOT = " NOT ";
	public static final String AS = " AS ";
	public static final String SELECT = "SELECT ";
	public static final String FROM = " FROM ";
	public static final String WHERE = " WHERE ";
	public static final String AND = " AND ";
	public static final String UNION = " UNION ";
	public static final String ASTERIX = "* ";
	public static final String CASCADE = " CASCADE ";
	public static final String ALL = " ALL ";
	public static final String LEFTJOIN = " LEFT JOIN ";	
	public static final String ON = " ON ";
	public static final String EQUALS = "=";
	public static final String RENAMETO = " RENAME TO ";
	public static final String ADD = " ADD ";	
	
	/* columnnames */
	public static final String PREORDER = "pre";
	public static final String POSTORDER = "post";
	public static final String PARENTPRE = "par";
	public static final String NODEKIND = "kind";
	public static final String TAGNAME = "tag";
	public static final String LEVEL = "level";
	public static final String POSSPRE = "possId";
	public static final String CDATA = "cdata";
	public static final String PROB = "prob";
	public static final String POSS = "poss";
	public static final String ID = "id";
	public static final String CODE = "code";
	public static final String PHASE = "phase";
	
	/* prefixes */
	public static final String PREFIX_ACCEL = "accel_";
	public static final String PREFIX_CONTENT = "content_";
	public static final String PREFIX_CP = "cp_";
	public static final String PREFIX_UCP = "ucp_";
	public static final String PREFIX_TEXTNODE = "text_";
	public static final String PREFIX_UACCEL = "uaccel_";
	public static final String PREFIX_PROBCOLUMN = "_pre";
	public static final String PREFIX_REPAIRKEY = "rk_";
	public static final String SHAREDPREFIX = "shared_";
	
	/* Starting values */
	public static final int INSTANTIATE_PREORDER = 0;
	public static final int INSTANTIATE_POSTORDER = 0;
	public static final int INSTANTIATE_LEVEL = 0;
	public static final int INSTANTIATE_PHASE = 0;
	public static final int START_ID = 0;
	public static final int START_POSS = 0;
	
	/* Defined XML elements */
	public static final String POSSNODE = "poss";
	public static final String PROBNODE = "prob";
	public static final String POSSATTR = "prob";
	public static final String ROOTNODE = "root";
	public static final String RESTPOSSNODE = "rest";
	
	/* other constants */
	public static final int NOVALUE = -1;
	public static final int ROOTPROBPRE = 0;
	public static final int ROOTPOSSPRE = -1;
}
