grammar While;

@parser::header {
    import program.*;
    import program.statements.*;
    import program.expressions.*;
    import static program.ParserHelper.*;
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
 : s1 = statement SEQ  s2 = statement { $value = makeStm(Sequence::new, $ctx.s1, $ctx.s2); }
 | s1 = statement NDET s2 = statement { $value = makeStm(Or::new, $ctx.s1, $ctx.s2); }
 | s1 = statement PAR  s2 = statement { $value = makeStm(Par::new, $ctx.s1, $ctx.s2); }

 | id = expression ASSIGN e = expression { $value = makeStm(Assignment::new, $ctx.id, $ctx.e); }
 | WHILE e = expression DO s = statement OD { $value = makeStm(While::new, $ctx.e, $ctx.s); }
 | IF e = expression THEN s1 = statement Else s2 = statement FI { $value = makeStm(If::new, $ctx.e, $ctx.s1, $ctx.s2); }

 | Skip  { $value = new Skip(); }
 | ABORT { $value = new Abort(); }
 ;

expression returns [IExpression value]
locals [BinOp.Arithmetic arit, BinOp.Relational rel, BinOp.Logical log]

 : MINUS e = expression { $value = makeExpr(UnOp.Arithmetic.NEG::of, $ctx.e); }
 | NOT   e = expression { $value = makeExpr(UnOp.Logical.NOT::of, $ctx.e); }

 | e1 = expression ( MUL { $arit = BinOp.Arithmetic.MUL; }
                   | DIV { $arit = BinOp.Arithmetic.DIV; }
                   | REM { $arit = BinOp.Arithmetic.REM; }
                   ) e2 = expression { $value = makeExpr($ctx.arit::of, $ctx.e1, $ctx.e2); }
 | e1 = expression ( PLUS  { $arit = BinOp.Arithmetic.ADD; }
                   | MINUS { $arit = BinOp.Arithmetic.SUB; }
                   ) e2 = expression { $value = makeExpr($ctx.arit::of, $ctx.e1, $ctx.e2); }

 | e1 = expression ( LT { $rel = BinOp.Relational.LT; }
                   | LE { $rel = BinOp.Relational.LE; }
                   | GT { $rel = BinOp.Relational.GT; }
                   | GE { $rel = BinOp.Relational.GE; }
                   ) e2 = expression { $value = makeExpr($ctx.rel::of, $ctx.e1, $ctx.e2); }
 | e1 = expression ( EQ { $rel = BinOp.Relational.EQ; }
                   | NE { $rel = BinOp.Relational.NE; }
                   ) e2 = expression { $value = makeExpr($ctx.rel::of, $ctx.e1, $ctx.e2); }

 | e1 = expression AND e2 = expression { $value = makeExpr(BinOp.Logical.AND::of, $ctx.e1, $ctx.e2); }
 | e1 = expression ( OR  { $log = BinOp.Logical.OR; }
                   | XOR { $log = BinOp.Logical.XOR; }
                   ) e2 = expression { $value = makeExpr($ctx.log::of, $ctx.e1, $ctx.e2); }

 | atom { $value = $atom.value; }
 ;

atom returns [IExpression value]
 : OPAR e = expression CPAR { $value = $e.value; }
 | INT { $value = new Value<>($INT.int); }
 | (TRUE { $value = new Value<>(true); } | FALSE  { $value = new Value<>(false); } )
 | ID { $value = new Identifier($ID.text); }
 ;

other: OTHER+;

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