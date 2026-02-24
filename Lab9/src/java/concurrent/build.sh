#!/bin/bash

BASE_DIR=$(dirname -- "$( readlink -f -- "$0"; )")

mkdir -p $BASE_DIR/bin

#javac -d $BASE_DIR/bin $BASE_DIR/*.java
#javac -d $BASE_DIR/bin $BASE_DIR/LogAnalyzer3.java
javac -d $BASE_DIR/bin $BASE_DIR/LogAnalyzer4.java
