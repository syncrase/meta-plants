
-- récupère une classification complète à partir de l'élément le plus profond
with recursive tr(from_id, to_id, level, rank, nom) as (
    select cronRank.id, cronRank.parent_id, 1, cronRank.rank, cronRank.nom_fr as level
    from cronquist_rank cronRank
    where cronRank.nom_fr = 'Anisoptera'
    union all
    select t.id, t.parent_id, tr.level + 1, t.rank, t.nom_fr
    from cronquist_rank t
             join tr on t.id = tr.to_id
)
select *
from tr;

-- Liste les rangs corrompus (sans parent)
SELECT *
from cronquist_rank
where parent_id is null
  and rank <> 'SUPERREGNE';
