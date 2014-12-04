// $ANTLR 3.5 FunChecker.g 2014-12-04 13:23:02

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class FunChecker extends TreeParser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ASSN", "BOOL", "COLON", "COMMENT", 
		"DIGIT", "DIV", "DO", "DOT", "ELSE", "EOL", "EQ", "FALSE", "FOR", "FORMAL", 
		"FUNC", "FUNCCALL", "GT", "ID", "IF", "IFELSE", "INT", "LETTER", "LPAR", 
		"LT", "MINUS", "NOACTUAL", "NOFORMAL", "NOT", "NUM", "PLUS", "PROC", "PROCCALL", 
		"PROG", "REPEAT", "RETURN", "RPAR", "SEQ", "SPACE", "TIMES", "TO", "TRUE", 
		"UNTIL", "VAR", "WHILE"
	};
	public static final int EOF=-1;
	public static final int ASSN=4;
	public static final int BOOL=5;
	public static final int COLON=6;
	public static final int COMMENT=7;
	public static final int DIGIT=8;
	public static final int DIV=9;
	public static final int DO=10;
	public static final int DOT=11;
	public static final int ELSE=12;
	public static final int EOL=13;
	public static final int EQ=14;
	public static final int FALSE=15;
	public static final int FOR=16;
	public static final int FORMAL=17;
	public static final int FUNC=18;
	public static final int FUNCCALL=19;
	public static final int GT=20;
	public static final int ID=21;
	public static final int IF=22;
	public static final int IFELSE=23;
	public static final int INT=24;
	public static final int LETTER=25;
	public static final int LPAR=26;
	public static final int LT=27;
	public static final int MINUS=28;
	public static final int NOACTUAL=29;
	public static final int NOFORMAL=30;
	public static final int NOT=31;
	public static final int NUM=32;
	public static final int PLUS=33;
	public static final int PROC=34;
	public static final int PROCCALL=35;
	public static final int PROG=36;
	public static final int REPEAT=37;
	public static final int RETURN=38;
	public static final int RPAR=39;
	public static final int SEQ=40;
	public static final int SPACE=41;
	public static final int TIMES=42;
	public static final int TO=43;
	public static final int TRUE=44;
	public static final int UNTIL=45;
	public static final int VAR=46;
	public static final int WHILE=47;

	// delegates
	public TreeParser[] getDelegates() {
		return new TreeParser[] {};
	}

	// delegators


	public FunChecker(TreeNodeStream input) {
		this(input, new RecognizerSharedState());
	}
	public FunChecker(TreeNodeStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return FunChecker.tokenNames; }
	@Override public String getGrammarFileName() { return "FunChecker.g"; }



		// Contextual errors

		private int errorCount = 0;

		private void reportError (String message,
		                          CommonTree ast) {
		// Print an error message relating to the given 
		// (sub)AST.
			int line = ast.getLine(),
			    column = ast.getCharPositionInLine() ;
			System.err.println("line " + line + ":" + column
			   + " " + message);
			errorCount++;
		}

		public int getNumberOfContextualErrors () {
		// Return the total number of errors so far detected.
			return errorCount;
		}


		// Scope checking

		private SymbolTable<Type> typeTable =
		   new SymbolTable<Type>();

		private void predefine () {
		// Add predefined procedures to the type table.
			typeTable.put("read",
			   new Type.Mapping(Type.VOID, Type.INT));
			typeTable.put("write",
			   new Type.Mapping(Type.INT, Type.VOID));
		}

		private void define (String id, Type type,
		                     CommonTree decl) {
		// Add id with its type to the type table, checking 
		// that id is not already declared in the same scope.
			boolean ok = typeTable.put(id, type);
			if (!ok)
				reportError(id + " is redeclared", decl);
		}

		private Type retrieve (String id, CommonTree occ) {
		// Retrieve id's type from the type table.
			Type type = typeTable.get(id);
			if (type == null) {
				reportError(id + " is undeclared", occ);
				return Type.ERROR;
			} else
				return type;
		}


		// Type checking

		private static final Type.Mapping
		   NOTTYPE = new Type.Mapping(Type.BOOL, Type.BOOL),
		   COMPTYPE = new Type.Mapping(
		      new Type.Pair(Type.INT, Type.INT), Type.BOOL),
		   ARITHTYPE = new Type.Mapping(
		      new Type.Pair(Type.INT, Type.INT), Type.INT),
		   MAINTYPE = new Type.Mapping(Type.VOID, Type.VOID);

		private void checkType (Type typeExpected,
		                        Type typeActual,
		                        CommonTree construct) {
		// Check that a construct's actual type matches 
		// the expected type.
			if (! typeActual.equiv(typeExpected))
				reportError("type is " + typeActual
				   + ", should be " + typeExpected,
				   construct);
		}

		private Type checkCall (String id, Type typeArg,
		                        CommonTree call) {
		// Check that a procedure call identifies a procedure 
		// and that its argument type matches the proecure's 
		// type. Return the type of the procedure call.
			Type typeProc = retrieve(id, call);
			if (! (typeProc instanceof Type.Mapping)) {
				reportError(id + " is not a procedure", call);
				return Type.ERROR;
			} else {
				Type.Mapping mapping = (Type.Mapping)typeProc;
				checkType(mapping.domain, typeArg, call);
				return mapping.range;
			}
		}

		private Type checkUnary (Type.Mapping typeOp,
		                         Type typeArg,
		                         CommonTree op) {
		// Check that a unary operator's operand type matches 
		// the operator's type. Return the type of the operator 
		// application.
			if (! (typeOp.domain instanceof Type.Primitive))
				reportError(
				   "unary operator should have 1 operand",
				   op);
			else
				checkType(typeOp.domain, typeArg, op);
			return typeOp.range;
		}

		private Type checkBinary (Type.Mapping typeOp,
		                          Type typeArg1, Type typeArg2,
		                          CommonTree op) {
		// Check that a binary operator's operand types match 
		// the operator's type. Return the type of the operator 
		// application.
			if (! (typeOp.domain instanceof Type.Pair))
				reportError(
				   "binary operator should have 2 operands",
				   op);
			else {
				Type.Pair pair =
				   (Type.Pair)typeOp.domain;
				checkType(pair.first, typeArg1, op);
				checkType(pair.second, typeArg2, op);
			}
			return typeOp.range;
		}




	// $ANTLR start "program"
	// FunChecker.g:156:1: program : ^( PROG ( var_decl )* ( proc_decl )+ ) ;
	public final void program() throws RecognitionException {
		CommonTree PROG1=null;

		try {
			// FunChecker.g:157:2: ( ^( PROG ( var_decl )* ( proc_decl )+ ) )
			// FunChecker.g:157:4: ^( PROG ( var_decl )* ( proc_decl )+ )
			{
			PROG1=(CommonTree)match(input,PROG,FOLLOW_PROG_in_program60); 
			 predefine(); 
			match(input, Token.DOWN, null); 
			// FunChecker.g:159:5: ( var_decl )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==VAR) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// FunChecker.g:159:5: var_decl
					{
					pushFollow(FOLLOW_var_decl_in_program72);
					var_decl();
					state._fsp--;

					}
					break;

				default :
					break loop1;
				}
			}

			// FunChecker.g:160:5: ( proc_decl )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0==FUNC||LA2_0==PROC) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// FunChecker.g:160:5: proc_decl
					{
					pushFollow(FOLLOW_proc_decl_in_program79);
					proc_decl();
					state._fsp--;

					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			match(input, Token.UP, null); 

			 Type tmain = retrieve("main", PROG1);
							  checkType(tmain, MAINTYPE, PROG1);
							
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "program"



	// $ANTLR start "proc_decl"
	// FunChecker.g:170:1: proc_decl : ( ^( PROC ID t= formal ( var_decl )* com ) | ^( FUNC t1= type ID t2= formal ( var_decl )* com t3= expr ) );
	public final void proc_decl() throws RecognitionException {
		CommonTree ID2=null;
		CommonTree PROC3=null;
		CommonTree ID4=null;
		CommonTree FUNC5=null;
		Type t =null;
		Type t1 =null;
		Type t2 =null;
		Type t3 =null;

		try {
			// FunChecker.g:171:2: ( ^( PROC ID t= formal ( var_decl )* com ) | ^( FUNC t1= type ID t2= formal ( var_decl )* com t3= expr ) )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==PROC) ) {
				alt5=1;
			}
			else if ( (LA5_0==FUNC) ) {
				alt5=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}

			switch (alt5) {
				case 1 :
					// FunChecker.g:171:4: ^( PROC ID t= formal ( var_decl )* com )
					{
					PROC3=(CommonTree)match(input,PROC,FOLLOW_PROC_in_proc_decl106); 
					match(input, Token.DOWN, null); 
					ID2=(CommonTree)match(input,ID,FOLLOW_ID_in_proc_decl112); 
					 typeTable.enterLocalScope();
									
					pushFollow(FOLLOW_formal_in_proc_decl126);
					t=formal();
					state._fsp--;

					 Type proctype =
									    new Type.Mapping(t, Type.VOID);
									  define((ID2!=null?ID2.getText():null), proctype, PROC3);
									  // ... to enable recursion
									
					// FunChecker.g:181:5: ( var_decl )*
					loop3:
					while (true) {
						int alt3=2;
						int LA3_0 = input.LA(1);
						if ( (LA3_0==VAR) ) {
							alt3=1;
						}

						switch (alt3) {
						case 1 :
							// FunChecker.g:181:5: var_decl
							{
							pushFollow(FOLLOW_var_decl_in_proc_decl138);
							var_decl();
							state._fsp--;

							}
							break;

						default :
							break loop3;
						}
					}

					pushFollow(FOLLOW_com_in_proc_decl145);
					com();
					state._fsp--;

					 typeTable.exitLocalScope();
									  define((ID2!=null?ID2.getText():null), proctype, PROC3);
									
					match(input, Token.UP, null); 

					}
					break;
				case 2 :
					// FunChecker.g:187:4: ^( FUNC t1= type ID t2= formal ( var_decl )* com t3= expr )
					{
					FUNC5=(CommonTree)match(input,FUNC,FOLLOW_FUNC_in_proc_decl162); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_type_in_proc_decl170);
					t1=type();
					state._fsp--;

					ID4=(CommonTree)match(input,ID,FOLLOW_ID_in_proc_decl176); 
					 typeTable.enterLocalScope();
									
					pushFollow(FOLLOW_formal_in_proc_decl190);
					t2=formal();
					state._fsp--;

					 Type functype =
									    new Type.Mapping(t2, t1);
									  define((ID4!=null?ID4.getText():null), functype, FUNC5);
									  // ... to enable recursion
									
					// FunChecker.g:198:5: ( var_decl )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( (LA4_0==VAR) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// FunChecker.g:198:5: var_decl
							{
							pushFollow(FOLLOW_var_decl_in_proc_decl202);
							var_decl();
							state._fsp--;

							}
							break;

						default :
							break loop4;
						}
					}

					pushFollow(FOLLOW_com_in_proc_decl209);
					com();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_proc_decl217);
					t3=expr();
					state._fsp--;

					 typeTable.exitLocalScope();
									  define((ID4!=null?ID4.getText():null), functype, FUNC5);
									  checkType(t1, t3, FUNC5);
									
					match(input, Token.UP, null); 

					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "proc_decl"



	// $ANTLR start "formal"
	// FunChecker.g:208:1: formal returns [Type type] : ( ^( FORMAL t= type ID ) | NOFORMAL );
	public final Type formal() throws RecognitionException {
		Type type = null;


		CommonTree ID6=null;
		CommonTree FORMAL7=null;
		Type t =null;

		try {
			// FunChecker.g:209:2: ( ^( FORMAL t= type ID ) | NOFORMAL )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==FORMAL) ) {
				alt6=1;
			}
			else if ( (LA6_0==NOFORMAL) ) {
				alt6=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// FunChecker.g:209:4: ^( FORMAL t= type ID )
					{
					FORMAL7=(CommonTree)match(input,FORMAL,FOLLOW_FORMAL_in_formal246); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_type_in_formal250);
					t=type();
					state._fsp--;

					ID6=(CommonTree)match(input,ID,FOLLOW_ID_in_formal252); 
					match(input, Token.UP, null); 

					 define((ID6!=null?ID6.getText():null), t, FORMAL7);
									  type = t;
									
					}
					break;
				case 2 :
					// FunChecker.g:213:4: NOFORMAL
					{
					match(input,NOFORMAL,FOLLOW_NOFORMAL_in_formal264); 
					 type = Type.VOID; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return type;
	}
	// $ANTLR end "formal"



	// $ANTLR start "var_decl"
	// FunChecker.g:217:1: var_decl : ^( VAR t1= type ID t2= expr ) ;
	public final void var_decl() throws RecognitionException {
		CommonTree ID8=null;
		CommonTree VAR9=null;
		Type t1 =null;
		Type t2 =null;

		try {
			// FunChecker.g:218:2: ( ^( VAR t1= type ID t2= expr ) )
			// FunChecker.g:218:4: ^( VAR t1= type ID t2= expr )
			{
			VAR9=(CommonTree)match(input,VAR,FOLLOW_VAR_in_var_decl282); 
			match(input, Token.DOWN, null); 
			pushFollow(FOLLOW_type_in_var_decl286);
			t1=type();
			state._fsp--;

			ID8=(CommonTree)match(input,ID,FOLLOW_ID_in_var_decl288); 
			pushFollow(FOLLOW_expr_in_var_decl292);
			t2=expr();
			state._fsp--;

			match(input, Token.UP, null); 

			 define((ID8!=null?ID8.getText():null), t1, VAR9);
							  checkType(t1, t2, VAR9);
							
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "var_decl"



	// $ANTLR start "type"
	// FunChecker.g:224:1: type returns [Type type] : ( BOOL | INT );
	public final Type type() throws RecognitionException {
		Type type = null;


		try {
			// FunChecker.g:225:2: ( BOOL | INT )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==BOOL) ) {
				alt7=1;
			}
			else if ( (LA7_0==INT) ) {
				alt7=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					// FunChecker.g:225:4: BOOL
					{
					match(input,BOOL,FOLLOW_BOOL_in_type317); 
					 type = Type.BOOL; 
					}
					break;
				case 2 :
					// FunChecker.g:226:4: INT
					{
					match(input,INT,FOLLOW_INT_in_type325); 
					 type = Type.INT; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return type;
	}
	// $ANTLR end "type"



	// $ANTLR start "com"
	// FunChecker.g:232:1: com : ( ^( ASSN ID t= expr ) | ^( PROCCALL ID t= expr ) | ^( IF t= expr com ) | ^( IFELSE t= expr com com ) | ^( WHILE t= expr com ) | ^( FOR ID t1= expr t2= expr com ) | ^( DO t1= expr com ) | ^( SEQ ( com )* ) );
	public final void com() throws RecognitionException {
		CommonTree ID10=null;
		CommonTree ASSN11=null;
		CommonTree ID12=null;
		CommonTree PROCCALL13=null;
		CommonTree IF14=null;
		CommonTree IFELSE15=null;
		CommonTree WHILE16=null;
		CommonTree ID17=null;
		CommonTree FOR18=null;
		CommonTree DO19=null;
		Type t =null;
		Type t1 =null;
		Type t2 =null;

		try {
			// FunChecker.g:233:2: ( ^( ASSN ID t= expr ) | ^( PROCCALL ID t= expr ) | ^( IF t= expr com ) | ^( IFELSE t= expr com com ) | ^( WHILE t= expr com ) | ^( FOR ID t1= expr t2= expr com ) | ^( DO t1= expr com ) | ^( SEQ ( com )* ) )
			int alt9=8;
			switch ( input.LA(1) ) {
			case ASSN:
				{
				alt9=1;
				}
				break;
			case PROCCALL:
				{
				alt9=2;
				}
				break;
			case IF:
				{
				alt9=3;
				}
				break;
			case IFELSE:
				{
				alt9=4;
				}
				break;
			case WHILE:
				{
				alt9=5;
				}
				break;
			case FOR:
				{
				alt9=6;
				}
				break;
			case DO:
				{
				alt9=7;
				}
				break;
			case SEQ:
				{
				alt9=8;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}
			switch (alt9) {
				case 1 :
					// FunChecker.g:233:4: ^( ASSN ID t= expr )
					{
					ASSN11=(CommonTree)match(input,ASSN,FOLLOW_ASSN_in_com343); 
					match(input, Token.DOWN, null); 
					ID10=(CommonTree)match(input,ID,FOLLOW_ID_in_com345); 
					pushFollow(FOLLOW_expr_in_com349);
					t=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 Type tvar =
									    retrieve((ID10!=null?ID10.getText():null), ASSN11);
									  checkType(tvar, t, ASSN11);
									
					}
					break;
				case 2 :
					// FunChecker.g:238:4: ^( PROCCALL ID t= expr )
					{
					PROCCALL13=(CommonTree)match(input,PROCCALL,FOLLOW_PROCCALL_in_com362); 
					match(input, Token.DOWN, null); 
					ID12=(CommonTree)match(input,ID,FOLLOW_ID_in_com364); 
					pushFollow(FOLLOW_expr_in_com368);
					t=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 Type tres = checkCall(
									    (ID12!=null?ID12.getText():null), t, PROCCALL13);
									  if (! tres.equiv(Type.VOID))
									    reportError(
									      "procedure should be void",
									      PROCCALL13);
									
					}
					break;
				case 3 :
					// FunChecker.g:246:4: ^( IF t= expr com )
					{
					IF14=(CommonTree)match(input,IF,FOLLOW_IF_in_com381); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_com385);
					t=expr();
					state._fsp--;

					pushFollow(FOLLOW_com_in_com387);
					com();
					state._fsp--;

					match(input, Token.UP, null); 

					 checkType(Type.BOOL, t, IF14);
									
					}
					break;
				case 4 :
					// FunChecker.g:249:4: ^( IFELSE t= expr com com )
					{
					IFELSE15=(CommonTree)match(input,IFELSE,FOLLOW_IFELSE_in_com400); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_com404);
					t=expr();
					state._fsp--;

					pushFollow(FOLLOW_com_in_com406);
					com();
					state._fsp--;

					pushFollow(FOLLOW_com_in_com408);
					com();
					state._fsp--;

					match(input, Token.UP, null); 

					 checkType(Type.BOOL, t, IFELSE15);
									
					}
					break;
				case 5 :
					// FunChecker.g:252:4: ^( WHILE t= expr com )
					{
					WHILE16=(CommonTree)match(input,WHILE,FOLLOW_WHILE_in_com421); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_com425);
					t=expr();
					state._fsp--;

					pushFollow(FOLLOW_com_in_com427);
					com();
					state._fsp--;

					match(input, Token.UP, null); 

					 checkType(Type.BOOL, t, WHILE16);
									
					}
					break;
				case 6 :
					// FunChecker.g:256:4: ^( FOR ID t1= expr t2= expr com )
					{
					FOR18=(CommonTree)match(input,FOR,FOLLOW_FOR_in_com442); 
					match(input, Token.DOWN, null); 
					ID17=(CommonTree)match(input,ID,FOLLOW_ID_in_com444); 
					pushFollow(FOLLOW_expr_in_com448);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_com452);
					t2=expr();
					state._fsp--;

					pushFollow(FOLLOW_com_in_com454);
					com();
					state._fsp--;

					match(input, Token.UP, null); 

					 Type tvar = retrieve((ID17!=null?ID17.getText():null), FOR18);  //  define((ID17!=null?ID17.getText():null), Type.INT, FOR18);
									  checkType(tvar, t1, FOR18);

									  //checkType(Type.INT, t1, FOR18);
									  checkType(tvar, t2, FOR18); // They should be the same type
					                  //define((ID17!=null?ID17.getText():null), tvar, FOR18);

									
					}
					break;
				case 7 :
					// FunChecker.g:266:4: ^( DO t1= expr com )
					{
					DO19=(CommonTree)match(input,DO,FOLLOW_DO_in_com470); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_com474);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_com_in_com476);
					com();
					state._fsp--;

					match(input, Token.UP, null); 

					 checkType(Type.INT, t1, DO19);
									
					}
					break;
				case 8 :
					// FunChecker.g:271:3: ^( SEQ ( com )* )
					{
					match(input,SEQ,FOLLOW_SEQ_in_com492); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// FunChecker.g:271:9: ( com )*
						loop8:
						while (true) {
							int alt8=2;
							int LA8_0 = input.LA(1);
							if ( (LA8_0==ASSN||LA8_0==DO||LA8_0==FOR||(LA8_0 >= IF && LA8_0 <= IFELSE)||LA8_0==PROCCALL||LA8_0==SEQ||LA8_0==WHILE) ) {
								alt8=1;
							}

							switch (alt8) {
							case 1 :
								// FunChecker.g:271:9: com
								{
								pushFollow(FOLLOW_com_in_com494);
								com();
								state._fsp--;

								}
								break;

							default :
								break loop8;
							}
						}

						match(input, Token.UP, null); 
					}

					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "com"



	// $ANTLR start "expr"
	// FunChecker.g:277:1: expr returns [Type type] : ( FALSE | TRUE | NUM | ID | ^( FUNCCALL ID t= expr ) | ^( EQ t1= expr t2= expr ) | ^( LT t1= expr t2= expr ) | ^( GT t1= expr t2= expr ) | ^( PLUS t1= expr t2= expr ) | ^( MINUS t1= expr t2= expr ) | ^( TIMES t1= expr t2= expr ) | ^( DIV t1= expr t2= expr ) | ^( NOT t= expr ) | NOACTUAL );
	public final Type expr() throws RecognitionException {
		Type type = null;


		CommonTree ID20=null;
		CommonTree ID21=null;
		CommonTree FUNCCALL22=null;
		CommonTree EQ23=null;
		CommonTree LT24=null;
		CommonTree GT25=null;
		CommonTree PLUS26=null;
		CommonTree MINUS27=null;
		CommonTree TIMES28=null;
		CommonTree DIV29=null;
		CommonTree NOT30=null;
		Type t =null;
		Type t1 =null;
		Type t2 =null;

		try {
			// FunChecker.g:278:2: ( FALSE | TRUE | NUM | ID | ^( FUNCCALL ID t= expr ) | ^( EQ t1= expr t2= expr ) | ^( LT t1= expr t2= expr ) | ^( GT t1= expr t2= expr ) | ^( PLUS t1= expr t2= expr ) | ^( MINUS t1= expr t2= expr ) | ^( TIMES t1= expr t2= expr ) | ^( DIV t1= expr t2= expr ) | ^( NOT t= expr ) | NOACTUAL )
			int alt10=14;
			switch ( input.LA(1) ) {
			case FALSE:
				{
				alt10=1;
				}
				break;
			case TRUE:
				{
				alt10=2;
				}
				break;
			case NUM:
				{
				alt10=3;
				}
				break;
			case ID:
				{
				alt10=4;
				}
				break;
			case FUNCCALL:
				{
				alt10=5;
				}
				break;
			case EQ:
				{
				alt10=6;
				}
				break;
			case LT:
				{
				alt10=7;
				}
				break;
			case GT:
				{
				alt10=8;
				}
				break;
			case PLUS:
				{
				alt10=9;
				}
				break;
			case MINUS:
				{
				alt10=10;
				}
				break;
			case TIMES:
				{
				alt10=11;
				}
				break;
			case DIV:
				{
				alt10=12;
				}
				break;
			case NOT:
				{
				alt10=13;
				}
				break;
			case NOACTUAL:
				{
				alt10=14;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}
			switch (alt10) {
				case 1 :
					// FunChecker.g:278:4: FALSE
					{
					match(input,FALSE,FOLLOW_FALSE_in_expr517); 
					 type = Type.BOOL; 
					}
					break;
				case 2 :
					// FunChecker.g:280:4: TRUE
					{
					match(input,TRUE,FOLLOW_TRUE_in_expr528); 
					 type = Type.BOOL; 
					}
					break;
				case 3 :
					// FunChecker.g:282:4: NUM
					{
					match(input,NUM,FOLLOW_NUM_in_expr539); 
					 type = Type.INT; 
					}
					break;
				case 4 :
					// FunChecker.g:284:4: ID
					{
					ID20=(CommonTree)match(input,ID,FOLLOW_ID_in_expr550); 
					 type = retrieve((ID20!=null?ID20.getText():null), ID20);
									
					}
					break;
				case 5 :
					// FunChecker.g:287:4: ^( FUNCCALL ID t= expr )
					{
					FUNCCALL22=(CommonTree)match(input,FUNCCALL,FOLLOW_FUNCCALL_in_expr562); 
					match(input, Token.DOWN, null); 
					ID21=(CommonTree)match(input,ID,FOLLOW_ID_in_expr564); 
					pushFollow(FOLLOW_expr_in_expr568);
					t=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 Type result = checkCall(
									    (ID21!=null?ID21.getText():null), t, FUNCCALL22);
									  if (result.equiv(Type.VOID))
									    reportError(
										 "procedure should be non-void",
									      FUNCCALL22);
									  type = result;
									
					}
					break;
				case 6 :
					// FunChecker.g:296:4: ^( EQ t1= expr t2= expr )
					{
					EQ23=(CommonTree)match(input,EQ,FOLLOW_EQ_in_expr581); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr585);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr589);
					t2=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 type = checkBinary(
									    COMPTYPE, t1, t2, EQ23); 
					}
					break;
				case 7 :
					// FunChecker.g:299:4: ^( LT t1= expr t2= expr )
					{
					LT24=(CommonTree)match(input,LT,FOLLOW_LT_in_expr602); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr606);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr610);
					t2=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 type = checkBinary(
									    COMPTYPE, t1, t2, LT24); 
					}
					break;
				case 8 :
					// FunChecker.g:302:4: ^( GT t1= expr t2= expr )
					{
					GT25=(CommonTree)match(input,GT,FOLLOW_GT_in_expr623); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr627);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr631);
					t2=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 type = checkBinary(
									    COMPTYPE, t1, t2, GT25); 
					}
					break;
				case 9 :
					// FunChecker.g:305:4: ^( PLUS t1= expr t2= expr )
					{
					PLUS26=(CommonTree)match(input,PLUS,FOLLOW_PLUS_in_expr644); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr648);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr652);
					t2=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 type = checkBinary(
									    ARITHTYPE, t1, t2, PLUS26); 
					}
					break;
				case 10 :
					// FunChecker.g:308:4: ^( MINUS t1= expr t2= expr )
					{
					MINUS27=(CommonTree)match(input,MINUS,FOLLOW_MINUS_in_expr665); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr669);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr673);
					t2=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 type = checkBinary(
									    ARITHTYPE, t1, t2, MINUS27); 
					}
					break;
				case 11 :
					// FunChecker.g:311:4: ^( TIMES t1= expr t2= expr )
					{
					TIMES28=(CommonTree)match(input,TIMES,FOLLOW_TIMES_in_expr686); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr690);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr694);
					t2=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 type = checkBinary(
									    ARITHTYPE, t1, t2, TIMES28); 
					}
					break;
				case 12 :
					// FunChecker.g:314:4: ^( DIV t1= expr t2= expr )
					{
					DIV29=(CommonTree)match(input,DIV,FOLLOW_DIV_in_expr707); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr711);
					t1=expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr715);
					t2=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 type = checkBinary(
									     ARITHTYPE, t1, t2, DIV29); 
					}
					break;
				case 13 :
					// FunChecker.g:317:4: ^( NOT t= expr )
					{
					NOT30=(CommonTree)match(input,NOT,FOLLOW_NOT_in_expr728); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr732);
					t=expr();
					state._fsp--;

					match(input, Token.UP, null); 

					 type = checkUnary(NOTTYPE, t, NOT30); 
					}
					break;
				case 14 :
					// FunChecker.g:319:4: NOACTUAL
					{
					match(input,NOACTUAL,FOLLOW_NOACTUAL_in_expr744); 
					 type = Type.VOID; 
					}
					break;

			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return type;
	}
	// $ANTLR end "expr"

	// Delegated rules



	public static final BitSet FOLLOW_PROG_in_program60 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_var_decl_in_program72 = new BitSet(new long[]{0x0000400400040000L});
	public static final BitSet FOLLOW_proc_decl_in_program79 = new BitSet(new long[]{0x0000000400040008L});
	public static final BitSet FOLLOW_PROC_in_proc_decl106 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_proc_decl112 = new BitSet(new long[]{0x0000000040020000L});
	public static final BitSet FOLLOW_formal_in_proc_decl126 = new BitSet(new long[]{0x0000C10800C10410L});
	public static final BitSet FOLLOW_var_decl_in_proc_decl138 = new BitSet(new long[]{0x0000C10800C10410L});
	public static final BitSet FOLLOW_com_in_proc_decl145 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_FUNC_in_proc_decl162 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_type_in_proc_decl170 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_proc_decl176 = new BitSet(new long[]{0x0000000040020000L});
	public static final BitSet FOLLOW_formal_in_proc_decl190 = new BitSet(new long[]{0x0000C10800C10410L});
	public static final BitSet FOLLOW_var_decl_in_proc_decl202 = new BitSet(new long[]{0x0000C10800C10410L});
	public static final BitSet FOLLOW_com_in_proc_decl209 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_proc_decl217 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_FORMAL_in_formal246 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_type_in_formal250 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_formal252 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NOFORMAL_in_formal264 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VAR_in_var_decl282 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_type_in_var_decl286 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_var_decl288 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_var_decl292 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_BOOL_in_type317 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_type325 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ASSN_in_com343 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_com345 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_com349 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_PROCCALL_in_com362 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_com364 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_com368 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_IF_in_com381 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_com385 = new BitSet(new long[]{0x0000810800C10410L});
	public static final BitSet FOLLOW_com_in_com387 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_IFELSE_in_com400 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_com404 = new BitSet(new long[]{0x0000810800C10410L});
	public static final BitSet FOLLOW_com_in_com406 = new BitSet(new long[]{0x0000810800C10410L});
	public static final BitSet FOLLOW_com_in_com408 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_WHILE_in_com421 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_com425 = new BitSet(new long[]{0x0000810800C10410L});
	public static final BitSet FOLLOW_com_in_com427 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_FOR_in_com442 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_com444 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_com448 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_com452 = new BitSet(new long[]{0x0000810800C10410L});
	public static final BitSet FOLLOW_com_in_com454 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_DO_in_com470 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_com474 = new BitSet(new long[]{0x0000810800C10410L});
	public static final BitSet FOLLOW_com_in_com476 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_SEQ_in_com492 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_com_in_com494 = new BitSet(new long[]{0x0000810800C10418L});
	public static final BitSet FOLLOW_FALSE_in_expr517 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRUE_in_expr528 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NUM_in_expr539 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_expr550 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNCCALL_in_expr562 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_expr564 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr568 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_EQ_in_expr581 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr585 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr589 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_LT_in_expr602 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr606 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr610 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_GT_in_expr623 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr627 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr631 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_PLUS_in_expr644 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr648 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr652 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_MINUS_in_expr665 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr669 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr673 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_TIMES_in_expr686 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr690 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr694 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_DIV_in_expr707 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr711 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr715 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NOT_in_expr728 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr732 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NOACTUAL_in_expr744 = new BitSet(new long[]{0x0000000000000002L});
}
