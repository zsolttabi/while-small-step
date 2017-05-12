grammar While;

@parser::header {
    import program.*;
    import program.statements.*;
    import program.expressions.*;
}

@parser::members {

    public static Statement parse(String input) {
        WhileParser parser = new WhileParser(new CommonTokenStream(new WhileLexer(CharStreams.fromString(input))));
        return parser.start().value;
    }

}

start returns [Statement value]:
    s = statement { $value = $s.value; }
;

statement returns [Statement value]:

    id = expression Assign e = expression { $value = new Assignment($id.value, $e.value); }
|
    s1 = statement SeqSeparator s2 = statement { $value = new Sequence($s1.value, $s2.value); }
|
    IF e = expression Then s1 = statement Else s2 = statement Fi { $value = new If($e.value, $s1.value, $s2.value); }
|
    WHILE e = expression Do s = statement Od { $value = new While($e.value, $s.value); }
|
    Skip { $value  = new Skip(); }
|
    Abort { $value = new Abort(); }
;

expression returns [Expression value]:

    Identifier { $value = new Identifier($Identifier.text); }
|
    Int { $value = new IntValue(Integer.parseInt($Int.text)); }
|
    e1 = expression Plus e2 = expression { $value = BinOp.add($e1.value, $e2.value); }
|
    e1 = expression Minus e2 = expression { $value = BinOp.subtract($e1.value, $e2.value); }
|
    Minus a = expression { $value = UnOp.neg($a.value); }
|
    True { $value = new BoolValue(true); }
|
    False { $value = new BoolValue(false); }
|
    e1 = expression Equals e2 = expression { $value = BinOp.areEquals($e1.value, $e2.value); }
|
    e1 = expression LessThen e2 = expression { $value = BinOp.lessThen($e1.value, $e2.value); }
|
    Not e = expression { $value = UnOp.not($e.value); }
|
    e1 = expression And e2 = expression  { $value = BinOp.and($e1.value, $e2.value); }
;

IF: 'if' ;

Fi: 'fi';

Then: 'then' ;

Else: 'else' ;

WHILE: 'while' ;

Abort: 'abort' ;

Do: 'do' ;

Od: 'od';

Assign: ':=' ;

SeqSeparator: ';' ;

Skip: 'SKIP' ;

True: 'tt' ;

False: 'ff' ;

Plus: '+' ;

Minus: '-';

Equals: '=' ;

LessThen: '<' ;

Not: '!' ;

And: '&&' ;

Int: '0' | ('-'? ('1'..'9') ('0'..'9')*) ;

Identifier: ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')* ;

WS: [ \t|\r\n] -> channel(HIDDEN);