package analizador2;
import static analizador2.Tokens.*;
%%
%class Lexer
%type Tokens
L=[a-zA-Z_]+
D=[0-9]+
espacio=[ \t,\r,\n]+
%{
    public String lexeme;
%}
%%

/* ───────────────────────────────────────────────
   Python-Inspired Keywords
   ─────────────────────────────────────────────── */
"if"        { lexeme=yytext(); return IF; }
"elif"      { lexeme=yytext(); return ELIF; }
"else"      { lexeme=yytext(); return ELSE; }
"while"     { lexeme=yytext(); return WHILE; }
"for"       { lexeme=yytext(); return FOR; }
"in"        { lexeme=yytext(); return IN; }
"return"    { lexeme=yytext(); return RETURN; }
"print"     { lexeme=yytext(); return PRINT; }
"true"      { lexeme=yytext(); return TRUE; }
"false"     { lexeme=yytext(); return FALSE; }
"none"      { lexeme=yytext(); return NONE; }


"def"     { lexeme=yytext(); return DEF; }
"as"        { lexeme=yytext(); return AS; }
"from"      { lexeme=yytext(); return FROM; }
"import"    { lexeme = yytext(); return IMPORT; }

"pass"      { lexeme=yytext(); return PASS; }
"break"     { lexeme=yytext(); return BREAK; }
"continue"  { lexeme=yytext(); return CONTINUE; }

/* ───────────────────────────────────────────────
   Rust-Inspired Keywords
   ─────────────────────────────────────────────── */
"fn"        { lexeme=yytext(); return FN; }
"let"       { lexeme=yytext(); return LET; }
"mut"       { lexeme=yytext(); return MUT; }
"struct"    { lexeme=yytext(); return STRUCT; }
"impl"      { lexeme=yytext(); return IMPL; }
"match"     { lexeme=yytext(); return MATCH; }
"loop"      { lexeme=yytext(); return LOOP; }
"enum"      { lexeme=yytext(); return ENUM; }
"mod"       { lexeme=yytext(); return MOD; }
"pub"       { lexeme=yytext(); return PUB; }
"ref"       { lexeme=yytext(); return REF; }
"self"      { lexeme=yytext(); return SELF; }
"super"     { lexeme=yytext(); return SUPER; }
"use"       { lexeme=yytext(); return USE; }
"crate"     { lexeme=yytext(); return CRATE; }
"const"     { lexeme=yytext(); return CONST; }
"static"    { lexeme=yytext(); return STATIC; }
"type"      { lexeme=yytext(); return TYPE; }
"trait"     { lexeme=yytext(); return TRAIT; }
"async"     { lexeme=yytext(); return ASYNC; }
"await"     { lexeme=yytext(); return AWAIT; }
"move"      { lexeme=yytext(); return MOVE; }
"unsafe"    { lexeme=yytext(); return UNSAFE; }

/* ───────────────────────────────────────────────
   Primitive Data Types
   ─────────────────────────────────────────────── */
"int"       { lexeme=yytext(); return INT; }
"float"     { lexeme=yytext(); return FLOAT; }
"bool"      { lexeme=yytext(); return BOOL; }
"string"    { lexeme=yytext(); return STRING; }
"char"      { lexeme=yytext(); return CHAR; }
"byte"      { lexeme=yytext(); return BYTE; }
"unit"      { lexeme=yytext(); return UNIT; }

/* ───────────────────────────────────────────────
   Compound / Abstract Data Types
   ─────────────────────────────────────────────── */
"vector"    { lexeme=yytext(); return VECTOR; }
"tuple"     { lexeme=yytext(); return TUPLE; }
"option"    { lexeme=yytext(); return OPTION; }
"result"    { lexeme=yytext(); return RESULT; }
"map"       { lexeme=yytext(); return MAP; }
"set"       { lexeme=yytext(); return SET; }
"array"     { lexeme=yytext(); return ARRAY; }

/* ───────────────────────────────────────────────
   Literals
   ─────────────────────────────────────────────── */
{D}+                    { lexeme=yytext(); return INTEGER_LITERAL; }
{D}+\.{D}+              { lexeme=yytext(); return FLOAT_LITERAL; }
\"([^\\\"]|\\.)*\"      { lexeme=yytext(); return STRING_LITERAL; }
\'([^\\\']|\\.)?\'      { lexeme=yytext(); return CHAR_LITERAL; }
b\'([^\\\']|\\.)?\'     { lexeme=yytext(); return BYTE_LITERAL; }

/* ───────────────────────────────────────────────
   Operators (Arithmetic, Comparison, Logical)
   ─────────────────────────────────────────────── */
"+"         { lexeme=yytext(); return PLUS; }
"-"         { lexeme=yytext(); return MINUS; }
"*"         { lexeme=yytext(); return MULTIPLY; }
"/"         { lexeme=yytext(); return DIVIDE; }
"%"         { lexeme=yytext(); return MODULO; }
"**"        { lexeme=yytext(); return POWER; }

"=="        { lexeme=yytext(); return EQUALS; }
"!="        { lexeme=yytext(); return NOT_EQUALS; }
">"         { lexeme=yytext(); return GREATER_THAN; }
"<"         { lexeme=yytext(); return LESS_THAN; }
">="        { lexeme=yytext(); return GREATER_EQUALS; }
"<="        { lexeme=yytext(); return LESS_EQUALS; }

"&&"        { lexeme=yytext(); return AND; }
"||"        { lexeme=yytext(); return OR; }
"!"         { lexeme=yytext(); return NOT; }

"="         { lexeme=yytext(); return ASSIGN; }
"+="        { lexeme=yytext(); return PLUS_ASSIGN; }
"-="        { lexeme=yytext(); return MINUS_ASSIGN; }
"*="        { lexeme=yytext(); return MULTIPLY_ASSIGN; }
"/="        { lexeme=yytext(); return DIVIDE_ASSIGN; }
"%="        { lexeme=yytext(); return MODULO_ASSIGN; }

/* ───────────────────────────────────────────────
   Delimiters and Punctuation
   ─────────────────────────────────────────────── */
"("         { lexeme=yytext(); return LEFT_PAREN; }
")"         { lexeme=yytext(); return RIGHT_PAREN; }
"{"         { lexeme=yytext(); return LEFT_BRACE; }
"}"         { lexeme=yytext(); return RIGHT_BRACE; }
"["         { lexeme=yytext(); return LEFT_BRACKET; }
"]"         { lexeme=yytext(); return RIGHT_BRACKET; }
";"         { lexeme=yytext(); return SEMICOLON; }
":"         { lexeme=yytext(); return COLON; }
","         { lexeme=yytext(); return COMMA; }
"."         { lexeme=yytext(); return DOT; }
"->"        { lexeme=yytext(); return ARROW; }

/* ───────────────────────────────────────────────
   Identifiers and Special
   ─────────────────────────────────────────────── */
{L}({L}|{D})*  { lexeme=yytext(); return IDENTIFIER; }

/* ───────────────────────────────────────────────
   Whitespace and Comments
   ─────────────────────────────────────────────── */
{espacio}   { /*Ignore*/ }
"//".* { /*Ignore*/ }

/* ───────────────────────────────────────────────
   Error Handling
   ─────────────────────────────────────────────── */
. { return ERROR; }
