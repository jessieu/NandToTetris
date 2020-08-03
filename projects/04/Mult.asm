// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

@0
D=M 
@R0 
M=D // R0 = RAM[0]

@1
D=M 
@R1 
M=D // R1 = RAM[1]

// determine whether one of the input is 0
@R0 
D=M
@ZERO
D;JEQ 
@R1 
D=M
@ZERO
D;JEQ 

@i 
M=0 // i =0

@R2
M=0 // R2 = 0 hold the multiplication result

(LOOP)
@i
D=M 
@R0 
D=D-M // i - R0

@END
D;JEQ // if i > R0, goto end

@R1 
D=M 
@R2 
M=M+D // R2 += R1
@i 
M=M+1 // i++
@LOOP
0;JMP

(END)
@END
0;JMP

(ZERO)
@R2 
M=0
@END 
0;JMP
