# meta-plants

 - The jhiRoot folder is where Jhipster projects must be generated
 - The integration folder contains some files to be integrated to the project
 - The JDLs folder contains all JDLs to re-run jhipster

# Run the project

## Run locally

Projects infrastructure :
 - The jhipster-registry must be running 
 - PostGreSQL must be running
 - Users and databases must exists in PostGreSQL
 - Then, projects can be launched

### Run the registry
The registry can be executed in two ways :
 - Compiling the source code and run it locally (this line must be added to /etc/hosts : 127.0.0.1	jhipster-registry)
 - Preferred way, launch a registry image in docker container

After cloning the registry source code, run the following command :
`docker-compose -f src/main/docker/jhipster-registry.yml up`

### Run PostGre

 - check if a cluster is running `pg_lsclusters`

### Users and databases in PostGreSQL

 - Access PostGre with postgres user : `$ sudo -i -u postgres`
 - Connect to PostGre server : `$ psql`
 - Create users (role and password) and databases for each project : plantsFront & plantsMS
 ```
CREATE USER plantsfront;
CREATE USER plantsms;
ALTER ROLE plantsfront WITH PASSWORD 'plantsfront';
ALTER ROLE plantsms WITH PASSWORD 'plantsms';
ALTER ROLE plantsfront WITH CREATEDB;
ALTER ROLE plantsms WITH CREATEDB;
CREATE DATABASE plantsfront;
CREATE DATABASE plantsms;
 ```

### Run project locally
For the backend, just run `sudo ./mvnw`.
For the front, first run `sudo ./mvnw` and then `npm start`

## Run with Docker
Build images with `./mvnw -Pprod verify jib:dockerBuild`
Run `docker-compose up -d` in the jhiRoot/docker-compose directory