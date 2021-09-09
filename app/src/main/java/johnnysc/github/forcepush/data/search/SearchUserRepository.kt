package johnnysc.github.forcepush.data.search

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.core.FirebaseDatabaseProvider
import johnnysc.github.forcepush.core.Save
import johnnysc.github.forcepush.data.login.UserInitial
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 18.08.2021
 **/
interface SearchUserRepository : Save<String> {

    suspend fun search(query: String): List<SearchData>
    suspend fun initChatWith(userId: String): Boolean

    class Base(
        private val firebaseDatabaseProvider: FirebaseDatabaseProvider,
        private val userIdContainer: Save<String>
    ) : SearchUserRepository {

        override fun save(data: String) = userIdContainer.save(data)

        override suspend fun search(query: String): List<SearchData> {
            val users = firebaseDatabaseProvider.provideDatabase()
                .child("users")
                .orderByChild("login")
                .equalTo(query)
            return handleResult(users).map { (key, data) ->
                SearchData.User(
                    key,
                    if (data.name.isEmpty()) data.login else data.name,
                    data.photoUrl
                )
            }
        }

        private val myUid = Firebase.auth.currentUser!!.uid

        override suspend fun initChatWith(userId: String): Boolean = firebaseDatabaseProvider
            .provideDatabase().child("users-chats").run {
                var result = handleData(child(myUid).child(userId).setValue(true))
                if (result) {
                    result = handleData(child(userId).child(myUid).setValue(true))
                    if (result) save(userId)
                }
                return result
            }

        private suspend fun handleData(data: Task<Void>) = suspendCoroutine<Boolean> { cont ->
            data.addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resume(false) }
        }

        private suspend fun handleResult(users: Query) =
            suspendCoroutine<List<Pair<String, UserInitial>>> { cont ->
                users.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) =
                        snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(UserInitial::class.java)!!)
                        }.let { cont.resume(it) }

                    override fun onCancelled(error: DatabaseError) = Unit
                })
            }
    }
}