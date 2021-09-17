package com.fusetech.mobilleltarkotlin.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.fragments.*
import com.fusetech.mobilleltarkotlin.showMe
import com.honeywell.aidc.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BarcodeReader.BarcodeListener,
    LoginFragment.WithMainActivity, MenuFragment.WithMainActivity,
    CikkPolcFragment.WithMainActivity{
    private val TAG = "MainActivity"
    private var manager: AidcManager? = null
    private var barcodeReader: BarcodeReader? = null
    private var loginFragment: LoginFragment? = null
    private var menuFragment: MenuFragment? = null
    private var cikkPolcFragment: CikkPolcFragment? = null
    private var barcodeData: String = ""
    private var tabbedFragment: TabbedFragment? = null

    companion object {
        const val read_connect =
            "jdbc:jtds:sqlserver://10.0.0.11;databaseName=Fusetech;user=scala_read;password=scala_read;loginTimeout=10"
        var containerCode: String = ""
        var cikkCode: String = ""
        val bundle = Bundle()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getLoginFragment()
        supportActionBar?.hide()
        AidcManager.create(this) { aidcManager ->
            manager = aidcManager
            try {
                barcodeReader = manager?.createBarcodeReader()
                barcodeReader?.claim()
            } catch (e: ScannerUnavailableException) {
                e.printStackTrace()
            } catch (e: InvalidScannerNameException) {
                e.printStackTrace()
            }
            try {
                barcodeReader?.setProperty(BarcodeReader.PROPERTY_CODE_39_ENABLED, true)
                barcodeReader?.setProperty(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true)
                barcodeReader?.setProperty(
                    BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                    BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL
                )
            } catch (e: UnsupportedPropertyException) {
                e.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    "Failed to apply properties",
                    Toast.LENGTH_SHORT
                ).show()
            }
            barcodeReader?.addBarcodeListener(this)
        }

    }

    override fun onBarcodeEvent(p0: BarcodeReadEvent?) {
        runOnUiThread {
            barcodeData = p0?.barcodeData!!
            if (getFragment("LOGIN")) {
                loginFragment?.setCode(barcodeData)
            }
            if (getFragment("CIKKPOLC")) {
                cikkPolcFragment?.setCode(barcodeData)
            }
            if (getFragment("f0")) {
                val fragment = supportFragmentManager.findFragmentByTag("f0")
                (fragment as LeltarFragment).setCikk(barcodeData)
            }
        }
    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {
        runOnUiThread {
            Toast.makeText(this@MainActivity, "Nem sikerült leolvasni", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFragment(fragmentName: String): Boolean {
        val myFrag = supportFragmentManager.findFragmentByTag(fragmentName)
        if (myFrag != null && myFrag.isVisible) {
            return true
        }
        return false
    }

    private fun getLoginFragment() {
        loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, loginFragment!!, "LOGIN")
            .commit()
    }

    override fun onExit() {
        finishAndRemoveTask()
    }

    override fun loadMenuFragment(hasRight: Boolean) {
        menuFragment = MenuFragment.newInstance(hasRight)
        supportFragmentManager.beginTransaction().replace(R.id.container, menuFragment!!, "MENU")
            .commit()
    }

    override fun loadLeltar() {
        tabbedFragment = TabbedFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, tabbedFragment!!, "TABBED").commit()
    }

    override fun loadLekerdezes() {
        cikkPolcFragment = CikkPolcFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, cikkPolcFragment!!, "CIKKPOLC").commit()
    }

    override fun loadKilepes() {
        finishAndRemoveTask()
    }

    override fun sendBundle(polcList: String) {
        val polcResult = PolcResultFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.sub_container, polcResult, "POLCITEMS").commit()
        containerCode = polcList
    }

    override fun sendCikk(cikk: String) {
        val cikkResult = CikkResultFragment()
        cikkResult.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.sub_container, cikkResult, "CIKKITEMS").commit()
        cikkCode = cikk
    }

    override fun removeFragment() {
        when {
            getFragment("CIKKITEMS") -> {
                removeFragment("CIKKITEMS")
            }
            getFragment("POLCITEMS") -> {
                removeFragment("POLCITEMS")
            }
            else -> {
                showMe("Egyik sem", this)
            }
        }
    }

    private fun removeFragment(fragment1: String) {
        val fragment = supportFragmentManager.findFragmentByTag(fragment1)
        if (fragment != null) supportFragmentManager.beginTransaction().remove(fragment)
            .commit()
    }
}