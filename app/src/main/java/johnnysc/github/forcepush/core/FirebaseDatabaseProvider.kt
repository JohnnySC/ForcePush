package johnnysc.github.forcepush.core

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * @author Asatryan on 22.08.2021
 */
interface FirebaseDatabaseProvider {

    fun provideDatabase(): DatabaseReference

    class Base : FirebaseDatabaseProvider {

        init {
            Firebase.database(DATABASE_URL).setPersistenceEnabled(false)

            provideDatabase().run {
                addValueEventListener(EmptyDataListener())
                child("users")
                    .addValueEventListener(EmptyDataListener())
            }
        }

        override fun provideDatabase(): DatabaseReference {
            return Firebase.database(DATABASE_URL).reference.root
        }

        private companion object {
            const val DATABASE_URL =
                "https://forcepush-acb5b-default-rtdb.europe-west1.firebasedatabase.app"
        }

        private inner class EmptyDataListener : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) = Unit
            override fun onCancelled(error: DatabaseError) = Unit
        }
    }
}