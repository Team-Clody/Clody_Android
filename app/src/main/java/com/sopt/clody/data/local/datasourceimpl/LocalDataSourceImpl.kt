package com.sopt.clody.data.local.datasourceimpl

import com.sopt.clody.data.local.datasource.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor() : LocalDataSource {
    override fun getLocalData(): String {
        return "Local Data"
    }
}
