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
- [Retrofit2](http://square.github.io/retrofit/)
- [Dagger2](https://google.github.io/dagger/)
- [Android Arch Lifecycle](https://developer.android.com/topic/libraries/architecture/index.html)
- [Mockito](http://site.mockito.org/)
- [JUnit](http://junit.org/junit4/)
- [Espresso](https://developer.android.com/training/testing/ui-testing/espresso-testing.html)


### Observations

- Espresso tests are stubbed by dagger

### Thank you for your time and I would love some code feedback! o/