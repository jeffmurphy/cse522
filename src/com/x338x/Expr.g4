/*
   LD A, <addr>
   LD B, <addr>
   ST A, <addr>
   ST B, <addr>

   ADD A, B // A = A + B
   SUB A, B // A = A - B
   MUL A, B // A = A * B 
   DIV A, B // A = A / B

   CMP A, B
   BEQ <label> // A == B
   BNZ <label>
   BGT <label> // A > B
   BLT <label> // A < B

   HALT

any line can begin with "<label>:" 

*/
grammar Expr;		
prog:	(expr NEWLINE)* ;
expr:   LABEL expr
    |   'LD' REG ',' ADDR
    |   'ST' REG ',' ADDR
    |   'LD' REG ',' LIT
    |   'ST' REG ',' LIT
    |   'ADD' DST ',' SRC
    |   'SUB' DST ',' SRC
    |   'MUL' DST ',' SRC
    |   'DIV' DST ',' SRC
    |   'BEQ' LHS ',' RHS ',' TARGET
    |   'BGT' LHS ',' RHS ',' TARGET
    |   'BLT' LHS ',' RHS ',' TARGET
    |   'BNZ' REG ',' TARGET
    ;
REG : [AB] ;
SRC : [AB] ;
DST : [AB] ;
LHS : [AB] ;
RHS : [AB] ;
ADDR : @[0-9]+ ;
LIT : @[0-9]+ ;
LABEL : [a-z0-9]+':' ;
TARGET : ':'[a-z0-9]+ ;
NEWLINE : [\r\n]+ ;
INT     : [0-9]+ ;
WS: [ \t]+ -> skip ;
