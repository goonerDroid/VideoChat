package com.sublime.videochat/*
 * Copyright (c) 2014-2024 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-video-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import io.getstream.video.android.datastore.delegate.StreamUserDataStore

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // We use the provided StreamUserDataStore in the demo app for user data storage.
        // This is a convenience class provided for storage but the SDK itself is not aware of
        // this instance and doesn't use it. You can use it to store the logged in user and then
        // retrieve the information for SDK initialisation.
        StreamUserDataStore.install(this, isEncrypted = true)
    }
}

val Context.app get() = applicationContext as App
