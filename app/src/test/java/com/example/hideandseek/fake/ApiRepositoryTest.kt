package com.example.hideandseek.fake

import com.example.hideandseek.data.repository.ApiRepositoryImpl
import org.junit.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ApiRepositoryTest {
    @Test
    fun apiRepository_getTest_verifyFakeResponseGetTest() = runTest {
        val repository = ApiRepositoryImpl(
            FakeRestApi(),
            StandardTestDispatcher(testScheduler)
        )
        assertEquals(FakeDataSource.fakeResponseGetTest, repository.getTest())
    }

    @Test
    fun apiRepository_getLocation_verifyFakeResponseGetSpacetime() = runTest {
        val repository = ApiRepositoryImpl(
            FakeRestApi(),
            StandardTestDispatcher(testScheduler)
        )
        assertEquals(FakeDataSource.fakeResponseGetLocation, repository.getLocation("fake", "00:00:00"))
    }

    @Test
    fun apiRepository_postLocation_verifyFakeResponsePost() = runTest {
        val repository = ApiRepositoryImpl(
            FakeRestApi(),
            StandardTestDispatcher(testScheduler)
        )
        assertEquals(FakeDataSource.fakeResponsePost, repository.postLocation(FakeDataSource.fakePostLocation))
    }
}
