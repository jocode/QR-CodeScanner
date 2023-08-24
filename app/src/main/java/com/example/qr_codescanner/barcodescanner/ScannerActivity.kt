package com.example.qr_codescanner.barcodescanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.qr_codescanner.MainActivity
import com.example.qr_codescanner.databinding.ActivityScannerBinding
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class ScannerActivity : AppCompatActivity() {

    private var scanResults = ""
    private lateinit var binding: ActivityScannerBinding

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
        binding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViews()

    }


    private fun setupViews() = with(binding) {
        btnBarcodeScanner.setOnClickListener { onButtonClick() }

        btnQRScanner.setOnClickListener {
            startActivity(
                Intent(this@ScannerActivity, MainActivity::class.java)
            )
        }

        btnGeneralScanner.setOnClickListener {
            scanMixedBarcodes()
        }

    }

    private fun updateView() {
        Log.e("Scanned", scanResults)

        try {
            val formattedValues = parseBarcodeValues(scanResults)
            binding.tvResult.text = formattedValues.toString()
        } catch (e: Exception) {
            binding.tvResult.text = scanResults
        }
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
    private fun scanMixedBarcodes() {
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