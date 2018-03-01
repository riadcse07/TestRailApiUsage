#!/bin/bash

echo $@

${jvmargs[@]}

DIR=$(dirname $0)

args=( "$@" )
javaProps=( )
mainclass=TestrailMain.Main
server_jvmargs=( -Djava.awt.headless=true -Xms1024m -Xmx1024m "${jvmargs[@]}" )
XX_HOME="$DIR"
chmod -R 777 "$XX_HOME/"

client_classpath="$XX_HOME/lib/*:$XX_HOME/bin"

BIN_PATH="$XX_HOME/bin"
SRC_PATH="$XX_HOME/src"

rm -rfv "$BIN_PATH"
mkdir -p "$BIN_PATH"

cd $DIR
	
javac \
	-cp "$client_classpath" \
	-d "$BIN_PATH" \
	-sourcepath $SRC_PATH src/TestrailMain/Main.java
		
javac \
	-cp "$client_classpath" \
	-d "$BIN_PATH" \
	-sourcepath $SRC_PATH src/util/*.java
	
java \
  "${server_jvmargs[@]}" \
  "${javaProps[@]}" \
  -Dxx.home="$XX_HOME" \
  -Duser.dir="$XX_HOME" \
  -cp "$client_classpath" \
  "$mainclass" $DIR $1 $2