#!/bin/bash
export CLASSPATH=`find ./lib -name "*.jar" | tr '\n' ':'`
export MAINCLASS=Tests
java -cp $CLASSPATH:classes $MAINCLASS
