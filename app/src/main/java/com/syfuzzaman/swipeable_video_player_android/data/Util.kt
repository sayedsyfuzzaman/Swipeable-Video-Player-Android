package com.syfuzzaman.swipeable_video_player_android.data


import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.CancellationException

suspend fun <T :  ShortsAPIResponse> tryIO(block: suspend () -> T): T {
    val response = block()
    return when{
        response.success != true ->{
            throw ApiException(
                -1,
                "API Exception occurred!"
            )
        }
        else->{
            response//seems like all fine ...return the body
        }
    }
}
suspend fun <T> resultFromResponse(networkCall: suspend () -> T): Resource<T> =
    try {
        val response = networkCall.invoke()
        Resource.Success(response)
    } catch (e:Exception){
        Resource.Failure(getError(e))
    }



//this will use non synchronized lazy method
fun <T>unsafeLazy(initializer: () -> T): Lazy<T>{
    return lazy(LazyThreadSafetyMode.NONE){
        initializer()
    }
}

fun getError(e: Exception): Error {
    e.printStackTrace()
//    CustomAnalytics.logException(e)
    when (e) {
        is HttpException -> {
            return Error(e.code(), e.message())
        }
        is SocketTimeoutException -> {
            return Error(-1, "Connection time out")
        }
        is IOException -> {
            return Error(-1, "Unable to connect server")
        }
        is CancellationException -> {
            return Error(-1, "Unknown error occurred")
        }
        is ApiException -> {
            return Error(e.errorCode, e.errorMessage)
        }
        else -> {
            return Error(-1, "Unknown error occurred")
        }
    }
}

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, body: (T) -> Unit = {}) {
    liveData.observe(if(this is Fragment && this !is DialogFragment) this.viewLifecycleOwner else this) { it?.let { t -> body(t) } }
}