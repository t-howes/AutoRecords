package sample.thowes.autoservice.repo

import android.content.Context
import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import sample.thowes.autoservice.base.BaseRepository
import sample.thowes.autoservice.database.CarWorkDb
import sample.thowes.autoservice.models.CarWork

class CarWorkRepository(context: Context,
                        private val carWorkDb: CarWorkDb) : BaseRepository(context) {

  fun getLiveCarWorkList(carId: Int): LiveData<List<CarWork>> {
    return carWorkDb.getLiveCarWorkList(carId)
  }

  fun getCarWork(id: Int): Single<CarWork> {
    return carWorkDb.getCarWork(id)
  }

  fun saveCarWork(carWork: CarWork): Completable {
    return Completable.fromCallable {
      carWorkDb.insertOrUpdateCarWork(carWork)
    }.subscribeOn(Schedulers.io())
  }

}