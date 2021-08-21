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
interface LoginEngine {

    suspend fun login(): Auth

    abstract class Abstract(activity: LoginActivity) : LoginEngine {

        protected val activityWeakReference = WeakReference(activity)
        protected val provider = OAuthProvider.newBuilder("github.com")

        protected suspend fun authResult(pending: Task<AuthResult>) =
            suspendCoroutine<AuthResult> { continuation ->
                pending
                    .addOnSuccessListener { continuation.resume(it) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
    }

    class Login(activity: LoginActivity) : Abstract(activity){

        override suspend fun login(): Auth {
            val authResult = if (Firebase.auth.pendingAuthResult == null)
                authInternal(activityWeakReference.get()!!)
            else
                authInternal()
            return Auth.Base(authResult.additionalUserInfo?.profile ?: emptyMap())
        }

        private suspend fun authInternal(activity: LoginActivity) =
            authResult(Firebase.auth.startActivityForSignInWithProvider(activity, provider.build()))

        private suspend fun authInternal() = authResult(Firebase.auth.pendingAuthResult!!)
    }

    class SignIn(activity: LoginActivity) : Abstract(activity) {

        override suspend fun login(): Auth {
            val data = Firebase.auth.currentUser!!.startActivityForReauthenticateWithProvider(
                activityWeakReference.get()!!,
                provider.build())
            authResult(data)
            return Auth.Base(emptyMap())
        }
    }
}