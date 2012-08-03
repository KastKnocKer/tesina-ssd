echo off
echo server
cd bin
java -Djava.rmi.server.codebase=file:./ Server

PAUSE > NUL