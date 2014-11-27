// $ANTLR 3.5.2 FunEncoder.g 2014-11-27 15:38:21

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class FunEncoder extends TreeParser {
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


	public FunEncoder(TreeNodeStream input) {
		this(input, new RecognizerSharedState());
	}
	public FunEncoder(TreeNodeStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return FunEncoder.tokenNames; }
	@Override public String getGrammarFileName() { return "FunEncoder.g"; }



		private SVM obj = new SVM();

		private int globalvaraddr = 0;
		private int localvaraddr = 0;
		private int currentLocale = Address.GLOBAL;

		private SymbolTable<Address> addrTable =
		   new SymbolTable<Address>();

		private void predefine () {
		// Add predefined procedures to the address table.
			addrTable.put("read",
			   new Address(SVM.READOFFSET, Address.CODE));
			addrTable.put("write",
			   new Address(SVM.WRITEOFFSET, Address.CODE));
		}




	// $ANTLR start "program"
	// FunEncoder.g:47:1: program returns [SVM objectprogram] : ^( PROG ( var_decl )* ( proc_decl )+ ) ;
	public final SVM program() throws RecognitionException {
		SVM objectprogram = null;


		try {
			// FunEncoder.g:48:2: ( ^( PROG ( var_decl )* ( proc_decl )+ ) )
			// FunEncoder.g:48:4: ^( PROG ( var_decl )* ( proc_decl )+ )
			{
			match(input,PROG,FOLLOW_PROG_in_program65); 
			 predefine();
							
			match(input, Token.DOWN, null); 
			// FunEncoder.g:51:5: ( var_decl )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==VAR) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// FunEncoder.g:51:5: var_decl
					{
					pushFollow(FOLLOW_var_decl_in_program77);
					var_decl();
					state._fsp--;

					}
					break;

				default :
					break loop1;
				}
			}

			 int calladdr =
							    obj.currentOffset();
							  obj.emit12(SVM.CALL, 0);
							  obj.emit1(SVM.HALT);
							
			// FunEncoder.g:57:5: ( proc_decl )+
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
					// FunEncoder.g:57:5: proc_decl
					{
					pushFollow(FOLLOW_proc_decl_in_program90);
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

			 int mainaddr =
							    addrTable.get("main").offset;
							  obj.patch12(calladdr, mainaddr);
							  objectprogram = obj;
							
			match(input, Token.UP, null); 

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
		return objectprogram;
	}
	// $ANTLR end "program"



	// $ANTLR start "proc_decl"
	// FunEncoder.g:69:1: proc_decl : ( ^( PROC ID formal ( var_decl )* com ) | ^( FUNC type ID formal ( var_decl )* com expr ) );
	public final void proc_decl() throws RecognitionException {
		CommonTree ID1=null;
		CommonTree ID2=null;

		try {
			// FunEncoder.g:70:2: ( ^( PROC ID formal ( var_decl )* com ) | ^( FUNC type ID formal ( var_decl )* com expr ) )
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
					// FunEncoder.g:70:4: ^( PROC ID formal ( var_decl )* com )
					{
					match(input,PROC,FOLLOW_PROC_in_proc_decl117); 
					match(input, Token.DOWN, null); 
					ID1=(CommonTree)match(input,ID,FOLLOW_ID_in_proc_decl123); 
					 String id = (ID1!=null?ID1.getText():null);
									  Address procaddr = new Address(
									    obj.currentOffset(), Address.CODE);
									  addrTable.put(id, procaddr);
									  addrTable.enterLocalScope();
									  currentLocale = Address.LOCAL;
									  localvaraddr = 2;
									  // ... allows 2 words for link data
									
					pushFollow(FOLLOW_formal_in_proc_decl135);
					formal();
					state._fsp--;

					// FunEncoder.g:82:5: ( var_decl )*
					loop3:
					while (true) {
						int alt3=2;
						int LA3_0 = input.LA(1);
						if ( (LA3_0==VAR) ) {
							alt3=1;
						}

						switch (alt3) {
						case 1 :
							// FunEncoder.g:82:5: var_decl
							{
							pushFollow(FOLLOW_var_decl_in_proc_decl141);
							var_decl();
							state._fsp--;

							}
							break;

						default :
							break loop3;
						}
					}

					pushFollow(FOLLOW_com_in_proc_decl148);
					com();
					state._fsp--;

					 obj.emit11(SVM.RETURN, 0);
									  addrTable.exitLocalScope();
									  currentLocale = Address.GLOBAL;
									
					match(input, Token.UP, null); 

					}
					break;
				case 2 :
					// FunEncoder.g:89:4: ^( FUNC type ID formal ( var_decl )* com expr )
					{
					match(input,FUNC,FOLLOW_FUNC_in_proc_decl165); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_type_in_proc_decl171);
					type();
					state._fsp--;

					ID2=(CommonTree)match(input,ID,FOLLOW_ID_in_proc_decl177); 
					 String id = (ID2!=null?ID2.getText():null);
									  Address procaddr = new Address(
									    obj.currentOffset(), Address.CODE);
									  addrTable.put(id, procaddr);
									  addrTable.enterLocalScope();
									  currentLocale = Address.LOCAL;
									  localvaraddr = 2;
									  // ... allows 2 words for link data
									
					pushFollow(FOLLOW_formal_in_proc_decl189);
					formal();
					state._fsp--;

					// FunEncoder.g:102:5: ( var_decl )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( (LA4_0==VAR) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// FunEncoder.g:102:5: var_decl
							{
							pushFollow(FOLLOW_var_decl_in_proc_decl195);
							var_decl();
							state._fsp--;

							}
							break;

						default :
							break loop4;
						}
					}

					pushFollow(FOLLOW_com_in_proc_decl202);
					com();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_proc_decl208);
					expr();
					state._fsp--;

					 obj.emit11(SVM.RETURN, 1);
									  addrTable.exitLocalScope();
									  currentLocale = Address.GLOBAL;
									
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
	// FunEncoder.g:112:1: formal : ( ^( FORMAL type ID ) | NOFORMAL );
	public final void formal() throws RecognitionException {
		CommonTree ID3=null;

		try {
			// FunEncoder.g:113:2: ( ^( FORMAL type ID ) | NOFORMAL )
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
					// FunEncoder.g:113:4: ^( FORMAL type ID )
					{
					match(input,FORMAL,FOLLOW_FORMAL_in_formal231); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_type_in_formal237);
					type();
					state._fsp--;

					ID3=(CommonTree)match(input,ID,FOLLOW_ID_in_formal243); 
					 String id = (ID3!=null?ID3.getText():null);
									  addrTable.put(id, new Address(
									    localvaraddr++, Address.LOCAL));
									  obj.emit11(SVM.COPYARG, 1); 
									
					match(input, Token.UP, null); 

					}
					break;
				case 2 :
					// FunEncoder.g:122:4: NOFORMAL
					{
					match(input,NOFORMAL,FOLLOW_NOFORMAL_in_formal259); 
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
	// $ANTLR end "formal"



	// $ANTLR start "var_decl"
	// FunEncoder.g:125:1: var_decl : ^( VAR type ID expr ) ;
	public final void var_decl() throws RecognitionException {
		CommonTree ID4=null;

		try {
			// FunEncoder.g:126:2: ( ^( VAR type ID expr ) )
			// FunEncoder.g:126:4: ^( VAR type ID expr )
			{
			match(input,VAR,FOLLOW_VAR_in_var_decl271); 
			match(input, Token.DOWN, null); 
			pushFollow(FOLLOW_type_in_var_decl277);
			type();
			state._fsp--;

			ID4=(CommonTree)match(input,ID,FOLLOW_ID_in_var_decl283); 
			pushFollow(FOLLOW_expr_in_var_decl289);
			expr();
			state._fsp--;

			 String id = (ID4!=null?ID4.getText():null);
							  switch (currentLocale) {
							    case Address.LOCAL:
							      addrTable.put(id, new Address(
							        localvaraddr++, Address.LOCAL));
							      break;
							    case Address.GLOBAL:
							      addrTable.put(id, new Address(
							        globalvaraddr++, Address.GLOBAL));
							  }
							
			match(input, Token.UP, null); 

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
	// FunEncoder.g:144:1: type : ( BOOL | INT );
	public final void type() throws RecognitionException {
		try {
			// FunEncoder.g:145:2: ( BOOL | INT )
			// FunEncoder.g:
			{
			if ( input.LA(1)==BOOL||input.LA(1)==INT ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
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
	// $ANTLR end "type"



	// $ANTLR start "com"
	// FunEncoder.g:152:1: com : ( ^( ASSN ID expr ) | ^( PROCCALL ID expr ) | ^( IF expr com ) | ^( IFELSE expr com com ) | ^( WHILE expr com ) | ^( SEQ ( com )* ) );
	public final void com() throws RecognitionException {
		CommonTree ID5=null;
		CommonTree ID6=null;

		try {
			// FunEncoder.g:153:2: ( ^( ASSN ID expr ) | ^( PROCCALL ID expr ) | ^( IF expr com ) | ^( IFELSE expr com com ) | ^( WHILE expr com ) | ^( SEQ ( com )* ) )
			int alt8=6;
			switch ( input.LA(1) ) {
			case ASSN:
				{
				alt8=1;
				}
				break;
			case PROCCALL:
				{
				alt8=2;
				}
				break;
			case IF:
				{
				alt8=3;
				}
				break;
			case IFELSE:
				{
				alt8=4;
				}
				break;
			case WHILE:
				{
				alt8=5;
				}
				break;
			case SEQ:
				{
				alt8=6;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 8, 0, input);
				throw nvae;
			}
			switch (alt8) {
				case 1 :
					// FunEncoder.g:153:4: ^( ASSN ID expr )
					{
					match(input,ASSN,FOLLOW_ASSN_in_com331); 
					match(input, Token.DOWN, null); 
					ID5=(CommonTree)match(input,ID,FOLLOW_ID_in_com337); 
					pushFollow(FOLLOW_expr_in_com343);
					expr();
					state._fsp--;

					 String id = (ID5!=null?ID5.getText():null);
									  Address varaddr = addrTable.get(id);
									  switch (varaddr.locale) {
									    case Address.GLOBAL:
									      obj.emit12(SVM.STOREG,
									        varaddr.offset);
									      break;
									    case Address.LOCAL:
									      obj.emit12(SVM.STOREL,
									        varaddr.offset);
									  }
									
					match(input, Token.UP, null); 

					}
					break;
				case 2 :
					// FunEncoder.g:169:4: ^( PROCCALL ID expr )
					{
					match(input,PROCCALL,FOLLOW_PROCCALL_in_com360); 
					match(input, Token.DOWN, null); 
					ID6=(CommonTree)match(input,ID,FOLLOW_ID_in_com366); 
					pushFollow(FOLLOW_expr_in_com372);
					expr();
					state._fsp--;

					 String id = (ID6!=null?ID6.getText():null);
									  Address procaddr = addrTable.get(id);
									  // Assume procaddr.locale == CODE.
									  obj.emit12(SVM.CALL,
									    procaddr.offset);
									
					match(input, Token.UP, null); 

					}
					break;
				case 3 :
					// FunEncoder.g:179:4: ^( IF expr com )
					{
					match(input,IF,FOLLOW_IF_in_com389); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_com395);
					expr();
					state._fsp--;

					 int condaddr = obj.currentOffset();
									  obj.emit12(SVM.JUMPF, 0);
									
					pushFollow(FOLLOW_com_in_com407);
					com();
					state._fsp--;

					 int exitaddr = obj.currentOffset();
									  obj.patch12(condaddr, exitaddr);
									
					match(input, Token.UP, null); 

					}
					break;
				case 4 :
					// FunEncoder.g:189:4: ^( IFELSE expr com com )
					{
					match(input,IFELSE,FOLLOW_IFELSE_in_com424); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_com430);
					expr();
					state._fsp--;

					 int condaddr = obj.currentOffset();
									  obj.emit12(SVM.JUMPF, 0);
									
					pushFollow(FOLLOW_com_in_com442);
					com();
					state._fsp--;

					 int jumpaddr = obj.currentOffset();
									  obj.emit12(SVM.JUMP, 0);
									  int elseaddr = obj.currentOffset();
									  obj.patch12(condaddr, elseaddr);
									
					pushFollow(FOLLOW_com_in_com454);
					com();
					state._fsp--;

					 int exitaddr = obj.currentOffset();
									  obj.patch12(jumpaddr, exitaddr);
									
					match(input, Token.UP, null); 

					}
					break;
				case 5 :
					// FunEncoder.g:205:4: ^( WHILE expr com )
					{
					match(input,WHILE,FOLLOW_WHILE_in_com471); 
					 int startaddr = obj.currentOffset();
									
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_com484);
					expr();
					state._fsp--;

					 int condaddr = obj.currentOffset();
									  obj.emit12(SVM.JUMPF, 0);
									
					pushFollow(FOLLOW_com_in_com497);
					com();
					state._fsp--;

					 obj.emit12(SVM.JUMP, startaddr);
									  int exitaddr = obj.currentOffset();
									  obj.patch12(condaddr, exitaddr);
									
					match(input, Token.UP, null); 

					}
					break;
				case 6 :
					// FunEncoder.g:218:4: ^( SEQ ( com )* )
					{
					match(input,SEQ,FOLLOW_SEQ_in_com514); 
					if ( input.LA(1)==Token.DOWN ) {
						match(input, Token.DOWN, null); 
						// FunEncoder.g:219:5: ( com )*
						loop7:
						while (true) {
							int alt7=2;
							int LA7_0 = input.LA(1);
							if ( (LA7_0==ASSN||(LA7_0 >= IF && LA7_0 <= IFELSE)||LA7_0==PROCCALL||LA7_0==SEQ||LA7_0==WHILE) ) {
								alt7=1;
							}

							switch (alt7) {
							case 1 :
								// FunEncoder.g:219:5: com
								{
								pushFollow(FOLLOW_com_in_com520);
								com();
								state._fsp--;

								}
								break;

							default :
								break loop7;
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
	// FunEncoder.g:226:1: expr : ( FALSE | TRUE | NUM | ID | ^( FUNCCALL ID expr ) | ^( EQ expr expr ) | ^( LT expr expr ) | ^( GT expr expr ) | ^( PLUS expr expr ) | ^( MINUS expr expr ) | ^( TIMES expr expr ) | ^( DIV expr expr ) | ^( NOT expr ) | NOACTUAL );
	public final void expr() throws RecognitionException {
		CommonTree NUM7=null;
		CommonTree ID8=null;
		CommonTree ID9=null;

		try {
			// FunEncoder.g:227:2: ( FALSE | TRUE | NUM | ID | ^( FUNCCALL ID expr ) | ^( EQ expr expr ) | ^( LT expr expr ) | ^( GT expr expr ) | ^( PLUS expr expr ) | ^( MINUS expr expr ) | ^( TIMES expr expr ) | ^( DIV expr expr ) | ^( NOT expr ) | NOACTUAL )
			int alt9=14;
			switch ( input.LA(1) ) {
			case FALSE:
				{
				alt9=1;
				}
				break;
			case TRUE:
				{
				alt9=2;
				}
				break;
			case NUM:
				{
				alt9=3;
				}
				break;
			case ID:
				{
				alt9=4;
				}
				break;
			case FUNCCALL:
				{
				alt9=5;
				}
				break;
			case EQ:
				{
				alt9=6;
				}
				break;
			case LT:
				{
				alt9=7;
				}
				break;
			case GT:
				{
				alt9=8;
				}
				break;
			case PLUS:
				{
				alt9=9;
				}
				break;
			case MINUS:
				{
				alt9=10;
				}
				break;
			case TIMES:
				{
				alt9=11;
				}
				break;
			case DIV:
				{
				alt9=12;
				}
				break;
			case NOT:
				{
				alt9=13;
				}
				break;
			case NOACTUAL:
				{
				alt9=14;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}
			switch (alt9) {
				case 1 :
					// FunEncoder.g:227:4: FALSE
					{
					match(input,FALSE,FOLLOW_FALSE_in_expr540); 
					 obj.emit12(SVM.LOADC, 0); 
					}
					break;
				case 2 :
					// FunEncoder.g:229:4: TRUE
					{
					match(input,TRUE,FOLLOW_TRUE_in_expr551); 
					 obj.emit12(SVM.LOADC, 1); 
					}
					break;
				case 3 :
					// FunEncoder.g:231:4: NUM
					{
					NUM7=(CommonTree)match(input,NUM,FOLLOW_NUM_in_expr562); 
					 int value =
									    Integer.parseInt((NUM7!=null?NUM7.getText():null));
									  obj.emit12(SVM.LOADC, value);
									
					}
					break;
				case 4 :
					// FunEncoder.g:236:4: ID
					{
					ID8=(CommonTree)match(input,ID,FOLLOW_ID_in_expr573); 
					 String id = (ID8!=null?ID8.getText():null);
									  Address varaddr = addrTable.get(id);
									  switch (varaddr.locale) {
									    case Address.GLOBAL:
									      obj.emit12(SVM.LOADG,
									        varaddr.offset);
									      break;
									    case Address.LOCAL:
									      obj.emit12(SVM.LOADL,
									        varaddr.offset);
									  }
									
					}
					break;
				case 5 :
					// FunEncoder.g:249:4: ^( FUNCCALL ID expr )
					{
					match(input,FUNCCALL,FOLLOW_FUNCCALL_in_expr585); 
					match(input, Token.DOWN, null); 
					ID9=(CommonTree)match(input,ID,FOLLOW_ID_in_expr591); 
					pushFollow(FOLLOW_expr_in_expr597);
					expr();
					state._fsp--;

					 String id = (ID9!=null?ID9.getText():null);
									  Address funcaddr = addrTable.get(id);
									  // Assume that funcaddr.locale == CODE.
									  obj.emit12(SVM.CALL,
									    funcaddr.offset);
									
					match(input, Token.UP, null); 

					}
					break;
				case 6 :
					// FunEncoder.g:259:4: ^( EQ expr expr )
					{
					match(input,EQ,FOLLOW_EQ_in_expr614); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr620);
					expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr626);
					expr();
					state._fsp--;

					 obj.emit1(SVM.CMPEQ); 
					match(input, Token.UP, null); 

					}
					break;
				case 7 :
					// FunEncoder.g:264:4: ^( LT expr expr )
					{
					match(input,LT,FOLLOW_LT_in_expr643); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr649);
					expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr655);
					expr();
					state._fsp--;

					 obj.emit1(SVM.CMPLT); 
					match(input, Token.UP, null); 

					}
					break;
				case 8 :
					// FunEncoder.g:269:4: ^( GT expr expr )
					{
					match(input,GT,FOLLOW_GT_in_expr672); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr678);
					expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr684);
					expr();
					state._fsp--;

					 obj.emit1(SVM.CMPGT); 
					match(input, Token.UP, null); 

					}
					break;
				case 9 :
					// FunEncoder.g:274:4: ^( PLUS expr expr )
					{
					match(input,PLUS,FOLLOW_PLUS_in_expr701); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr707);
					expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr713);
					expr();
					state._fsp--;

					 obj.emit1(SVM.ADD); 
					match(input, Token.UP, null); 

					}
					break;
				case 10 :
					// FunEncoder.g:279:4: ^( MINUS expr expr )
					{
					match(input,MINUS,FOLLOW_MINUS_in_expr730); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr736);
					expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr742);
					expr();
					state._fsp--;

					 obj.emit1(SVM.SUB); 
					match(input, Token.UP, null); 

					}
					break;
				case 11 :
					// FunEncoder.g:284:4: ^( TIMES expr expr )
					{
					match(input,TIMES,FOLLOW_TIMES_in_expr759); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr765);
					expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr771);
					expr();
					state._fsp--;

					 obj.emit1(SVM.MUL); 
					match(input, Token.UP, null); 

					}
					break;
				case 12 :
					// FunEncoder.g:289:4: ^( DIV expr expr )
					{
					match(input,DIV,FOLLOW_DIV_in_expr788); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr794);
					expr();
					state._fsp--;

					pushFollow(FOLLOW_expr_in_expr800);
					expr();
					state._fsp--;

					 obj.emit1(SVM.DIV); 
					match(input, Token.UP, null); 

					}
					break;
				case 13 :
					// FunEncoder.g:294:4: ^( NOT expr )
					{
					match(input,NOT,FOLLOW_NOT_in_expr817); 
					match(input, Token.DOWN, null); 
					pushFollow(FOLLOW_expr_in_expr823);
					expr();
					state._fsp--;

					 obj.emit1(SVM.INV); 
					match(input, Token.UP, null); 

					}
					break;
				case 14 :
					// FunEncoder.g:298:4: NOACTUAL
					{
					match(input,NOACTUAL,FOLLOW_NOACTUAL_in_expr839); 
					 
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
	// $ANTLR end "expr"

	// Delegated rules



	public static final BitSet FOLLOW_PROG_in_program65 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_var_decl_in_program77 = new BitSet(new long[]{0x0000400400040000L});
	public static final BitSet FOLLOW_proc_decl_in_program90 = new BitSet(new long[]{0x0000000400040008L});
	public static final BitSet FOLLOW_PROC_in_proc_decl117 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_proc_decl123 = new BitSet(new long[]{0x0000000040020000L});
	public static final BitSet FOLLOW_formal_in_proc_decl135 = new BitSet(new long[]{0x0000C10800C00010L});
	public static final BitSet FOLLOW_var_decl_in_proc_decl141 = new BitSet(new long[]{0x0000C10800C00010L});
	public static final BitSet FOLLOW_com_in_proc_decl148 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_FUNC_in_proc_decl165 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_type_in_proc_decl171 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_proc_decl177 = new BitSet(new long[]{0x0000000040020000L});
	public static final BitSet FOLLOW_formal_in_proc_decl189 = new BitSet(new long[]{0x0000C10800C00010L});
	public static final BitSet FOLLOW_var_decl_in_proc_decl195 = new BitSet(new long[]{0x0000C10800C00010L});
	public static final BitSet FOLLOW_com_in_proc_decl202 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_proc_decl208 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_FORMAL_in_formal231 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_type_in_formal237 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_formal243 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NOFORMAL_in_formal259 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_VAR_in_var_decl271 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_type_in_var_decl277 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_var_decl283 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_var_decl289 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_ASSN_in_com331 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_com337 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_com343 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_PROCCALL_in_com360 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_com366 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_com372 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_IF_in_com389 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_com395 = new BitSet(new long[]{0x0000810800C00010L});
	public static final BitSet FOLLOW_com_in_com407 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_IFELSE_in_com424 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_com430 = new BitSet(new long[]{0x0000810800C00010L});
	public static final BitSet FOLLOW_com_in_com442 = new BitSet(new long[]{0x0000810800C00010L});
	public static final BitSet FOLLOW_com_in_com454 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_WHILE_in_com471 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_com484 = new BitSet(new long[]{0x0000810800C00010L});
	public static final BitSet FOLLOW_com_in_com497 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_SEQ_in_com514 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_com_in_com520 = new BitSet(new long[]{0x0000810800C00018L});
	public static final BitSet FOLLOW_FALSE_in_expr540 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRUE_in_expr551 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NUM_in_expr562 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_expr573 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNCCALL_in_expr585 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_ID_in_expr591 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr597 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_EQ_in_expr614 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr620 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr626 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_LT_in_expr643 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr649 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr655 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_GT_in_expr672 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr678 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr684 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_PLUS_in_expr701 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr707 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr713 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_MINUS_in_expr730 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr736 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr742 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_TIMES_in_expr759 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr765 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr771 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_DIV_in_expr788 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr794 = new BitSet(new long[]{0x00001403B838C200L});
	public static final BitSet FOLLOW_expr_in_expr800 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NOT_in_expr817 = new BitSet(new long[]{0x0000000000000004L});
	public static final BitSet FOLLOW_expr_in_expr823 = new BitSet(new long[]{0x0000000000000008L});
	public static final BitSet FOLLOW_NOACTUAL_in_expr839 = new BitSet(new long[]{0x0000000000000002L});
}
