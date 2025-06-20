package com.example.myapplication.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.Contact
import com.example.myapplication.data.ContactManager

class MainActivity : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var nameText: TextView
    private lateinit var bioText: TextView
    private lateinit var emailText: TextView
    private lateinit var phoneText: TextView
    private lateinit var emailButton: Button
    private lateinit var callButton: Button
    private lateinit var websiteButton: Button
    private lateinit var aboutButton: Button
    private lateinit var addContactButton: Button
    private lateinit var viewContactsButton: Button

    private lateinit var contactMgr: ContactManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactMgr = ContactManager(this)
        initializeViews()
        setupClickListeners()
        loadProfileData()
    }

    private fun initializeViews() {
        profileImage       = findViewById(R.id.profile_image)
        nameText           = findViewById(R.id.name_text)
        bioText            = findViewById(R.id.bio_text)
        emailText          = findViewById(R.id.email_text)
        phoneText          = findViewById(R.id.phone_text)
        emailButton        = findViewById(R.id.email_button)
        callButton         = findViewById(R.id.call_button)
        websiteButton      = findViewById(R.id.website_button)
        aboutButton        = findViewById(R.id.about_button)
        addContactButton   = findViewById(R.id.add_user_button)
        viewContactsButton = findViewById(R.id.view_users_button)
    }

    private fun setupClickListeners() {
        emailButton.setOnClickListener { sendEmail() }
        callButton.setOnClickListener { makePhoneCall() }
        websiteButton.setOnClickListener { openWebsite() }
        aboutButton.setOnClickListener { showAboutInfo() }
        profileImage.setOnClickListener {
            Toast.makeText(this, "Hello! 👋", Toast.LENGTH_SHORT).show()
        }
        addContactButton.setOnClickListener { showAddContactDialog() }
        viewContactsButton.setOnClickListener { showContactList() }
    }

    private fun loadProfileData() {
        nameText.text  = getString(R.string.profile_name)
        bioText.text   = getString(R.string.profile_bio)
        emailText.text = getString(R.string.profile_email)
        phoneText.text = getString(R.string.profile_phone)
    }

    private fun sendEmail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:${getString(R.string.profile_email)}")
            putExtra(Intent.EXTRA_SUBJECT, "Hello from Profile App")
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "No email app found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makePhoneCall() {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${getString(R.string.profile_phone)}")
        }
        try {
            startActivity(callIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "No phone app found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openWebsite() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.profile_website)))
        try {
            startActivity(browserIntent)
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(this, "No browser found!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAboutInfo() {
        Toast.makeText(
            this,
            "Profile App v1.0\nBuilt with ❤️ in Android Studio",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showAddContactDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_add_user, null)
        AlertDialog.Builder(this)
            .setTitle("Add New Contact")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                val c = Contact(
                    name    = view.findViewById<EditText>(R.id.etName).text.toString(),
                    bio     = view.findViewById<EditText>(R.id.etBio).text.toString(),
                    email   = view.findViewById<EditText>(R.id.etEmail).text.toString(),
                    phone   = view.findViewById<EditText>(R.id.etPhone).text.toString(),
                    website = view.findViewById<EditText>(R.id.etWebsite).text.toString()
                )
                contactMgr.add(c)
                Toast.makeText(this, "Contact added!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showContactList() {
        val contacts = contactMgr.getAll()
        if (contacts.isEmpty()) return Toast.makeText(this, "No contacts yet.", Toast.LENGTH_SHORT).show()
        val names = contacts.map { it.name }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("All Contacts")
            .setItems(names) { _, idx ->
                val c = contacts[idx]
                AlertDialog.Builder(this)
                    .setTitle(c.name)
                    .setMessage(
                        "Bio: ${c.bio}\n" +
                                "Email: ${c.email}\n" +
                                "Phone: ${c.phone}\n" +
                                "Website: ${c.website}"
                    )
                    .setPositiveButton("Delete") { _, _ ->
                        contactMgr.remove(idx)
                        Toast.makeText(this, "Deleted.", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Close", null)
                    .show()
            }
            .show()
    }
}