#!/bin/bash
baseDir=$(dirname $0)

# Load env variables.

source $baseDir/env.sh

if [ $# -eq 0 ]; then
	echo 'Main entry shell file is required!'
	exit
fi

. $1