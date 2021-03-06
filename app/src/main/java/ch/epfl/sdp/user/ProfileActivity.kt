package ch.epfl.sdp.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import ch.epfl.sdp.authentication.IUserConnector
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityProfileBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import javax.inject.Inject
import ch.epfl.sdp.authentication.LocalUser

/**
 * Activity that shows the current user profile
 */
class ProfileActivity: AppCompatActivity(), Callback {
    private lateinit var binding: ActivityProfileBinding
    private var newProfilePic: Bitmap? = null
    @Inject lateinit var connector: IUserConnector
    private val cache = UserCache()

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.profilePictureView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)

        }
        binding.okButton.setOnClickListener {
            validateInformation()
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 2)
        } else {
            setInformations()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setInformations()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val picturePath: Uri = if(data.data != null) data.data!! else data.extras!!.get("picturePath") as Uri
            Picasso.with(this)
                    .load(picturePath)
                    .fit().centerCrop()
                    .into(binding.profilePictureView, this)
        }
    }

    private fun setInformations() {
        binding.pseudoText.text = Editable.Factory().newEditable(LocalUser.pseudo)
        if(LocalUser.profilePic == null) {
            Picasso.with(this)
                    .load("https://cdn0.iconfinder.com/data/icons/seo-marketing-glyphs-vol-7/52/user__avatar__man__profile__Person__target__focus-512.png")
                    .into(binding.profilePictureView)
        }
        else
            binding.profilePictureView.setImageBitmap(LocalUser.profilePic)
    }

    private fun validateInformation() {
        val bmp = binding.profilePictureView.drawable.toBitmap()
        val ps = binding.pseudoText.text.toString()
        var pseudoModify: String? = null
        var picModify: Bitmap? = null
        if(LocalUser.profilePic == null || !LocalUser.profilePic!!.sameAs(newProfilePic)) {
            LocalUser.profilePic = bmp
            picModify = bmp
        }
        if(LocalUser.pseudo != ps) {
            LocalUser.pseudo = ps
            pseudoModify = ps
        }
        cache.put(this)
        connector.modify(pseudoModify, picModify, {
            if(picModify != null) showSuccessAndKill()
            else finish()
        }, {onError()})
    }

    private fun showSuccessAndKill() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
                .setMessage("Successfully uploaded profile pic")
                .setCancelable(false)
                .setPositiveButton("OK") { _, _ -> finish()}
                .setOnDismissListener {finish()}
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onSuccess() {
        newProfilePic = binding.profilePictureView.drawable.toBitmap()
    }

    override fun onError() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
                .setMessage("Error uploading profile picture")
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .setOnDismissListener {finish()}
        val alertDialog = builder.create()
        alertDialog.show()
    }
}