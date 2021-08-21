package johnnysc.github.forcepush.data.login

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.sl.core.DATABASE_URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 15.08.2021
 **/
interface LoginRepository {

    suspend fun saveUser(user: UserInitial)

    fun user(): Any?

    class Base : LoginRepository {
        override fun user() = Firebase.auth.currentUser
        override suspend fun saveUser(user: UserInitial) {
            val value =
                Firebase.database(DATABASE_URL)
                    .reference.root.child("users").child(user()!!.uid).setValue(user)
            handleResult(value)
        }

        private suspend fun handleResult(value: Task<Void>): Unit =
            suspendCoroutine { continuation ->
                value.addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
            }
    }
}