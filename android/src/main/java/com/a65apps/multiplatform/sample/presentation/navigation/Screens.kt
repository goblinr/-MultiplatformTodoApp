package com.a65apps.multiplatform.sample.presentation.navigation

import androidx.fragment.app.Fragment
import com.a65apps.multiplatform.interaction.navigation.Route
import com.a65apps.multiplatform.interaction.navigation.Screen as DomainScreen
import com.a65apps.multiplatform.sample.presentation.create.CreateFragment
import com.a65apps.multiplatform.sample.presentation.todo.TodoListFragment
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen

object Screens {

    fun transform(route: Route): Screen = when (route.screen) {
        DomainScreen.TODO_LIST -> TodoListScreen()
        DomainScreen.CREATE_TASK -> CreateTaskScreen()
    }
}

class TodoListScreen : SupportAppScreen() {

    override fun getFragment(): Fragment = TodoListFragment()
}

class CreateTaskScreen : SupportAppScreen() {

    override fun getFragment(): Fragment = CreateFragment()
}
