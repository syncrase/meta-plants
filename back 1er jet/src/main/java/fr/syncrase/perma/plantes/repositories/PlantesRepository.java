package fr.syncrase.perma.plantes.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import fr.syncrase.perma.plantes.modeles.Plante;

public interface PlantesRepository extends MongoRepository<Plante, String> {

	public Plante findByNom(String nom);
//	  public List<Plantes> findByLastName(String lastName);

}
