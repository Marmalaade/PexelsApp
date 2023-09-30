
<div id="top"></div>

<br />
<div align="center">
  <a>
<img src="assets/Drawable/app_icon.png" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">PexelsApp</h3>
  
</div>

<div style="height: 2px; background-color: #000; position: relative;">
  <div style="width: 100%; height: 100%; background-color: #f00; position: absolute; animation: expand 2s ease-in-out infinite;"></div>
</div>

## Тестовое задание


Задание представляет собой мобильное приложение, аналогичное Pinterest, с использованием PixelApi. Приложение включает в себя три основных экрана с поддержкой навигации и функционалом для отображения разнообразных фотографий. Отображение изображений осуществляется как автоматически, так и в результате пользовательского поиска. Пользователи имеют возможность просматривать изображения в более детальном режиме, а также могут скачивать и добавлять их в закладки. Присутствует поддержка тёмной темы и локализации.

При разработке приложения предусмотрен функционал для выполнения сетевых запросов к PixelApi, механизм хранения информации в базе данных, кэширование данных, а также обработка ошибок, связанных с отсутствием интернет-соединения и отсутствием необходимой информации.

### Список основных используемых технологий


|                         |                                |
|------------------------|-----------------------------------|
| Language               | Kotlin                            |
| Architecture           | Single Activity, Clean Architecture, MVVM |
| Navigation             | Navigation Component              |
| Dependency Injection   | Hilt                      |
| Networking             | Retrofit                          |
| Concurrency            | RxJava                            |
| Storage                | Room                              |
| Image Loading          | Glide                             |
| Splash Screen          | Splash Screen API     |
| Caching                | Retrofit/Glide caching

### Демонстрация проекта
---

* Splash Screen

<div align="left">



  <img src="assets/Videos/splashscreen.gif" width="45%"  />
  </p>

  * Home Fragment

  <p>
    <img src="assets/Screen/homefragment1.png"width="35%" height="35%" >
    <img src="assets/Screen/homefragment2.png"width="35%" height="35%" >
    <img src="assets/Screen/homefragment3.png"width="35%" height="35%" >
      <img src="assets/Screen/homefragment4.png"width="35%" height="35%" >    
  </p>

   * Details Fragment

   <div align="left">
<img src="assets/Videos/detailsfragment.gif" width="45%"  />
  </p>


 <p>
    <img src="assets/Screen/detailsfragment1.png"width="35%" height="35%" >
    <img src="assets/Screen/detailsfragment2.png"width="35%" height="35%" >
    <img src="assets/Screen/detailsfragment3.png"width="35%" height="35%" >
      <img src="assets/Screen/detailsfragment4.png"width="35%" height="35%" >    
  </p>
  </div>

  * Bookmarks Fragment

   <div align="left">
<img src="assets/Videos/bookmarksfragment.gif" width="45%"  />
  </p>


 <p>
    <img src="assets/Screen/bookmarksfragment2.png"width="35%" height="35%" >
    <img src="assets/Screen/bookmarksfragment1.png"width="35%" height="35%" >  
  </p>
  </div>

  ### Тёмная тема
---

<div align="left">

 <p>
  <img src="assets/Screen/dark1.png"width="30%" height="30%" >
  <img src="assets/Screen/dark2.png"width="30%" height="30%" >
  <img src="assets/Screen/dark3.png"width="30%" height="30%" >  
  </p>
  </div>
  
  ----
  Приложение протестировано на эмуляторе   Android Studio (Google Pixel 4).

  ----
  Также проводилось тестирование на реальном устройстве.

  ----
  ### Тестирование на реальном устройстве
---

<div align="left">

 <p>
  <img src="assets/Screen/realphone1.jpg"width="30%" height="30%" >
  <img src="assets/Screen/realphone2.jpg"width="30%" height="30%" >
  <img src="assets/Screen/realphone3.jpg"width="30%" height="30%" >  
  </p>
  </div>
  
  ### Возможные улучшения
  ----
  * Разделение репозитория по назначению реализуемых задач.
  * Декомпозирование файла конфигураций отдельно для каждого слоя.
  * Написание логики тестирования приложения.
  * Оптимизация функционала.
  * ...



<div style="height: 2px; background-color: #000; position: relative;">
  <div style="width: 100%; height: 100%; background-color: #f00; position: absolute; animation: expand 2s ease-in-out infinite;"></div>
</div>
