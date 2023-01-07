package by.homework.hlazarseni.noteswithfab.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import by.homework.hlazarseni.noteswithfab.list.ListViewModel

internal val viewModelModule= module {
    viewModelOf(::ListViewModel)
}