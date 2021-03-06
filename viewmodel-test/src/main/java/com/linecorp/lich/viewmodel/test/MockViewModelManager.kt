/*
 * Copyright 2019 LINE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:JvmName("MockViewModelManagers")

package com.linecorp.lich.viewmodel.test

import android.content.Context
import androidx.lifecycle.ViewModelStoreOwner
import androidx.test.core.app.ApplicationProvider
import com.linecorp.lich.viewmodel.AbstractViewModel
import com.linecorp.lich.viewmodel.ViewModelFactory
import com.linecorp.lich.viewmodel.internal.getViewModelManager

/**
 * An API to mock ViewModels for tests.
 *
 * Every `mockFactory` set via [setMockViewModel] is tied to the `applicationContext` from which
 * this [MockViewModelManager] was created.
 */
interface MockViewModelManager {
    /**
     * Sets [mockFactory] as a factory function of mock ViewModels for [factory].
     *
     * When creating a new ViewModel instance, [mockFactory] will be called with the [ViewModelStoreOwner]
     * (e.g. Activity, Fragment). Then, the instance returned by [mockFactory] is registered to the
     * [ViewModelStoreOwner].
     *
     * @param factory a [ViewModelFactory] to be mocked.
     * @param mockFactory a function that returns a mock ViewModel for the given [ViewModelStoreOwner].
     */
    fun <T : AbstractViewModel> setMockViewModel(
        factory: ViewModelFactory<T>,
        mockFactory: (ViewModelStoreOwner) -> T
    )

    /**
     * Clears the `mockFactory` that was previously set via [setMockViewModel].
     *
     * @param factory a [ViewModelFactory] that will be no longer mocked.
     */
    fun <T : AbstractViewModel> clearMockViewModel(factory: ViewModelFactory<T>)

    /**
     * Creates a new ViewModel for the given [factory].
     * This function returns a "real" instance of the ViewModel regardless of [setMockViewModel].
     *
     * For example, a `mockFactory` of [setMockViewModel] may use this function for "spying" a real
     * ViewModel.
     */
    fun <T : AbstractViewModel> createRealViewModel(factory: ViewModelFactory<T>): T

    /**
     * Clears all mock factories that were previously set to this [MockViewModelManager].
     *
     * Note that this function doesn't clear ViewModels that are already in `ViewModelStore`.
     */
    fun clearAllMockViewModels()
}

/**
 * Obtains a [MockViewModelManager] for the given [applicationContext].
 *
 * By default, the applicationContext is obtained from `ApplicationProvider.getApplicationContext()`.
 * It is initialized by AndroidX Test framework.
 */
@JvmName("of")
fun getMockViewModelManager(
    applicationContext: Context = ApplicationProvider.getApplicationContext()
): MockViewModelManager = getViewModelManager(applicationContext) as MockViewModelManager
