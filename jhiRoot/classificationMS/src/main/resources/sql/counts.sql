
-- taxons par rang
SELECT rank, count(*)
from cronquist_rank
group by rank
order by count(*);

-- Combien de rangs avec des taxons en double?
select count(*)
from (SELECT rank, count(*)
      from cronquist_rank
      group by rank
     ) as foo
where foo.count > 1;


-- combien d'éléments par table ?
select (select count(*)
        from cronquist_rank
       )                         as ranks,
       (select count(*)
        from classification_nom) as noms,
       (select count(*)
        from url)                as urls;
