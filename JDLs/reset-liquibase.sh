#!/bin/bash
# Copy freshly generated liquibase files in the existing project

# Trouver tous les répertoires liquibase dans le projet existant et dans le fraichement généré

JHIROOT_PATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"/../jhiRoot
FRESH_GENERATION_PATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"/../../generate
#echo $JHIROOT_PATH
#cd $JHIROOT_PATH
find $JHIROOTPATH -name liquibase -type d  -not -path "*/node_modules/*"  -not -path "*/target/*"
# Pour chacun des répertoires => le supprimer

find $FRESH_GENERATION_PATH -name liquibase -type d  -not -path "*/node_modules/*"  -not -path "*/target/*"
# Pour chacun des répertoires => le copier dans les anciens path