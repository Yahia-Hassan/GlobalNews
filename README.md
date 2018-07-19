# Global News
Global News is an Android app that shows you top headlines from the US, Canada, UK, France, Sweden and Egypt, it uses the [News API](https://newsapi.org/)

You have to create two files first,

1- gradle.properties and add this in it:

API_KEY="Your NewsAPI Key"

GLOBAL_NEWS_PLACES_API_KEY="Your Google Places SDK for Android Key", You can signup for an API Key [here.](https://developers.google.com/places/android-sdk/signup)



2- keystore.properties and add this in it:

storePassword=myStorePassword

keyPassword=mykeyPassword

keyAlias=myKeyAlias

storeFile=myStoreFileLocation