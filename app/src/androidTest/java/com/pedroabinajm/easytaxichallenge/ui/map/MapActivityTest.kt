package com.pedroabinajm.easytaxichallenge.ui.map


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.idling.CountingIdlingResource
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.utils.DrawableMatcher
import com.pedroabinajm.easytaxichallenge.utils.RecyclerViewMatcher
import org.hamcrest.Matcher
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
    fun mapActivityInitialLocationTest() {
        val placeNameText = onView(allOf(withId(R.id.place_name_text), isDisplayed()))
        placeNameText.check(ViewAssertions.matches(withText("Rua Luis Gois, 206")))

        val placeDescriptionText = onView(allOf(withId(R.id.place_description_text), isDisplayed()))
        placeDescriptionText.check(matches(withText("Vila Mariana, Sao Paulo")))

        onView(allOf(withId(R.id.favorite_icon), isDisplayed()))
                .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.my_location_button), isDisplayed()))
                .check(matches(isDisplayed()))
    }

    @Test
    fun mapActivityMapLocationTest() {
        onView(withId(R.id.map))
                .perform(swipeUp())

        Thread.sleep(1000)

        onView(allOf(withId(R.id.place_name_text), isDisplayed()))
                .check(matches(withText("Rua Luis Gois, 666")))
        onView(allOf(withId(R.id.place_description_text), isDisplayed()))
                .check(matches(withText("Vila Mariana, Sao Paulo")))

        onView(withId(R.id.my_location_button))
                .perform(click())

        Thread.sleep(1000)

        onView(allOf(withId(R.id.place_name_text), isDisplayed()))
                .check(matches(withText("Rua Luis Gois, 206")))
        onView(allOf(withId(R.id.place_description_text), isDisplayed()))
                .check(matches(withText("Vila Mariana, Sao Paulo")))
    }

    @Test
    fun favoriteLocationTest() {
        onView(withId(R.id.favorite_button))
                .check(matches(withDrawable(R.drawable.ic_favorite_off)))

        onView(withId(R.id.favorite_button))
                .perform(click())

        onView(withId(R.id.alias_text))
                .check(matches(isDisplayed()))

        onView(withId(R.id.alias_text))
                .perform(click())
                .perform(typeText("Bookmark"))
                .check(matches(withText("Bookmark")))

        onView(withId(R.id.positive_button))
                .perform(click())

        onView(withId(R.id.favorite_button))
                .check(matches(withDrawable(R.drawable.ic_favorite_on)))

        onView(withId(R.id.favorite_button))
                .perform(click())

        onView(withId(R.id.favorite_button))
                .check(matches(withDrawable(R.drawable.ic_favorite_off)))
    }

    @Test
    fun pickPlaceTest() {
        val placeNameText = onView(allOf(withId(R.id.place_name_text), isDisplayed()))
        placeNameText.check(ViewAssertions.matches(withText("Rua Luis Gois, 206")))

        onView(withId(R.id.place_view))
                .perform(click())

        val childAt0 = onView(withRecyclerView(R.id.places_recycler).atPosition(0))
        childAt0.check(matches(hasDescendant(withText("Origem"))))
        childAt0.check(matches(hasDescendant(withText("Avenida dos Ourives, 480, Pq. Bristol, Sao Paulo"))))
        childAt0.check(matches(hasDescendant(withDrawable(R.drawable.ic_favorite_on))))

        onView(withId(R.id.places_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                        click()))

        placeNameText.check(ViewAssertions.matches(withText("Origem")))
        onView(allOf(withId(R.id.place_description_text), isDisplayed()))
                .check(ViewAssertions.matches(withText("Avenida dos Ourives, 480, Pq. Bristol, Sao Paulo")))
        onView(withId(R.id.favorite_button))
                .check(matches(withDrawable(R.drawable.ic_favorite_on)))
    }

    private fun withDrawable(resourceId: Int): Matcher<View> {
        return DrawableMatcher(resourceId)
    }

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }
}
