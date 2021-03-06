package com.duskencodings.autologs.views.cars.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.Single
import com.duskencodings.autologs.base.BaseViewModel
import com.duskencodings.autologs.extensions.applySchedulers
import com.duskencodings.autologs.log.Logger
import com.duskencodings.autologs.models.Car
import com.duskencodings.autologs.models.Resource
import com.duskencodings.autologs.repo.CarRepository
import javax.inject.Inject

class CarsViewModel @Inject constructor(private val repo: CarRepository) : BaseViewModel() {

  var state: MutableLiveData<Resource<List<Car>>> = MutableLiveData()
  private lateinit var carsLiveData: LiveData<List<Car>>
  private lateinit var carsObserver: Observer<List<Car>>

  lateinit var adapter: CarAdapter

  fun getCars() {
    carsLiveData = repo.getCars()
    carsObserver = Observer {
      Observable.just(it)
          .applySchedulers()
          .map { cars ->
            cars.sortedByDescending { car -> car.year }
          }
          .doOnSubscribe { state.value = Resource.loading() }
          .doAfterTerminate { state.value = Resource.idle() }
          .subscribe({ cars ->
            state.value = Resource.success(cars)
          }, { error ->
            state.value = Resource.error(error)
          })
    }

    carsLiveData.observeForever(carsObserver)
  }

  fun deleteCar(car: Car) {
    addSub(
      repo.deleteCar(car)
        .applySchedulers()
        .doOnSubscribe { state.value = Resource.loading() }
        .doAfterTerminate { state.value = Resource.idle() }
        .subscribe({
          // do nothing here, car will disappear
        }, {
          Logger.e("CAR DELETE", "Failed to delete car", it)
          state.value = Resource.error(RuntimeException("Unable to delete car at this time"))
        })
    )
  }
}