package johnnysc.github.forcepush.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.ui.core.BaseActivity
import johnnysc.github.forcepush.ui.login.LoginActivity

/**
 * //todo make this better
 * @author Asatryan on 14.08.2021
 **/
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val avatar = findViewById<ImageView>(R.id.avatarImageView)

        val sharedPreferences = getSharedPreferences("ForceAppSharedPref", Context.MODE_PRIVATE)
        val profile = sharedPreferences.getString("profile", "")
        Firebase.auth.currentUser?.let {
            val text = it.displayName + "\n" + profile
            findViewById<TextView>(R.id.textView).text = text
            Picasso.get().load(it.photoUrl).into(avatar)
        }

        findViewById<View>(R.id.signOutButton).setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}