package com.example.testhydromate.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Ekstensi DataStore (Top Level)
// Ini membuat kita bisa memanggil context.dataStore di mana saja
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "reminder_settings")

@Module
@InstallIn(SingletonComponent::class) // 2. Scope Global
object AppModule {

    // FIREBASE

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        // instance Auth untuk Login/Register
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        // instance Firestore untuk Database
        return FirebaseFirestore.getInstance()
    }

    // DATASTORE (Penyimpanan Lokal)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        // 3. Inject Context
        // DataStore butuh "Context" (akses ke sistem Android) untuk membuat file.
        // Hilt menyediakan @ApplicationContext secara otomatis.
        return context.dataStore
    }
}