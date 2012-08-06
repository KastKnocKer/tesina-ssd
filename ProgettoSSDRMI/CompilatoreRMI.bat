echo off
cd src

echo -- JAVAC --
javac -d ../bin *.java

echo -- RMIC --
cd ..
cd bin
rmic Server
rmic Hello

copy * D:\Dropbox\Public\SSD
ECHO DONE

PAUSE >NUL