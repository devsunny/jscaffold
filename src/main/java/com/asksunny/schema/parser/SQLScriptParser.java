package com.asksunny.schema.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.TabableView;

import com.asksunny.codegen.CodeGenAnnotation;
import com.asksunny.codegen.FieldDomainType;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Field;
import com.asksunny.schema.Schema;

@SuppressWarnings("unused")
public class SQLScriptParser {

	private final SQLScriptLookaheadTokenReader tokenReader;
	private boolean debug = true;
	private PrintWriter debugWriter = null;

	public void debug(Object... args) {
		if (this.debug) {
			if (debugWriter == null) {
				debugWriter = new PrintWriter(System.out);
			}
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof Token) {
					Token t = (Token) args[i];
					debugWriter.println(String.format("%d:%d [%s] [%s] [%s]", t.getLine(), t.getColumn(), t.getImage(),
							t.getKind(), t.getKeyword()));
					debugWriter.flush();
				} else {
					if (args[i] instanceof String && args.length > 1) {
						debugWriter.print(args[i]);
						debugWriter.print(" ");

					} else {
						debugWriter.println(args[i]);
					}

				}

			}
			debugWriter.println();
			debugWriter.flush();
		}
	}

	public SQLScriptParser(SQLScriptLexer sqlLexer) throws IOException {
		super();
		this.tokenReader = new SQLScriptLookaheadTokenReader(3, sqlLexer);
	}

	public SQLScriptParser(File sqlFile) throws IOException {
		this(new FileReader(sqlFile));
	}

	public SQLScriptParser(String sqlText) throws IOException {
		this(new StringReader(sqlText));
	}

	public SQLScriptParser(InputStream in, Charset encode) throws IOException {
		this(new InputStreamReader(in, encode));
	}

	public SQLScriptParser(Reader reader) throws IOException {
		super();
		this.tokenReader = new SQLScriptLookaheadTokenReader(3, new SQLScriptLexer(reader));
	}

	public Schema parseSql() throws IOException {
		Schema schema = new Schema();
		try {
			Token t = null;
			while ((t = peek()) != null) {
				debug(t);
				switch (t.getKind()) {
				case KEYWORD:
					debug(String.format("start parsing statement:[%s]", t.getImage()));
					switch (t.getKeyword()) {
					case CREATE:
						consume();
						parseCreateBody(schema);
						break;
					case ALTER:
						consume();
						break;
					case DROP:
						consume();
						break;
					case SELECT:
					case INSERT:
					case UPDATE:
					case DELETE:
						consume();
						break;
					default:
						consume();
						break;
					}
					// parseStatement(schema, t);
					break;
				default:
					consume();
					break;
				}
			}
		} finally {
			close();
		}
		return schema;
	}

	protected Token consume() throws IOException {
		Token tt = tokenReader.read();
		if (tt == null) {
			throw new InvalidSQLException("Unexpected end of token stream");
		}
		return tt;
	}

	protected Token peek() throws IOException {
		Token tt = tokenReader.peek(0);
		return tt;
	}

	protected Token peek(int idx) throws IOException {
		Token tt = tokenReader.peek(idx);
		return tt;
	}

	protected void drain(int number) throws IOException {
		int d = number;
		while (d > 0) {
			Token tt = tokenReader.read();
			if (tt == null) {
				throw new InvalidSQLException("Unexpected end of token stream");
			} else {
				d--;
			}
		}
	}

	protected boolean matchAny(Token tok, LexerTokenKind... kinds) throws IOException {
		for (int i = 0; i < kinds.length; i++) {
			if (tok != null && tok.getKind() == kinds[i]) {
				return true;
			}
		}
		return false;
	}

	protected boolean matchAny(Token tok, Keyword... keywords) throws IOException {
		for (int i = 0; i < keywords.length; i++) {
			if (tok != null && tok.getKeyword() == keywords[i]) {
				return true;
			}
		}
		return false;
	}

	protected void consumeAllNotMatch(LexerTokenKind... kinds) throws IOException {
		Token tok = null;
		while ((tok = peek()) != null) {
			if (matchAny(tok, kinds)) {
				return;
			} else {
				consume();
			}
		}

	}

	protected void consumeAllNotMatch(Keyword... keywords) throws IOException {
		Token tok = null;
		while ((tok = peek()) != null) {
			if (matchAny(tok, keywords)) {
				return;
			} else {
				consume();
			}
		}

	}

	protected void consumeParenthesis() throws IOException {
		int p = 1;
		consume();
		Token tok = null;
		while ((tok = peek()) != null) {
			consume();
			if (tok.getKind() == LexerTokenKind.LPAREN) {
				p++;
			} else if (tok.getKind() == LexerTokenKind.RPAREN) {
				p--;
			}
			if (p == 0) {
				return;
			}
		}
	}

	public boolean peekMatch(int idx, LexerTokenKind kind, LexerTokenKind... kinds) {
		Token p = tokenReader.peek(idx);
		if (p == null) {
			throw new InvalidSQLException("Unexpected end of token stream");
		}
		for (LexerTokenKind k : kinds) {
			if (k == p.getKind()) {
				return true;
			}
		}
		return (p.getKind() == kind);
	}

	public boolean peekMatch(int idx, LexerTokenKind kind) {
		Token p = tokenReader.peek(idx);
		if (p == null) {
			throw new InvalidSQLException("Unexpected end of token stream");
		}
		return (p.getKind() == kind);
	}

	public boolean peekMatch(int idx, Keyword keyword) {
		Token p = tokenReader.peek(idx);
		if (p == null) {
			throw new InvalidSQLException("Unexpected end of token stream");
		}
		return (p.getKind() == LexerTokenKind.KEYWORD && p.getKeyword() == keyword);
	}

	protected String parseTableName() throws IOException {
		Token tok = consume();
		Token speTok = peek();
		if (speTok != null && speTok.getImage().equals(".")) {
			consume();
			tok = consume();
		}
		if (tok == null) {
			throw new InvalidSQLException("<table_name>", "null", -1, -1);
		}
		return tok.getImage();
	}

	protected void parseCreateBody(Schema schema) throws IOException {
		consumeAllNotMatch(Keyword.TABLE, Keyword.INDEX);
		Token tok = consume();
		switch (tok.getKeyword()) {
		case TABLE:
			String tbName = parseTableName();
			consumeAllNotMatch(LexerTokenKind.LPAREN);
			consume();
			Entity entity = new Entity(tbName);
			parseFields(entity);
			schema.addEntity(entity);
			break;
		case ALTER:
			consumeAllNotMatch(Keyword.CREATE, Keyword.ALTER, Keyword.DROP, Keyword.INSERT, Keyword.UPDATE,
					Keyword.DELETE, Keyword.SELECT);
			break;
		default:
			consumeAllNotMatch(Keyword.CREATE, Keyword.ALTER, Keyword.DROP, Keyword.INSERT, Keyword.UPDATE,
					Keyword.DELETE, Keyword.SELECT);
			break;
		}
	}

	public void close() throws IOException {
		if (this.tokenReader != null) {
			this.tokenReader.close();
		}
	}

	protected void parseFields(Entity entity) throws IOException {
		Token tok = peek();
		while (!(matchAny(tok, Keyword.PRIMARY, Keyword.CONSTRAINT, Keyword.FOREIGN))) {
			if (tok.getKind() == LexerTokenKind.RPAREN) {
				break;
			}
			parseField(entity);
			tok = peek();
		}

		//Parse table level key and constraints:
		
		while ((tok = peek()) != null) {
			switch (tok.getKind()) {
			case SEMICOLON:
				consume();
				return;
			case RPAREN:
				consume();
				break;
			default:
				switch (tok.getKeyword()) {
				case PRIMARY:
					//TODO:
					consume();
					break;
				case UNIQUE:
					//TODO:
					consume();
					break;
				case CONSTRAINT:
					//TODO:
					consume();
					break;
				case FOREIGN:
					//TODO:
					consume();
					break;
				default:
					if (tok.getKeyword() == Keyword.CREATE || tok.getKeyword() == Keyword.DROP
							|| tok.getKeyword() == Keyword.ALTER || tok.getKeyword() == Keyword.INSERT
							|| tok.getKeyword() == Keyword.UPDATE || tok.getKeyword() == Keyword.DELETE
							|| tok.getKeyword() == Keyword.SELECT) {
						return;
					} else {
						consume();
					}
					break;
				}
				break;
			}
		}

	}

	/**
	 * column_name type_name (1, 2)
	 * 
	 * @param entity
	 * @throws IOException
	 */
	protected void parseField(Entity entity) throws IOException {

		Token tok = consume();
		Field field = new Field();
		field.setName(tok.getImage());
		Token tokType = consume();
		field.setJdbcType(JdbcSqlTypeMap.getInstance().findJdbcType(tokType.getImage()));
		field.setDbTypeName(tokType.getImage());
		entity.addField(field);

		if (peekMatch(0, LexerTokenKind.LPAREN)) {
			consume();
			parseFieldSizeSpec(field);
		}
		while ((tok = peek()) != null) {
			switch (tok.getKind()) {
			case COMMA:
				consume();
				tok = peek();
				if (tok != null && tok.getKind() == LexerTokenKind.ANNOTATION_COMMENT) {
					Token annoToken = consume();
					parseAnnotationComment(field, annoToken.getImage());
				}
				return;
			case RPAREN:
				return;
			default:
				switch (tok.getKeyword()) {
				case NOT:
					consume();
					if (peekMatch(0, Keyword.NULL)) {
						consume();
						field.setNullable(false);
					}
					break;
				case NULL:
					field.setNullable(true);
					consume();
					break;
				case PRIMARY:
					consume();
					if (peekMatch(0, Keyword.KEY)) {
						consume();
						field.setPrimaryKey(true);
					}
					break;
				case KEY:
					consume();
					field.setPrimaryKey(true);
					break;
				case DEFAULT:
					consume();
					Token dftok = consume();
					field.setDefaultValue(dftok.getImage());
					break;
				case UNIQUE:
					consume();
					if (peekMatch(0, Keyword.KEY)) {
						consume();
					}
					field.setUnique(true);
					break;
				case FORMAT:
					consume();
					Token ftok = consume();
					field.setFormat(ftok.getImage());
					break;
				case IDENTITY:
					consume();
					field.setDataType(FieldDomainType.SEQUENCE);
					Token px = peek();
					if (px.getKind() == LexerTokenKind.LPAREN) {
						consumeAllNotMatch(LexerTokenKind.RPAREN);
						consume();
					}
					break;
				default:
					if (tok.getKind() == LexerTokenKind.LPAREN) {
						consumeParenthesis();
					} else {
						consume();
					}
					break;
				}
				break;
			}
		}

	}

	protected void parseFieldSizeSpec(Field field) throws IOException {
		Token tok = consume();

		if (tok.getKeyword() == Keyword.ASTERISK) {
			field.setPrecision(16);
			field.setDisplaySize(16);
			consume();
		} else if (tok.getKind() == LexerTokenKind.NUMBER) {
			field.setPrecision(Integer.valueOf(tok.getImage()));
			field.setDisplaySize(field.getPrecision());
			if (peekMatch(0, LexerTokenKind.COMMA)) {
				consume();
				tok = consume();
				if (tok.getKind() != LexerTokenKind.NUMBER) {
					throw new InvalidSQLException("NUMBER", tok.getImage(), tok.getLine(), tok.getColumn());
				} else {
					field.setScale(Integer.valueOf(tok.getImage()));
				}
			}
			consumeAllNotMatch(LexerTokenKind.RPAREN);
			consume();
		} else if (tok.getKind() == LexerTokenKind.RPAREN) {
			field.setPrecision(16);
			field.setDisplaySize(16);
		} else {
			throw new InvalidSQLException("NUMBER", tok.getImage(), tok.getLine(), tok.getColumn());
		}
	}

	protected void parseAnnotationComment(Field field, String commentText) {
		try {
			CodeGenAnnoParser parser = new CodeGenAnnoParser(new CodeGenAnnoLexer(new StringReader(commentText), 0, 0));
			CodeGenAnnotation anno = parser.parseCodeAnnotation();
			field.setDataType(anno.getCodeGenType());
			if (anno.getRef() != null) {
				String[] refs = anno.getRef().split("\\.");
				if (refs.length != 2) {
					throw new InvalidSQLException(String.format("Invalid ref format[%s]", anno.getRef()));
				}
				Field xf = new Field();
				xf.setName(refs[1]);
				xf.setContainer(new Entity(refs[0]));
				field.setReference(xf);
			}
			field.setAnnotation(anno);
			parser.close();
		} catch (Exception ex) {
			throw new RuntimeException("Invalid Annotation:" + commentText, ex);
		}

	}

	protected List<Token> consumeItemList() throws IOException {
		List<Token> toks = new ArrayList<Token>();
		if (peekMatch(0, LexerTokenKind.LPAREN)) {
			consume();
			while (!peekMatch(0, LexerTokenKind.RPAREN)) {
				Token t = consume();
				debug("consumeItemList", t);
				if (t.kind != LexerTokenKind.COMMA) {
					toks.add(t);
				}
			}
			Token tt = consume();
			debug("RPAREN", tt);
		}
		return toks;
	}

	protected void ignoreStatement() throws IOException {
		Token t = null;
		while ((t = tokenReader.read()) != null) {
			if (t.getKind() == LexerTokenKind.SEMICOLON) {
				break;
			}
		}
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public PrintWriter getDebugWriter() {
		return debugWriter;
	}

	public void setDebugWriter(PrintWriter debugWriter) {
		this.debugWriter = debugWriter;
	}

}
