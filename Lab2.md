Replace with your writeup using Markdown, just like we did in Lab 1.

#Lab 2

## Grammars:SyntheticExamples
1. 
	1. a ∈ Vobject
	2. b ∈ Vobject
	3. If A ∈ Aobject, then A & A ∈ Aobject
	4. If V ∈ Vobject, then V ∈ Aobject

    ___________   ___________
    a ∈ Vobject   b ∈ Vobject


    A ∈ Aobject      V ∈ Vobject
    _______________  ____________
    A & A ∈ Aobject  V ∈ Aobject

2. Because of "A & A"

3. S::= A|B|C  A::= aA|a  B::=bB|ε  C::=cC|c
```
    S               S
    |               |
    A               A
   / \              |
  a   A     OR      a
     / \
    a   A
        |
        a
For this condition, the result could only be "a"(the number of "a" is unlimited). For the condition of "C", the grammar is same.

    S               S
    |               |
    B               B
   / \              |
  b   B     OR      ε
     / \
    b   B
        |
        ε
For this condition, the result must be ended by "ε", because "ε" is terminals.
```


4. 1 and 5 can be generated in the grammar.
	1. S => AaBb => baBb => baab
	4. S => AaBb => AbaBb=> bbaBb => bbaab

5. 1 and 5 can be generated in the grammar.

	1.
	```
	 S
	/ \
   a  ScB
     /   \
    b     d
    ```

    5.
    ```
     S
    / \
   a  ScB
     /   \
    A     A
    |     |
    c     c
    ```
### Grammars:Understanding a Language
1.
    1. For grammar 1, it could be "operand" or "operand operator operand"(and the number of operands could be unlimited). For grammar 2, the result could be "operand ε" or "operand operator operand ε"(the number of operands could be unlimited, but the result must be ended by ε).
    2. I think they have same expressions, as we can see from the first part of the question, their results are same.

2. 
    (4-2)<<2 result: 8
    4-(2<<2) result: -4
    Binary expresion of 2: 10. So when we left shift "10" by 2, the result would be "1000", it is "8" in decimal. Therefore in the last expression it is "4-8", that's why the result is "-4",

3. 
    Number N::= 1 zero N|2 zero N|3 zero N|4 zero N|5 zero N|6 zero N|7 zero N|8 zero N|9 zero N |D
    Zero  zero=0zero|ε 
    Digit D=0|1|2|3|4|5|6|7|8|9
    Unary operator uop::=-|ε
    point ::= .
    Expression e::=[uop N zero point N] "E" N zero| uop 0 point N "E" N zero | uop D | uop N
