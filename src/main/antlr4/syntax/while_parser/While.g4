grammar While;

@parser::header {
    import program.*;
    import program.statements.*;
    import program.expressions.*;
}

@parser::members {

    public static IStatement parse(String input) {
        WhileParser parser = new WhileParser(new CommonTokenStream(new WhileLexer(CharStreams.fromString(input))));
        return parser.start().value;
    }

}

start returns [IStatement value]
 : s = statement { $value = $s.value; } EOF
;

statement returns [IStatement value]
 : s1 = statement  SEQ    s2 = statement { $value = new Sequence($s1.value, $s2.value); }
 | s1 = statement  NDET   s2 = statement { $value = new Or($s1.value, $s2.value); }
 | s1 = statement  PAR    s2 = statement { $value = new Par($s1.value, $s2.value); }

 | id = expression ASSIGN e = expression { $value = new Assignment($id.value, $e.value); }
 | WHILE expression DO statement OD { $value = new While($expression.value, $statement.value); }
 | IF expression THEN s1 = statement Else s2 = statement FI { $value = new If($expression.value, $s1.value, $s2.value); }

 | Skip  { $value  = new Skip(); }
 | ABORT { $value = new Abort(); }
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;

expression returns [IExpression value]
locals [BinOp.Arithmetic arit, BinOp.Relational rel, BinOp.Logical log]

 : MINUS expression { $value = UnOp.Arithmetic.NEG.of($expression.value); }
 | NOT   expression { $value = UnOp.Logical.NOT.of($expression.value); }

 | e1 = expression ( MUL { $arit = BinOp.Arithmetic.MUL; }
                   | DIV { $arit = BinOp.Arithmetic.DIV; }
                   | REM { $arit = BinOp.Arithmetic.REM; }
                   ) e2 = expression { $value = $arit.of($e1.value, $e2.value); }
 | e1 = expression ( PLUS  { $arit = BinOp.Arithmetic.ADD; }
                   | MINUS { $arit = BinOp.Arithmetic.SUB; }
                   ) e2 = expression { $value = $arit.of($e1.value, $e2.value); }

 | e1 = expression ( LT { $rel = BinOp.Relational.LT; }
                   | LE { $rel = BinOp.Relational.LE; }
                   | GT { $rel = BinOp.Relational.GT; }
                   | GE { $rel = BinOp.Relational.GE; }
                   ) e2 = expression { $value = $rel.of($e1.value, $e2.value); }
 | e1 = expression ( EQ { $rel = BinOp.Relational.EQ; }
                   | NE { $rel = BinOp.Relational.NE; }
                   ) e2 = expression { $value = $rel.of($e1.value, $e2.value); }

 | e1 = expression AND e2 = expression { $value = BinOp.Logical.AND.of($e1.value, $e2.value); }
 | e1 = expression ( OR  { $log = BinOp.Logical.OR; }
                   | XOR { $log = BinOp.Logical.XOR; }
                   ) e2 = expression { $value = $log.of($e1.value, $e2.value); }

 | atom { $value = $atom.value; }
 ;

atom returns [IExpression value]
 : OPAR e = expression CPAR { $value = $e.value; }
 | INT { $value = new Value<>($INT.int); }
 | (TRUE { $value = new Value<>(true); } | FALSE  { $value = new Value<>(false); } )
 | ID { $value = new Identifier($ID.text); }
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