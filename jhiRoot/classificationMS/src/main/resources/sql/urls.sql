-- Combien de doublons pour chaque url?
select *
from (
         select count(*), url
         from url
         group by url
         order by count(*)) as foo
where foo.count > 1;

-- Toutes les urls dupliquées
select *
from url
where url.url = (
    select url
    from (
             select count(*), url
             from url
             group by url
             order by count(*)) as foo
    where foo.count > 1);
