# Evaluacion-UPAX

## Requisitos
1. Consumir un servicio API Rest ([https://www.themoviedb.org/documentation/api](https://www.themoviedb.org/documentation/api))
2. Persistir los datos que devuelva el servicio de manera local y mostrar los datos al usuario con una UI amigable e intuitiva, dichos datos deben estar disponibles cuando el dispositivo móvil no tenga conexión a Internet (modo offline).
3. Agregar la ubicación del dispositivo a una consola de Firebase para persistir (CloudFirestore) cada 5 minutos y notificar al usuario mediante NotificationCompat.
4. Consumir desde la consola de Firebase (Cloud Firestore) y mostrar las ubicaciones en un Mapa mostrando adicionalmente la fecha de almacenamiento usando una UI amigable e intuitiva.
5. Capturar o seleccionar de la galería del dispositivo una o varias imágenes y subirlas a Firebase Storage.

## Arquitectura
![](https://github.com/LeonardoMorales/Evaluacion-UPAX/blob/master/art/architecture.jpg)

## Librerías
+ Jetpack
    + Android KTX
    + AndroidX
    + ViewBinding
    + LiveData
    + ViewModel
    + Navigation
    + Room
    + Hilt
+ Coroutines
+ Retrofit
+ Coil
+ Firebase
+ Google Play Services
+ Maps
