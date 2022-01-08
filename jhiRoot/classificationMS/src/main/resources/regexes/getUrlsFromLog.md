# Reproduction de la suite de scrap effectué
À partir du fichier de log on extrait toutes les url qui ont été extraites pour reproduire une éventuelle erreur

*Match les urls que je veux garder*

\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3}\s+\w+ \d{2,6} --- \[\s+restartedMain\]\s+[\w\.]+\s+: Get classification from : https:\/\/fr\.wikipedia\.org\/wiki\/\S+
\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3}\s+\w+ \d{2,6} --- \[\s+restartedMain\]\s+[\w\.]+\s+: ((Created Cronquist classification .*)|(Le taxon .*))

# Ne garder que les lignes qui contiennent les urls que je souhaite
Inverse l'expression pour supprimer tout ce qui n'est pas une url dont a été extrait une classification de cronquist
^(?:(?!\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3}\s+\w+ \d{2,6} --- \[\s+restartedMain\]\s+[\w\.]+\s+: Get classification from : (https:\/\/fr\.wikipedia\.org\/wiki\/\S+)\n\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3}\s+\w+ \d{2,6} --- \[\s+restartedMain\]\s+[\w\.]+\s+: ((Created Cronquist classification .*)|(Le taxon .*))).)+$\n

Remplacer par rien

# Transformer en code java qui peut être copier/coller directement
Ensuite, récupérer l'url de chaque ligne
\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}\.\d{3}\s+\w+ \d{2,6} --- \[\s+restartedMain\]\s+[\w\.]+\s+: Get classification from : (https:\/\/fr\.wikipedia\.org\/wiki\/\S+)

Et la remplacer par la ligne java
scrapWiki("$1");

