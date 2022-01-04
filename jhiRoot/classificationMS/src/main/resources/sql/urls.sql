-- Combien d'éléments par table?
SELECT (
           SELECT COUNT(*)
           FROM url
       ) AS urls,
       (
           SELECT COUNT(*)
           FROM cronquist_rank
       ) AS cronquists;


-- Combien de doublons pour chaque url?
select count(*), url from url
group by url
order by count(*);
