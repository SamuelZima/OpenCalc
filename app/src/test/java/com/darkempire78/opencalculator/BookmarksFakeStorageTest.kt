package com.darkempire78.opencalculator.bookmarks

import org.junit.Assert.*
import org.junit.Test
import java.util.UUID

/**
 * This test provides a FakeBookmarkStore (in-memory list) to verify the add/remove/check flow.
 */
class BookmarksFakeStorageTest {

    // Minimal storage abstraction the service will depend on
    interface BookmarkStore {
        fun load(): List<Bookmark>
        fun save(list: List<Bookmark>)
    }

    // Simple domain service that manipulates bookmarks via the store
    class BookmarksService(private val store: BookmarkStore) {

        fun add(calculation: String, result: String): Bookmark {
            val b = Bookmark(
                calculation = calculation,
                result = result,
                time = System.currentTimeMillis().toString(),
                id = UUID.randomUUID().toString()
            )
            val updated = store.load() + b
            store.save(updated)
            return b
        }

        fun removeById(id: String) {
            val updated = store.load().filterNot { it.id == id }
            store.save(updated)
        }

        fun isBookmarked(calculation: String, result: String): Boolean {
            return store.load().any { it.calculation == calculation && it.result == result }
        }

        fun all(): List<Bookmark> = store.load()
    }

    // Fake in-memory implementation used only by tests
    class FakeBookmarkStore(initial: List<Bookmark> = emptyList()) : BookmarkStore {
        private var data: MutableList<Bookmark> = initial.toMutableList()
        override fun load(): List<Bookmark> = data.toList()
        override fun save(list: List<Bookmark>) { data = list.toMutableList() }
    }

    @Test
    fun `fake storage add-remove-isBookmarked flow`() {
        val fakeStore = FakeBookmarkStore()
        val service = BookmarksService(fakeStore)

        // initially empty
        assertTrue(service.all().isEmpty())
        assertFalse(service.isBookmarked("1+2", "3"))

        // add a bookmark
        val b = service.add("1+2", "3")
        assertEquals(1, service.all().size)
        assertTrue(service.isBookmarked("1+2", "3"))

        // add another distinct bookmark
        service.add("2*5", "10")
        assertEquals(2, service.all().size)
        assertTrue(service.isBookmarked("2*5", "10"))

        // remove the first by id
        service.removeById(b.id)
        assertEquals(1, service.all().size)
        assertFalse(service.isBookmarked("1+2", "3"))
        assertTrue(service.isBookmarked("2*5", "10"))
    }
}
