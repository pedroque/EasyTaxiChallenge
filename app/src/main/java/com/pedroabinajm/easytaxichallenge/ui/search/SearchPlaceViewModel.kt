package com.pedroabinajm.easytaxichallenge.ui.search

import android.arch.lifecycle.MutableLiveData
import com.pedroabinajm.easytaxichallenge.data.location.PlaceAutocompleteProvider
import com.pedroabinajm.easytaxichallenge.data.model.EasyPlace
import com.pedroabinajm.easytaxichallenge.schedulers.ISchedulerProvider
import com.pedroabinajm.easytaxichallenge.ui.base.BaseViewModel
import com.pedroabinajm.easytaxichallenge.ui.commons.Resource
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.TimeUnit


class SearchPlaceViewModel constructor(
        val placeAutocompleteProvider: PlaceAutocompleteProvider,
        private val debounce: Long,
        provider: ISchedulerProvider
) : BaseViewModel(provider) {

    private var observableQuery: Subject<String> = BehaviorSubject.create()

    val places = MutableLiveData<Resource<List<EasyPlace>>>()
    val place = MutableLiveData<Resource<EasyPlace>>()

    var query = ""
        set(value) {
            field = value
            observableQuery.onNext(value)
        }


    init {
        setQuery()
    }

    private fun setQuery(): Observable<List<EasyPlace>> {
        return execute(observableQuery.debounce(debounce, TimeUnit.MILLISECONDS)
                .flatMap { query ->
                    places.postValue(Resource.loading(null))
                    if (query.isBlank()) {
                        return@flatMap Observable.just(mutableListOf<EasyPlace>())
                    } else {
                        return@flatMap placeAutocompleteProvider.getPlaces(query)
                    }
                }, {
            places.postValue(Resource.success(it))
        }, {
            places.postValue(Resource.error(it))
            setQuery()
        })
    }

    fun getLatLng(place: EasyPlace): Observable<EasyPlace> {
        this.place.postValue(Resource.loading(place))
        return execute(placeAutocompleteProvider.getLatLang(place).map {
            place.latitude = it.latitude
            place.longitude = it.longitude
            place
        }, {
            this.place.postValue(Resource.success(it))
        }, {
            this.place.postValue(Resource.error(it, place))
        })
    }
}