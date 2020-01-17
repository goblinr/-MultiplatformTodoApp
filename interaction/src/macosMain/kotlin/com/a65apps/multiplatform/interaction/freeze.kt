package com.a65apps.multiplatform.interaction

import com.badoo.reaktive.utils.freeze

actual fun <T : State> T.freeze(): T = this.freeze()
