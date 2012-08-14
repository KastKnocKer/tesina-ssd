echo off
cd src

echo -- JAVAC --
echo commented!
javac -d ../bin *.java

echo -- RMIC --
cd ..
cd bin
rmic test.Server
rmic RMI.SIP
rmic RMI.Client

echo -- COPYING -- (compulsory for CodeBase!)
echo copying for Kast...
echo commented!
:: copy * D:\Dropbox\Public\SSD
echo -------------
echo copying for Fabio Vaio...
copy * C:\Users\Fabio\Dropbox\Public\SSD
echo -------------
ECHO DONE

PAUSE >NUL