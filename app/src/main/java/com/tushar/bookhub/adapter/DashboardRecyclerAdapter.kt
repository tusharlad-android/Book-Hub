package com.tushar.bookhub.adapter

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tushar.bookhub.Model.Book
import com.tushar.bookhub.R
import com.tushar.bookhub.activity.DescriptionActivity
import org.w3c.dom.Text

class DashboardRecyclerAdapter(val context:Context,val ItemList :  ArrayList<Book>):RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {
    class DashboardViewHolder(view: View):RecyclerView.ViewHolder(view){

        val bookName:TextView=view.findViewById(R.id.txtBookName)
        val authorName:TextView=view.findViewById(R.id.txtAuthorName)
        val bookPrice:TextView=view.findViewById(R.id.txtCost)
        val bookRating:TextView=view.findViewById(R.id.txtRating)
        val bookImage:ImageView=view.findViewById(R.id.imgBookImage)
        val bookList:LinearLayout=view.findViewById(R.id.booklist)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
      val view =LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ItemList.size
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = ItemList[position]
        holder.bookName.text=book.bookName
        holder.authorName.text=book.bookAuthor
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.bookImage);
        holder.bookPrice.text=book.bookPrice
        holder.bookRating.text=book.bookRating
        holder.bookList.setOnClickListener{
            val intent= Intent(context,DescriptionActivity::class.java)
            intent.putExtra("BookId",book.book_id)
            context.startActivity(intent)
        }
        



    }
}