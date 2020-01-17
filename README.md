# KafkaTwitter

## Introduction :

Le but de ce projet est de découvrir Kafka et son application en programation. Il était nécessaire d'avoir une source de données, 
nous avons choisis Twitter. Ensuite nous avons créé un producer (qui crée le premier topic), un Kafka Stream (qui récupère le premier
topic et en crée un deuxième avec une sélection) et enfin, un consumer (qui peut consommer les deux topics).

#### Arborescence et ordre de lancement :

Touts les fichiers Java sont dans le src/main/java, nous avons séparé les 3 entités en 3 package : Broker, Consumer et Kstream. Les fichiers
à runner (contenant un main) sont Broker/starter, Consummer/AppConsumer et KStream/AppStream.

Deux procédures sont à lancer (dans l'ordre) :
- Premier topic : lancer le producer (starter) et le consumer (AppConsumer) en veillant à changer le topicName par "Macron".
- Deux topics : lancer le producer (starter), le Kstream (AppStream) et le consumer (AppConsumer) en veillant à changer le topicName par
"Macron_Actu".

## Quelles données et topics ?

Nous utilisons l'API Twitter qui nous permet de récupérer un stream en temps réel de tweets. Nous avons donc créé deux topics, 
le premier topic, qui est le topic principal est sur Macron, il récupère tous les tweets où Macron est mentionné dans le texte (User, Text, IsRetweet, Date).
Ensuite, le deuxième topic (produit par le KStream) est une sous-catégorie du premier, où l'on réalise une condition : si ça parle de gilets jaunes, de la retraite ou autre, on le mentionne et l'on sélectionne uniquement le texte du tweet.

## Choix d'implémentation du code

Au moment de collecter les tweets, on veille à ne prendre que ces informations: Texte, User, la date du tweet, si c'est un retweet et la tweetKey. 
On publie dans notre premier topic sur ce format:

```python
publisher.publish(keyTweet,"{\"User\":\""+user+"\", \"Text\":\""+text+"\", \"Is Retweet\":\""+isRetweet+"\", \"Date\":\""+dateTweet+"\"}");

```
Note: Il a fallu faire attention à ne pas insérer de guillemets dans les textes des tweets.


Au niveau de la topology qui lit sur le premier topic pour publier sur le deuxième, les données sont filtrées.

On récupère le contenu du premier topic sous forme de String. Celui-ci est converti en JSON grace à la fonction:

```python
jsonNode = mapper.readTree(jsonRecordString);
```
On accède à l'element texte du tweet de cette façon:

```python
jsonNode.get("Text");
```

On met alors des conditions pour prendre uniquement les informations qui nous interessent pour les mettre dans le nouveau topic:

```python

 if(fieldsMode.asText().contains("retraite") || fieldsMode.asText().contains("retraites") || fieldsMode.asText().contains("Retraite")){
            return "Réforme de la retraite :"+ fieldsMode.asText() ;
        }
        else if(fieldsMode.asText().contains("jaune") || fieldsMode.asText().contains("gilet jaune") || fieldsMode.asText().contains("gilets jaunes") ){

            return " Gilets jaunes :"+ fieldsMode.asText() ;
        }
        else{
            return "Autres infos : " + fieldsMode.asText();
        }
```


#### Producer :

On a repris le code
Ce qui a changé c'est le collector : twitter Api (pas de schedule repetor car c'est un stream)
Format de données JSON (clé : Id tweet, value : user,text, etc...)

#### Consumer :

On a repris le code, on a rien changé

#### KStream

On a repris le code mais on a eu beaucoup d'erreurs et de problèmes.
Ca prend un stream en input (celui du producer), on parse en json et on récupère le texte
On fait des tests (if, else , etc...)
On crée un kafkaStream en output (on garde la même clé qu'avant, mais on change la value qu'avec le texte + Gilets Jaunes : .... ou 
Autre ....)


