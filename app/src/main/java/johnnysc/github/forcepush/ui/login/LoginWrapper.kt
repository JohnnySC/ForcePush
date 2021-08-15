package johnnysc.github.forcepush.ui.login

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 14.08.2021
 **/
interface LoginWrapper { //todo rename

    suspend fun login(): Auth

    class Base(activity: LoginActivity) : LoginWrapper {

        private val activityWeakReference = WeakReference(activity)
        private val provider = OAuthProvider.newBuilder("github.com")

        override suspend fun login() = try {
            val authResult = if (Firebase.auth.pendingAuthResult == null)
                authInternal(activityWeakReference.get()!!)
            else
                authInternal()
            Auth.Base(authResult.additionalUserInfo?.profile ?: mapOf("bio" to ""))
        } catch (e: Exception) {
            Auth.Fail(e)
        }

        private suspend fun authInternal(activity: LoginActivity) =
            authResult(Firebase.auth.startActivityForSignInWithProvider(activity, provider.build()))

        private suspend fun authInternal() = authResult(Firebase.auth.pendingAuthResult!!)

        private suspend fun authResult(pending: Task<AuthResult>) =
            suspendCoroutine<AuthResult> { continuation ->
                pending
                    .addOnSuccessListener { continuation.resume(it) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
    }
}