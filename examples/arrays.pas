program Arrays;
var
a: Integer; 
input: Integer;
arr: Array[0..10] of integer;


begin { Main }
for a:= 0 to 10 do
begin
	writeln('Enter value for index ', a);
	readln(input);
	arr[a] := input;
	a:= a+1
end;
writeln('Printing array');
for a:= 0 to length(arr) - 1 do
begin

write(arr[a]);

if a < (length(arr) - 1)
then
write(',');

end;
end.  { Main }