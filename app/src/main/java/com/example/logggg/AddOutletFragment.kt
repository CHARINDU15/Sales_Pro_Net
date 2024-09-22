package com.example.logggg

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddOutletFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLatitude: Double? = null
    private var currentLongitude: Double? = null

    private lateinit var routeRecyclerView: RecyclerView
    private lateinit var routesAdapter: RoutesAdapter
    private val routes = mutableListOf<Route>()

    private lateinit var photoImageView: ImageView
    private lateinit var capturePhotoButton: Button
    private lateinit var selectPhotoButton: Button

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2
    private  val REQUEST_PERMISSION_CODE = 1000


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_outlet, container, false)
        // Initialize views

        // Initialize UI elements
        photoImageView = view.findViewById(R.id.photoImageView)
        capturePhotoButton = view.findViewById(R.id.capturePhotoButton)
        selectPhotoButton = view.findViewById(R.id.selectPhotoButton)

        // Set up button listeners
        capturePhotoButton.setOnClickListener {
            requestPermissionsIfNeeded()
            dispatchTakePictureIntent()
        }

        selectPhotoButton.setOnClickListener {
            requestPermissionsIfNeeded()
            dispatchPickImageIntent()
        }
        return view
    }
    private fun requestPermissionsIfNeeded() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE
            )
        }
    }


    private fun dispatchTakePictureIntent() {
        try {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                Log.e("AddOutletFragment", "No camera app found.")
                Toast.makeText(context, "No camera app found.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("AddOutletFragment", "Error starting camera intent: ${e.message}")
            Toast.makeText(context, "Error starting camera intent.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dispatchPickImageIntent() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    photoImageView.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    photoImageView.setImageURI(imageUri)
                }
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize fusedLocationClient for fetching current location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fetchCurrentLocation()

        val outletNameInput = view.findViewById<EditText>(R.id.outletNameInput)
        val outletCategoryInput = view.findViewById<EditText>(R.id.outletCategoryInput)
        val contactPersonNameInput = view.findViewById<EditText>(R.id.contactPersonNameInput)
        val contactNumberInput = view.findViewById<EditText>(R.id.contactNumberInput)
        val designationInput = view.findViewById<EditText>(R.id.designationInput)
        val ownersInput = view.findViewById<EditText>(R.id.ownersInput)
        val addOutletButton = view.findViewById<Button>(R.id.saveButton)
        val routeSelectionLabel = view.findViewById<TextView>(R.id.routeSelectionLabel)
        routeRecyclerView = view.findViewById(R.id.routeRecyclerView)

        // Initialize RecyclerView
        routeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        routesAdapter = RoutesAdapter(routes) { selectedRoute ->
            // Handle route selection
            routeSelectionLabel.text = selectedRoute.routeName
            routeRecyclerView.visibility = View.GONE // Hide RecyclerView after selection
        }
        routeRecyclerView.adapter = routesAdapter

        // Fetch and populate routes
        fetchRoutes()

        // Add animations for EditText fields
        arrayOf(outletNameInput, outletCategoryInput, contactPersonNameInput, contactNumberInput, designationInput, ownersInput).forEach { editText ->
            editText.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale_in))
                } else {
                    view.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale_out))
                }
            }
        }

        // Add button click listener
        addOutletButton.setOnClickListener {
            val name = outletNameInput.text.toString()
            val category = outletCategoryInput.text.toString()
            val contactPersonName = contactPersonNameInput.text.toString()
            val contactNumber = contactNumberInput.text.toString()
            val designation = designationInput.text.toString()
            val owners = ownersInput.text.toString()
            val route = routeSelectionLabel.text.toString()

            // Validate fields
            if (name.isEmpty()) {
                outletNameInput.error = "Outlet name is required"
                return@setOnClickListener
            }
            if (category.isEmpty()) {
                outletCategoryInput.error = "Category is required"
                return@setOnClickListener
            }
            if (contactPersonName.isEmpty()) {
                contactPersonNameInput.error = "Contact person name is required"
                return@setOnClickListener
            }
            if (contactNumber.isEmpty()) {
                contactNumberInput.error = "Contact number is required"
                return@setOnClickListener
            }
            if (designation.isEmpty()) {
                designationInput.error = "Designation is required"
                return@setOnClickListener
            }
            if (owners.isEmpty()) {
                ownersInput.error = "Owners field is required"
                return@setOnClickListener
            }
            if (routeSelectionLabel.text == "Select Route") {
                routeSelectionLabel.error = "Please Select The Route"
                return@setOnClickListener
            }

            // Check if location was fetched successfully
            if (currentLatitude == null || currentLongitude == null) {
                Toast.makeText(context, "Failed to fetch current location", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Display summary of entered data
            val summary = """
                Outlet Name: $name
                Category: $category
                Contact Person: $contactPersonName
                Contact Number: $contactNumber
                Designation: $designation
                Owners: $owners
                Location: ($currentLatitude, $currentLongitude)
            """.trimIndent()

            Toast.makeText(context, "Outlet Added:\n$summary", Toast.LENGTH_LONG).show()

            // Add an animation to the button after clicking
            addOutletButton.startAnimation(android.view.animation.AnimationUtils.loadAnimation(context, R.anim.bounce))
        }
    }

    private fun fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1000
            )
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                currentLatitude = location.latitude
                currentLongitude = location.longitude
            } else {
                Log.w("AddOutletFragment", "Location is null.")
                Toast.makeText(context, "Unable to fetch location.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Log.e("AddOutletFragment", "Failed to get location: ${e.message}")
            Toast.makeText(context, "Failed to get location.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchRoutes() {
        // Example data fetching function for routes
        lifecycleScope.launch {
            try {
                // Simulate network or database operation
                delay(2000)
                routes.addAll(
                    listOf(
                        Route("Route 1","Heloooooo"),
                        Route("Route 2","Route 02"),
                        Route("Route 3","route 03")
                    )
                )
                routesAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.e("AddOutletFragment", "Error fetching routes: ${e.message}")
                Toast.makeText(context, "Error fetching routes.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
