package com.pedroabinajm.easytaxichallenge.ui.map


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.pedroabinajm.easytaxichallenge.R
import org.hamcrest.core.AllOf.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MapActivityTest {
    @Rule
    @JvmField
    internal var grantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MapActivity::class.java)

    private var googleMap: GoogleMap? = null
    private val countingIdlingResource = CountingIdlingResource("MapReady")

    @Before
    @Throws(Throwable::class)
    fun beforeEach() {
        countingIdlingResource.increment()

        val mapFragment = activityTestRule.activity.getMapFragment()
        val onMapReadyCallback = OnMapReadyCallback { map ->
            countingIdlingResource.decrement()
            googleMap = map
        }
        activityTestRule.runOnUiThread {
            mapFragment.getMapAsync(onMapReadyCallback)
        }

        IdlingRegistry.getInstance().register(countingIdlingResource)
    }

    @After
    fun afterEach() {
        IdlingRegistry.getInstance().unregister(countingIdlingResource)
    }

    @Test
    fun mapActivityTest() {
        val placeNameText = onView(allOf(withId(R.id.place_name_text), isDisplayed()))
        placeNameText.check(ViewAssertions.matches(withText("Rua Luis Gois, 206")))

        val placeDescriptionText = onView(allOf(withId(R.id.place_description_text), isDisplayed()))
        placeDescriptionText.check(matches(withText("Vila Mariana, Sao Paulo")))

        onView(allOf(withId(R.id.search_icon), isDisplayed()))
                .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.my_location_button), isDisplayed()))
                .check(matches(isDisplayed()))

        onView(withId(R.id.map))
                .perform(swipeUp())

        Thread.sleep(1000)

        placeNameText.check(matches(withText("Rua Luis Gois, 666")))
        placeDescriptionText.check(matches(withText("Vila Mariana, Sao Paulo")))

        onView(withId(R.id.my_location_button))
                .perform(click())

        Thread.sleep(1000)

        placeNameText.check(matches(withText("Rua Luis Gois, 206")))
        placeDescriptionText.check(matches(withText("Vila Mariana, Sao Paulo")))
    }
}
