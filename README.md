# Activities
## PhotoGalleryActivity
* Uses constraint layout to show its simplicity but in reality that screen is so simple that the full benefits of a flat layout are not leveraged (It is not computationally expensive).
* Uses the traditional MVC pattern which, in my opinion, decreases testability. Application logic is easily mixed with UI code.
* Since the list of pictures might be large, a RecyclerView has been chosen to display thumbnails.

## PhotoActivity
* Uses a more traditional layout.
* Leverages data binding to implement the MVVM pattern. I am a strong advocate of this pattern due to its test friendliness, clear division of UI code and business logic 
and very concise code to support UI updates. 

# 3rd Party Components
* Retrofit - HTTP client that allows a simple interface to define endpoints. 
* okhttp - Highly flexible HTTP client. It also includes WebSocket support.
* Butterknife - Provides a clean way to access view components.

# Improvements
## Tests
* Dependency injection provides higher testability since it provides a loosely coupled design.
* Mock network to avoid false positives dues to network instability.
* Rely on mockito and espresso.
## UI
* The Grid view should adapt its number of columns to the screen size (including tablets) and to landscape mode.
## Additional recommended 3rd party components
* RxJava - Provides a powerful observable pattern framework.
* Realm - Cloud Data for android. It greatly simplifyes data management support .
* Fresco - Efficient image caching.
* Dagger 2 - Dependency injection framework.
# Bugs
* The photo fetcher is blindly adding the same query parameters to all requests even when they are not needed.
