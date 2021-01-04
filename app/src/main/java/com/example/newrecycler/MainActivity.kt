package com.example.newrecycler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newrecycler.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.app.AlertDialog


import android.view.View
import android.widget.Button

import kotlinx.android.synthetic.main.layout_item_view.*


class MainActivity : AppCompatActivity(), OnCarItemClickListner{

  internal var dbHelper = DatabaseHelper(this)



    lateinit var binding: ActivityMainBinding
    lateinit var carlist: ArrayList<Cars>



    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_car_details)

        handleInserts()
        handleUpdates()
        handleDeletes()
        handleViewing()
        handleShare()




        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{

            (setContentView(R.layout.activity_main))
            //setContentView(R.layout.activity_main)
            //runmethods()
            setContentView(R.layout.activity_car_details)
            binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
            carlist = ArrayList()
            addCars()




            carRecycler.layoutManager = LinearLayoutManager(this)
            carRecycler.addItemDecoration(DividerItemDecoration(this,1))
            carRecycler.adapter = CarAdapter(carlist,this)



        }

        /* runmethods()
        setContentView(R.layout.activity_main)
        runmethods()
        setContentView(R.layout.activity_car_details)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        carlist = ArrayList()
        addCars()




        carRecycler.layoutManager = LinearLayoutManager(this)
        carRecycler.addItemDecoration(DividerItemDecoration(this,1))
        carRecycler.adapter = CarAdapter(carlist,this)

*/
    }




fun runmethods(){

    handleInserts()
    handleUpdates()
    handleDeletes()
    handleViewing()
    handleShare()
}

    override fun onItemClick(item: Cars, position: Int) {
        Toast.makeText(this, item.name , Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CarDetailsActivity::class.java)
        intent.putExtra("CARNAME", item.name)
        intent.putExtra("CARDESC", item.description)
        intent.putExtra("CARLOGO", item.logo.toString())
        startActivity(intent)


    }







    //####################################################################################################################################//
    /**
     * Toast Message
     * https://stackoverflow.com/questions/36826004/how-do-you-display-a-toast-using-kotlin-on-android
     * Android Toast is used to display a sort time notification to the user without affecting the user interaction with UI.
     * The message displayed using
     * Toast class displays quickly, and it disappears after some time. The message in the Toast can be of type text, image or both.
     */
    fun showToast(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()

    }

    //####################################################################################################################################//

    /**
     * Each time a value has been added it will clear the fields that had values present
     */
    fun clearEditTexts() {

        carType.setText("")
        modificationsMade.setText("")
        bhpAmount.setText("")
        carId.setText("")
        torqueTotal.setText("")
        CostOfModifications.setText("")
        acceleration.setText("")

    }







    fun handleShare(){
        share.setOnClickListener {
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "https://play.google.com/store/apps/details?id=air.com.A3dtuning.Tuning3D&gl=IE"
            val shareSub = "https://play.google.com/store/apps/details?id=air.com.A3dtuning.Tuning3D&gl=IE"
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share using"))
        }
    }
    //####################################################################################################################################//
    /**
     * Alerts the user to changes that have taken place ie delete
     */
    fun showDialog(title: String, Message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(Message)
        builder.show()
    }
    //####################################################################################################################################//
    /**
     *When our handleInserts button is clicked.
*/
    fun handleInserts() {
        insertBtn.setOnClickListener {
            try {
                dbHelper.insertData(carType.text.toString(), modificationsMade.text.toString(), bhpAmount.text.toString(),
                    torqueTotal.text.toString(), CostOfModifications.text.toString(), acceleration.text.toString()
                )
                clearEditTexts()
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(e.message.toString())
            }


        }
    }



    //####################################################################################################################################//
    /**
     * When our handleUpdates data button is clicked
     */


    fun handleUpdates() {
        updateBtn.setOnClickListener {
            try {
                val isUpdate = dbHelper.updateData(carId.text.toString(),
                    carType.text.toString(),
                    modificationsMade.text.toString(),
                    bhpAmount.text.toString(),
                    torqueTotal.text.toString(),
                    CostOfModifications.text.toString(),
                    acceleration.text.toString())
                if (isUpdate == true)
                    showToast("Data Updated Successfully")
                else
                    showToast("Data Not Updated")
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(e.message.toString())
            }
        }
    }



    //####################################################################################################################################//
    /**
     * When our handleDeletes button is clicked
*/
    fun handleDeletes() {
        deleteBtn.setOnClickListener {
            try {
                dbHelper.deleteData(carId.text.toString())
                clearEditTexts()
            } catch (e: Exception) {
                e.printStackTrace()
                showToast(e.message.toString())
            }
        }
    }


    //####################################################################################################################################//
    /**
     * When our View All is clicked
*/
    fun handleViewing() {
        viewBtn.setOnClickListener(
            View.OnClickListener {
                val res = dbHelper.allData
                if (res.count == 0) {
                    showDialog("Error", "No Data Found")
                    return@OnClickListener
                }

                val buffer = StringBuffer()
                while (res.moveToNext()) {
                    buffer.append("CARID :" + res.getString(0) + "\n")
                    buffer.append("CARNAMETYPE :" + res.getString(2) + "\n")
                    buffer.append("MODIFICATIONSMADE :" + res.getString(3) + "\n")
                    buffer.append("BHPAMOUNT :" + res.getString(4) + "\n")
                    buffer.append("TORQUETOTAL :" + res.getString(5) + "\n")
                    buffer.append("COSTOFMODIFICATIONS :" + res.getString(6) + "\n")
                    buffer.append("ACCELERATION :" + res.getString(7) + "\n\n")

                }
                showDialog("Data Listing", buffer.toString())



            }
        )
    }


    fun addCars(){
        //Audi Desc


        carlist.add(Cars("Audi","German Car group owned by Volkswagen Group founded 1932", R.drawable.audi) )

        //Bently Desc
        carlist.add(Cars("Bently","Bentley Motors Limited is a British manufacturer and marketer of luxury cars and SUVs", R.drawable.bently) )

        //BMW Desc
        carlist.add(Cars("BMW","Bavarian Motor Works, BMW or BMW AG, is a German automobile, motorcycle and engine manufacturing company founded in 1916.", R.drawable.bmw) )

        //Dodge Desc
        carlist.add(Cars("Dodge","Dodge is an American brand of automobile manufactured by FCA US LLC, based in Auburn Hills, Michigan.", R.drawable.dodge) )

        //Ford Desc
        carlist.add(Cars("Ford","Ford Motor Company, commonly known as Ford, is an American multinational automaker that has its main headquarters in Dearborn, Michigan, a suburb of Detroit", R.drawable.ford) )

        //Honda Desc
        carlist.add(Cars("Honda","Honda is not only known for producing durable, lasting cars but also for its safety on the roads. Honda holds an obligation of “safety for everyone” with each vehicle produced.", R.drawable.honda) )

        //Jaguar Desc
        carlist.add(Cars("Jaguar","aguar Cars is a brand of cars made by Jaguar Land Rover. This is a British car builder, owned by the Indian builder Cans Tata Motors since the beginning of 2008.", R.drawable.jaguar) )

        //Lotus Desc
        carlist.add(Cars("Lotus","Lotus Cars Limited is a British automotive company headquartered in Norfolk, England. It manufactures sports cars noted for their light weight and fine handling characteristics", R.drawable.lotus) )

        //Maserati Desc
        carlist.add(Cars("Maserati","Maserati is an Italian manufacturer specializing in ultra-luxury vehicles. With a reputation for high performance grand tourers, founded in 1993", R.drawable.maserati) )

        //Mazda Desc
        carlist.add(Cars("Mazda","Mazda Motor Corporation (Japanese: マツダ株式会社, Hepburn: Matsuda Kabushiki-gaisha) (commonly referred to as simply Mazda) is a Japanese multinational automaker based in Fuchū, Japan.", R.drawable.mazda) )

        //Mercedes-Benz Desc
        carlist.add(Cars("Mercedes-Benz","Mercedes-Benz is known for producing luxury vehicles and commercial vehicles. The headquarters is in Stuttgart, Baden-Württemberg. The name first appeared in 1926", R.drawable.mercedes) )

        //Mitsubishi Desc
        carlist.add(Cars("Mitsubishi","COMPANY HISTORY. Mitsubishi's automotive story begins in 1917 when the Mitsubishi Shipbuilding CO., LTD. introduced its very first car, the Mitsubishi Model-A.", R.drawable.mitsubishi) )

        //Nissan Desc
        carlist.add(Cars("Nissan","The name 'Nissan' originated during the 1930s as an abbreviation used on the Tokyo stock market for Nippon Sangyo. In 1930, Aikawa merged Tobata Casting's automobile parts department with DAT Motors.", R.drawable.nissan) )

        //Subaru Desc
        carlist.add(Cars("Subaru","The company we know today as Subaru was incorporated as Fuji Heavy Industries (FHI) on July 15, 1953, in Tokyo, Japan, and still has its headquarters there today.", R.drawable.subaru) )

        //Toyota Desc
        carlist.add(Cars("Toyota","The company was founded by Kiichiro Toyoda in 1937, as a spinoff from his father's company Toyota Industries, to manufacture automobiles.", R.drawable.toyota) )

        //Volvo Desc
        carlist.add(Cars("Volvo","Founded by Assar Gabrielsson and Gustaf Larsson, the company was formed on a background of quality and safety which were both of paramount importance.", R.drawable.volvo) )
    }




}
