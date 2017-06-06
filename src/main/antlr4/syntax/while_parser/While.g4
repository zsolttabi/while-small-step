grammar While;

start
 : s = stm EOF
 ;

stm
 : s1 = stm SEQ  s2 = stm                     # Sequence
 | s1 = stm NDET s2 = stm                     # Or
 | s1 = stm PAR  s2 = stm                     # Par
 | id = expr ASSIGN e = expr                  # Assignment
 | WHILE e = expr DO s = stm OD               # While
 | IF e = expr THEN s1 = stm Else s2 = stm FI # If
 | Skip                                       # Skip
 | ABORT                                      # Abort
 | OTHER+                                     # OtherStm
 ;

expr
 : MINUS e = expr # Minus
 | NOT   e = expr # Not
 | e1 = expr op = ( MUL | DIV | REM ) e2 = expr   # MulDivRem
 | e1 = expr op = ( PLUS | MINUS )    e2 = expr   # AddSub
 | e1 = expr op = ( LT | LE | GT | GE ) e2 = expr # Rel1
 | e1 = expr op = ( EQ | NE ) e2 = expr           # Rel2
 | e1 = expr AND e2 = expr                        # And
 | e1 = expr op = ( OR | XOR ) e2 = expr          # OrXor
 | atom                                           # AtomExpr
 | OTHER+                                         # OtherExpr
 ;

atom
 : OPAR e = expr CPAR   # Parenthesis
 | INT                  # Integer
 | tf = (TRUE | FALSE ) # Bool
 | ID                   # Identifier
 ;

IF: 'if';
THEN: 'then';
Else: 'else';
FI: 'fi';
WHILE: 'while';
DO: 'do' ;
OD: 'od';
SEQ: ';';
ASSIGN: ':=';
Skip: 'skip';
ABORT: 'abort';
NDET: 'or';
PAR: 'par';

TRUE: 'true';
FALSE: 'false';

OPAR : '(';
CPAR : ')';

POW: '^';
PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
REM: '%';

EQ: '=';
NE: '!=';
LT: '<';
LE: '<=';
GT: '>';
GE: '>=';

AND: '&&';
OR:  '||';
XOR: '^';
NOT: '!';

ID: [a-zA-Z_] [a-zA-Z_0-9]*;

INT: '0' | (('1'..'9') ('0'..'9')*) ;

//FLOAT: [0-9]+ '.' [0-9]* | '.' [0-9]+;

//STRING: '"' (~["\r\n] | '""')* '"';

//COMMENT: '//' ~[\r\n]* -> skip;

WS: [ \t|\r\n] -> channel(HIDDEN);

OTHER: .;