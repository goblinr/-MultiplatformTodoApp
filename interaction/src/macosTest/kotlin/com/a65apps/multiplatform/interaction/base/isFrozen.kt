package com.a65apps.multiplatform.interaction.base

import com.badoo.reaktive.utils.isFrozen

actual fun <T> T.isFrozen(): Boolean = isFrozen
