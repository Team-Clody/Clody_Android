package com.sopt.clody.presentation.utils.extension

fun String?.isJsonObject(): Boolean = this?.startsWith("{") == true && this.endsWith("}") // JSON Object인지 확인, ViewModel에서 쓰면 됨
fun String?.isJsonArray(): Boolean = this?.startsWith("[") == true && this.endsWith("]") // JSON Array인지 확인
