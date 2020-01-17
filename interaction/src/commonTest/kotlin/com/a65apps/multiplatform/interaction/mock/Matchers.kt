package com.a65apps.multiplatform.interaction.mock

interface Matcher<T> {
    fun match(other: T): Boolean
}

class EqualsMatcher<T>(
    private val value: T
) : Matcher<T> {

    override fun match(other: T): Boolean = value == other

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as EqualsMatcher<*>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int = value.hashCode()
}

fun <T> eq(value: T): Matcher<T> = EqualsMatcher(value)

class AnyMatcher<T> : Matcher<T> {

    override fun match(other: T): Boolean = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }
}

fun <T> any(): Matcher<T> = AnyMatcher()

object MatcherKey0

data class MatcherKey1<A>(
    val first: Matcher<A>
)

fun <A> MatcherKey1<A>.match(first: A): Boolean = this.first.match(first)

data class MatcherKey2<A, B>(
    val first: Matcher<A>,
    val second: Matcher<B>
)

fun <A, B> MatcherKey2<A, B>.match(first: A, second: B): Boolean = this.first.match(first) &&
        this.second.match(second)

data class MatcherKey3<A, B, C>(
    val first: Matcher<A>,
    val second: Matcher<B>,
    val third: Matcher<C>
)

fun <A, B, C> MatcherKey3<A, B, C>.match(first: A, second: B, third: C): Boolean =
    this.first.match(first) &&
            this.second.match(second) &&
            this.third.match(third)

data class MatcherKey4<A, B, C, D>(
    val first: Matcher<A>,
    val second: Matcher<B>,
    val third: Matcher<C>,
    val fourth: Matcher<D>
)

fun <A, B, C, D> MatcherKey4<A, B, C, D>.match(
    first: A,
    second: B,
    third: C,
    fourth: D
): Boolean = this.first.match(first) &&
        this.second.match(second) &&
        this.third.match(third) &&
        this.fourth.match(fourth)
