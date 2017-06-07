grammar While;

start
 : s = stm EOF
 ;

stm
 : stm ';'   stm                        # Sequence
 | stm 'or'  stm                        # Or
 | stm 'par' stm                        # Par
 | expr ':=' expr                       # Assignment
 | 'while' expr 'do' stm 'od'           # While
 | 'if' expr 'then' stm 'else' stm 'fi' # If
 | 'skip'                               # Skip
 | 'abort'                              # Abort
 ;

expr
 : atom                             # AtomExpr
 | '-' expr                         # Minus
 | '!' expr                         # Not
 | expr op=('*'|'/'|'%') expr       # MulDivRem
 | expr op=('+'|'-') expr           # AddSub
 | expr op=('<'|'<='|'>'|'>=') expr # Rel1
 | expr op=('='|'!=') expr          # Rel2
 | expr '&&' expr                   # And
 | expr op=('||'|'\\^') expr        # OrXor
 ;

atom
 : '(' expr ')'        # Parenthesis
 | Integer             # Integer
 | tf=('true'|'false') # Bool
 | Identifier          # Identifier
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

Identifier
 : Letter LetterOrDigit*
 ;

fragment
Letter
 : [a-zA-Z_]
 ;

fragment
LetterOrDigit
 : [a-zA-Z0-9_]
 ;

Integer: '0' | NonZeroDigit Digit* ;

fragment
Digit
 : '0'
 | NonZeroDigit
 ;

fragment
NonZeroDigit
 : [1-9]
 ;

WS: [ \t\r\n\u000C]+ -> skip;
