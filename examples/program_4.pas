Program Prog4; { simple procedures and functions }

Var
  Name : String;

Procedure GoodBye(Name : String);
Begin
  Write('GoodBye, ');
  Write(Name);
  WriteLn('.');
End;

Function Hello() : String;
Var Name : String;
Begin
  WriteLn('Hello!');
  WriteLn('What is your name?');
  ReadLn(Name);
  Hello := Name;
End;

Begin
  Name := Hello();
  WriteLn('This is simple example.');
  GoodBye(Name);
End.
