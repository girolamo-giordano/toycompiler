set arg1=%1
set arg2=%2
java -jar C:\CUP\java-cup-11b.jar %arg1% -locations -interface -parser Parser -destdir src srcjflexcup/toy.cup %arg2%
