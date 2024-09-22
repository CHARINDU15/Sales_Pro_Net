package com.example.logggg

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class Sales : Fragment() {

    private lateinit var userImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var chartWebView: WebView
    private lateinit var monthlyTargetTextView: TextView
    private lateinit var currentAchievementTextView: TextView
    private lateinit var totalOutletsTextView: TextView
    private lateinit var totalBillsTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sales, container, false)

        // Initialize views
        userImageView = view.findViewById(R.id.user_image_view)
        userNameTextView = view.findViewById(R.id.user_name_text_view)
        userEmailTextView = view.findViewById(R.id.user_email_text_view)
        chartWebView = view.findViewById(R.id.chart_webview)
        monthlyTargetTextView = view.findViewById(R.id.monthly_target)
        currentAchievementTextView = view.findViewById(R.id.current_achievement)
        totalOutletsTextView = view.findViewById(R.id.total_outlets)
        totalBillsTextView = view.findViewById(R.id.total_bills)

        // Fetch user details from Firebase
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // Load user details
            userNameTextView.text = user.displayName ?: "User Name"
            userEmailTextView.text = user.email ?: "User Email"

            // Load user profile picture
            val profilePictureUrl = user.photoUrl
            if (profilePictureUrl != null) {
                Glide.with(this)
                    .load(profilePictureUrl)
                    .placeholder(R.drawable.ic_success) // Default image while loading
                    .error(R.drawable.ic_error) // Default image if there's an error
                    .into(userImageView)
            } else {
                // No profile picture available
                userImageView.setImageResource(R.drawable.reshot_icon_email_c4xdb2taem)
            }
        }

        // Configure WebView to display Google Chart
        chartWebView.settings.javaScriptEnabled = true
        chartWebView.settings.domStorageEnabled = true
        chartWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        chartWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Log.e("WebViewError", "Error: $description")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("WebView", "Page loaded: $url")
            }
        }

        val chartHtml = """
            <html>
            <head>
                <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
                <script type="text/javascript">
                    google.charts.load('current', {'packages':['corechart']});
                    google.charts.setOnLoadCallback(drawChart);
                    function drawChart() {
                        var data = google.visualization.arrayToDataTable([
                            ['Category', 'Amount'],
                            ['Monthly Target', 5000],
                            ['Current Achievement', 3200]
                        ]);
                        var options = {
                            title: 'Target vs Achievement',
                            pieHole: 0.4, // Make it a donut chart
                            slices: {
                                0: {offset: 0.1},
                                1: {offset: 0.1}
                            },
                            colors: ['#FF5722', '#4CAF50'] // Customize colors
                        };
                        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
                        chart.draw(data, options);
                    }
                </script>
            </head>
            <body>
                <div id="chart_div" style="width: 100%; height: 300px;"></div>
            </body>
            </html>
        """

        chartWebView.loadData(chartHtml, "text/html", "UTF-8")

        // Update statistics
        monthlyTargetTextView.text = "Monthly Target: $5000.00"
        currentAchievementTextView.text = "Current Achievement: $3200.00"
        totalOutletsTextView.text = "Total Outlets: 15"
        totalBillsTextView.text = "Total Bills: $1200.00"

        return view
    }
}
