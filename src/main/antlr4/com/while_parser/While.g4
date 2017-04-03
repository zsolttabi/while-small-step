grammar While;

@parser::header {
    import ast.AST;
    import ast.statement.*;
    import ast.expression.*;
    import ast.expression.values.*;
}

@parser::members {

    public static AST parseAst(String input) {
        WhileParser parser = new WhileParser(new CommonTokenStream(new WhileLexer(new ANTLRInputStream(input))));
        return new AST(parser.start().value);
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
    If e = expression Then s1 = statement Else s2 = statement { $value = new If($e.value, $s1.value, $s2.value); }
|
    While e = expression Do s = statement { $value = new While($e.value, $s.value); }
|
    Skip { $value  = new Skip(); }
;

expression returns [Expression value]:

    Identifier { $value = new Identifier($Identifier.text); }
|
    Int { $value = new IntValue(Integer.parseInt($Int.text)); }
|
    e1 = expression Plus e2 = expression { $value = new Plus($e1.value, $e2.value); }
|
    e1 = expression Minus e2 = expression { $value = new Minus($e1.value, $e2.value); }
|
    Minus a = expression { $value = new Negate($a.value); }
|
    True { $value = new BoolValue(true); }
|
    False { $value = new BoolValue(false); }
|
    e1 = expression Equals e2 = expression { $value = new Equals($e1.value, $e2.value); }
|
    e1 = expression LessThen e2 = expression { $value = new LessThen($e1.value, $e2.value); }
|
    Not e = expression { $value = new Not($e.value); }
|
    e1 = expression And e2 = expression  { $value = new LessThen($e1.value, $e2.value); }
;

If: 'if' ;

Then: 'then' ;

Else: 'else' ;

While: 'while' ;

Do: 'do' ;

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


WS: [ \t|\r\n] -> skip;