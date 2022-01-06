#!/bin/bash
# This script display the content of all entites directories
# Needs tree to be executed (sudo apt install tree)

echo -e "\033[0;32m____________________________________________________________________________"
echo -e "\033[0;32mVérification des entités de plantsMS"
echo -e "\033[0;32m____________________________________________________________________________"
tree -C plantsMS/src/main/java/fr/syncrase/ecosyst/domain/

echo -e "\033[0;32m____________________________________________________________________________"
echo -e "\033[0;32mVérification des entités de classificationMS"
echo -e "\033[0;32m____________________________________________________________________________"
tree -C classificationMS/src/main/java/fr/syncrase/ecosyst/domain/

echo -e "\033[0;32m____________________________________________________________________________"
echo -e "\033[0;32mVérification des entités de plantsFront"
echo -e "\033[0;32m____________________________________________________________________________"
tree -d -L 2 plantsFront/src/main/webapp/app/entities/

echo -e "\033[0;32m____________________________________________________________________________"
echo -e "\033[0;32mVérification des entités java de plantsFront (doit être vide)"
echo -e "\033[0;32m____________________________________________________________________________"
tree plantsFront/src/main/java/fr/syncrase/ecosyst/domain/