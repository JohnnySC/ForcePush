package johnnysc.github.forcepush.ui.login

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 27.04.2023
 */
interface LoginEngine {

    fun handle(activity: Activity)

    abstract class Abstract(private val handleTask: HandleTask) : LoginEngine {

        protected val provider = OAuthProvider.newBuilder("github.com")

        override fun handle(activity: Activity) =
            handleTask.handle {
                val result = authResult(makeTask(activity))
                Auth.Base(result.additionalUserInfo?.profile ?: emptyMap())
            }

        protected abstract fun makeTask(activity: Activity): Task<AuthResult>

        private suspend fun authResult(pending: Task<AuthResult>) =
            suspendCoroutine<AuthResult> { continuation ->
                pending
                    .addOnSuccessListener { continuation.resume(it) }
                    .addOnFailureListener { continuation.resumeWithException(it) }
            }
    }

    class Login(handleTask: HandleTask) : Abstract(handleTask) {
        override fun makeTask(activity: Activity) =
            Firebase.auth.pendingAuthResult ?: Firebase.auth.startActivityForSignInWithProvider(
                activity,
                provider.build()
            )
    }

    class SignIn(handleTask: HandleTask) : Abstract(handleTask) {
        override fun makeTask(activity: Activity) =
            Firebase.auth.currentUser!!.startActivityForReauthenticateWithProvider(
                activity,
                provider.build()
            )
    }
}