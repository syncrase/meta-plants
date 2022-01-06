
-- récupère une classification complète à partir de l'élément le plus profond
with recursive tr(from_id, to_id, level, rank, nom) as (
    select cronRank.id, cronRank.parent_id, 1, cronRank.rank, cn.nom_fr as level
    from cronquist_rank cronRank
             inner join classification_nom cn on cronRank.id = cn.cronquist_rank_id
    where cn.nom_fr = 'Corylopsis'
    union all
    select cronRank.id, cronRank.parent_id, tr.level + 1, cronRank.rank, cn.nom_fr
    from cronquist_rank cronRank
             inner join classification_nom cn on cronRank.id = cn.cronquist_rank_id
             join tr on cronRank.id = tr.to_id
)
select *
from tr;

