package com.example.hideandseek.fake

import com.example.hideandseek.data.datasource.remote.PostData
import com.example.hideandseek.data.datasource.remote.ResponseData
import retrofit2.Response

object FakeDataSource {

    private val fakeGetTest = ResponseData.ResponseGetTest("test")

    val fakeResponseGetTest: Response<ResponseData.ResponseGetTest> = Response.success(
        fakeGetTest,
    )

    private val fakePost = ResponseData.ResponsePost("local test success")

    val fakeResponsePost: Response<ResponseData.ResponsePost> = Response.success(
        fakePost,
    )

    private val locationOne = ResponseData.ResponseGetLocation("fake", "00:00:00", 0.0, 0.0, 0)
    private val locationSecond = ResponseData.ResponseGetLocation("fake", "00:00:00", 0.0, 0.0, 0)

    private val fakeGetLocationList = listOf(
        locationOne,
        locationSecond
    )

    val fakeResponseGetLocation: Response<List<ResponseData.ResponseGetLocation>> = Response.success(
        fakeGetLocationList,
    )

    val fakePostLocation = PostData.PostLocation(
        locationOne.secret_words,
        locationOne.relative_time,
        locationOne.latitude,
        locationOne.longitude,
        locationOne.status
    )

    private val playerOne = ResponseData.ResponseGetPlayer("fake", "player1", 1, 1)
    private val playerSecond = ResponseData.ResponseGetPlayer("fake", "player2", 2, 2)

    private val fakeGetPlayerList = listOf(
        playerOne,
        playerSecond
    )

    val fakeResponseGetPlayer: Response<List<ResponseData.ResponseGetPlayer>> = Response.success(
        fakeGetPlayerList,
    )

    val fakePostPlayer =  PostData.PostPlayer(
        playerOne.secret_words,
        playerOne.name,
        playerOne.icon,
        playerOne.status
    )

    private val roomOne = ResponseData.ResponseGetRoom("fake", 0)
    private val roomSecond = ResponseData.ResponseGetRoom("fake2", 0)

    private val fakeGetRoomList = listOf(
        roomOne,
        roomSecond
    )

    val fakeResponseGetRoom: Response<List<ResponseData.ResponseGetRoom>> = Response.success(
        fakeGetRoomList
    )

    val fakePostRoom = PostData.PostRoom(
        roomOne.secret_words,
        roomOne.is_start
    )
}
