//////////////////////////////////////////////////////////////
//
// Specification of the Fun code generator.
//
// Developed June 2012 by David Watt (University of Glasgow).
//
//////////////////////////////////////////////////////////////


tree grammar FunEncoder;

// This specifies the Fun -> SVM code generator.


options {
	tokenVocab = Fun;
	ASTLabelType = CommonTree;
}


//////// Auxiliary variables and methods

@members {

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

}


//////// Programs

program			returns [SVM objectprogram]
	:	^(PROG
				{ predefine();
				}
		  var_decl*
				{ int calladdr =
				    obj.currentOffset();
				  obj.emit12(SVM.CALL, 0);
				  obj.emit1(SVM.HALT);
				}
		  proc_decl+
				{ int mainaddr =
				    addrTable.get("main").offset;
				  obj.patch12(calladdr, mainaddr);
				  $objectprogram = obj;
				}
		 )
	;


//////// Declarations

proc_decl
	:	^(PROC
		  ID
				{ String id = $ID.text;
				  Address procaddr = new Address(
				    obj.currentOffset(), Address.CODE);
				  addrTable.put(id, procaddr);
				  addrTable.enterLocalScope();
				  currentLocale = Address.LOCAL;
				  localvaraddr = 2;
				  // ... allows 2 words for link data
				}
		  formal
		  var_decl*
		  com
				{ obj.emit11(SVM.RETURN, 0);
				  addrTable.exitLocalScope();
				  currentLocale = Address.GLOBAL;
				}
		 )
	|	^(FUNC
		  type
		  ID
				{ String id = $ID.text;
				  Address procaddr = new Address(
				    obj.currentOffset(), Address.CODE);
				  addrTable.put(id, procaddr);
				  addrTable.enterLocalScope();
				  currentLocale = Address.LOCAL;
				  localvaraddr = 2;
				  // ... allows 2 words for link data
				}
		  formal
		  var_decl*
		  com
		  expr
				{ obj.emit11(SVM.RETURN, 1);
				  addrTable.exitLocalScope();
				  currentLocale = Address.GLOBAL;
				}
		 )
	;

formal
	:	^(FORMAL
		  type
		  ID
				{ String id = $ID.text;
				  addrTable.put(id, new Address(
				    localvaraddr++, Address.LOCAL));
				  obj.emit11(SVM.COPYARG, 1); 
				}
		 )
	|	NOFORMAL
	;

var_decl
	:	^(VAR
		  type
		  ID
		  expr
				{ String id = $ID.text;
				  switch (currentLocale) {
				    case Address.LOCAL:
				      addrTable.put(id, new Address(
				        localvaraddr++, Address.LOCAL));
				      break;
				    case Address.GLOBAL:
				      addrTable.put(id, new Address(
				        globalvaraddr++, Address.GLOBAL));
				  }
				}
		 )
	;

type
	:	BOOL
	|	INT
	;


//////// Commands

com
	:	^(ASSN
		  ID
		  expr
				{ String id = $ID.text;
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
				}
		 )
	|	^(PROCCALL
		  ID
		  expr
				{ String id = $ID.text;
				  Address procaddr = addrTable.get(id);
				  // Assume procaddr.locale == CODE.
				  obj.emit12(SVM.CALL,
				    procaddr.offset);
				}
		 )
	|	^(IF
		  expr
				{ int condaddr = obj.currentOffset();
				  obj.emit12(SVM.JUMPF, 0);
				}
		  com
				{ int exitaddr = obj.currentOffset();
				  obj.patch12(condaddr, exitaddr);
				}
		 )
	|	^(IFELSE
		  expr
				{ int condaddr = obj.currentOffset();
				  obj.emit12(SVM.JUMPF, 0);
				}
		  com
				{ int jumpaddr = obj.currentOffset();
				  obj.emit12(SVM.JUMP, 0);
				  int elseaddr = obj.currentOffset();
				  obj.patch12(condaddr, elseaddr);
				}
		  com
				{ int exitaddr = obj.currentOffset();
				  obj.patch12(jumpaddr, exitaddr);
				}
		 )
	|	^(WHILE
				{ int startaddr = obj.currentOffset();
				}
		   expr
				{ int condaddr = obj.currentOffset();
				  obj.emit12(SVM.JUMPF, 0);
				}
		   com
				{ obj.emit12(SVM.JUMP, startaddr);
				  int exitaddr = obj.currentOffset();
				  obj.patch12(condaddr, exitaddr);
				}
		 )
	//////// Additions by Tom Wallis BEGIN
	|	^(FOR
				{ int startaddr = obj.currentOffset();
				}
			ID
			e1=expr
				{ String id = $ID.text; 
				  Address varaddr = addrTable.get(id);
				  System.out.println(varaddr.offset);
				  switch (varaddr.locale) {
				    case Address.GLOBAL:
				      obj.emit12(SVM.STOREG,
				        varaddr.offset);
				      break;
				    case Address.LOCAL:
				      obj.emit12(SVM.STOREL,
				        varaddr.offset);
				  }
				}
			expr
				{ int conaddr = obj.currentOffset();
				  // IN HERE, COMPARE I AND M, PUSH TO THE QUEUE, AND JUMPF.
				  obj.emit12(SVM.JUMPF, exitaddr);
				}
			com
				{ 
				  obj.emit12(SVM.LOADC, 1);
				  switch (varaddr.locale) {
					case Address.GLOBAL:
					  obj.emit12(SVM.LOADG, varaddr.offset);
					  obj.emit1(SVM.ADD);
					  obj.emit12(SVM.STOREG, varaddr.offset);
					  break;
					case Address.LOCAL:
					  obj.emit12(SVM.LOADL, varaddr.offset);
					  obj.emit1(SVM.ADD);
					  obj.emit12(SVM.STOREL, varaddr.offset);
				  }
				  obj.emit12(SVM.JUMP, conaddr);
				  int exitaddr = obj.currentOffset();
				  obj.patch12(conaddr, exitaddr);
				}
		)
	| ^(DO
				{ int startaddr = obj.currentOffset();
				}
			com
				{ int conaddr = obj.currentOffset();
				  
				}
			expr
				{ // Here, test the expression. push to stack and jumpf.
				  obj.emit12(SVM.JUMPF, 0); // Is this right?!
				}
	  )
	//////// Additions by Tom Wallis END
	|	^(SEQ
		  com*
		 )
	;


//////// Expressions

expr
	:	FALSE
				{ obj.emit12(SVM.LOADC, 0); }
	|	TRUE
				{ obj.emit12(SVM.LOADC, 1); }
	|	NUM
				{ int value =
				    Integer.parseInt($NUM.text);
				  obj.emit12(SVM.LOADC, value);
				}
	|	ID
				{ String id = $ID.text;
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
	|	^(FUNCCALL
		  ID
		  expr
				{ String id = $ID.text;
				  Address funcaddr = addrTable.get(id);
				  // Assume that funcaddr.locale == CODE.
				  obj.emit12(SVM.CALL,
				    funcaddr.offset);
				}
		 )
	|	^(EQ
		  expr
		  expr
				{ obj.emit1(SVM.CMPEQ); }
		 )
	|	^(LT
		  expr
		  expr
				{ obj.emit1(SVM.CMPLT); }
		 )
	|	^(GT
		  expr
		  expr
				{ obj.emit1(SVM.CMPGT); }
		 )
	|	^(PLUS
		  expr
		  expr
				{ obj.emit1(SVM.ADD); }
		 )
	|	^(MINUS
		  expr
		  expr
				{ obj.emit1(SVM.SUB); }
		 )
	|	^(TIMES
		  expr
		  expr
				{ obj.emit1(SVM.MUL); }
		 )
	|	^(DIV
		  expr
		  expr
				{ obj.emit1(SVM.DIV); }
		 )
	|	^(NOT
		  expr
				{ obj.emit1(SVM.INV); }
		 )
	|	NOACTUAL
				{ }
	;
