package com.pedroabinajm.easytaxichallenge.ui.search


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.pedroabinajm.easytaxichallenge.R
import com.pedroabinajm.easytaxichallenge.utils.DrawableMatcher
import com.pedroabinajm.easytaxichallenge.utils.RecyclerViewMatcher
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchPlaceActivityTest {
    @Rule
    @JvmField
    internal var grantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(SearchPlaceActivity::class.java)

    @Test
    fun initialBookmarksTest() {
        val childAt0 = onView(withRecyclerView(R.id.places_recycler).atPosition(0))
        childAt0.check(matches(hasDescendant(withText("Origem"))))
        childAt0.check(matches(hasDescendant(withText("Avenida dos Ourives, 480, Pq. Bristol, Sao Paulo"))))
        childAt0.check(matches(hasDescendant(withDrawable(R.drawable.ic_favorite_on))))
        val childAt1 = onView(withRecyclerView(R.id.places_recycler).atPosition(1))
        childAt1.check(matches(hasDescendant(withText("Atual"))))
        childAt1.check(matches(hasDescendant(withText("Rua Luis Gois, 206, Vila Mariana, Sao Paulo"))))
        childAt1.check(matches(hasDescendant(withDrawable(R.drawable.ic_favorite_on))))
        val childAt2 = onView(withRecyclerView(R.id.places_recycler).atPosition(2))
        childAt2.check(matches(hasDescendant(withText("Casa Da Dani"))))
        childAt2.check(matches(hasDescendant(withText("Rua Paraguassu, 253, Olimpico, Sao Caetano do Sul"))))
        childAt2.check(matches(hasDescendant(withDrawable(R.drawable.ic_favorite_on))))
    }

    @Test
    fun searchPlaceTest() {
        onView(withId(R.id.search_text))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Some address"))
                .check(matches(withText("Some address")))

        val childAt0 = onView(withRecyclerView(R.id.places_recycler).atPosition(0))
        childAt0.check(matches(hasDescendant(withText("Avenida dos Ourives, 480"))))
        childAt0.check(matches(hasDescendant(withText("Pq. Bristol, Sao Paulo"))))
        childAt0.check(matches(hasDescendant(withDrawable(R.drawable.ic_favorite_off))))
        val childAt1 = onView(withRecyclerView(R.id.places_recycler).atPosition(1))
        childAt1.check(matches(hasDescendant(withText("Rua Luis Gois, 206"))))
        childAt1.check(matches(hasDescendant(withText("Vila Mariana, Sao Paulo"))))
        childAt1.check(matches(hasDescendant(withDrawable(R.drawable.ic_favorite_off))))
        val childAt2 = onView(withRecyclerView(R.id.places_recycler).atPosition(2))
        childAt2.check(matches(hasDescendant(withText("Rua Paraguassu, 253"))))
        childAt2.check(matches(hasDescendant(withText("Olimpico, Sao Caetano do Sul"))))
        childAt2.check(matches(hasDescendant(withDrawable(R.drawable.ic_favorite_off))))
    }

    @Test
    fun favoriteLocationTest() {
        onView(withRecyclerView(R.id.places_recycler).atPosition(0))
                .check(matches(hasDescendant(withDrawable(R.drawable.ic_favorite_on))))

        onView(withId(R.id.places_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                        clickChildViewWithId(R.id.favorite_icon)))

        Thread.sleep(100)

        onView(withRecyclerView(R.id.places_recycler).atPosition(0))
                .check(matches(hasDescendant(DrawableMatcher(R.drawable.ic_favorite_off))))

        onView(withId(R.id.places_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                        clickChildViewWithId(R.id.favorite_icon)))

        onView(withId(R.id.alias_text))
                .check(matches(isDisplayed()))

        onView(withId(R.id.alias_text))
                .perform(ViewActions.click())
                .perform(ViewActions.typeText("Bookmark"))
                .check(matches(withText("Bookmark")))

        onView(withId(R.id.positive_button))
                .perform(ViewActions.click())

    }

    private fun withDrawable(resourceId: Int): Matcher<View> {
        return DrawableMatcher(resourceId)
    }

    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private fun clickChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View>? {
                return null
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController, view: View) {
                val v = view.findViewById<View>(id)
                v.performClick()
            }
        }
    }
}
