package fr.syncrase.perma.plantes.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import fr.syncrase.perma.plantes.modeles.Plante;

//@RepositoryRestResource(collectionResourceRel = "plantes", path = "plantes")
public interface PlantesRepositoryREST extends MongoRepository<Plante, String> {

	List<Plante> findByNom(@Param("name") String name);
}
