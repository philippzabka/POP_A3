  ADDI x1, x0, 0 # a=0
  ADDI x2, x0, 1 # b=1
  ADDI x3, x0, 3 # c=3
  ADDI x4, x0, 4 # d=4
  BLT x1, x2, expr # if a<b -> expr
  ADDI x1, x0, 2 # res=2
  JAL x0, done # jump to done with return val 0

expr:
  SUB t0, x4, x3 # d-c=e
  ADD t1, x1, x2 # a+b=f
  ADD t0, t1, t0 # e+f=g
  ADDI t2, t0, 1 # g+1=h

  SUB t4, x3, x1 # c-a=e
  SUB t5, x4, x2 # d-b=f
  ADD t4, t5, t4 # e+f=i

  SUB x1, t2, t4 # h-i

done:
  ret