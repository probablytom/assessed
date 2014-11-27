// $ANTLR 3.5.2 Fun.g 2014-11-27 11:26:47

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class FunParser extends Parser {
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
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public FunParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public FunParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return FunParser.tokenNames; }
	@Override public String getGrammarFileName() { return "Fun.g"; }


	public static class program_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "program"
	// Fun.g:39:1: program : ( var_decl )* ( proc_decl )+ EOF -> ^( PROG ( var_decl )* ( proc_decl )+ ) ;
	public final FunParser.program_return program() throws RecognitionException {
		FunParser.program_return retval = new FunParser.program_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token EOF3=null;
		ParserRuleReturnScope var_decl1 =null;
		ParserRuleReturnScope proc_decl2 =null;

		CommonTree EOF3_tree=null;
		RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
		RewriteRuleSubtreeStream stream_proc_decl=new RewriteRuleSubtreeStream(adaptor,"rule proc_decl");
		RewriteRuleSubtreeStream stream_var_decl=new RewriteRuleSubtreeStream(adaptor,"rule var_decl");

		try {
			// Fun.g:40:2: ( ( var_decl )* ( proc_decl )+ EOF -> ^( PROG ( var_decl )* ( proc_decl )+ ) )
			// Fun.g:40:4: ( var_decl )* ( proc_decl )+ EOF
			{
			// Fun.g:40:4: ( var_decl )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0==BOOL||LA1_0==INT) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// Fun.g:40:4: var_decl
					{
					pushFollow(FOLLOW_var_decl_in_program116);
					var_decl1=var_decl();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_var_decl.add(var_decl1.getTree());
					}
					break;

				default :
					break loop1;
				}
			}

			// Fun.g:40:14: ( proc_decl )+
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
					// Fun.g:40:14: proc_decl
					{
					pushFollow(FOLLOW_proc_decl_in_program119);
					proc_decl2=proc_decl();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_proc_decl.add(proc_decl2.getTree());
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					if (state.backtracking>0) {state.failed=true; return retval;}
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			EOF3=(Token)match(input,EOF,FOLLOW_EOF_in_program122); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_EOF.add(EOF3);

			// AST REWRITE
			// elements: proc_decl, var_decl
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 40:31: -> ^( PROG ( var_decl )* ( proc_decl )+ )
			{
				// Fun.g:40:34: ^( PROG ( var_decl )* ( proc_decl )+ )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PROG, "PROG"), root_1);
				// Fun.g:41:35: ( var_decl )*
				while ( stream_var_decl.hasNext() ) {
					adaptor.addChild(root_1, stream_var_decl.nextTree());
				}
				stream_var_decl.reset();

				if ( !(stream_proc_decl.hasNext()) ) {
					throw new RewriteEarlyExitException();
				}
				while ( stream_proc_decl.hasNext() ) {
					adaptor.addChild(root_1, stream_proc_decl.nextTree());
				}
				stream_proc_decl.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "program"


	public static class proc_decl_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "proc_decl"
	// Fun.g:48:1: proc_decl : ( PROC ID LPAR formal RPAR COLON ( var_decl )* seq_com DOT -> ^( PROC ID formal ( var_decl )* seq_com ) | FUNC type ID LPAR formal RPAR COLON ( var_decl )* seq_com RETURN expr DOT -> ^( FUNC type ID formal ( var_decl )* seq_com expr ) );
	public final FunParser.proc_decl_return proc_decl() throws RecognitionException {
		FunParser.proc_decl_return retval = new FunParser.proc_decl_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token PROC4=null;
		Token ID5=null;
		Token LPAR6=null;
		Token RPAR8=null;
		Token COLON9=null;
		Token DOT12=null;
		Token FUNC13=null;
		Token ID15=null;
		Token LPAR16=null;
		Token RPAR18=null;
		Token COLON19=null;
		Token RETURN22=null;
		Token DOT24=null;
		ParserRuleReturnScope formal7 =null;
		ParserRuleReturnScope var_decl10 =null;
		ParserRuleReturnScope seq_com11 =null;
		ParserRuleReturnScope type14 =null;
		ParserRuleReturnScope formal17 =null;
		ParserRuleReturnScope var_decl20 =null;
		ParserRuleReturnScope seq_com21 =null;
		ParserRuleReturnScope expr23 =null;

		CommonTree PROC4_tree=null;
		CommonTree ID5_tree=null;
		CommonTree LPAR6_tree=null;
		CommonTree RPAR8_tree=null;
		CommonTree COLON9_tree=null;
		CommonTree DOT12_tree=null;
		CommonTree FUNC13_tree=null;
		CommonTree ID15_tree=null;
		CommonTree LPAR16_tree=null;
		CommonTree RPAR18_tree=null;
		CommonTree COLON19_tree=null;
		CommonTree RETURN22_tree=null;
		CommonTree DOT24_tree=null;
		RewriteRuleTokenStream stream_RETURN=new RewriteRuleTokenStream(adaptor,"token RETURN");
		RewriteRuleTokenStream stream_PROC=new RewriteRuleTokenStream(adaptor,"token PROC");
		RewriteRuleTokenStream stream_FUNC=new RewriteRuleTokenStream(adaptor,"token FUNC");
		RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
		RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");
		RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_formal=new RewriteRuleSubtreeStream(adaptor,"rule formal");
		RewriteRuleSubtreeStream stream_seq_com=new RewriteRuleSubtreeStream(adaptor,"rule seq_com");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
		RewriteRuleSubtreeStream stream_var_decl=new RewriteRuleSubtreeStream(adaptor,"rule var_decl");
		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

		try {
			// Fun.g:49:2: ( PROC ID LPAR formal RPAR COLON ( var_decl )* seq_com DOT -> ^( PROC ID formal ( var_decl )* seq_com ) | FUNC type ID LPAR formal RPAR COLON ( var_decl )* seq_com RETURN expr DOT -> ^( FUNC type ID formal ( var_decl )* seq_com expr ) )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==PROC) ) {
				alt5=1;
			}
			else if ( (LA5_0==FUNC) ) {
				alt5=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 5, 0, input);
				throw nvae;
			}

			switch (alt5) {
				case 1 :
					// Fun.g:49:4: PROC ID LPAR formal RPAR COLON ( var_decl )* seq_com DOT
					{
					PROC4=(Token)match(input,PROC,FOLLOW_PROC_in_proc_decl219); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_PROC.add(PROC4);

					ID5=(Token)match(input,ID,FOLLOW_ID_in_proc_decl221); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID5);

					LPAR6=(Token)match(input,LPAR,FOLLOW_LPAR_in_proc_decl227); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAR.add(LPAR6);

					pushFollow(FOLLOW_formal_in_proc_decl229);
					formal7=formal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_formal.add(formal7.getTree());
					RPAR8=(Token)match(input,RPAR,FOLLOW_RPAR_in_proc_decl231); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAR.add(RPAR8);

					COLON9=(Token)match(input,COLON,FOLLOW_COLON_in_proc_decl233); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COLON.add(COLON9);

					// Fun.g:51:5: ( var_decl )*
					loop3:
					while (true) {
						int alt3=2;
						int LA3_0 = input.LA(1);
						if ( (LA3_0==BOOL||LA3_0==INT) ) {
							alt3=1;
						}

						switch (alt3) {
						case 1 :
							// Fun.g:51:5: var_decl
							{
							pushFollow(FOLLOW_var_decl_in_proc_decl239);
							var_decl10=var_decl();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_var_decl.add(var_decl10.getTree());
							}
							break;

						default :
							break loop3;
						}
					}

					pushFollow(FOLLOW_seq_com_in_proc_decl242);
					seq_com11=seq_com();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_seq_com.add(seq_com11.getTree());
					DOT12=(Token)match(input,DOT,FOLLOW_DOT_in_proc_decl244); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DOT.add(DOT12);

					// AST REWRITE
					// elements: formal, ID, var_decl, PROC, seq_com
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 51:30: -> ^( PROC ID formal ( var_decl )* seq_com )
					{
						// Fun.g:51:33: ^( PROC ID formal ( var_decl )* seq_com )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_PROC.nextNode(), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_1, stream_formal.nextTree());
						// Fun.g:53:35: ( var_decl )*
						while ( stream_var_decl.hasNext() ) {
							adaptor.addChild(root_1, stream_var_decl.nextTree());
						}
						stream_var_decl.reset();

						adaptor.addChild(root_1, stream_seq_com.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// Fun.g:54:4: FUNC type ID LPAR formal RPAR COLON ( var_decl )* seq_com RETURN expr DOT
					{
					FUNC13=(Token)match(input,FUNC,FOLLOW_FUNC_in_proc_decl336); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_FUNC.add(FUNC13);

					pushFollow(FOLLOW_type_in_proc_decl338);
					type14=type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_type.add(type14.getTree());
					ID15=(Token)match(input,ID,FOLLOW_ID_in_proc_decl340); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID15);

					LPAR16=(Token)match(input,LPAR,FOLLOW_LPAR_in_proc_decl346); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAR.add(LPAR16);

					pushFollow(FOLLOW_formal_in_proc_decl348);
					formal17=formal();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_formal.add(formal17.getTree());
					RPAR18=(Token)match(input,RPAR,FOLLOW_RPAR_in_proc_decl350); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAR.add(RPAR18);

					COLON19=(Token)match(input,COLON,FOLLOW_COLON_in_proc_decl352); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COLON.add(COLON19);

					// Fun.g:56:5: ( var_decl )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( (LA4_0==BOOL||LA4_0==INT) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// Fun.g:56:5: var_decl
							{
							pushFollow(FOLLOW_var_decl_in_proc_decl358);
							var_decl20=var_decl();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_var_decl.add(var_decl20.getTree());
							}
							break;

						default :
							break loop4;
						}
					}

					pushFollow(FOLLOW_seq_com_in_proc_decl361);
					seq_com21=seq_com();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_seq_com.add(seq_com21.getTree());
					RETURN22=(Token)match(input,RETURN,FOLLOW_RETURN_in_proc_decl367); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RETURN.add(RETURN22);

					pushFollow(FOLLOW_expr_in_proc_decl369);
					expr23=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(expr23.getTree());
					DOT24=(Token)match(input,DOT,FOLLOW_DOT_in_proc_decl371); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DOT.add(DOT24);

					// AST REWRITE
					// elements: expr, formal, var_decl, type, ID, seq_com, FUNC
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 57:30: -> ^( FUNC type ID formal ( var_decl )* seq_com expr )
					{
						// Fun.g:57:33: ^( FUNC type ID formal ( var_decl )* seq_com expr )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_FUNC.nextNode(), root_1);
						adaptor.addChild(root_1, stream_type.nextTree());
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_1, stream_formal.nextTree());
						// Fun.g:59:35: ( var_decl )*
						while ( stream_var_decl.hasNext() ) {
							adaptor.addChild(root_1, stream_var_decl.nextTree());
						}
						stream_var_decl.reset();

						adaptor.addChild(root_1, stream_seq_com.nextTree());
						adaptor.addChild(root_1, stream_expr.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "proc_decl"


	public static class formal_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "formal"
	// Fun.g:63:1: formal : ( type ID -> ^( FORMAL type ID ) | -> NOFORMAL );
	public final FunParser.formal_return formal() throws RecognitionException {
		FunParser.formal_return retval = new FunParser.formal_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token ID26=null;
		ParserRuleReturnScope type25 =null;

		CommonTree ID26_tree=null;
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

		try {
			// Fun.g:64:2: ( type ID -> ^( FORMAL type ID ) | -> NOFORMAL )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==BOOL||LA6_0==INT) ) {
				alt6=1;
			}
			else if ( (LA6_0==RPAR) ) {
				alt6=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// Fun.g:64:4: type ID
					{
					pushFollow(FOLLOW_type_in_formal513);
					type25=type();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_type.add(type25.getTree());
					ID26=(Token)match(input,ID,FOLLOW_ID_in_formal515); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID26);

					// AST REWRITE
					// elements: ID, type
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 64:31: -> ^( FORMAL type ID )
					{
						// Fun.g:64:34: ^( FORMAL type ID )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FORMAL, "FORMAL"), root_1);
						adaptor.addChild(root_1, stream_type.nextTree());
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// Fun.g:65:31: 
					{
					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 65:31: -> NOFORMAL
					{
						adaptor.addChild(root_0, (CommonTree)adaptor.create(NOFORMAL, "NOFORMAL"));
					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "formal"


	public static class var_decl_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "var_decl"
	// Fun.g:68:1: var_decl : type ID ASSN expr -> ^( VAR type ID expr ) ;
	public final FunParser.var_decl_return var_decl() throws RecognitionException {
		FunParser.var_decl_return retval = new FunParser.var_decl_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token ID28=null;
		Token ASSN29=null;
		ParserRuleReturnScope type27 =null;
		ParserRuleReturnScope expr30 =null;

		CommonTree ID28_tree=null;
		CommonTree ASSN29_tree=null;
		RewriteRuleTokenStream stream_ASSN=new RewriteRuleTokenStream(adaptor,"token ASSN");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
		RewriteRuleSubtreeStream stream_type=new RewriteRuleSubtreeStream(adaptor,"rule type");

		try {
			// Fun.g:69:2: ( type ID ASSN expr -> ^( VAR type ID expr ) )
			// Fun.g:69:4: type ID ASSN expr
			{
			pushFollow(FOLLOW_type_in_var_decl589);
			type27=type();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_type.add(type27.getTree());
			ID28=(Token)match(input,ID,FOLLOW_ID_in_var_decl591); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ID.add(ID28);

			ASSN29=(Token)match(input,ASSN,FOLLOW_ASSN_in_var_decl593); if (state.failed) return retval; 
			if ( state.backtracking==0 ) stream_ASSN.add(ASSN29);

			pushFollow(FOLLOW_expr_in_var_decl595);
			expr30=expr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) stream_expr.add(expr30.getTree());
			// AST REWRITE
			// elements: type, ID, expr
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 69:31: -> ^( VAR type ID expr )
			{
				// Fun.g:69:34: ^( VAR type ID expr )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(VAR, "VAR"), root_1);
				adaptor.addChild(root_1, stream_type.nextTree());
				adaptor.addChild(root_1, stream_ID.nextNode());
				adaptor.addChild(root_1, stream_expr.nextTree());
				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "var_decl"


	public static class type_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "type"
	// Fun.g:72:1: type : ( BOOL -> BOOL | INT -> INT );
	public final FunParser.type_return type() throws RecognitionException {
		FunParser.type_return retval = new FunParser.type_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token BOOL31=null;
		Token INT32=null;

		CommonTree BOOL31_tree=null;
		CommonTree INT32_tree=null;
		RewriteRuleTokenStream stream_BOOL=new RewriteRuleTokenStream(adaptor,"token BOOL");
		RewriteRuleTokenStream stream_INT=new RewriteRuleTokenStream(adaptor,"token INT");

		try {
			// Fun.g:73:2: ( BOOL -> BOOL | INT -> INT )
			int alt7=2;
			int LA7_0 = input.LA(1);
			if ( (LA7_0==BOOL) ) {
				alt7=1;
			}
			else if ( (LA7_0==INT) ) {
				alt7=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 7, 0, input);
				throw nvae;
			}

			switch (alt7) {
				case 1 :
					// Fun.g:73:4: BOOL
					{
					BOOL31=(Token)match(input,BOOL,FOLLOW_BOOL_in_type627); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_BOOL.add(BOOL31);

					// AST REWRITE
					// elements: BOOL
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 73:31: -> BOOL
					{
						adaptor.addChild(root_0, stream_BOOL.nextNode());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// Fun.g:74:4: INT
					{
					INT32=(Token)match(input,INT,FOLLOW_INT_in_type658); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_INT.add(INT32);

					// AST REWRITE
					// elements: INT
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 74:31: -> INT
					{
						adaptor.addChild(root_0, stream_INT.nextNode());
					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "type"


	public static class com_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "com"
	// Fun.g:80:1: com : ( ID ASSN expr -> ^( ASSN ID expr ) | ID LPAR actual RPAR -> ^( PROCCALL ID actual ) | IF expr COLON c1= seq_com ( DOT -> ^( IF expr $c1) | ELSE COLON c2= seq_com DOT -> ^( IFELSE expr $c1 $c2) ) | WHILE expr COLON seq_com DOT -> ^( WHILE expr seq_com ) | FOR ID ASSN e1= expr TO e2= expr COLON seq_com DOT -> ^( FOR ID $e1 $e2 seq_com ) | REPEAT seq_com UNTIL expr DOT -> ^( REPEAT seq_com expr ) | DO seq_com WHILE expr DOT -> ^( DO seq_com expr ) );
	public final FunParser.com_return com() throws RecognitionException {
		FunParser.com_return retval = new FunParser.com_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token ID33=null;
		Token ASSN34=null;
		Token ID36=null;
		Token LPAR37=null;
		Token RPAR39=null;
		Token IF40=null;
		Token COLON42=null;
		Token DOT43=null;
		Token ELSE44=null;
		Token COLON45=null;
		Token DOT46=null;
		Token WHILE47=null;
		Token COLON49=null;
		Token DOT51=null;
		Token FOR52=null;
		Token ID53=null;
		Token ASSN54=null;
		Token TO55=null;
		Token COLON56=null;
		Token DOT58=null;
		Token REPEAT59=null;
		Token UNTIL61=null;
		Token DOT63=null;
		Token DO64=null;
		Token WHILE66=null;
		Token DOT68=null;
		ParserRuleReturnScope c1 =null;
		ParserRuleReturnScope c2 =null;
		ParserRuleReturnScope e1 =null;
		ParserRuleReturnScope e2 =null;
		ParserRuleReturnScope expr35 =null;
		ParserRuleReturnScope actual38 =null;
		ParserRuleReturnScope expr41 =null;
		ParserRuleReturnScope expr48 =null;
		ParserRuleReturnScope seq_com50 =null;
		ParserRuleReturnScope seq_com57 =null;
		ParserRuleReturnScope seq_com60 =null;
		ParserRuleReturnScope expr62 =null;
		ParserRuleReturnScope seq_com65 =null;
		ParserRuleReturnScope expr67 =null;

		CommonTree ID33_tree=null;
		CommonTree ASSN34_tree=null;
		CommonTree ID36_tree=null;
		CommonTree LPAR37_tree=null;
		CommonTree RPAR39_tree=null;
		CommonTree IF40_tree=null;
		CommonTree COLON42_tree=null;
		CommonTree DOT43_tree=null;
		CommonTree ELSE44_tree=null;
		CommonTree COLON45_tree=null;
		CommonTree DOT46_tree=null;
		CommonTree WHILE47_tree=null;
		CommonTree COLON49_tree=null;
		CommonTree DOT51_tree=null;
		CommonTree FOR52_tree=null;
		CommonTree ID53_tree=null;
		CommonTree ASSN54_tree=null;
		CommonTree TO55_tree=null;
		CommonTree COLON56_tree=null;
		CommonTree DOT58_tree=null;
		CommonTree REPEAT59_tree=null;
		CommonTree UNTIL61_tree=null;
		CommonTree DOT63_tree=null;
		CommonTree DO64_tree=null;
		CommonTree WHILE66_tree=null;
		CommonTree DOT68_tree=null;
		RewriteRuleTokenStream stream_REPEAT=new RewriteRuleTokenStream(adaptor,"token REPEAT");
		RewriteRuleTokenStream stream_DOT=new RewriteRuleTokenStream(adaptor,"token DOT");
		RewriteRuleTokenStream stream_FOR=new RewriteRuleTokenStream(adaptor,"token FOR");
		RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
		RewriteRuleTokenStream stream_COLON=new RewriteRuleTokenStream(adaptor,"token COLON");
		RewriteRuleTokenStream stream_DO=new RewriteRuleTokenStream(adaptor,"token DO");
		RewriteRuleTokenStream stream_UNTIL=new RewriteRuleTokenStream(adaptor,"token UNTIL");
		RewriteRuleTokenStream stream_ASSN=new RewriteRuleTokenStream(adaptor,"token ASSN");
		RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
		RewriteRuleTokenStream stream_ELSE=new RewriteRuleTokenStream(adaptor,"token ELSE");
		RewriteRuleTokenStream stream_WHILE=new RewriteRuleTokenStream(adaptor,"token WHILE");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
		RewriteRuleTokenStream stream_IF=new RewriteRuleTokenStream(adaptor,"token IF");
		RewriteRuleSubtreeStream stream_actual=new RewriteRuleSubtreeStream(adaptor,"rule actual");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");
		RewriteRuleSubtreeStream stream_seq_com=new RewriteRuleSubtreeStream(adaptor,"rule seq_com");

		try {
			// Fun.g:81:2: ( ID ASSN expr -> ^( ASSN ID expr ) | ID LPAR actual RPAR -> ^( PROCCALL ID actual ) | IF expr COLON c1= seq_com ( DOT -> ^( IF expr $c1) | ELSE COLON c2= seq_com DOT -> ^( IFELSE expr $c1 $c2) ) | WHILE expr COLON seq_com DOT -> ^( WHILE expr seq_com ) | FOR ID ASSN e1= expr TO e2= expr COLON seq_com DOT -> ^( FOR ID $e1 $e2 seq_com ) | REPEAT seq_com UNTIL expr DOT -> ^( REPEAT seq_com expr ) | DO seq_com WHILE expr DOT -> ^( DO seq_com expr ) )
			int alt9=7;
			switch ( input.LA(1) ) {
			case ID:
				{
				int LA9_1 = input.LA(2);
				if ( (LA9_1==ASSN) ) {
					alt9=1;
				}
				else if ( (LA9_1==LPAR) ) {
					alt9=2;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 9, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case IF:
				{
				alt9=3;
				}
				break;
			case WHILE:
				{
				alt9=4;
				}
				break;
			case FOR:
				{
				alt9=5;
				}
				break;
			case REPEAT:
				{
				alt9=6;
				}
				break;
			case DO:
				{
				alt9=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}
			switch (alt9) {
				case 1 :
					// Fun.g:81:4: ID ASSN expr
					{
					ID33=(Token)match(input,ID,FOLLOW_ID_in_com699); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID33);

					ASSN34=(Token)match(input,ASSN,FOLLOW_ASSN_in_com701); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ASSN.add(ASSN34);

					pushFollow(FOLLOW_expr_in_com703);
					expr35=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(expr35.getTree());
					// AST REWRITE
					// elements: ASSN, ID, expr
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 81:31: -> ^( ASSN ID expr )
					{
						// Fun.g:81:34: ^( ASSN ID expr )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_ASSN.nextNode(), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_1, stream_expr.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// Fun.g:82:4: ID LPAR actual RPAR
					{
					ID36=(Token)match(input,ID,FOLLOW_ID_in_com732); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID36);

					LPAR37=(Token)match(input,LPAR,FOLLOW_LPAR_in_com734); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAR.add(LPAR37);

					pushFollow(FOLLOW_actual_in_com736);
					actual38=actual();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_actual.add(actual38.getTree());
					RPAR39=(Token)match(input,RPAR,FOLLOW_RPAR_in_com738); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAR.add(RPAR39);

					// AST REWRITE
					// elements: ID, actual
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 82:31: -> ^( PROCCALL ID actual )
					{
						// Fun.g:82:34: ^( PROCCALL ID actual )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(PROCCALL, "PROCCALL"), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_1, stream_actual.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// Fun.g:84:4: IF expr COLON c1= seq_com ( DOT -> ^( IF expr $c1) | ELSE COLON c2= seq_com DOT -> ^( IFELSE expr $c1 $c2) )
					{
					IF40=(Token)match(input,IF,FOLLOW_IF_in_com772); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_IF.add(IF40);

					pushFollow(FOLLOW_expr_in_com774);
					expr41=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(expr41.getTree());
					COLON42=(Token)match(input,COLON,FOLLOW_COLON_in_com776); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COLON.add(COLON42);

					pushFollow(FOLLOW_seq_com_in_com780);
					c1=seq_com();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_seq_com.add(c1.getTree());
					// Fun.g:85:5: ( DOT -> ^( IF expr $c1) | ELSE COLON c2= seq_com DOT -> ^( IFELSE expr $c1 $c2) )
					int alt8=2;
					int LA8_0 = input.LA(1);
					if ( (LA8_0==DOT) ) {
						alt8=1;
					}
					else if ( (LA8_0==ELSE) ) {
						alt8=2;
					}

					else {
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 8, 0, input);
						throw nvae;
					}

					switch (alt8) {
						case 1 :
							// Fun.g:85:7: DOT
							{
							DOT43=(Token)match(input,DOT,FOLLOW_DOT_in_com788); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DOT.add(DOT43);

							// AST REWRITE
							// elements: expr, IF, c1
							// token labels: 
							// rule labels: c1, retval
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							if ( state.backtracking==0 ) {
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_c1=new RewriteRuleSubtreeStream(adaptor,"rule c1",c1!=null?c1.getTree():null);
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

							root_0 = (CommonTree)adaptor.nil();
							// 85:30: -> ^( IF expr $c1)
							{
								// Fun.g:85:33: ^( IF expr $c1)
								{
								CommonTree root_1 = (CommonTree)adaptor.nil();
								root_1 = (CommonTree)adaptor.becomeRoot(stream_IF.nextNode(), root_1);
								adaptor.addChild(root_1, stream_expr.nextTree());
								adaptor.addChild(root_1, stream_c1.nextTree());
								adaptor.addChild(root_0, root_1);
								}

							}


							retval.tree = root_0;
							}

							}
							break;
						case 2 :
							// Fun.g:86:7: ELSE COLON c2= seq_com DOT
							{
							ELSE44=(Token)match(input,ELSE,FOLLOW_ELSE_in_com826); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_ELSE.add(ELSE44);

							COLON45=(Token)match(input,COLON,FOLLOW_COLON_in_com828); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_COLON.add(COLON45);

							pushFollow(FOLLOW_seq_com_in_com838);
							c2=seq_com();
							state._fsp--;
							if (state.failed) return retval;
							if ( state.backtracking==0 ) stream_seq_com.add(c2.getTree());
							DOT46=(Token)match(input,DOT,FOLLOW_DOT_in_com840); if (state.failed) return retval; 
							if ( state.backtracking==0 ) stream_DOT.add(DOT46);

							// AST REWRITE
							// elements: expr, c1, c2
							// token labels: 
							// rule labels: c1, retval, c2
							// token list labels: 
							// rule list labels: 
							// wildcard labels: 
							if ( state.backtracking==0 ) {
							retval.tree = root_0;
							RewriteRuleSubtreeStream stream_c1=new RewriteRuleSubtreeStream(adaptor,"rule c1",c1!=null?c1.getTree():null);
							RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);
							RewriteRuleSubtreeStream stream_c2=new RewriteRuleSubtreeStream(adaptor,"rule c2",c2!=null?c2.getTree():null);

							root_0 = (CommonTree)adaptor.nil();
							// 87:30: -> ^( IFELSE expr $c1 $c2)
							{
								// Fun.g:87:33: ^( IFELSE expr $c1 $c2)
								{
								CommonTree root_1 = (CommonTree)adaptor.nil();
								root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(IFELSE, "IFELSE"), root_1);
								adaptor.addChild(root_1, stream_expr.nextTree());
								adaptor.addChild(root_1, stream_c1.nextTree());
								adaptor.addChild(root_1, stream_c2.nextTree());
								adaptor.addChild(root_0, root_1);
								}

							}


							retval.tree = root_0;
							}

							}
							break;

					}

					}
					break;
				case 4 :
					// Fun.g:89:4: WHILE expr COLON seq_com DOT
					{
					WHILE47=(Token)match(input,WHILE,FOLLOW_WHILE_in_com873); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WHILE.add(WHILE47);

					pushFollow(FOLLOW_expr_in_com875);
					expr48=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(expr48.getTree());
					COLON49=(Token)match(input,COLON,FOLLOW_COLON_in_com877); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COLON.add(COLON49);

					pushFollow(FOLLOW_seq_com_in_com883);
					seq_com50=seq_com();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_seq_com.add(seq_com50.getTree());
					DOT51=(Token)match(input,DOT,FOLLOW_DOT_in_com885); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DOT.add(DOT51);

					// AST REWRITE
					// elements: WHILE, seq_com, expr
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 90:30: -> ^( WHILE expr seq_com )
					{
						// Fun.g:90:33: ^( WHILE expr seq_com )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_WHILE.nextNode(), root_1);
						adaptor.addChild(root_1, stream_expr.nextTree());
						adaptor.addChild(root_1, stream_seq_com.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// Fun.g:92:9: FOR ID ASSN e1= expr TO e2= expr COLON seq_com DOT
					{
					FOR52=(Token)match(input,FOR,FOLLOW_FOR_in_com923); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_FOR.add(FOR52);

					ID53=(Token)match(input,ID,FOLLOW_ID_in_com925); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID53);

					ASSN54=(Token)match(input,ASSN,FOLLOW_ASSN_in_com927); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ASSN.add(ASSN54);

					pushFollow(FOLLOW_expr_in_com931);
					e1=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(e1.getTree());
					TO55=(Token)match(input,TO,FOLLOW_TO_in_com933); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_TO.add(TO55);

					pushFollow(FOLLOW_expr_in_com937);
					e2=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(e2.getTree());
					COLON56=(Token)match(input,COLON,FOLLOW_COLON_in_com939); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_COLON.add(COLON56);

					pushFollow(FOLLOW_seq_com_in_com951);
					seq_com57=seq_com();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_seq_com.add(seq_com57.getTree());
					DOT58=(Token)match(input,DOT,FOLLOW_DOT_in_com953); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DOT.add(DOT58);

					// AST REWRITE
					// elements: e2, seq_com, FOR, e1, ID
					// token labels: 
					// rule labels: e1, e2, retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_e1=new RewriteRuleSubtreeStream(adaptor,"rule e1",e1!=null?e1.getTree():null);
					RewriteRuleSubtreeStream stream_e2=new RewriteRuleSubtreeStream(adaptor,"rule e2",e2!=null?e2.getTree():null);
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 93:36: -> ^( FOR ID $e1 $e2 seq_com )
					{
						// Fun.g:93:39: ^( FOR ID $e1 $e2 seq_com )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_FOR.nextNode(), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_1, stream_e1.nextTree());
						adaptor.addChild(root_1, stream_e2.nextTree());
						adaptor.addChild(root_1, stream_seq_com.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 6 :
					// Fun.g:94:9: REPEAT seq_com UNTIL expr DOT
					{
					REPEAT59=(Token)match(input,REPEAT,FOLLOW_REPEAT_in_com993); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_REPEAT.add(REPEAT59);

					pushFollow(FOLLOW_seq_com_in_com995);
					seq_com60=seq_com();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_seq_com.add(seq_com60.getTree());
					UNTIL61=(Token)match(input,UNTIL,FOLLOW_UNTIL_in_com997); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_UNTIL.add(UNTIL61);

					pushFollow(FOLLOW_expr_in_com999);
					expr62=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(expr62.getTree());
					DOT63=(Token)match(input,DOT,FOLLOW_DOT_in_com1001); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DOT.add(DOT63);

					// AST REWRITE
					// elements: expr, REPEAT, seq_com
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 95:36: -> ^( REPEAT seq_com expr )
					{
						// Fun.g:95:39: ^( REPEAT seq_com expr )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_REPEAT.nextNode(), root_1);
						adaptor.addChild(root_1, stream_seq_com.nextTree());
						adaptor.addChild(root_1, stream_expr.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 7 :
					// Fun.g:96:9: DO seq_com WHILE expr DOT
					{
					DO64=(Token)match(input,DO,FOLLOW_DO_in_com1056); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DO.add(DO64);

					pushFollow(FOLLOW_seq_com_in_com1058);
					seq_com65=seq_com();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_seq_com.add(seq_com65.getTree());
					WHILE66=(Token)match(input,WHILE,FOLLOW_WHILE_in_com1060); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_WHILE.add(WHILE66);

					pushFollow(FOLLOW_expr_in_com1062);
					expr67=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(expr67.getTree());
					DOT68=(Token)match(input,DOT,FOLLOW_DOT_in_com1064); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_DOT.add(DOT68);

					// AST REWRITE
					// elements: seq_com, expr, DO
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 96:36: -> ^( DO seq_com expr )
					{
						// Fun.g:96:39: ^( DO seq_com expr )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_DO.nextNode(), root_1);
						adaptor.addChild(root_1, stream_seq_com.nextTree());
						adaptor.addChild(root_1, stream_expr.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "com"


	public static class seq_com_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "seq_com"
	// Fun.g:99:1: seq_com : ( com )* -> ^( SEQ ( com )* ) ;
	public final FunParser.seq_com_return seq_com() throws RecognitionException {
		FunParser.seq_com_return retval = new FunParser.seq_com_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope com69 =null;

		RewriteRuleSubtreeStream stream_com=new RewriteRuleSubtreeStream(adaptor,"rule com");

		try {
			// Fun.g:100:2: ( ( com )* -> ^( SEQ ( com )* ) )
			// Fun.g:100:4: ( com )*
			{
			// Fun.g:100:4: ( com )*
			loop10:
			while (true) {
				int alt10=2;
				int LA10_0 = input.LA(1);
				if ( (LA10_0==WHILE) ) {
					int LA10_5 = input.LA(2);
					if ( (synpred15_Fun()) ) {
						alt10=1;
					}

				}
				else if ( (LA10_0==DO||LA10_0==FOR||(LA10_0 >= ID && LA10_0 <= IF)||LA10_0==REPEAT) ) {
					alt10=1;
				}

				switch (alt10) {
				case 1 :
					// Fun.g:100:4: com
					{
					pushFollow(FOLLOW_com_in_seq_com1086);
					com69=com();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_com.add(com69.getTree());
					}
					break;

				default :
					break loop10;
				}
			}

			// AST REWRITE
			// elements: com
			// token labels: 
			// rule labels: retval
			// token list labels: 
			// rule list labels: 
			// wildcard labels: 
			if ( state.backtracking==0 ) {
			retval.tree = root_0;
			RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

			root_0 = (CommonTree)adaptor.nil();
			// 100:31: -> ^( SEQ ( com )* )
			{
				// Fun.g:100:34: ^( SEQ ( com )* )
				{
				CommonTree root_1 = (CommonTree)adaptor.nil();
				root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(SEQ, "SEQ"), root_1);
				// Fun.g:100:40: ( com )*
				while ( stream_com.hasNext() ) {
					adaptor.addChild(root_1, stream_com.nextTree());
				}
				stream_com.reset();

				adaptor.addChild(root_0, root_1);
				}

			}


			retval.tree = root_0;
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "seq_com"


	public static class expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "expr"
	// Fun.g:106:1: expr : sec_expr ( ( EQ ^| LT ^| GT ^) sec_expr )? ;
	public final FunParser.expr_return expr() throws RecognitionException {
		FunParser.expr_return retval = new FunParser.expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token EQ71=null;
		Token LT72=null;
		Token GT73=null;
		ParserRuleReturnScope sec_expr70 =null;
		ParserRuleReturnScope sec_expr74 =null;

		CommonTree EQ71_tree=null;
		CommonTree LT72_tree=null;
		CommonTree GT73_tree=null;

		try {
			// Fun.g:107:2: ( sec_expr ( ( EQ ^| LT ^| GT ^) sec_expr )? )
			// Fun.g:107:4: sec_expr ( ( EQ ^| LT ^| GT ^) sec_expr )?
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_sec_expr_in_expr1132);
			sec_expr70=sec_expr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, sec_expr70.getTree());

			// Fun.g:108:5: ( ( EQ ^| LT ^| GT ^) sec_expr )?
			int alt12=2;
			int LA12_0 = input.LA(1);
			if ( (LA12_0==EQ||LA12_0==GT||LA12_0==LT) ) {
				alt12=1;
			}
			switch (alt12) {
				case 1 :
					// Fun.g:108:7: ( EQ ^| LT ^| GT ^) sec_expr
					{
					// Fun.g:108:7: ( EQ ^| LT ^| GT ^)
					int alt11=3;
					switch ( input.LA(1) ) {
					case EQ:
						{
						alt11=1;
						}
						break;
					case LT:
						{
						alt11=2;
						}
						break;
					case GT:
						{
						alt11=3;
						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 11, 0, input);
						throw nvae;
					}
					switch (alt11) {
						case 1 :
							// Fun.g:108:8: EQ ^
							{
							EQ71=(Token)match(input,EQ,FOLLOW_EQ_in_expr1141); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							EQ71_tree = (CommonTree)adaptor.create(EQ71);
							root_0 = (CommonTree)adaptor.becomeRoot(EQ71_tree, root_0);
							}

							}
							break;
						case 2 :
							// Fun.g:108:14: LT ^
							{
							LT72=(Token)match(input,LT,FOLLOW_LT_in_expr1146); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							LT72_tree = (CommonTree)adaptor.create(LT72);
							root_0 = (CommonTree)adaptor.becomeRoot(LT72_tree, root_0);
							}

							}
							break;
						case 3 :
							// Fun.g:108:20: GT ^
							{
							GT73=(Token)match(input,GT,FOLLOW_GT_in_expr1151); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							GT73_tree = (CommonTree)adaptor.create(GT73);
							root_0 = (CommonTree)adaptor.becomeRoot(GT73_tree, root_0);
							}

							}
							break;

					}

					pushFollow(FOLLOW_sec_expr_in_expr1155);
					sec_expr74=sec_expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, sec_expr74.getTree());

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expr"


	public static class sec_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "sec_expr"
	// Fun.g:111:1: sec_expr : prim_expr ( ( PLUS ^| MINUS ^| TIMES ^| DIV ^) prim_expr )* ;
	public final FunParser.sec_expr_return sec_expr() throws RecognitionException {
		FunParser.sec_expr_return retval = new FunParser.sec_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token PLUS76=null;
		Token MINUS77=null;
		Token TIMES78=null;
		Token DIV79=null;
		ParserRuleReturnScope prim_expr75 =null;
		ParserRuleReturnScope prim_expr80 =null;

		CommonTree PLUS76_tree=null;
		CommonTree MINUS77_tree=null;
		CommonTree TIMES78_tree=null;
		CommonTree DIV79_tree=null;

		try {
			// Fun.g:112:2: ( prim_expr ( ( PLUS ^| MINUS ^| TIMES ^| DIV ^) prim_expr )* )
			// Fun.g:112:4: prim_expr ( ( PLUS ^| MINUS ^| TIMES ^| DIV ^) prim_expr )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_prim_expr_in_sec_expr1169);
			prim_expr75=prim_expr();
			state._fsp--;
			if (state.failed) return retval;
			if ( state.backtracking==0 ) adaptor.addChild(root_0, prim_expr75.getTree());

			// Fun.g:113:5: ( ( PLUS ^| MINUS ^| TIMES ^| DIV ^) prim_expr )*
			loop14:
			while (true) {
				int alt14=2;
				int LA14_0 = input.LA(1);
				if ( (LA14_0==DIV||LA14_0==MINUS||LA14_0==PLUS||LA14_0==TIMES) ) {
					alt14=1;
				}

				switch (alt14) {
				case 1 :
					// Fun.g:113:7: ( PLUS ^| MINUS ^| TIMES ^| DIV ^) prim_expr
					{
					// Fun.g:113:7: ( PLUS ^| MINUS ^| TIMES ^| DIV ^)
					int alt13=4;
					switch ( input.LA(1) ) {
					case PLUS:
						{
						alt13=1;
						}
						break;
					case MINUS:
						{
						alt13=2;
						}
						break;
					case TIMES:
						{
						alt13=3;
						}
						break;
					case DIV:
						{
						alt13=4;
						}
						break;
					default:
						if (state.backtracking>0) {state.failed=true; return retval;}
						NoViableAltException nvae =
							new NoViableAltException("", 13, 0, input);
						throw nvae;
					}
					switch (alt13) {
						case 1 :
							// Fun.g:113:8: PLUS ^
							{
							PLUS76=(Token)match(input,PLUS,FOLLOW_PLUS_in_sec_expr1178); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							PLUS76_tree = (CommonTree)adaptor.create(PLUS76);
							root_0 = (CommonTree)adaptor.becomeRoot(PLUS76_tree, root_0);
							}

							}
							break;
						case 2 :
							// Fun.g:113:16: MINUS ^
							{
							MINUS77=(Token)match(input,MINUS,FOLLOW_MINUS_in_sec_expr1183); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							MINUS77_tree = (CommonTree)adaptor.create(MINUS77);
							root_0 = (CommonTree)adaptor.becomeRoot(MINUS77_tree, root_0);
							}

							}
							break;
						case 3 :
							// Fun.g:113:25: TIMES ^
							{
							TIMES78=(Token)match(input,TIMES,FOLLOW_TIMES_in_sec_expr1188); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							TIMES78_tree = (CommonTree)adaptor.create(TIMES78);
							root_0 = (CommonTree)adaptor.becomeRoot(TIMES78_tree, root_0);
							}

							}
							break;
						case 4 :
							// Fun.g:113:34: DIV ^
							{
							DIV79=(Token)match(input,DIV,FOLLOW_DIV_in_sec_expr1193); if (state.failed) return retval;
							if ( state.backtracking==0 ) {
							DIV79_tree = (CommonTree)adaptor.create(DIV79);
							root_0 = (CommonTree)adaptor.becomeRoot(DIV79_tree, root_0);
							}

							}
							break;

					}

					pushFollow(FOLLOW_prim_expr_in_sec_expr1197);
					prim_expr80=prim_expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) adaptor.addChild(root_0, prim_expr80.getTree());

					}
					break;

				default :
					break loop14;
				}
			}

			}

			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "sec_expr"


	public static class prim_expr_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "prim_expr"
	// Fun.g:116:1: prim_expr : ( FALSE -> FALSE | TRUE -> TRUE | NUM -> NUM | ID -> ID | ID LPAR actual RPAR -> ^( FUNCCALL ID actual ) | NOT prim_expr -> ^( NOT prim_expr ) | LPAR expr RPAR -> expr );
	public final FunParser.prim_expr_return prim_expr() throws RecognitionException {
		FunParser.prim_expr_return retval = new FunParser.prim_expr_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token FALSE81=null;
		Token TRUE82=null;
		Token NUM83=null;
		Token ID84=null;
		Token ID85=null;
		Token LPAR86=null;
		Token RPAR88=null;
		Token NOT89=null;
		Token LPAR91=null;
		Token RPAR93=null;
		ParserRuleReturnScope actual87 =null;
		ParserRuleReturnScope prim_expr90 =null;
		ParserRuleReturnScope expr92 =null;

		CommonTree FALSE81_tree=null;
		CommonTree TRUE82_tree=null;
		CommonTree NUM83_tree=null;
		CommonTree ID84_tree=null;
		CommonTree ID85_tree=null;
		CommonTree LPAR86_tree=null;
		CommonTree RPAR88_tree=null;
		CommonTree NOT89_tree=null;
		CommonTree LPAR91_tree=null;
		CommonTree RPAR93_tree=null;
		RewriteRuleTokenStream stream_NOT=new RewriteRuleTokenStream(adaptor,"token NOT");
		RewriteRuleTokenStream stream_LPAR=new RewriteRuleTokenStream(adaptor,"token LPAR");
		RewriteRuleTokenStream stream_NUM=new RewriteRuleTokenStream(adaptor,"token NUM");
		RewriteRuleTokenStream stream_TRUE=new RewriteRuleTokenStream(adaptor,"token TRUE");
		RewriteRuleTokenStream stream_RPAR=new RewriteRuleTokenStream(adaptor,"token RPAR");
		RewriteRuleTokenStream stream_FALSE=new RewriteRuleTokenStream(adaptor,"token FALSE");
		RewriteRuleTokenStream stream_ID=new RewriteRuleTokenStream(adaptor,"token ID");
		RewriteRuleSubtreeStream stream_actual=new RewriteRuleSubtreeStream(adaptor,"rule actual");
		RewriteRuleSubtreeStream stream_prim_expr=new RewriteRuleSubtreeStream(adaptor,"rule prim_expr");
		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// Fun.g:117:2: ( FALSE -> FALSE | TRUE -> TRUE | NUM -> NUM | ID -> ID | ID LPAR actual RPAR -> ^( FUNCCALL ID actual ) | NOT prim_expr -> ^( NOT prim_expr ) | LPAR expr RPAR -> expr )
			int alt15=7;
			switch ( input.LA(1) ) {
			case FALSE:
				{
				alt15=1;
				}
				break;
			case TRUE:
				{
				alt15=2;
				}
				break;
			case NUM:
				{
				alt15=3;
				}
				break;
			case ID:
				{
				int LA15_4 = input.LA(2);
				if ( (LA15_4==LPAR) ) {
					alt15=5;
				}
				else if ( (LA15_4==EOF||(LA15_4 >= BOOL && LA15_4 <= COLON)||(LA15_4 >= DIV && LA15_4 <= ELSE)||LA15_4==EQ||LA15_4==FOR||LA15_4==FUNC||(LA15_4 >= GT && LA15_4 <= IF)||LA15_4==INT||(LA15_4 >= LT && LA15_4 <= MINUS)||(LA15_4 >= PLUS && LA15_4 <= PROC)||(LA15_4 >= REPEAT && LA15_4 <= RPAR)||(LA15_4 >= TIMES && LA15_4 <= TO)||LA15_4==UNTIL||LA15_4==WHILE) ) {
					alt15=4;
				}

				else {
					if (state.backtracking>0) {state.failed=true; return retval;}
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 15, 4, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

				}
				break;
			case NOT:
				{
				alt15=6;
				}
				break;
			case LPAR:
				{
				alt15=7;
				}
				break;
			default:
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}
			switch (alt15) {
				case 1 :
					// Fun.g:117:4: FALSE
					{
					FALSE81=(Token)match(input,FALSE,FOLLOW_FALSE_in_prim_expr1211); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_FALSE.add(FALSE81);

					// AST REWRITE
					// elements: FALSE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 117:31: -> FALSE
					{
						adaptor.addChild(root_0, stream_FALSE.nextNode());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// Fun.g:118:4: TRUE
					{
					TRUE82=(Token)match(input,TRUE,FOLLOW_TRUE_in_prim_expr1241); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_TRUE.add(TRUE82);

					// AST REWRITE
					// elements: TRUE
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 118:31: -> TRUE
					{
						adaptor.addChild(root_0, stream_TRUE.nextNode());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 3 :
					// Fun.g:119:4: NUM
					{
					NUM83=(Token)match(input,NUM,FOLLOW_NUM_in_prim_expr1272); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NUM.add(NUM83);

					// AST REWRITE
					// elements: NUM
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 119:31: -> NUM
					{
						adaptor.addChild(root_0, stream_NUM.nextNode());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 4 :
					// Fun.g:120:4: ID
					{
					ID84=(Token)match(input,ID,FOLLOW_ID_in_prim_expr1304); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID84);

					// AST REWRITE
					// elements: ID
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 120:31: -> ID
					{
						adaptor.addChild(root_0, stream_ID.nextNode());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 5 :
					// Fun.g:121:4: ID LPAR actual RPAR
					{
					ID85=(Token)match(input,ID,FOLLOW_ID_in_prim_expr1337); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_ID.add(ID85);

					LPAR86=(Token)match(input,LPAR,FOLLOW_LPAR_in_prim_expr1339); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAR.add(LPAR86);

					pushFollow(FOLLOW_actual_in_prim_expr1341);
					actual87=actual();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_actual.add(actual87.getTree());
					RPAR88=(Token)match(input,RPAR,FOLLOW_RPAR_in_prim_expr1343); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAR.add(RPAR88);

					// AST REWRITE
					// elements: ID, actual
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 121:31: -> ^( FUNCCALL ID actual )
					{
						// Fun.g:121:34: ^( FUNCCALL ID actual )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot((CommonTree)adaptor.create(FUNCCALL, "FUNCCALL"), root_1);
						adaptor.addChild(root_1, stream_ID.nextNode());
						adaptor.addChild(root_1, stream_actual.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 6 :
					// Fun.g:123:4: NOT prim_expr
					{
					NOT89=(Token)match(input,NOT,FOLLOW_NOT_in_prim_expr1399); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_NOT.add(NOT89);

					pushFollow(FOLLOW_prim_expr_in_prim_expr1401);
					prim_expr90=prim_expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_prim_expr.add(prim_expr90.getTree());
					// AST REWRITE
					// elements: NOT, prim_expr
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 123:31: -> ^( NOT prim_expr )
					{
						// Fun.g:123:34: ^( NOT prim_expr )
						{
						CommonTree root_1 = (CommonTree)adaptor.nil();
						root_1 = (CommonTree)adaptor.becomeRoot(stream_NOT.nextNode(), root_1);
						adaptor.addChild(root_1, stream_prim_expr.nextTree());
						adaptor.addChild(root_0, root_1);
						}

					}


					retval.tree = root_0;
					}

					}
					break;
				case 7 :
					// Fun.g:124:4: LPAR expr RPAR
					{
					LPAR91=(Token)match(input,LPAR,FOLLOW_LPAR_in_prim_expr1427); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_LPAR.add(LPAR91);

					pushFollow(FOLLOW_expr_in_prim_expr1429);
					expr92=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(expr92.getTree());
					RPAR93=(Token)match(input,RPAR,FOLLOW_RPAR_in_prim_expr1431); if (state.failed) return retval; 
					if ( state.backtracking==0 ) stream_RPAR.add(RPAR93);

					// AST REWRITE
					// elements: expr
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 124:31: -> expr
					{
						adaptor.addChild(root_0, stream_expr.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "prim_expr"


	public static class actual_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "actual"
	// Fun.g:127:1: actual : ( expr -> expr | -> NOACTUAL );
	public final FunParser.actual_return actual() throws RecognitionException {
		FunParser.actual_return retval = new FunParser.actual_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		ParserRuleReturnScope expr94 =null;

		RewriteRuleSubtreeStream stream_expr=new RewriteRuleSubtreeStream(adaptor,"rule expr");

		try {
			// Fun.g:128:2: ( expr -> expr | -> NOACTUAL )
			int alt16=2;
			int LA16_0 = input.LA(1);
			if ( (LA16_0==FALSE||LA16_0==ID||LA16_0==LPAR||(LA16_0 >= NOT && LA16_0 <= NUM)||LA16_0==TRUE) ) {
				alt16=1;
			}
			else if ( (LA16_0==RPAR) ) {
				alt16=2;
			}

			else {
				if (state.backtracking>0) {state.failed=true; return retval;}
				NoViableAltException nvae =
					new NoViableAltException("", 16, 0, input);
				throw nvae;
			}

			switch (alt16) {
				case 1 :
					// Fun.g:128:4: expr
					{
					pushFollow(FOLLOW_expr_in_actual1458);
					expr94=expr();
					state._fsp--;
					if (state.failed) return retval;
					if ( state.backtracking==0 ) stream_expr.add(expr94.getTree());
					// AST REWRITE
					// elements: expr
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 128:31: -> expr
					{
						adaptor.addChild(root_0, stream_expr.nextTree());
					}


					retval.tree = root_0;
					}

					}
					break;
				case 2 :
					// Fun.g:129:31: 
					{
					// AST REWRITE
					// elements: 
					// token labels: 
					// rule labels: retval
					// token list labels: 
					// rule list labels: 
					// wildcard labels: 
					if ( state.backtracking==0 ) {
					retval.tree = root_0;
					RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.getTree():null);

					root_0 = (CommonTree)adaptor.nil();
					// 129:31: -> NOACTUAL
					{
						adaptor.addChild(root_0, (CommonTree)adaptor.create(NOACTUAL, "NOACTUAL"));
					}


					retval.tree = root_0;
					}

					}
					break;

			}
			retval.stop = input.LT(-1);

			if ( state.backtracking==0 ) {
			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);
			}
		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
			retval.tree = (CommonTree)adaptor.errorNode(input, retval.start, input.LT(-1), re);
		}
		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "actual"

	// $ANTLR start synpred15_Fun
	public final void synpred15_Fun_fragment() throws RecognitionException {
		// Fun.g:100:4: ( com )
		// Fun.g:100:4: com
		{
		pushFollow(FOLLOW_com_in_synpred15_Fun1086);
		com();
		state._fsp--;
		if (state.failed) return;

		}

	}
	// $ANTLR end synpred15_Fun

	// Delegated rules

	public final boolean synpred15_Fun() {
		state.backtracking++;
		int start = input.mark();
		try {
			synpred15_Fun_fragment(); // can never throw exception
		} catch (RecognitionException re) {
			System.err.println("impossible: "+re);
		}
		boolean success = !state.failed;
		input.rewind(start);
		state.backtracking--;
		state.failed=false;
		return success;
	}



	public static final BitSet FOLLOW_var_decl_in_program116 = new BitSet(new long[]{0x0000000401040020L});
	public static final BitSet FOLLOW_proc_decl_in_program119 = new BitSet(new long[]{0x0000000400040000L});
	public static final BitSet FOLLOW_EOF_in_program122 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_PROC_in_proc_decl219 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_proc_decl221 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_LPAR_in_proc_decl227 = new BitSet(new long[]{0x0000008001000020L});
	public static final BitSet FOLLOW_formal_in_proc_decl229 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_RPAR_in_proc_decl231 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_COLON_in_proc_decl233 = new BitSet(new long[]{0x0000802001610C20L});
	public static final BitSet FOLLOW_var_decl_in_proc_decl239 = new BitSet(new long[]{0x0000802001610C20L});
	public static final BitSet FOLLOW_seq_com_in_proc_decl242 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_DOT_in_proc_decl244 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FUNC_in_proc_decl336 = new BitSet(new long[]{0x0000000001000020L});
	public static final BitSet FOLLOW_type_in_proc_decl338 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_proc_decl340 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_LPAR_in_proc_decl346 = new BitSet(new long[]{0x0000008001000020L});
	public static final BitSet FOLLOW_formal_in_proc_decl348 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_RPAR_in_proc_decl350 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_COLON_in_proc_decl352 = new BitSet(new long[]{0x0000806001610420L});
	public static final BitSet FOLLOW_var_decl_in_proc_decl358 = new BitSet(new long[]{0x0000806001610420L});
	public static final BitSet FOLLOW_seq_com_in_proc_decl361 = new BitSet(new long[]{0x0000004000000000L});
	public static final BitSet FOLLOW_RETURN_in_proc_decl367 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_proc_decl369 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_DOT_in_proc_decl371 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_in_formal513 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_formal515 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_type_in_var_decl589 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_var_decl591 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_ASSN_in_var_decl593 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_var_decl595 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BOOL_in_type627 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_INT_in_type658 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_com699 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_ASSN_in_com701 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_com703 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_com732 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_LPAR_in_com734 = new BitSet(new long[]{0x0000108184208000L});
	public static final BitSet FOLLOW_actual_in_com736 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_RPAR_in_com738 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IF_in_com772 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_com774 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_COLON_in_com776 = new BitSet(new long[]{0x0000802000611C00L});
	public static final BitSet FOLLOW_seq_com_in_com780 = new BitSet(new long[]{0x0000000000001800L});
	public static final BitSet FOLLOW_DOT_in_com788 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ELSE_in_com826 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_COLON_in_com828 = new BitSet(new long[]{0x0000802000610C00L});
	public static final BitSet FOLLOW_seq_com_in_com838 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_DOT_in_com840 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_WHILE_in_com873 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_com875 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_COLON_in_com877 = new BitSet(new long[]{0x0000802000610C00L});
	public static final BitSet FOLLOW_seq_com_in_com883 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_DOT_in_com885 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FOR_in_com923 = new BitSet(new long[]{0x0000000000200000L});
	public static final BitSet FOLLOW_ID_in_com925 = new BitSet(new long[]{0x0000000000000010L});
	public static final BitSet FOLLOW_ASSN_in_com927 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_com931 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_TO_in_com933 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_com937 = new BitSet(new long[]{0x0000000000000040L});
	public static final BitSet FOLLOW_COLON_in_com939 = new BitSet(new long[]{0x0000802000610C00L});
	public static final BitSet FOLLOW_seq_com_in_com951 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_DOT_in_com953 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_REPEAT_in_com993 = new BitSet(new long[]{0x0000A02000610400L});
	public static final BitSet FOLLOW_seq_com_in_com995 = new BitSet(new long[]{0x0000200000000000L});
	public static final BitSet FOLLOW_UNTIL_in_com997 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_com999 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_DOT_in_com1001 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DO_in_com1056 = new BitSet(new long[]{0x0000802000610400L});
	public static final BitSet FOLLOW_seq_com_in_com1058 = new BitSet(new long[]{0x0000800000000000L});
	public static final BitSet FOLLOW_WHILE_in_com1060 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_com1062 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_DOT_in_com1064 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_com_in_seq_com1086 = new BitSet(new long[]{0x0000802000610402L});
	public static final BitSet FOLLOW_sec_expr_in_expr1132 = new BitSet(new long[]{0x0000000008104002L});
	public static final BitSet FOLLOW_EQ_in_expr1141 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_LT_in_expr1146 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_GT_in_expr1151 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_sec_expr_in_expr1155 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_prim_expr_in_sec_expr1169 = new BitSet(new long[]{0x0000040210000202L});
	public static final BitSet FOLLOW_PLUS_in_sec_expr1178 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_MINUS_in_sec_expr1183 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_TIMES_in_sec_expr1188 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_DIV_in_sec_expr1193 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_prim_expr_in_sec_expr1197 = new BitSet(new long[]{0x0000040210000202L});
	public static final BitSet FOLLOW_FALSE_in_prim_expr1211 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_TRUE_in_prim_expr1241 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NUM_in_prim_expr1272 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_prim_expr1304 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_prim_expr1337 = new BitSet(new long[]{0x0000000004000000L});
	public static final BitSet FOLLOW_LPAR_in_prim_expr1339 = new BitSet(new long[]{0x0000108184208000L});
	public static final BitSet FOLLOW_actual_in_prim_expr1341 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_RPAR_in_prim_expr1343 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_prim_expr1399 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_prim_expr_in_prim_expr1401 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAR_in_prim_expr1427 = new BitSet(new long[]{0x0000100184208000L});
	public static final BitSet FOLLOW_expr_in_prim_expr1429 = new BitSet(new long[]{0x0000008000000000L});
	public static final BitSet FOLLOW_RPAR_in_prim_expr1431 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_expr_in_actual1458 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_com_in_synpred15_Fun1086 = new BitSet(new long[]{0x0000000000000002L});
}
