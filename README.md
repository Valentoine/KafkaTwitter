# KafkaTwitter

## Introduction :

Le but de ce projet est de découvrir Kafka et son application en programation. Il était nécessaire d'avoir une source de données, 
nous avons choisis Twitter. Ensuite nous avons créer un producer (qui crée le premier topic), un Kafka Stream (qui récupère le premier
topic et en crée un deuxième topic avec une sélection) et enfin un consumer (qui peut consommer les deux topics).

#### Arborescence et ordre de lancement :

Touts les fichiers Java sont dans le src/main/java, nous avons séparer les 3 entités en 3 package : Broker, Consumer et Kstream. Les fichiers
à runner (contiennent un main) sont Broker/starter, Consummer/AppConsumer et KStream/AppStream.

Deux procédures sont à lancer (dans l'ordre) :
- Premier topic : lancer le producer (starter) et le consumer (AppConsumer) en veillant à changer le topicName par "Macron".
- Deux topics : lancer le producer (starter), le Kstream (AppStream) et le consumer (AppConsumer) en veillant à changer le topicName par
"Macron_Actu".

## Quelles données et topics ?

Nous utilisons l'API Twitter qui nous permet de récupérer un stream en temps réel de tweets. Nous avons donc créer deux topics, 
le premier topic principal est sur Macron, il récupère tout les tweets où Macron est mentionné dans le texte (User, Text, IsRetweet, Date).
Et ensuite le deuxième topic (produit par le KStream) est une sous-catégorie du premier, ou l'on réalise une condition : si ca parle de gilets
jaunes, de la retraite ou autre, on le mentionne et l'on sélectionne uniquement le texte du tweet.

## Explication du code 

#### Producer :

On a repris le code
Ce qui a changé c'est le collector : twitter Api (pas de schedule repetor car c'est un stream)
Format de données JSON (clé : Id tweet, value : user,text, etc...)

#### Consumer :

On a repris le code, on a rien changé

#### KStream

On a repris le code mais on a eu beaucoup d'erreurs et de problème.
Ca prend un stream en input (celui du producer), on parse en json et on récupère le texte
On fait des tests (if, else , etc...)
On créer un kafkaStream en output (on garde la même clé qu'avant, mais on change la value qu'avec le texte + Gilets Jaunes : .... ou 
Autre ....)


