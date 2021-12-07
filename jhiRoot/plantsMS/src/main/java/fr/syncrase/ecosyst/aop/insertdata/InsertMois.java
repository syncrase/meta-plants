package fr.syncrase.ecosyst.aop.insertdata;

import fr.syncrase.ecosyst.domain.Mois;
import fr.syncrase.ecosyst.repository.MoisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import java.util.Optional;

public class InsertMois {

    private final Logger log = LoggerFactory.getLogger(InsertMois.class);
    private MoisRepository moisRepository;

    public InsertMois(MoisRepository moisRepository) {
        this.moisRepository = moisRepository;
    }

    public void insertAllMois() {
        for (MoisEnum mois : MoisEnum.values()) {
            getOrInsertMois(mois.getMois());
        }
    }

    protected Mois getOrInsertMois(Mois mois) {
        if (!moisRepository.exists(Example.of(mois))) {
            moisRepository.save(mois);
        } else {
            Optional<Mois> returned = moisRepository.findOne(Example.of(mois));
            if (returned.isPresent()) {
                mois = returned.get();
                log.info("Existing mois : " + mois);
            } else {
                log.error("Unable to get instance of : " + mois);
            }
        }
        return mois;
    }
}
