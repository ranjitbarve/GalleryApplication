# GalleryApplication Using Imgur gallery API
Create an application that searches for the top images of the week from
the Imgur gallery API and displays them in a list.

# GalleryApplication
[Download the APK](https://drive.google.com/file/d/1oUYCaofrlsM1eJiEWn-EBtMqBY3VHtsu/view?usp=sharing)

## Prerequisites
- Android Studio installed
- A physical Android device or an emulator

## Getting Started

1. **Clone the repository:**

    ```bash
    git clone https://github.com/ranjitbarve/GalleryApplication.git
    ```

2. **Open the Project in Android Studio:**

  - Open Android Studio.
  - Select `Open an existing Android Studio project`.
  - Navigate to the directory where you cloned the repository and select the `build.gradle` file.

3. **Build and Run:**

  - Connect your Android device to your computer or start an emulator.
  - Click on the green "Run" button in Android Studio or use the shortcut `Shift + F10` to build and run the application on your device or emulator.

## GalleryApplication Features
* Show Gallery images in a list and grid view
* Show image title, date time, images count
* Cache images in both memory and disk
* When clicking an image in the gallery - show its details: big image, title, description.
* Allow switching between list/grid view
* Hide/show the action bar when scrolling the list

## Usage

1. **Launch the App:**

  - Open the GalleryApplication on your Android device.

2. **Explore the Gallery:**

  - Navigate through the gallery to view images.
  - Perform actions such as view image in larger size.

## Technical Skills
* [Kotlin](https://kotlinlang.org/) + [Coroutines](https://developer.android.com/kotlin/coroutines) for performing background operations
* [Hilt](https://dagger.dev/hilt/) for dependency injection
* [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for infinte scroll
* [Jetpack](https://developer.android.com/jetpack)
    * [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) for in-App navigation
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) for notifying views about data change
    * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) for performing an action when lifecycle state changes
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) for storing and managing UI-related data in a lifecycle conscious way
* [Retrofit](https://square.github.io/retrofit/) for REST API communication
* [OkHttp](https://square.github.io/okhttp/) for http & logging
* [Glide](https://bumptech.github.io/glide/) for image loading and caching
* [Moshi](https://github.com/square/moshi) for parsing JSON into Java objects
* [Lottie](https://airbnb.design/lottie/) for animations
* [Timber](https://github.com/JakeWharton/timber) for logging
* Tests
    * [Unit Tests](https://en.wikipedia.org/wiki/Unit_testing) ([JUnit](https://junit.org/junit4/)) for writing repeatable tests
    * [MockK](https://mockk.io/) for mocking inside the Unit Test

## Architecture
Application is built on top of MVVM model and Clean Architecture. The code is written in Kotlin.
The goal is to present modern Android application architecture that is modular, scalable, maintainable and testable.

#### Presentation layer
This layer is closest to what the user sees on the screen. The `presentation` layer follows MVVM architecture.

#### Domain layer
This is the core layer of the application. Notice that the `domain` layer is independent of any other layers.
This allows to make domain models and business logic independent from other layers.

Components:
- **UseCase** - contains business logic
- **DomainModel** - defies the core structure of the data that will be used within the application. This is the source of truth for application data.
- **Repository interface** - required to keep the `domain` layer independent from the `data layer`

#### Data layer
Manages application data and exposes these data sources as repositories to the `domain` layer.
Typical responsibilities of this layer would be to retrieve data from the internet and optionally cache this data locally.

Components:
- **Repository** is exposing data to the `domain` layer. Depending on application structure and quality of the external APIs repository can also merge, filter, and transform the data. The intention of
  these operations is to create high-quality data source for the `domain` layer, not to perform any business logic (`domain` layer `use case` responsibility).

- **RetrofitService** - defines a set of API endpoints.
- **DataModel** - defines the structure of the data retrieved from the network and contains annotations, so Retrofit (Moshi) understands how to parse this network data (XML, JSON, Binary...) this data into objects.


## Build
You need to register the App on [Imgur](https://api.imgur.com/oauth2/addclient). After registering
you'll get a **Client ID** and a **Client Secret Key**.

Create a file named **secrets.properties** in root folder and copy the below in
```
CLIENT_ID = "*********"
CLIENT_SECRET = "**********"
```
and replace the xxxx with the Client ID and the Client Secret Key you got from registering at [Imgur](https://api.imgur.com/oauth2/addclient).

### Design decisions
* Architecture - MVVM separates your view (i.e. Activitys and Fragments) from your business logic.
  MVVM is enough for small projects, but when your codebase becomes huge, your ViewModels start bloating.
  Separating responsibilities becomes hard. MVVM with Clean Architecture is pretty good in such cases.
  It goes one step further in separating the responsibilities of your code base. It clearly abstracts
  the logic of the actions that can be performed in your app.

* Caching - Glide checks multiple layers of caches before starting a new request for an image.
  It first checks to see if the resource is in memory and if so, return the image immediately. In the second step it checks to see if the image is on disk and return quickly, but asynchronously.
  If above steps fail to find the image, then Glide will go back to the original source to retrieve the data (the original File, Uri, Url etc).

* Infinite loading & Error states handling - The Paging library helps you load and display pages of data from a larger dataset from local storage or over network and provides below features:
    * In-memory caching for your paged data.
    * Configurable RecyclerView adapters that automatically request data as the user scrolls toward the end of the loaded data.
    * Built-in support for error handling, including refresh and retry capabilities.

* Scroll behavior - CollapsingToolbarLayout is a wrapper for Toolbar which implements a collapsing app bar.

* Switching between list/grid view - By changing the LayoutManager a RecyclerView can be used to implement a standard vertically scrolling list, a uniform grid scrolling collections and more.

## Screens
![Screenshot_list](Screenshot_list.jpg)
![Screenshot_grid](Screenshot_grid.jpg)
![Screenshot_detail](Screenshot_detail.jpg)
[Screenshot_list](https://drive.google.com/file/d/1llta4W5JafLhD3dAhiXfqpT2nDai32VV/view?usp=sharing)
[Screenshot_grid](https://drive.google.com/file/d/1bCBQZltoRPDI0Dbzk4chTUNNVOT0r6a-/view?usp=sharing)
[Screenshot_detail](https://drive.google.com/file/d/1HTzhZEy4fWPqBzoMNaE7I3csAR0bwlLj/view?usp=sharing)