package ru.hotmule.lastik.data.remote

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.hotmule.lastik.data.prefs.prefsDataModule
import ru.hotmule.lastik.data.remote.api.AuthApi
import ru.hotmule.lastik.data.remote.api.TrackApi
import ru.hotmule.lastik.data.remote.api.UserApi

val remoteDataModule = DI.Module("remoteData") {

    importOnce(prefsDataModule)

    bindSingleton { Credentials() }
    bindSingleton { EngineFactory(di) }
    bindSingleton { LastikHttpClientFactory(instance(), instance(), instance()).create() }

    bindSingleton { AuthApi(instance(), instance()) }
    bindSingleton { UserApi(instance(), instance(), instance()) }
    bindSingleton { TrackApi(instance(), instance(), instance()) }
}