package com.fusetech.mobilleltarkotlin.activity

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.fusetech.mobilleltarkotlin.R
import com.fusetech.mobilleltarkotlin.dataItems.RaktarAdat
import com.fusetech.mobilleltarkotlin.fragments.*
import com.fusetech.mobilleltarkotlin.showMe
import com.honeywell.aidc.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BarcodeReader.BarcodeListener,
    LoginFragment.WithMainActivity, MenuFragment.WithMainActivity, TabbedFragment.With,
    CikkPolcFragment.WithMainActivity, TetelFragment.ChangeTab, LeltarFragment.MainMenuInteract {
    private var manager: AidcManager? = null
    private var barcodeReader: BarcodeReader? = null
    private var loginFragment: LoginFragment? = null
    private var menuFragment: MenuFragment? = null
    private var cikkPolcFragment: CikkPolcFragment? = null
    private var barcodeData: String = ""
    private var tabbedFragment: TabbedFragment? = null
    private lateinit var myTimer: CountDownTimer

    /*
    * Ahogy felvettem a ratárra egy cikket akkor beír a LeltarRakhEll-be 1es státuszt kap. A tételt felveszi a Leltaradat táblába 1es státusszal (RaktHely,DolgozoKezd,Statusz,KezdDatum)
    * Az új rakhely lenoymása után megkérdezi lezárom-e, ha igen akkor a LeltarRakhEll 2es státusz (DolgozoBef,BefDatum, Statusz)
    * Ha a cikk üres akkor 0 státsuz a fejbe
    * */

    companion object {
        const val read_connect =
            "jdbc:jtds:sqlserver://10.0.0.11;databaseName=Fusetech;user=scala_read;password=scala_read;loginTimeout=10"
        const val write_connect =
            "jdbc:jtds:sqlserver://10.0.0.11;databaseName=leltar;user=Raktarrendszer;password=PaNNoN0132;loginTimeout=10"
        var containerCode: String = ""
        var cikkCode: String = ""
        val bundle = Bundle()
        var rakhelyInfo: ArrayList<RaktarAdat> = ArrayList()//MutableLiveData<ArrayList<RaktarAdat>>
        var dolgKod = ""
        var rakthely = ""
        var selectFocus = 1
        var count = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //loadMenuFragment(true)
        supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

        myTimer = object: CountDownTimer(1200000,1000){
            override fun onTick(millisUntilFinished: Long) {
                count++
                Log.d(TAG, "onTick: $count")
            }

            override fun onFinish() {
               when{
                   getFragment("MENU") -> {
                       getLoginFragment()
                   }
                   getFragment("f0") -> {
                       getLoginFragment()
                   }
                   getFragment("f1") -> {
                       getLoginFragment()
                   }
                   else -> {
                       finishAndRemoveTask()
                   }
               }
            }

        }
        myTimer.start()
    }

    override fun onResume() {
        super.onResume()
        getLoginFragment()
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
            .addToBackStack(null)
            .commit()
    }

    override fun loadLeltar() {
        count = 0
        myTimer.cancel()
        tabbedFragment = TabbedFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, tabbedFragment!!, "TABBED").addToBackStack(null).commit()
        myTimer.start()
    }

    override fun loadLekerdezes() {
        count = 0
        myTimer.cancel()
        cikkPolcFragment = CikkPolcFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, cikkPolcFragment!!, "CIKKPOLC").addToBackStack(null).commit()
        myTimer.start()
    }

    override fun loadKilepes() {
        myTimer.cancel()
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

    override fun getString() {
        try {
            val fragmentName = supportFragmentManager.findFragmentByTag("f0")
            rakthely = (fragmentName as LeltarFragment).getRakhely()
            Log.d("MMM", "getString: $rakthely")
        } catch (e: Exception) {
            Log.d("MMM", "getString: $e")
        }
    }

    override fun changeTab() {
        val fragment = supportFragmentManager.findFragmentByTag("TABBED")
        (fragment as TabbedFragment).changeTab()
    }

    override fun setData(
        cikkszam: String,
        meg1: String?,
        meg2: String?,
        qty: Double,
        megjegyzes: String?,
        bizszam: Int
    ) {
        val fragment = supportFragmentManager.findFragmentByTag("f0")
        (fragment as LeltarFragment).getData(cikkszam, meg1, meg2, qty, megjegyzes, bizszam)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        myTimer.cancel()
        count = 0
        if(getFragment("TABBED")){
            val fragment = supportFragmentManager.findFragmentByTag("TABBED")
            when(event?.keyCode){
                22->{
                    (fragment as TabbedFragment).changeTetelTab()
                }
                21->{
                    (fragment as TabbedFragment).changeTab()
                }
            }
        }
        myTimer.start()
        return super.onKeyLongPress(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        myTimer.cancel()
        count = 0
        if (getFragment("MENU")) {
            when (keyCode){
                8 -> {
                    val fragment = supportFragmentManager.findFragmentByTag("MENU")
                    (fragment as MenuFragment).leltarFocus()
                    loadLeltar()
                }
                9 -> {
                    val fragment = supportFragmentManager.findFragmentByTag("MENU")
                    (fragment as MenuFragment).lekerdezesFocus()
                    loadLekerdezes()
                }
                10 ->{
                    val fragment = supportFragmentManager.findFragmentByTag("MENU")
                    (fragment as MenuFragment).kilepesFocus()
                    finishAndRemoveTask()
                }
            }
        }
        if (getFragment("f1")){
            when(keyCode){
                20 -> {
                        val fragment = supportFragmentManager.findFragmentByTag("f1")
                        (fragment as TetelFragment).focusRecycler()
                }
            }
        }
        if(getFragment("f0")){
            when(keyCode){
                20 -> {
                    val fragment = supportFragmentManager.findFragmentByTag("f0")
                    (fragment as LeltarFragment).setFocus()
                }
                else -> {
                    val fragment = supportFragmentManager.findFragmentByTag("f0")
                    (fragment as LeltarFragment).deleteRakhely()
                }
            }
        }
        myTimer.start()
        return super.onKeyUp(keyCode, event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        myTimer.cancel()
        count = 0
        if(getFragment("TABBED")){
            when(keyCode){
                22->{
                    event?.startTracking()
                    return true
                    //(fragment as TabbedFragment).changeTetelTab()
                }
                21->{
                    event?.startTracking()
                    return true
                }
            }
        }
        myTimer.start()
        return super.onKeyDown(keyCode, event)
    }

    override fun tabSwitch() {
        val fragment = supportFragmentManager.findFragmentByTag("TABBED")
        (fragment as TabbedFragment).changeTetelTab()
    }

    override fun timerCancel() {
        count = 0
        myTimer.cancel()
        Log.d(TAG, "timerCancel: Cancel")
        myTimer.start()
        Log.d(TAG, "timerCancel: Start")
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
       // window.setDecorFitsSystemWindows(false) api > 30

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,mainConstraint).let { controller ->
            //controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
