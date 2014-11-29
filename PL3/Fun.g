//////////////////////////////////////////////////////////////
//
// Specification of the Fun syntactic analyser.
//
// Developed June 2012 by David Watt (University of Glasgow).
// Extended October 2014 by Simon Gay (University of Glasgow).
//
//////////////////////////////////////////////////////////////


grammar Fun;

// This specifies the Fun syntactic analyser.
// In detail, it specifies the syntax of Fun and the 
// translation of Fun phrases to ASTs.


options {
	output = AST;
	ASTLabelType = CommonTree;
       backtrack = true;  // Needed for DO loop syntax, in the simple approach used here.
}

tokens {        // special tokens for labeling AST nodes
	PROG;
	VAR;
	FORMAL;
	NOFORMAL;
	IFELSE;
	SEQ; 
	PROCCALL;
	FUNCCALL;
	NOACTUAL;
}


//////// Programs

program
	:	var_decl* proc_decl+ EOF   -> ^(PROG 
		                                var_decl*
		                                proc_decl+)
	;


//////// Declarations

proc_decl
	:	PROC ID
		  LPAR formal RPAR COLON
		  var_decl* seq_com DOT    -> ^(PROC
		                                ID formal 
		                                var_decl* seq_com)
	|	FUNC type ID
		  LPAR formal RPAR COLON
		  var_decl* seq_com
		  RETURN expr DOT          -> ^(FUNC
		                                type ID formal 
		                                var_decl* seq_com
		                                expr)
	;

formal
	:	type ID                    -> ^(FORMAL type ID)
	|	                           -> NOFORMAL
	;

var_decl
	:	type ID ASSN expr          -> ^(VAR type ID expr)
	;

type
	:	BOOL                       -> BOOL
	|	INT                        -> INT
	;


//////// Commands

com
	:	ID ASSN expr               -> ^(ASSN ID expr)
	|	ID LPAR actual RPAR        -> ^(PROCCALL
							     ID actual)
	|	IF expr COLON c1=seq_com
		  ( DOT                    -> ^(IF expr $c1)
		  | ELSE COLON
		    c2=seq_com DOT         -> ^(IFELSE expr $c1 $c2)
		  )
	|	WHILE expr COLON
		  seq_com DOT              -> ^(WHILE expr seq_com)
    // Next three clauses added by SG
    |   FOR ID ASSN e1=expr TO e2=expr COLON
          seq_com DOT              -> ^(FOR ID $e1 $e2 seq_com) 
    |   REPEAT seq_com UNTIL expr DOT
                                   -> ^(REPEAT seq_com expr)
    |   DO seq_com WHILE expr DOT  -> ^(DO seq_com expr)
	;

seq_com
	:	com*                       -> ^(SEQ com*)
	;


//////// Expressions

expr
	:	sec_expr
		  ( (EQ^ | LT^ | GT^) sec_expr )?
	;

sec_expr
	:	prim_expr
		  ( (PLUS^ | MINUS^ | TIMES^ | DIV^) prim_expr )*
	;

prim_expr
	:	FALSE                      -> FALSE
	|	TRUE                       -> TRUE
	|	NUM                        -> NUM
	|	ID                         -> ID
	|	ID LPAR actual RPAR        -> ^(FUNCCALL
		                                ID actual)
	|	NOT prim_expr              -> ^(NOT prim_expr)
	|	LPAR expr RPAR             -> expr
	;

actual
	:	expr                       -> expr
	|	                           -> NOACTUAL
	;


//////// Lexicon

BOOL	:	'bool' ;
ELSE	:	'else' ;
FALSE	:	'false' ;
FUNC	:	'func' ;
IF	:	'if' ;
INT	:	'int' ;
PROC	:	'proc' ;
RETURN :	'return' ;
TRUE	:	'true' ;
WHILE	:	'while' ;
FOR     :   'for' ; // Added by SG
TO      :   'to' ; // Added by SG
REPEAT  :   'repeat' ; // Added by SG
UNTIL   :   'until' ; // Added by SG
DO      :   'do' ; // Added by SG

EQ	:	'==' ;
LT	:	'<' ;
GT	:	'>' ;
PLUS	:	'+' ;
MINUS	:	'-' ;
TIMES	:	'*' ;
DIV	:	'/' ;
NOT	:	'not' ;

ASSN	:	'=' ;

LPAR	:	'(' ;
RPAR	:	')' ;
COLON	:	':' ;
DOT	:	'.' ;

NUM	:	DIGIT+ ;

ID	:	LETTER (LETTER | DIGIT)* ;

SPACE	:	(' ' | '\t')+             {skip();} ;
EOL	:	'\r'? '\n'                {skip();} ;
COMMENT :	'#' ~('\r' | '\n')*
		  '\r'? '\n'              {skip();} ;

fragment LETTER : 'a'..'z' | 'A'..'Z' ;
fragment DIGIT  : '0'..'9' ;
