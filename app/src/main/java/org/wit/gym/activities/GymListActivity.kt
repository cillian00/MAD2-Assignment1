    package org.wit.gym.activities

    import android.app.Activity
    import android.content.Intent
    import android.os.Bundle
    import android.util.Log
    import android.view.Menu
    import android.view.MenuItem
    import android.view.View
    import android.widget.AdapterView
    import android.widget.ArrayAdapter
    import android.widget.Spinner
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.appcompat.app.AppCompatActivity
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.google.firebase.Firebase
    import com.google.firebase.auth.FirebaseAuth
    import com.google.firebase.auth.auth
    import org.wit.gym.adapters.PlacemarkAdapter
    import org.wit.gym.adapters.PlacemarkListener
    import org.wit.gym.main.MainApp
    import org.wit.gym.models.GymCloudStore
    import org.wit.gym.models.GymModel
    import org.wit.placemark.R
    import org.wit.placemark.databinding.ActivityGymListBinding

    class GymListActivity : AppCompatActivity(), PlacemarkListener {

        lateinit var app: MainApp

        private lateinit var binding: ActivityGymListBinding
        private lateinit var auth: FirebaseAuth
        private lateinit var cloud: GymCloudStore

        override fun onStart() {
            binding.recyclerView.adapter = PlacemarkAdapter(app.gyms.findAll(), this)
            super.onStart()
        }

        override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            binding = ActivityGymListBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupBottomNavigation()
            auth = Firebase.auth
            val currentUser = auth.currentUser
            if (currentUser != null) {
                Log.i(currentUser.email ?: "No email", "Logged in")
                binding.user.text = currentUser.email
            }

            binding.toolbar.title = title
            setSupportActionBar(binding.toolbar)
            cloud = GymCloudStore(this)

            app = application as MainApp

            val layoutManager = LinearLayoutManager(this)
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = PlacemarkAdapter(app.gyms.findAll(), this)

        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.menu_main, menu)
            menuInflater.inflate(R.menu.bottom_nav_menu, menu)

            val item = menu.findItem(R.id.item_filter)
            val spinner = item.actionView as Spinner

            val adapter = ArrayAdapter.createFromResource(
                this,
                R.array.filter_options,
                android.R.layout.simple_spinner_dropdown_item
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val selectedFilter = parent.getItemAtPosition(position).toString()

                    app.gyms.findAllFiltered(selectedFilter) { filteredPlacemarks ->
                        runOnUiThread {
                            binding.recyclerView.adapter = PlacemarkAdapter(filteredPlacemarks, this@GymListActivity)
                        }
                    }
                    Log.i(selectedFilter, "New Filter")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }


        }

            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.item_add -> {
                    val launcherIntent = Intent(this, GymActivity::class.java)
                    getResult.launch(launcherIntent)
                }
                R.id.item_map -> {
                    val launcherIntent = Intent(this, GymMapsActivity::class.java)
                    mapIntentLauncher.launch(launcherIntent)
                }
            }
            return super.onOptionsItemSelected(item)
        }



         private val getResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    (binding.recyclerView.adapter)?.
                    notifyItemRangeChanged(0,app.gyms.findAll().size)
                }
            }

        override fun onPlacemarkClick(placemark: GymModel) {
            val launcherIntent = Intent(this, GymActivity::class.java)
            launcherIntent.putExtra("gym_edit", placemark)
            getClickResult.launch(launcherIntent)
        }

        private val getClickResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                if (it.resultCode == Activity.RESULT_OK) {
                    (binding.recyclerView.adapter)?.
                    notifyItemRangeChanged(0,app.gyms.findAll().size)
                }
            }

        private val mapIntentLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            )    { }

        private fun setupBottomNavigation() {
            binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.action_signout -> {
                        auth.signOut()
                        Log.i("btn", "Working")
                        val loginIntent = Intent(this, Login::class.java)
                        startActivity(loginIntent)
                        finish()
                        true
                    }

                    else -> {false}
                }
        }

    }}