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

start returns [IStatement value]:
    s = statement { $value = $s.value; }
;

statement returns [IStatement value]:

    id = expression Assign e  = expression     { $value = new Assignment($id.value, $e.value); } |
    s1 = statement  Seq    s2 = statement      { $value = new Sequence($s1.value, $s2.value);  } |
    s1 = statement  OR     s2 = statement      { $value = new Or($s1.value, $s2.value);        } |

    WHILE e = expression  Do     s  = statement Od                     { $value = new While($e.value, $s.value);          } |
    IF    e = expression  Then   s1 = statement Else s2 = statement Fi { $value = new If($e.value, $s1.value, $s2.value); } |

    Skip  { $value  = new Skip(); } |
    Abort { $value = new Abort(); }

;

expression returns [IExpression value]:

    Identifier { $value = new Identifier($Identifier.text); } |
    Int        { $value = new Value<>($Int.int);     } |
    True       { $value = new Value<>(true);         } |
    False      { $value = new Value<>(false);        } |

    e1 = expression Plus     e2 = expression { $value = BinOp.Arithmetic.ADD.of($e1.value, $e2.value); } |
    e1 = expression Minus    e2 = expression { $value = BinOp.Arithmetic.SUB.of($e1.value, $e2.value); } |
    e1 = expression Asterisk e2 = expression { $value = BinOp.Arithmetic.MUL.of($e1.value, $e2.value); } |
    e1 = expression Slash    e2 = expression { $value = BinOp.Arithmetic.DIV.of($e1.value, $e2.value); } |
    e1 = expression Percent  e2 = expression { $value = BinOp.Arithmetic.REM.of($e1.value, $e2.value); } |

    e1 = expression Equals        e2 = expression { $value = BinOp.Relational.EQ.of($e1.value, $e2.value); } |
    e1 = expression LessThen      e2 = expression { $value = BinOp.Relational.LT.of($e1.value, $e2.value); } |
    e1 = expression LessThenEq    e2 = expression { $value = BinOp.Relational.LE.of($e1.value, $e2.value); } |
    e1 = expression GreaterThen   e2 = expression { $value = BinOp.Relational.GT.of($e1.value, $e2.value); } |
    e1 = expression GreaterThenEq e2 = expression { $value = BinOp.Relational.GE.of($e1.value, $e2.value); } |

    e1 = expression And e2 = expression { $value = BinOp.Logical.AND.of($e1.value, $e2.value); } |
    e1 = expression Or  e2 = expression { $value = BinOp.Logical.OR.of($e1.value, $e2.value);  } |
    e1 = expression Xor e2 = expression { $value = BinOp.Logical.XOR.of($e1.value, $e2.value); } |

    Minus a = expression { $value = UnOp.Arithmetic.NEG.of($a.value); } |
    Not   e = expression { $value = UnOp.Logical.NOT.of($e.value);    }
;

IF: 'if';
Then: 'then';
Else: 'else';
Fi: 'fi';

WHILE: 'while';
Do: 'do' ;
Od: 'od';

Seq: ';';

Assign: ':=';

Skip: 'SKIP';

Abort: 'abort';

OR: 'OR';

True: 'true';
False: 'false';

Plus: '+';
Minus: '-';
Asterisk: '*';
Slash: '/';
Percent: '%';

Equals: '=';
LessThen: '<';
LessThenEq: '<=';
GreaterThen: '>';
GreaterThenEq: '>=';

And: 'and';
Or: 'or';
Xor: 'xor';

Not: 'not';

Int: '0' | ('-'? ('1'..'9') ('0'..'9')*) ;

Identifier: ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')* ;

WS: [ \t|\r\n] -> channel(HIDDEN);