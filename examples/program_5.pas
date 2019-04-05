Program Prog5; { functions with parameters }
Var
  A : Integer;
  B : Integer;
  C : Integer;
  
Function Min(X : Integer; Y : Integer) : Integer;
Begin
  If X<Y Then Min := X Else Min := Y;
End;

Begin
  A := 2;
  B := 5;
  C := Min(A,B)+1;
  Writeln(C);
End.

