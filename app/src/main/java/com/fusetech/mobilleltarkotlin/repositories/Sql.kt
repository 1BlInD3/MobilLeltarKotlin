package com.fusetech.mobilleltarkotlin.repositories

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.fusetech.mobilleltarkotlin.activity.MainActivity
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.bundle
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.dolgKod
import com.fusetech.mobilleltarkotlin.activity.MainActivity.Companion.rakhelyInfo
import com.fusetech.mobilleltarkotlin.dataItems.CikkItems
import com.fusetech.mobilleltarkotlin.dataItems.PolcItems
import com.fusetech.mobilleltarkotlin.dataItems.RaktarAdat
import java.sql.Connection
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "Sql"

class Sql {
    fun userLogin(code: String): Boolean {
        var hasRight = false
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            val connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement =
                connection.prepareStatement("SELECT LeltarKuty_Jog as Jog FROM [Fusetech].[dbo].Jogok WHERE Pecset = ?")
            statement.setString(1, code)
            val resultSet = statement.executeQuery()
            hasRight = resultSet.next()
        } catch (e: Exception) {

        }
        return hasRight
    }

   /* fun cikkPolcQuery(code: String) {
        val connection: Connection
        val bundle = Bundle()
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try {
            connection = DriverManager.getConnection(MainActivity.read_connect)
            val preparedStatement: PreparedStatement =
                connection.prepareStatement("SELECT WarehouseID, BinNumber, InternalName, BinDescript2 FROM [ScaCompDB].[dbo].VF_SC360300_StockBinNo left outer join [ScaCompDB].[dbo].VF_SC230300_WarehouseInfo ON WarehouseID = Warehouse where BinNumber = ?")
            preparedStatement.setString(1, code)
            val resultSet: ResultSet = preparedStatement.executeQuery()
            if (!resultSet.next()) {
                val preparedStatement1: PreparedStatement =
                    connection.prepareStatement("SELECT SUM(SC33005) as [BalanceQty], MAX([SC33004])as BinNumber,MAX([ScaCompDB].[dbo].SC230300.SC23002) AS Warehouse, MAX(VF_SY240300_QTCategory.TextDescription) as QcCategory, MAX([SC01002]) as Description1, MAX([SC01003]) as Description2, MAX([SC01093]) as IntRem, MAX([Description]) as Unit, SC33001 as [StockItem] FROM [ScaCompDB].[dbo].[VF_SC360300_StockBinNo] left outer join [ScaCompDB].[dbo].SC330300 on BinNumber = SC33004 left outer join [ScaCompDB].[dbo].[SC010300] on SC33001 = SC01001 left join [ScaCompDB].[dbo].[VF_SCUN0300_UnitCode] on SC01133 = UnitCode LEFT OUTER JOIN [ScaCompDB].[dbo].SC230300 ON [ScaCompDB].[dbo].SC230300.SC23001 = [ScaCompDB].[dbo].SC330300.SC33002 LEFT OUTER JOIN [ScaCompDB].[dbo].VF_SY240300_QTCategory ON  SC33038 = VF_SY240300_QTCategory.Key1 where SC33005 > 0 and SC33001= ? group by SC33001, SC33004, SC33010, case when isnull(SC33038,\\'\\')=\\'\\' then \\'00\\' else SC33038 end\";")
                preparedStatement1.setString(1, code)
                val resultSet1: ResultSet = preparedStatement1.executeQuery()
                if (!resultSet1.next()) {
                    val statement2 =
                        connection.prepareStatement("SELECT SUM(SC33005) as [BalanceQty], MAX([SC33004])as BinNumber,MAX([ScaCompDB].[dbo].SC230300.SC23002) AS Warehouse, MAX(VF_SY240300_QTCategory.TextDescription) as QcCategory, MAX([SC01002]) as Description1, MAX([SC01003]) as Description2, MAX([SC01093]) as IntRem, MAX([Description]) as Unit, SC33001 as [StockItem] FROM [ScaCompDB].[dbo].[VF_SC360300_StockBinNo] left outer join [ScaCompDB].[dbo].SC330300 on BinNumber = SC33004 left outer join [ScaCompDB].[dbo].[SC010300] on SC33001 = SC01001 left join [ScaCompDB].[dbo].[VF_SCUN0300_UnitCode] on SC01133 = UnitCode LEFT OUTER JOIN [ScaCompDB].[dbo].SC230300 ON [ScaCompDB].[dbo].SC230300.SC23001 = [ScaCompDB].[dbo].SC330300.SC33002 LEFT OUTER JOIN [ScaCompDB].[dbo].VF_SY240300_QTCategory ON  SC33038 = VF_SY240300_QTCategory.Key1 where SC33001= ? group by SC33001, SC33004, SC33010, case when isnull(SC33038,\\'\\')=\\'\\' then \\'00\\' else SC33038 end\";")
                    statement2.setString(1, code)
                    val resultSet2 = statement2.executeQuery()
                    if (!resultSet2.next()) {
                        //ide ha nincs a rendszerben
                    } else {/*
                        context.cikkResultFragment = CikkResultFragment()
                        val megjegyzes1: String? = resultSet2.getString("Description1")
                        val megjegyzes2: String? = resultSet2.getString("Description2")
                        val unit: String? = resultSet2.getString("Unit")
                        val intrem: String? = resultSet2.getString("IntRem")
                        bundle.putSerializable("cikk", context.cikkItems)
                        bundle.putString("megjegyzes", megjegyzes1)
                        bundle.putString("megjegyzes2", megjegyzes2)
                        bundle.putString("unit", unit)
                        bundle.putString("intrem", intrem)
                        context.cikkResultFragment?.arguments = bundle
                        context.supportFragmentManager.beginTransaction()
                            .replace(R.id.cikk_container, context.cikkResultFragment!!, "CRF")
                            .commit()*/
                    }
                } else {
                    /*context.cikkResultFragment = CikkResultFragment()
                    val megjegyzes1: String? = resultSet1.getString("Description1")
                    val megjegyzes2: String? = resultSet1.getString("Description2")
                    val unit: String? = resultSet1.getString("Unit")
                    val intrem: String? = resultSet1.getString("IntRem")
                    context.cikkItems.clear()
                    do {
                        context.cikkItems.add(
                            CikkItems(
                                resultSet1.getDouble("BalanceQty"),
                                resultSet1.getString("BinNumber"),
                                resultSet1.getString("Warehouse"),
                                resultSet1.getString("QcCategory")
                            )
                        )
                    } while (resultSet1.next())
                    bundle.putSerializable("cikk", context.cikkItems)
                    bundle.putString("megjegyzes", megjegyzes1)
                    bundle.putString("megjegyzes2", megjegyzes2)
                    bundle.putString("unit", unit)
                    bundle.putString("intrem", intrem)
                    context.cikkResultFragment?.arguments = bundle
                    context.supportFragmentManager.beginTransaction()
                        .replace(R.id.cikk_container, context.cikkResultFragment!!, "CRF").commit()*/
                }
            } else {
                val preparedStatement2: PreparedStatement =
                    connection.prepareStatement("SELECT SC33001 as [StockItem],SUM(SC33005) as [BalanceQty],SUM(SC33008) as [ReceivedQty],MAX(VF_SY240300_QTCategory.TextDescription) as QcCategory,MAX([SC01002]) as Description1,MAX([SC01003]) as Description2,MAX([SC01093]) as IntRem,MAX([SC01094]) as IntRem2,rtrim(MAX([Description])) as Unit ,MAX(WarehouseID)as WarehouseID,MAX(InternalName)as Warehouse\tFROM [ScaCompDB].[dbo].[VF_SC360300_StockBinNo] left outer join [ScaCompDB].[dbo].SC330300 on BinNumber = SC33004 left outer join [ScaCompDB].[dbo].[SC010300] on SC33001 = SC01001 left join [ScaCompDB].[dbo].[VF_SCUN0300_UnitCode] on SC01133 = UnitCode LEFT OUTER JOIN [ScaCompDB].[dbo].VF_SY240300_QTCategory ON  SC33038 = VF_SY240300_QTCategory.Key1 left outer join [ScaCompDB].[dbo].VF_SC230300_WarehouseInfo as wi ON wi.Warehouse = WarehouseID\twhere SC33005 > 0 and BinNumber= ? group by SC33001, SC33010")
                preparedStatement2.setString(1, code)
                val resultSet2: ResultSet = preparedStatement2.executeQuery()
                /* if (!resultSet2.next()) {
                     val loadFragment = LoadFragment.newInstance("A polc üres")
                     context.supportFragmentManager.beginTransaction()
                         .replace(R.id.cikk_container, loadFragment).commit()
                 } else {
                     context.polcResultFragment = PolcResultFragment()
                     do {
                         context.polcItems.add(
                             PolcItems(
                                 resultSet2.getDouble("BalanceQty"),
                                 resultSet2.getString("Unit"),
                                 resultSet2.getString("Description1"),
                                 resultSet2.getString("Description2"),
                                 resultSet2.getString("IntRem"),
                                 resultSet2.getString("QcCategory"),
                                 resultSet2.getString("StockItem")
                             )
                         )

                     } while (resultSet2.next())
                     bundle.putSerializable("polc", context.polcItems)
                     context.polcResultFragment?.arguments = bundle
                     context.supportFragmentManager.beginTransaction()
                         .replace(R.id.cikk_container, context.polcResultFragment!!, "PRF").commit()
                 }*/
            }
        } catch (e: Exception) {
            /* Log.d(TAG, "$e")
             context.loadFragment = LoadFragment.newInstance("A feldolgozás során hiba lépett fel")
             context.supportFragmentManager.beginTransaction()
                 .replace(R.id.cikk_container, context.loadFragment!!, "LRF")
                 .commit()
             writeLog(e.stackTraceToString(), "arg1 $code")*/
        }
    }
*/
    fun isPolc(code: String): Boolean {
        var polc = false
        val connection: Connection
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try {
            connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement =
                connection.prepareStatement("SELECT WarehouseID, BinNumber, InternalName, BinDescript2 FROM [ScaCompDB].[dbo].VF_SC360300_StockBinNo left outer join [ScaCompDB].[dbo].VF_SC230300_WarehouseInfo ON WarehouseID = Warehouse where BinNumber = ?")
            statement.setString(1, code)
            val resultSet = statement.executeQuery()
            polc = resultSet.next()
            if(polc){
                bundle.putString("RAKKOD",resultSet.getString("WarehouseID"))
                bundle.putString("RAK",resultSet.getString("InternalName"))
            }
        } catch (e: Exception) {
            Log.d("sql", "isPolc: ")
        }
        return polc
    }
    fun isPolcOpen(code: String): Boolean{
        val rakhelyList : ArrayList<RaktarAdat> = ArrayList()
        var polc: Boolean = false
        val connection : Connection
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try {
            connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement = connection.prepareStatement("SELECT * FROM [leltar].[dbo].[LeltarRakhEll] WHERE RaktHely = ? AND Statusz = 1")
            statement.setString(1,code)
            val resultSet = statement.executeQuery()
            polc = resultSet.next()
        }catch (e: Exception){
            Log.d(TAG, "isPolcOpen: $e")
        }
        return polc
    }
    fun isPolcEmpty(code:String): Boolean{
        var empty = false
        val connection: Connection
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try{
            connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement = connection.prepareStatement("SELECT * FROM [leltar].[dbo].[LeltarRakhEll] WHERE RaktHely = ? AND Statusz = 0")
            statement.setString(1,code)
            val resultSet = statement.executeQuery()
            empty = resultSet.next()
        }catch (e: Exception){
            Log.d(TAG, "isPolcEmpty: $e")
        }
        return empty
    }
    fun isPolcClosed(code: String): Boolean{
        var closed = false
        val connection: Connection
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try{
            connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement = connection.prepareStatement("SELECT * FROM [leltar].[dbo].[LeltarRakhEll] WHERE RaktHely = ? AND Statusz = 2")
            statement.setString(1,code)
            val resultSet = statement.executeQuery()
            closed = resultSet.next()
        }catch (e: Exception){
            Log.d(TAG, "isPolcEmpty: $e")
        }
        return closed
    }
    fun closePolcLeltar(code: String){
        val connection: Connection
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try {
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
            connection = DriverManager.getConnection(MainActivity.write_connect)
            val statement = connection.prepareStatement("UPDATE [leltar].[dbo].[LeltarRakhEll] SET DolgozoBef = ?, Statusz = ?, BefDatum = ? WHERE RaktHely = ?")
            statement.setString(1, dolgKod)
            statement.setInt(2,2)
            statement.setString(3,date)
            statement.setString(4,code)
            statement.executeUpdate()
        }catch (e: Exception){
            Log.d(TAG, "closePolcLeltar: $e")
        }
    }
    @SuppressLint("SimpleDateFormat")
    fun uploadPolc(code: String): Boolean{
        var upload = false
        val connection: Connection
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try{
            val date = SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())
            connection = DriverManager.getConnection(MainActivity.write_connect)
            val statement = connection.prepareStatement("INSERT INTO [leltar].[dbo].[LeltarRakhEll] (RaktHely,DolgozoKezd,Statusz,KezdDatum) VALUES(?,?,?,?)")
            statement.setString(1,code)
            statement.setString(2, dolgKod)
            statement.setInt(3,1)
            statement.setString(4,date)
            statement.executeUpdate()
            upload = true
        }catch (e: Exception){
            Log.d(TAG, "uploadPolc: $e")
            upload = false
        }
        return upload
    }
    fun polcResultQuery(code: String): MutableLiveData<ArrayList<PolcItems>> {
        val myList = MutableLiveData<ArrayList<PolcItems>>()
        val polc: ArrayList<PolcItems> = ArrayList()
        val connection: Connection
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try {
            connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement =
                connection.prepareStatement("SELECT SC33001 as [StockItem],SUM(SC33005) as [BalanceQty],SUM(SC33008) as [ReceivedQty],MAX(VF_SY240300_QTCategory.TextDescription) as QcCategory,MAX([SC01002]) as Description1,MAX([SC01003]) as Description2,MAX([SC01093]) as IntRem,MAX([SC01094]) as IntRem2,rtrim(MAX([Description])) as Unit ,MAX(WarehouseID)as WarehouseID,MAX(InternalName)as Warehouse\tFROM [ScaCompDB].[dbo].[VF_SC360300_StockBinNo] left outer join [ScaCompDB].[dbo].SC330300 on BinNumber = SC33004 left outer join [ScaCompDB].[dbo].[SC010300] on SC33001 = SC01001 left join [ScaCompDB].[dbo].[VF_SCUN0300_UnitCode] on SC01133 = UnitCode LEFT OUTER JOIN [ScaCompDB].[dbo].VF_SY240300_QTCategory ON  SC33038 = VF_SY240300_QTCategory.Key1 left outer join [ScaCompDB].[dbo].VF_SC230300_WarehouseInfo as wi ON wi.Warehouse = WarehouseID\twhere SC33005 > 0 and BinNumber= ? group by SC33001, SC33010")
            statement.setString(1, code)
            val resultSet = statement.executeQuery()
            if (!resultSet.next()) {
                Log.d("sql", "polcResultQuery: ")
            } else {
                do {
                    polc.add(
                        PolcItems(
                            resultSet.getDouble("BalanceQty"),
                            resultSet.getString("Unit"),
                            resultSet.getString("Description1"),
                            resultSet.getString("Description2"),
                            resultSet.getString("IntRem"),
                            resultSet.getString("QcCategory"),
                            resultSet.getString("StockItem")
                        )
                    )
                } while (resultSet.next())
                myList.postValue(polc)
            }
        } catch (e: Exception) {
            Log.d("sql", "polcResultQuery: $e")
        }
        return myList
    }

    fun isCikk(code: String): Boolean {
        var cikk = false
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try {
            val connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement =
                connection.prepareStatement("""SELECT SUM(SC33005) as [BalanceQty], MAX([SC33004])as BinNumber,MAX([ScaCompDB].[dbo].SC230300.SC23002) AS Warehouse, MAX(VF_SY240300_QTCategory.TextDescription) as QcCategory, MAX([SC01002]) as Description1, MAX([SC01003]) as Description2, MAX([SC01093]) as IntRem, MAX([Description]) as Unit, SC33001 as [StockItem] FROM [ScaCompDB].[dbo].[VF_SC360300_StockBinNo] left outer join [ScaCompDB].[dbo].SC330300 on BinNumber = SC33004 left outer join [ScaCompDB].[dbo].[SC010300] on SC33001 = SC01001 left join [ScaCompDB].[dbo].[VF_SCUN0300_UnitCode] on SC01133 = UnitCode LEFT OUTER JOIN [ScaCompDB].[dbo].SC230300 ON [ScaCompDB].[dbo].SC230300.SC23001 = [ScaCompDB].[dbo].SC330300.SC33002 LEFT OUTER JOIN [ScaCompDB].[dbo].VF_SY240300_QTCategory ON  SC33038 = VF_SY240300_QTCategory.Key1 where SC33005 > 0 and SC33001= ? group by SC33001, SC33004, SC33010, case when isnull(SC33038,'')='' then '00' else SC33038 end;""")
            statement.setString(1, code)
            val resultSet = statement.executeQuery()
            cikk = resultSet.next()
            bundle.putString("MEG1", resultSet.getString("Description1"))
            bundle.putString("MEG2", resultSet.getString("Description2"))
            bundle.putString("INTREM", resultSet.getString("IntRem"))
            bundle.putString("UNIT", resultSet.getString("Unit"))
        } catch (e: Exception) {
            Log.d(TAG, "isCikk: $e")
            return false
        }
        return cikk
    }

    fun cikkResultQuery(code: String): MutableLiveData<ArrayList<CikkItems>> {
        val connection: Connection
        val cikkList = MutableLiveData<ArrayList<CikkItems>>()
        val cikkItemList: ArrayList<CikkItems> = ArrayList()
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try {
            connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement =
                connection.prepareStatement("""SELECT SUM(SC33005) as [BalanceQty], MAX([SC33004])as BinNumber,MAX([ScaCompDB].[dbo].SC230300.SC23002) AS Warehouse, MAX(VF_SY240300_QTCategory.TextDescription) as QcCategory, MAX([SC01002]) as Description1, MAX([SC01003]) as Description2, MAX([SC01093]) as IntRem, MAX([Description]) as Unit, SC33001 as [StockItem] FROM [ScaCompDB].[dbo].[VF_SC360300_StockBinNo] left outer join [ScaCompDB].[dbo].SC330300 on BinNumber = SC33004 left outer join [ScaCompDB].[dbo].[SC010300] on SC33001 = SC01001 left join [ScaCompDB].[dbo].[VF_SCUN0300_UnitCode] on SC01133 = UnitCode LEFT OUTER JOIN [ScaCompDB].[dbo].SC230300 ON [ScaCompDB].[dbo].SC230300.SC23001 = [ScaCompDB].[dbo].SC330300.SC33002 LEFT OUTER JOIN [ScaCompDB].[dbo].VF_SY240300_QTCategory ON  SC33038 = VF_SY240300_QTCategory.Key1 where SC33005 > 0 and SC33001= ? group by SC33001, SC33004, SC33010, case when isnull(SC33038,'')='' then '00' else SC33038 end;""")
            statement.setString(1, code)
            val resultSet = statement.executeQuery()
            if (!resultSet.next()) {
                Log.d(TAG, "cikkResultQuery: A polc lehet hogy üres")
            } else {
                do {
                    cikkItemList.add(
                        CikkItems(
                            resultSet.getDouble("BalanceQty"),
                            resultSet.getString("BinNumber"),
                            resultSet.getString("Warehouse"),
                            resultSet.getString("QcCategory")
                        )
                    )
                } while (resultSet.next())
                cikkList.postValue(cikkItemList)
            }
        } catch (e: Exception) {
            Log.d(TAG, "cikkResultQuery: $e")
        }
        return cikkList
    }
    fun loadBinItems(code: String): MutableLiveData<ArrayList<RaktarAdat>>{
        val connection: Connection
        var myData = MutableLiveData<ArrayList<RaktarAdat>>()
        rakhelyInfo.clear()
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try{
            connection = DriverManager.getConnection(MainActivity.read_connect)
            val statement = connection.prepareStatement("SELECT * FROM [leltar].[dbo].[Kuty_Leltaradat_polc] WHERE RaktHely = ? ORDER BY Bizszam")
            statement.setString(1,code)
            val resultSet = statement.executeQuery()
            if(!resultSet.next()){
                Log.d(TAG, "loadBinItems: Üres")
            }else{
               // rakhelyList.clear()
                do {
                    rakhelyInfo.add(RaktarAdat(resultSet.getString("Cikkszam"),resultSet.getString("Description1"),resultSet.getString("Description2"),resultSet.getDouble("Mennyiseg"),resultSet.getString("Megjegyzes"),resultSet.getInt("Bizszam")))
                }while(resultSet.next())
               // rakhelyInfo = rakhelyList
               myData.postValue(rakhelyInfo)
            }
        }catch (e: Exception){
            Log.d(TAG, "loadBinItems: $e")
        }
        return myData
    }
    fun insertData(cikk: String, mennyiseg: Double, dolgKod: String, raktar: String, rakhely: String, megjegyzes: String?): Boolean{
        val connection: Connection
        var insert = false
        Class.forName("net.sourceforge.jtds.jdbc.Driver")
        try {
            connection = DriverManager.getConnection(MainActivity.write_connect)
            val statement = connection.prepareStatement("INSERT INTO [leltar].[dbo].[Leltaradat] (Cikkszam,Mennyiseg,Dolgozo,Raktar,RaktHely,Megjegyzes,Nyomtatva,Status,EllStatus) VALUES (?,?,?,?,?,?,?,?,?)")
            statement.setString(1,cikk)
            statement.setDouble(2,mennyiseg)
            statement.setString(3,dolgKod)
            statement.setString(4,raktar)
            statement.setString(5,rakhely)
            statement.setString(6,megjegyzes)
            statement.setString(7,"n")
            statement.setInt(8,1)
            statement.setInt(9,0)
            statement.executeUpdate()
            insert = true
        }catch (e: Exception){
            insert = false
            Log.d(TAG, "insertData: $e")
        }
        return insert
    }
}