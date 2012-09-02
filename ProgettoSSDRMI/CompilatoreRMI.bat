echo off
cd src

echo -- Kill rmid, rmiregistry --
:: killo rmid ed rmiregistry, perchè altrimenti rimarrebbero attivi
TASKKILL /F /IM "rmid.exe"
TASKKILL /F /IM "rmiregistry.exe"


echo -- JAVAC --
:: echo commented!
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