#!/bin/bash

cd src
javac *.java

COMMANDONE="$1"
COMMANDTWO="$3"
CONFIDENCE=""
SUPPORT=""

if [[ "$COMMANDONE" == "-c" ]]
then
    CONFIDENCE=$2
elif [[ "$COMMANDONE" == "-s" ]]
then
    SUPPORT=$2
fi

if [[ "$COMMANDTWO" == "-c" ]]
then
    CONFIDENCE=$4
elif [[ "$COMMANDTWO" == "-s" ]]
then
    SUPPORT=$4
fi

clear

if [[ -n $CONFIDENCE ]] && [[ -n $SUPPORT ]]
then
    java Main "-s=$SUPPORT" "-c=$CONFIDENCE"

elif [[ -n $CONFIDENCE ]]
then
    java Main "-c=$CONFIDENCE"
elif [[ -n $SUPPORT ]]
then
    java Main "-s=$SUPPORT"
fi
