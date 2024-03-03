package com.tushar.bookhub.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.health.HealthStats
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.compose.runtime.snapshots.SnapshotApplyResult
import androidx.compose.ui.platform.TextToolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import com.tushar.bookhub.Database.BookDatabase
import com.tushar.bookhub.Database.BookEntities
import com.tushar.bookhub.R
import org.json.JSONObject
import org.w3c.dom.Text
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {
    lateinit var BookImage:ImageView
    lateinit var BookName:TextView
    lateinit var BookAuthor:TextView
    lateinit var BookPrice:TextView
    lateinit var BookRating:TextView
    lateinit var AboutText:TextView
    lateinit var BookDescription:TextView
    lateinit var btnAddToFavourites:Button
    lateinit var progressBar: ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var toolbar: Toolbar



    var bookId:String?="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        BookImage = findViewById(R.id.imgBookImageDes)
        BookName = findViewById(R.id.txtDesBookName)
        BookPrice = findViewById(R.id.txtDesBookPrice)
        BookAuthor = findViewById(R.id.txtDesAuthorName)
        BookRating = findViewById(R.id.bookRating)
        AboutText = findViewById(R.id.txtAbout)
        BookDescription = findViewById(R.id.txtBookDes)
        btnAddToFavourites = findViewById(R.id.btndesaddtofav)
        progressBar=findViewById(R.id.progressBardes)
        progressLayout=findViewById(R.id.progressLayoutDes)
        progressBar.visibility=View.VISIBLE
        progressLayout.visibility=View.VISIBLE
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title="Book Details"






        if (intent != null) {
            bookId = intent.getStringExtra("BookId")
            print(bookId)
        }
        else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Unexpected Error has Occured",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Unexpected Error has Occured",
                Toast.LENGTH_SHORT
            ).show()
        }
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)
        val jsonRequest =object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                try {
                    val success = it.getBoolean("success")
                    if (success) {
                        val jsonBookObject = it.getJSONObject("book_data")
                        progressLayout.visibility=View.GONE

                        val bookImageUrl=jsonBookObject.getString("image")



                        BookName.text = jsonBookObject.getString("name")
                        BookDescription.text = jsonBookObject.getString("description")
                        BookAuthor.text = jsonBookObject.getString("author")
                        BookRating.text = jsonBookObject.getString("rating")
                        BookPrice.text = jsonBookObject.getString("price")
                        val bookEntities=BookEntities(
                            bookId?.toInt() as Int,
                            BookName.text.toString(),
                            BookAuthor.text.toString(),
                            BookPrice.text.toString(),
                            BookRating.text.toString(),
                            BookDescription.text.toString(),
                            bookImageUrl
                        )
                        val checkFav=DBAsyncTask(applicationContext,bookEntities,1).execute()

                        val favColor= ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                        val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)

                        val isFav=checkFav.get()
                        if(isFav){
                            btnAddToFavourites.text="Remove From Favourites"

                            btnAddToFavourites.setBackgroundColor(favColor)
                        }
                        else{
                            btnAddToFavourites.text="Add to Favourites"
                            val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                            btnAddToFavourites.setBackgroundColor(noFavColor)
                        }
                        btnAddToFavourites.setOnClickListener{
                            if(!DBAsyncTask(applicationContext,bookEntities,1).execute().get()){
                                val async =DBAsyncTask(applicationContext,bookEntities,2).execute()
                                val result=async.get()
                                if(result){
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Book added to favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    btnAddToFavourites.text="Remove from Favourites"
                                    btnAddToFavourites.setBackgroundColor(favColor)

                                }else{
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Some Error has Occured",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            else{
                                val async=DBAsyncTask(applicationContext,bookEntities,3).execute()
                                val result=async.get()
                                if(result){
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Book Removed From Favourites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    btnAddToFavourites.text="Add to Favourites"
                                    btnAddToFavourites.setBackgroundColor(noFavColor)

                                }
                                else{
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Some error has occured",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }



                    }


                }
                catch (e: Exception) {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Unexpected Error has Occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }, Response.ErrorListener {
                Toast.makeText(
                    this@DescriptionActivity,
                    "Unexpected Error has Occured",
                    Toast.LENGTH_SHORT
                ).show()


            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "9102e2e51be482"
                return headers

            }


        }
        queue.add(jsonRequest)
    }


    class DBAsyncTask(val context: Context,val bookEntities: BookEntities,val mode:Int) : AsyncTask<Void,Void,Boolean>(){
       /*
       Mode 1 -> Check DB if the book is favourite or not
       Mode 2 -> Save the book into DB as favourite
       Mode 3 -> Remove the favourite book
        */
        val db= Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {

            when(mode){

                1->{
                   // Check DB if the book is favourite or not
                    val book:BookEntities?=db.bookDao().getBookById(bookEntities.book_id.toString())
                    db.close()
                    return book!=null
                }
                2->{
                    //Save the book into DB as favourites
                    db.bookDao().insertBook(bookEntities)
                    db.close()
                    return true
                }
                3->{
                    //Remove the favourite book
                    db.bookDao().deleteBook(bookEntities)
                    db.close()
                    return true
                }
                
            }
           return false
        }

    }



}