ADDI x8,x0,1
ADDI x9,x0,2
ADDI x19,x0,3
ADDI x10,x0,4
BLT x8,x9,IF
ADDI x4,x0,2
JAL x0,DONE
IF:
SUB x21,x10,x19
ADD x20,x8,x9
ADD x12,x21,x20
ADDI x1,x0,1
ADD x11,x12,x1
SUB x22,x19,x8
SUB x2,x10,x9
ADD x14,x22,x2
SUB x3,x11,x14
DONE:
ret