package com.williamfzc.randunit.operations

import android.content.ContentProvider
import org.robolectric.Robolectric

class ContentProviderOperation(t: Class<*>) : AbstractAndroidOperation(t) {
    override fun getInstance(): ContentProvider {
        @Suppress("UNCHECKED_CAST")
        return Robolectric.setupContentProvider(type as Class<ContentProvider>)
    }
}
