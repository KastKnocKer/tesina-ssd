echo off
cd src

echo -- JAVAC --
javac -d ../bin *.java

echo -- RMIC --
cd ..
cd bin
rmic Server
rmic RMI.SIP
rmic RMI.Client

copy * D:\Dropbox\Public\SSD
ECHO DONE

PAUSE >NUL