ADDI x1, x0, 11 # x=11
ADD x2, x0, x0 # n=0
ADDI x3, x0, 1 # m=1

#Loop
ADD x4, x0, x3 # i=m
BGE x1, x4, loop # x>=i -> loop

loop:
  ADDI x2, x2, 1 # n=n+1
  ADDI x3, x3, 2 # m=m+2
  ADD x4, x4, x3 # i+=m
  BGE x1, x4, loop # x=>i

ADD x1, x0, x2 # res=n
ret # return

