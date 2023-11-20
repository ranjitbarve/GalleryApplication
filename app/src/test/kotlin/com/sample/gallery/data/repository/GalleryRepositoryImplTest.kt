package com.sample.gallery.data.repository

import DataFixtures
import com.ranjit.data.base.ApiException
import com.ranjit.galleryapplication.data.model.toDomain
import com.ranjit.galleryapplication.data.remote.RetrofitService
import com.ranjit.galleryapplication.data.repository.GalleryRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class GalleryRepositoryImplTest {

    @Test
    fun test() {
        assertTrue(true)
    }
    @MockK
    internal lateinit var retrofitService: RetrofitService
    private lateinit var galleryRepositoryImpl: GalleryRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        galleryRepositoryImpl = GalleryRepositoryImpl(retrofitService)
    }

    @Test
    fun `repository fetches gallery data and maps to domain model`() = runBlocking {
        coEvery {
            retrofitService.getGallery(
                section = "hot",
                sort = "viral",
                window = "day",
                page = 1,
                showViral = true
            )
        } returns Response.success(DataFixtures.getGalleryResponseData())

        val result = galleryRepositoryImpl.getGallery(
            section = "hot",
            sort = "viral",
            window = "day",
            page = 1,
            showViral = true
        )
        assertEquals(result, DataFixtures.getGalleryResponseData().toDomain())
    }

    @Test(expected = ApiException::class)
    fun `repository fails to fetch gallery data`() = runBlocking {
        coEvery {
            retrofitService.getGallery(
                section = "hot",
                sort = "viral",
                window = "day",
                page = 1,
                showViral = true
            )
        } throws ApiException("Invalid client_id")

        val result = galleryRepositoryImpl.getGallery(
            section = "hot",
            sort = "viral",
            window = "day",
            page = 1,
            showViral = true
        )
        assertNull(result)
    }

    @Test
    fun `repository fetches gallery data and returns empty`() = runBlocking {
        coEvery {
            retrofitService.getGallery(
                section = "hot",
                sort = "viral",
                window = "day",
                page = 1,
                showViral = true
            )
        } returns Response.success(DataFixtures.getEmptyGalleryResponseData())

        val result = galleryRepositoryImpl.getGallery(
            section = "hot",
            sort = "viral",
            window = "day",
            page = 1,
            showViral = true
        )

        assertEquals(result, DataFixtures.getEmptyGalleryResponseData().toDomain())
        assertNotNull(result.data)
        assert(result.data.count() == 0)
    }
}