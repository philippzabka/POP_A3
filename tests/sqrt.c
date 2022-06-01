/*
  Example for handling for-loops

  Function main computes ISQRT(x) from lecture slides about IR:
  integer square root (truncated square root) for non-negative integers x
  are assigned to variable res (and printed if possible).
*/
int main()
{
   unsigned int i,n,m,x,res;

   x=11;
   // printf("Please input a non-negative integer value: ");
   // scanf("%d", &x);

   n=0;
   m=1;

   for (i=m; i<=x; i+=m)
     {
       n=n+1;
       m=m+2;
     }

   res=n; // for 11: res==3
   // printf("sqrt(%d)=%d\n",x,res);
}

