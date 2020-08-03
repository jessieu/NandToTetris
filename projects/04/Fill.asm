// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.
(END)
@24576
D=A 
@keyAddr
M=D // keyAddr = 24576

A=M 
D=M // RAM[24576]

@WHITEN
D;JEQ // if RAM[24576] == 0, key up
@BLACKEN // program’s end
0;JMP // infinite loop

// WHITEN SCREEN
(WHITEN)
@SCREEN
D=A
@addr
M=D

// n = 8191 
@8191
D=A
@n
M=D

// i = 0
@i
M=0
(WHITENLOOP)
@i
D=M
@n
D=D-M // i - n
@END
D;JGT // if i - n > 0 goto END

@addr
A=M
M=0 // RAM[addr]=1111111111111111

@i
M=M+1 // i = i + 1
@1
D=A
@addr
M=D+M // addr = addr + 1
@WHITENLOOP
0;JMP // goto LOOP

//////////////////////////////////////////////////////////////
// Blacken
// address = screen
(BLACKEN)
@SCREEN
D=A
@addr
M=D

// n = 8191 
@8191
D=A
@n
M=D

// i = 0
@i
M=0

(LOOP)
@i
D=M
@n
D=D-M // i - n
@END
D;JGT // if i - n > 0 goto END

@addr
A=M
M=-1 // RAM[addr]=1111111111111111

@i
M=M+1 // i = i + 1
@1
D=A
@addr
M=D+M // addr = addr + 1
@LOOP
0;JMP // goto LOOP
