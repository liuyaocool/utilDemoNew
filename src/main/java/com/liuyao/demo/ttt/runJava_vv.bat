@ECHO OFF
"C:\Program Files\Java\jdk1_7_32\bin\java" -version
ECHO ------------------------
cd %~dp1
ECHO ...... cddir %~dp1
IF EXIST %~n1.class (
DEL %~n1.class
ECHO ...... DELETE %~n1.class
)
ECHO ...... Compiling %~nx1
"C:\Program Files\Java\jdk1_7_32\bin\javac" -Djava.ext.dirs=./lib %~nx1
IF EXIST %~n1.class (
ECHO ...... load jar %~dp1lib\...
ECHO ...... runjava %~n1.class
"C:\Program Files\Java\jdk1_7_32\bin\java" -Djava.ext.dirs=./lib %~n1
)