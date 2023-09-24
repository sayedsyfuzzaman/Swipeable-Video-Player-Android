package com.nexdecade.nd_shorts.data

data class ApiException(val errorCode:Int, val errorMessage: String):Exception(errorMessage)