#!/bin/bash
# This script will import the full_application_MERGED.jdl in the directory where this script is called

#echo launch from : `pwd`
#
#echo $0
#
SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
#echo $SCRIPTPATH

jhipster jdl $SCRIPTPATH/full_application_MERGED.jdl --skip-fake-data --force