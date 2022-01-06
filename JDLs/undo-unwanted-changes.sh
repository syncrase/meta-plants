#!/bin/bash
# Undo certains changements du re-import du jdl
# À ne pas exécuter lors des changement d'infra (changement de pom par exemple)

for file in pom.xml angular.json application-dev.yml app.module.ts navbar.component.html
do
    #clear
    echo *$file
	git diff *$file
	# TODO check if empty string. If empty, continue to the next file
	echo "Do you wish to undo these changes?"
	select yn in "Yes" "No"; do
    	case $yn in
        	Yes ) 
				git checkout *$file; 
				break;;
        	No ) break;;
    	esac
	done
done
