# meta-plants

# Exécuter le projet en local
## Mode prod
Pour exécuter le projet en prod, exécuter la commande suivante lance tous les projets dans des containers
`sudo docker-compose -f docker-compose/docker-compose.prod.yml up -d`

## Mode dev
Pour exécuter les projets en dev, exécuter la commande suivante ne lance que les applications d'architecture
`sudo docker-compose -f docker-compose/docker-compose.yml up -d`

Les projets jhipster ainsi que leurs bases doivent être lancés séparément. Ce qui implique de modifier le port postgre de l'une des base.
`sudo docker-compose -f microservice/src/main/docker/postgre.yml up -d`
`./microservice/mvnw`
`sudo docker-compose -f gateway/src/main/docker/postgre.yml up -d`
`./gateway/mvnw`
