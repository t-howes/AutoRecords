package sample.thowes.autoservice.views.cars.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.Single
import sample.thowes.autoservice.base.BaseViewModel
import sample.thowes.autoservice.extensions.applySchedulers
import sample.thowes.autoservice.log.Logger
import sample.thowes.autoservice.models.Car
import sample.thowes.autoservice.models.Resource
import sample.thowes.autoservice.repo.CarRepository
import javax.inject.Inject

class CarsViewModel @Inject constructor(private val repo: CarRepository) : BaseViewModel() {

  var state: MutableLiveData<Resource<List<Car>>> = MutableLiveData()
  private lateinit var carsLiveData: LiveData<List<Car>>
  private lateinit var carsObserver: Observer<List<Car>>

  fun getCars() {
    carsLiveData = repo.getCars()
    carsObserver = Observer {
      Single.just(it)
          .applySchedulers()
          .flatMap { cars ->
            Single.just(cars.sortedByDescending { car -> car.year })
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
          Logger.e("CAR DELETE", "Failed to delete car: ${it.localizedMessage}")
          state.value = Resource.error(RuntimeException("Unable to delete car at this time"))
        })
    )
  }
}