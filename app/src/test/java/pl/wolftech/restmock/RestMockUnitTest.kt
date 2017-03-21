package pl.wolftech.restmock

import io.appflate.restmock.RESTMockServer
import io.appflate.restmock.utils.RequestMatchers.pathEndsWith
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config
import pl.wolftech.restmock.util.AndroidSampleRobolectricRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


@RunWith(AndroidSampleRobolectricRunner::class)
@Config(constants = BuildConfig::class,
        sdk = intArrayOf(25))
open class RestMockUnitTest {

    val FILE_LOGIN_SUCCESS = "login_results/login_success.json"

    lateinit var activity: MainActivity
    lateinit var retrofit: Retrofit

    @Before
    fun setup() {
        activity = Robolectric.setupActivity(MainActivity::class.java)
        RESTMockServer.reset()
        retrofit = Retrofit.Builder()
                .baseUrl(RESTMockServer.getUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Test
    fun retrofitTest() {
        RESTMockServer
                .whenGET(pathEndsWith("/user/login"))
                .thenReturnFile(200, FILE_LOGIN_SUCCESS)

        retrofit.create(LoginVideostarRetrofitSpecificationApiCall::class.java)
                .performLogin("mateusz", "password", 1, "netvi")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    (status) ->
                    Assert.assertEquals("ok", status)
                }
    }

    private interface LoginVideostarRetrofitSpecificationApiCall {

        @FormUrlEncoded
        @POST("/user/login")
        fun performLogin(@Field("login") login: String,
                         @Field("password") password: String,
                         @Field("permanent") permanent: Int,
                         @Field("operator") operator: String)
                : Single<LoginResult>
    }
}

data class LoginResult(val status: String)