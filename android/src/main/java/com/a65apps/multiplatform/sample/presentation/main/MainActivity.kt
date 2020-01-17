package com.a65apps.multiplatform.sample.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.a65apps.multiplatform.interaction.Schedulers
import com.a65apps.multiplatform.interaction.Store
import com.a65apps.multiplatform.interaction.main.MainAction
import com.a65apps.multiplatform.interaction.main.MainState
import com.a65apps.multiplatform.sample.R
import com.a65apps.multiplatform.sample.di.ComponentBuilder
import com.a65apps.multiplatform.sample.di.ComponentFactoryHolder
import com.a65apps.multiplatform.sample.di.ViewModelFactory
import com.a65apps.multiplatform.sample.presentation.base.BaseViewModel
import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder

private const val SAVED_STATE = "main_saved_state"

class MainViewModel @Inject constructor(
    store: Store<MainState, MainAction>,
    factory: Map<Class<*>, @JvmSuppressWildcards ComponentBuilder>
) : BaseViewModel<MainState, MainAction>(store, factory)

class MainActivity : AppCompatActivity(), HasAndroidInjector, ComponentFactoryHolder {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var navigatorHolder: NavigatorHolder
    @Inject
    lateinit var navigator: Navigator
    @Inject
    lateinit var factory: Map<Class<*>, @JvmSuppressWildcards ComponentBuilder>
    @Inject
    lateinit var schedulers: Schedulers

    private lateinit var viewModel: MainViewModel
    private var savedState: MainState? = null

    private lateinit var renderingDisposable: Disposable

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        savedState = savedInstanceState?.let {
            it.getParcelable<MainParcelable>(SAVED_STATE)?.toDomain()
        }
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory<MainState, MainViewModel>(savedState, factory)
        ).get(MainViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        renderingDisposable = viewModel.state
            .observeOn(schedulers.main)
            .subscribe(isThreadLocal = true) {
                title = it.screen.name
            }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        savedState?.apply {
            outState.putParcelable(SAVED_STATE, toParcelable())
        }
    }

    override fun onDestroy() {
        renderingDisposable.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        viewModel.acceptAction(MainAction.NavigateBack)
    }

    override fun factory(): Map<Class<*>, ComponentBuilder> = viewModel.factory()
}
