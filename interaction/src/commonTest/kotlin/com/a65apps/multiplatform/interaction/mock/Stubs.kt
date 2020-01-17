@file:Suppress("unused")

package com.a65apps.multiplatform.interaction.mock

import com.badoo.reaktive.utils.atomic.AtomicReference
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

interface Mock<Key, R> {
    var keys: List<Key>
    var stubs: Map<Key, List<R>>
    val invocations: AtomicReference<Map<Key, Int>>
}

open class StubExecutor<Key, R>(
    private val key: Key,
    private val mock: Mock<Key, R>
) {

    fun doExecute(): R {
        val results = mock.stubs[key]
        assertNotNull(actual = results, message = "method for key: $key is not stubbed")
        val result = results.firstOrNull()
        assertTrue(actual = result != null, message = "method for key: $key is not stubbed")

        val map = mock.invocations.value
        val count = map[key] ?: 1
        val newMap = map.plus(key to count)

        @Suppress("ControlFlowWithEmptyBody")
        while (!mock.invocations.compareAndSet(map, newMap)) {}

        return result
    }
}

private class StubExecutor0<R>(
    key: MatcherKey0,
    mock: Mock<MatcherKey0, R>
) : StubExecutor<MatcherKey0, R>(key, mock) {

    fun execute(): R {
        return doExecute()
    }
}

fun <R> stub(
    mock: Mock<MatcherKey0, R>
): R = StubExecutor0(searchKey(mock.keys), mock).execute()

fun <R> stubMethod(
    mock: Mock<MatcherKey0, R>,
    result: R
) {
    mock.keys = mock.keys + MatcherKey0
    val list = mock.stubs.getOrElse(MatcherKey0) { listOf() }
    mock.stubs = mock.stubs.plus(MatcherKey0 to list + result)
}

private fun searchKey(keys: List<MatcherKey0>): MatcherKey0 = keys.first()

private class StubExecutor1<A, R>(
    private val key: MatcherKey1<A>,
    mock: Mock<MatcherKey1<A>, R>
) : StubExecutor<MatcherKey1<A>, R>(key, mock) {

    fun execute(first: A): R {
        assertTrue(actual = key.match(first), message = "method for key: $key is not stubbed")

        return doExecute()
    }
}

fun <A, R> stub(
    mock: Mock<MatcherKey1<A>, R>,
    first: A
): R = StubExecutor1(searchKey(mock.keys, first), mock).execute(first)

fun <A, R> stubMethod(
    mock: Mock<MatcherKey1<A>, R>,
    first: Matcher<A>,
    result: R
) {
    val key = MatcherKey1(first)
    mock.keys = mock.keys + key
    val list = mock.stubs.getOrElse(key) { listOf() }
    mock.stubs = mock.stubs.plus(key to list + result)
}

private fun <A> searchKey(keys: List<MatcherKey1<A>>, first: A): MatcherKey1<A> {
    lateinit var key: MatcherKey1<A>
    keys.forEach {
        if (it.match(first)) {
            key = it
            return@forEach
        }
    }

    return key
}

private class StubExecutor2<A, B, R>(
    private val key: MatcherKey2<A, B>,
    mock: Mock<MatcherKey2<A, B>, R>
) : StubExecutor<MatcherKey2<A, B>, R>(key, mock) {

    fun execute(first: A, second: B): R {
        assertTrue(
            actual = key.match(first, second),
            message = "method for key: $key is not stubbed"
        )

        return doExecute()
    }
}

fun <A, B, R> stub(
    mock: Mock<MatcherKey2<A, B>, R>,
    first: A,
    second: B
): R = StubExecutor2(searchKey(mock.keys, first, second), mock).execute(first, second)

fun <A, B, R> stubMethod(
    mock: Mock<MatcherKey2<A, B>, R>,
    first: Matcher<A>,
    second: Matcher<B>,
    result: R
) {
    val key = MatcherKey2(first, second)
    mock.keys = mock.keys + key
    val list = mock.stubs.getOrElse(key) { listOf() }
    mock.stubs = mock.stubs.plus(key to list + result)
}

private fun <A, B> searchKey(
    keys: List<MatcherKey2<A, B>>,
    first: A,
    second: B
): MatcherKey2<A, B> {
    lateinit var key: MatcherKey2<A, B>
    keys.forEach {
        if (it.match(first, second)) {
            key = it
            return@forEach
        }
    }

    return key
}

private class StubExecutor3<A, B, C, R>(
    private val key: MatcherKey3<A, B, C>,
    mock: Mock<MatcherKey3<A, B, C>, R>
) : StubExecutor<MatcherKey3<A, B, C>, R>(key, mock) {

    fun execute(first: A, second: B, third: C): R {
        assertTrue(
            actual = key.match(first, second, third),
            message = "method for key: $key is not stubbed"
        )

        return doExecute()
    }
}

fun <A, B, C, R> stub(
    mock: Mock<MatcherKey3<A, B, C>, R>,
    first: A,
    second: B,
    third: C
): R = StubExecutor3(searchKey(mock.keys, first, second, third), mock)
    .execute(first, second, third)

fun <A, B, C, R> stubMethod(
    mock: Mock<MatcherKey3<A, B, C>, R>,
    first: Matcher<A>,
    second: Matcher<B>,
    third: Matcher<C>,
    result: R
) {
    val key = MatcherKey3(first, second, third)
    mock.keys = mock.keys + key
    val list = mock.stubs.getOrElse(key) { listOf() }
    mock.stubs = mock.stubs.plus(key to list + result)
}

private fun <A, B, C> searchKey(
    keys: List<MatcherKey3<A, B, C>>,
    first: A,
    second: B,
    third: C
): MatcherKey3<A, B, C> {
    lateinit var key: MatcherKey3<A, B, C>
    keys.forEach {
        if (it.match(first, second, third)) {
            key = it
            return@forEach
        }
    }

    return key
}

private class StubExecutor4<A, B, C, D, R>(
    private val key: MatcherKey4<A, B, C, D>,
    mock: Mock<MatcherKey4<A, B, C, D>, R>
) : StubExecutor<MatcherKey4<A, B, C, D>, R>(key, mock) {

    fun execute(first: A, second: B, third: C, fourth: D): R {
        assertTrue(
            actual = key.match(first, second, third, fourth),
            message = "method for key: $key is not stubbed"
        )

        return doExecute()
    }
}

fun <A, B, C, D, R> stub(
    mock: Mock<MatcherKey4<A, B, C, D>, R>,
    first: A,
    second: B,
    third: C,
    fourth: D
): R = StubExecutor4(searchKey(mock.keys, first, second, third, fourth), mock)
    .execute(first, second, third, fourth)

fun <A, B, C, D, R> stubMethod(
    mock: Mock<MatcherKey4<A, B, C, D>, R>,
    first: Matcher<A>,
    second: Matcher<B>,
    third: Matcher<C>,
    fourth: Matcher<D>,
    result: R
) {
    val key = MatcherKey4(first, second, third, fourth)
    mock.keys = mock.keys + key
    val list = mock.stubs.getOrElse(key) { listOf() }
    mock.stubs = mock.stubs.plus(key to list + result)
}

private fun <A, B, C, D> searchKey(
    keys: List<MatcherKey4<A, B, C, D>>,
    first: A,
    second: B,
    third: C,
    fourth: D
): MatcherKey4<A, B, C, D> {
    lateinit var key: MatcherKey4<A, B, C, D>
    keys.forEach {
        if (it.match(first, second, third, fourth)) {
            key = it
            return@forEach
        }
    }

    return key
}
