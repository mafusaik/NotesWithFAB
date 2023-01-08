package by.homework.hlazarseni.noteswithfab

import android.app.Application
import by.homework.hlazarseni.noteswithfab.di.apiModule
import by.homework.hlazarseni.noteswithfab.di.databaseModule
import by.homework.hlazarseni.noteswithfab.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin

class NoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            fragmentFactory()
            androidContext(this@NoteApplication)
            modules(
                databaseModule,
                viewModelModule,
                apiModule
            )
        }
    }
}