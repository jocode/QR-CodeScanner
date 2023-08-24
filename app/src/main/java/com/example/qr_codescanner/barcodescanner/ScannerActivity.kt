package com.example.qr_codescanner.barcodescanner

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qr_codescanner.R
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class ScannerActivity : AppCompatActivity() {

    private var scanResults = ""

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            val originalIntent = result.originalIntent
            if (originalIntent == null) {
                Log.d("MainActivity", "Cancelled scan")
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d(
                    "MainActivity",
                    "Cancelled scan due to missing camera permission"
                )
                Toast.makeText(
                    this,
                    "Cancelled due to missing camera permission",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Log.d("MainActivity", "Scanned")
            scanResults = result.contents
            updateView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)

        setupViews()

    }


    private fun setupViews() {
        val scanButton = findViewById<View>(R.id.btnScan)
        scanButton.setOnClickListener { onButtonClick() }
    }

    private fun updateView() {
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val formattedValues = parseBarcodeValues(scanResults)
        tvResult.text = formattedValues.toString()
        Log.e("Scanned", scanResults)
    }

    /**
     * When the button is clicked, launch the scan activity.
     */
    private fun onButtonClick() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.PDF_417)
        options.setPrompt("Scan a barcode")
        options.setCameraId(0) // Use a specific camera of the device

        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(options)
    }


    /**
     * Launch the scan activity with the default options.
     */
    fun scanBarcode() {
        barcodeLauncher.launch(ScanOptions())
    }

    /**
     * Launch the scan activity with inverted scan enabled.
     */
    fun scanBarcodeInverted() {
        val options = ScanOptions()
        options.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.INVERTED_SCAN)
        barcodeLauncher.launch(options)
    }

    /**
     * Launch the scan activity with mixed scan enabled.
     */
    fun scanMixedBarcodes() {
        val options = ScanOptions()
        options.addExtra(Intents.Scan.SCAN_TYPE, Intents.Scan.MIXED_SCAN)
        barcodeLauncher.launch(options)
    }

    /**
     * Launch the scan activity with the front camera using the PDF_417 format.
     */
    fun scanPDF417() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.PDF_417)
        options.setPrompt("Scan something")
        options.setOrientationLocked(false)
        options.setBeepEnabled(false)
        barcodeLauncher.launch(options)
    }

}