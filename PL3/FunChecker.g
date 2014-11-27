//////////////////////////////////////////////////////////////
//
// Specification of the Fun contextual analyser.
//
// Developed June 2012 by David Watt (University of Glasgow).
//
//////////////////////////////////////////////////////////////


tree grammar FunChecker;

// This specifies the Fun contextual analyser,
// which performs scope checking and type checking.


options {
	tokenVocab = Fun;
	ASTLabelType = CommonTree;
}


//////// Auxiliary variables and methods

@members {

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

}


//////// Programs

program
	:	^(PROG
				{ predefine(); }
		  var_decl*
		  proc_decl+
		 )
				{ Type tmain = retrieve("main", $PROG);
				  checkType(tmain, MAINTYPE, $PROG);
				}
	;


//////// Declarations

proc_decl
	:	^(PROC
		  ID
				{ typeTable.enterLocalScope();
				}
		  t=formal
				{ Type proctype =
				    new Type.Mapping(t, Type.VOID);
				  define($ID.text, proctype, $PROC);
				  // ... to enable recursion
				}
		  var_decl*
		  com
				{ typeTable.exitLocalScope();
				  define($ID.text, proctype, $PROC);
				}
		 )
	|	^(FUNC
		  t1=type
		  ID
				{ typeTable.enterLocalScope();
				}
		  t2=formal
				{ Type functype =
				    new Type.Mapping(t2, t1);
				  define($ID.text, functype, $FUNC);
				  // ... to enable recursion
				}
		  var_decl*
		  com
		  t3=expr
				{ typeTable.exitLocalScope();
				  define($ID.text, functype, $FUNC);
				  checkType(t1, t3, $FUNC);
				}
		 )
	;

formal			returns [Type type]
	:	^(FORMAL t=type ID)
				{ define($ID.text, t, $FORMAL);
				  $type = t;
				}
	|	NOFORMAL
				{ $type = Type.VOID; }
	;

var_decl
	:	^(VAR t1=type ID t2=expr)
				{ define($ID.text, t1, $VAR);
				  checkType(t1, t2, $VAR);
				}
	;

type				returns [Type type]
	:	BOOL		{ $type = Type.BOOL; }
	|	INT		{ $type = Type.INT; }
	;


//////// Commands

com
	:	^(ASSN ID t=expr)
				{ Type tvar =
				    retrieve($ID.text, $ASSN);
				  checkType(tvar, t, $ASSN);
				}
	|	^(PROCCALL ID t=expr)
				{ Type tres = checkCall(
				    $ID.text, t, $PROCCALL);
				  if (! tres.equiv(Type.VOID))
				    reportError(
				      "procedure should be void",
				      $PROCCALL);
				}
	|	^(IF t=expr com)
				{ checkType(Type.BOOL, t, $IF);
				}
	|	^(IFELSE t=expr com com)
				{ checkType(Type.BOOL, t, $IFELSE);
				}
	|	^(WHILE t=expr com)
				{ checkType(Type.BOOL, t, $WHILE);
				}
	|	^(FOR ID t1=$e1 t2=$e2 seq_com) 
				{checkType(type.INT, t1, $FOR);
				 checkType(type.INT, t2, $FOR)
				}
|	^(SEQ com*)
	;


//////// Expressions

expr				returns [Type type]
	:	FALSE
				{ $type = Type.BOOL; }
	|	TRUE
				{ $type = Type.BOOL; }
	|	NUM
				{ $type = Type.INT; }
	|	ID
				{ $type = retrieve($ID.text, $ID);
				}
	|	^(FUNCCALL ID t=expr)
				{ Type result = checkCall(
				    $ID.text, t, $FUNCCALL);
				  if (result.equiv(Type.VOID))
				    reportError(
					 "procedure should be non-void",
				      $FUNCCALL);
				  $type = result;
				}
	|	^(EQ t1=expr t2=expr)
				{ $type = checkBinary(
				    COMPTYPE, t1, t2, $EQ); }
	|	^(LT t1=expr t2=expr)
				{ $type = checkBinary(
				    COMPTYPE, t1, t2, $LT); }
	|	^(GT t1=expr t2=expr)
				{ $type = checkBinary(
				    COMPTYPE, t1, t2, $GT); }
	|	^(PLUS t1=expr t2=expr)
				{ $type = checkBinary(
				    ARITHTYPE, t1, t2, $PLUS); }
	|	^(MINUS t1=expr t2=expr)
				{ $type = checkBinary(
				    ARITHTYPE, t1, t2, $MINUS); }
	|	^(TIMES t1=expr t2=expr)
				{ $type = checkBinary(
				    ARITHTYPE, t1, t2, $TIMES); }
	|	^(DIV t1=expr t2=expr)
				{ $type = checkBinary(
				     ARITHTYPE, t1, t2, $DIV); }
	|	^(NOT t=expr)
				{ $type = checkUnary(NOTTYPE, t, $NOT); }
	|	NOACTUAL
				{ $type = Type.VOID; }
	;
