SELECT * from regne r
                  inner join sous_regne sr on r.id = sr.regne_id
                  inner join rameau rameau on sr.id = rameau.sous_regne_id
                  inner join infra_regne infraDiv on rameau.id = infraDiv.rameau_id
                  inner join super_division superDiv on infraDiv.id = superDiv.infra_regne_id
                  inner join division div on superDiv.id = div.super_division_id
                  inner join sous_division sousDiv on div.id = sousDiv.division_id
                  inner join infra_embranchement infraEmbran on sousDiv.id = infraEmbran.sous_division_id
                  inner join micro_embranchement me on infraEmbran.id = me.infra_embranchement_id
                  inner join super_classe superClasse on me.id = superClasse.micro_embranchement_id
                  inner join classe c on superClasse.id = c.super_classe_id
                  inner join sous_classe sousClasse on c.id = sousClasse.classe_id
                  inner join infra_classe ic on sousClasse.id = ic.sous_classe_id
                  inner join super_ordre superOrdre on ic.id = superOrdre.infra_classe_id
                  inner join ordre o on superOrdre.id = o.super_ordre_id
                  inner join sous_ordre sousOrdre on o.id = sousOrdre.ordre_id
                  inner join infra_ordre io on sousOrdre.id = io.sous_ordre_id
                  inner join micro_ordre mo on io.id = mo.infra_ordre_id
                  inner join super_famille superFamille on mo.id = superFamille.micro_ordre_id
                  inner join famille f on superFamille.id = f.super_famille_id
                  inner join sous_famille sousFamille on f.id = sousFamille.famille_id
                  inner join tribu t on sousFamille.id = t.sous_famille_id
                  inner join sous_tribu st on t.id = st.tribu_id
where st.nom_fr = 'Ptychospermatinae';


-- inner join genre g on st.id = g.sous_tribu_id
-- inner join sous_genre sg on g.id = sg.genre_id
-- inner join section s2 on sg.id = s2.sous_genre_id
-- inner join sous_section ss on s2.id = ss.section_id
-- inner join espece e on ss.id = e.sous_section_id
-- inner join sous_espece se on e.id = se.espece_id
-- inner join variete v on se.id = v.sous_espece_id
-- inner join sous_variete sv on v.id = sv.variete_id
-- inner join forme f2 on sv.id = f2.sous_variete_id
-- inner join sous_forme sf3 on f2.id = sf3.forme_id

