-- Requête pour obtenir tous les rang intermédiaires superflus (après un merge de branches, la branche intermédiaire ne sert plus)
select cte.from_id
from (
         with recursive tr(from_id, to_id, level, rank, nom) as (
             -- Non recursive term : tous les rangs intermédiaires sans enfant
             select cronRank.id, cronRank.parent_id, 1, cronRank.rank, cn.nom_fr as level
             from cronquist_rank cronRank
                      inner join classification_nom cn on cronRank.id = cn.cronquist_rank_id
             where cronRank.id not in (
                 select parent_id
                 from cronquist_rank
                 where parent_id is not null
             )
               and cn.nom_fr is null
             union all
             -- terme recursif : tous les rangs intermédiaires supérieurs
             select t.id, t.parent_id, tr.level + 1, t.rank, cn.nom_fr
             from cronquist_rank t
                      inner join classification_nom cn on t.id = cn.cronquist_rank_id
                      join tr on t.id = tr.to_id
             where cn.nom_fr is null
         )
         select *
         from tr
     ) as cte;


-- Liste les rangs corrompus (sans getRangSuperieur)
SELECT *
from cronquist_rank
where parent_id is null
  and rank <> 'SUPERREGNE';
