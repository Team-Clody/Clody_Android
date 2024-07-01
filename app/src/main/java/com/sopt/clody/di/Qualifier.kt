package com.sopt.clody.di

import javax.inject.Qualifier

//의존성 주입 구분
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CLODY

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ANOTHER
