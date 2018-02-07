# Code Challenge
Search locations by text or dragging the pin on the map. <br/>

Save or delete favorite locations if you want.

## Architecture
- MVVM
  - This pattern was chosen because it isolates business logic on view model from view, so it's easier to create separated tests reducing dependence of each other.

## Requirements

- Android 16+

## Used libraries

- [Rx](https://github.com/ReactiveX/RxJava)
  - Architecture requirement for reactive apps with MVVM
- [Retrofit2](http://square.github.io/retrofit/)
  - Easier RESTful APIs implementations
- [Dagger2](https://google.github.io/dagger/)
  - The most loved dependency injection for Android
- [Android Arch Lifecycle](https://developer.android.com/topic/libraries/architecture/index.html)
  - Helper for View Model architecture
- [Mockito](http://site.mockito.org/), [PowerMock](https://github.com/powermock/powermock), [JUnit](http://junit.org/junit4/)
  - Mock and unit testing

### Observations

- Espresso tests are stubbed by dagger

### Thank you for your time and I would love some code feedback! o/