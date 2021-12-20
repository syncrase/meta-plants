SELECT *
from cronquist_rank superregne
         inner join cronquist_rank regne on superregne.id = regne.parent_id
         inner join cronquist_rank sousregne on regne.id = sousregne.parent_id
         inner join cronquist_rank rameau on sousregne.id = rameau.parent_id
         inner join cronquist_rank infraregne on rameau.id = infraregne.parent_id
         inner join cronquist_rank superembranchement on infraregne.id = superembranchement.parent_id
         inner join cronquist_rank embranchement on superembranchement.id = embranchement.parent_id
         inner join cronquist_rank sousembranchement on embranchement.id = sousembranchement.parent_id
         inner join cronquist_rank infraembranchement on sousembranchement.id = infraembranchement.parent_id
         inner join cronquist_rank microembranchement on infraembranchement.id = microembranchement.parent_id
         inner join cronquist_rank superclasse on microembranchement.id = superclasse.parent_id
         inner join cronquist_rank classe on superclasse.id = classe.parent_id
         inner join cronquist_rank sousclasse on classe.id = sousclasse.parent_id
         inner join cronquist_rank infraclasse on sousclasse.id = infraclasse.parent_id
         inner join cronquist_rank superordre on infraclasse.id = superordre.parent_id
         inner join cronquist_rank ordre on superordre.id = ordre.parent_id
         inner join cronquist_rank sousordre on ordre.id = sousordre.parent_id
         inner join cronquist_rank infraordre on sousordre.id = infraordre.parent_id
         inner join cronquist_rank microordre on infraordre.id = microordre.parent_id
         inner join cronquist_rank superfamille on microordre.id = superfamille.parent_id
         inner join cronquist_rank famille on superfamille.id = famille.parent_id
         inner join cronquist_rank sousfamille on famille.id = sousfamille.parent_id
         inner join cronquist_rank tribu on sousfamille.id = tribu.parent_id
         inner join cronquist_rank soustribu on tribu.id = soustribu.parent_id
where soustribu.nom_fr = 'Ptychospermatinae';
