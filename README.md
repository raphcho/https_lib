Https Lib
=======
L'utilisation de cette librairie est très simple. Elle a pour but de faciliter la réalisations de requêtes Https.
En Android il est souvent très difficile d'effectuer une requêtes Https. Plus avec cette librairie ;-)

## Exécuter une requête get

```java
HttpsWorker.doGetToString(url);
``` 

## Exécuter une requête post
```java
HttpsWorker.doPostToString(url);
```
## Exécuter une requête put
```java
HttpsWorker.doPutToString(url);
```
## Exécuter une requête delete
```java
HttpsWorker.doDeleteToString(url);
```

## Ajouter des paramètres
```java
ArrayList<HttpsParameter> parameters = new ArrayList<HttpsParameter>();
HttpsWorker.doPostToString(url, parameters);
```
