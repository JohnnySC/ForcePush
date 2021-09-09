package johnnysc.github.forcepush.data.profile

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.core.FirebaseDatabaseProvider
import johnnysc.github.forcepush.data.login.UserInitial
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 18.08.2021
 **/
interface MyProfileRepository {

    suspend fun profile(): MyProfileData
    fun signOut()

    class Base(private val firebaseDatabaseProvider: FirebaseDatabaseProvider) :
        MyProfileRepository {
        private val uid = Firebase.auth.currentUser!!.uid

        override suspend fun profile(): MyProfileData {
            val user = firebaseDatabaseProvider.provideDatabase().child("users")
                .child(uid)
            return MyProfileData.Base(handleResult(user), canCreateGroups(uid))
        }

        override fun signOut() = Firebase.auth.signOut()

        private suspend fun canCreateGroups(uid: String) = suspendCoroutine<Boolean> { cont ->
            firebaseDatabaseProvider.provideDatabase().child("group-creators").child(uid)
                .get().addOnSuccessListener { snapshot ->
                    cont.resume(snapshot.getValue(Boolean::class.java) == true)
                }.addOnFailureListener {
                    cont.resume(false)
                }
        }

        private suspend fun handleResult(reference: DatabaseReference) =
            suspendCoroutine<UserInitial> { cont ->
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        cont.resume(snapshot.getValue(UserInitial::class.java)!!)
                    }
                    override fun onCancelled(error: DatabaseError) = Unit
                })
            }
    }
}