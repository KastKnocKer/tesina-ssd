echo off
cd src

echo -- JAVAC --
javac -d ../bin *.java

echo -- RMIC --
cd ..
cd bin
rmic ComputeEngine

copy * D:\Dropbox\Public\SSD
ECHO DONE

PAUSE >NUL