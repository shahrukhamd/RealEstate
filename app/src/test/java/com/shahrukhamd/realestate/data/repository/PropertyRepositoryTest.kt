package com.shahrukhamd.realestate.data.repository

import com.shahrukhamd.realestate.data.api.AvivService
import com.shahrukhamd.realestate.data.model.PropertyItem
import com.shahrukhamd.realestate.data.model.RealEstateListResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class PropertyRepositoryTest {

    private val expectedPropertyList1 = """
[
    {
      "bedrooms": 4,
      "city": "Villers-sur-Mer",
      "id": 1,
      "area": 250.0,
      "url": "https://v.seloger.com/s/crop/590x330/visuels/1/7/t/3/17t3fitclms3bzwv8qshbyzh9dw32e9l0p0udr80k.jpg",
      "price": 1500000.0,
      "professional": "GSL EXPLORE",
      "propertyType": "Maison - Villa",
      "offerType": 1,
      "rooms": 8
    },
    {
      "bedrooms": 7,
      "city": "Deauville",
      "id": 2,
      "area": 600.0,
      "url": "https://v.seloger.com/s/crop/590x330/visuels/2/a/l/s/2als8bgr8sd2vezcpsj988mse4olspi5rfzpadqok.jpg",
      "price": 3500000.0,
      "professional": "GSL STICKINESS",
      "propertyType": "Maison - Villa",
      "offerType": 2,
      "rooms": 11
    }
]"""

    private val expectedPropertyList2 = """
[
     {
      "city": "Bordeaux",
      "id": 3,
      "area": 550.0,
      "price": 3000000.0,
      "professional": "GSL OWNERS",
      "propertyType": "Maison - Villa",
      "offerType": 1,
      "rooms": 7
    },
    {
      "city": "Nice",
      "id": 4,
      "area": 250.0,
      "url": "https://v.seloger.com/s/crop/590x330/visuels/1/9/f/x/19fx7n4og970dhf186925d7lrxv0djttlj5k9dbv8.jpg",
      "price": 5000000.0,
      "professional": "GSL CONTACTING",
      "offerType": 3,
      "propertyType": "Maison - Villa"
    }
]"""

    @MockK
    private lateinit var avivService: AvivService

    private lateinit var propertyRepository: PropertyRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)
        propertyRepository = PropertyRepositoryImpl(avivService)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `property repo should return list of sample data 1 returned from aviv service`() = runTest {
        val res = Response.success<RealEstateListResponse>(RealEstateListResponse(getSampleResponse(expectedPropertyList1)))

        coEvery {
            avivService.getPropertyList()
        } returns res

        val items = propertyRepository.getProperties().first()

        assertEquals(res.body()?.items, items.responseData)
    }

    @Test
    fun `property repo should not return another sample data not returned from aviv service`() = runTest {
        val res1 = Response.success<RealEstateListResponse>(RealEstateListResponse(getSampleResponse(expectedPropertyList1)))
        val res2 = Response.success<RealEstateListResponse>(RealEstateListResponse(getSampleResponse(expectedPropertyList2)))

        coEvery {
            avivService.getPropertyList()
        } returns res2

        val items = propertyRepository.getProperties().first()

        assertNotEquals(res1.body()?.items, items.responseData)
    }

    @Test
    fun `property repo should return empty list returned from aviv service`() = runTest {
        val res = Response.success<RealEstateListResponse>(RealEstateListResponse(emptyList()))

        coEvery {
            avivService.getPropertyList()
        } returns res

        val items = propertyRepository.getProperties().first()

        assertEquals(res.body()?.items, items.responseData)
    }

    @Test
    fun `property repo should return 400 when the same is returned from aviv service`() =
        runTest {
            val res = Response.error<RealEstateListResponse>(400, EMPTY_RESPONSE)

            coEvery {
                avivService.getPropertyList()
            } returns res

            val item = propertyRepository.getProperties().first()

            assertEquals(item.code, 400)
        }

    @Test
    fun `property repo should return 200 when the same is returned from aviv service`() =
        runTest {
            val res = Response.success<RealEstateListResponse>(RealEstateListResponse(getSampleResponse(expectedPropertyList1)))

            coEvery {
                avivService.getPropertyList()
            } returns res

            val item = propertyRepository.getProperties().first()

            assertEquals(item.code, 200)
        }

    private fun getSampleResponse(data: String): List<PropertyItem>? {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val type = Types.newParameterizedType(List::class.java, PropertyItem::class.java)
        val adapter: JsonAdapter<List<PropertyItem>> = moshi.adapter(type)
        return adapter.fromJson(data)
    }
}