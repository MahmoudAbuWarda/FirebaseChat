package com.example.firebasechat

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class ChatFragment : Fragment() {
    lateinit var adapter: MessageAdapter
    lateinit var auth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var rv :RecyclerView
    lateinit var messages:ArrayList<Message>

    lateinit var firebaseStorage:FirebaseStorage
    lateinit var attachBtn:Button
    var username:String?=null
    var modefied=0
    lateinit var msgEd:EditText
    //lateinit var msg:Message

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        msgEd = view.findViewById<EditText>(R.id.msgEd)
       msgEd.setText(" ")
         firebaseFirestore=  FirebaseFirestore.getInstance()


    //   firebaseFirestore.collection("messages").whereNotEqualTo("username",FirebaseAuth.getInstance().currentUser?.email).addSnapshotListener { value, error ->
        firebaseFirestore.collection("messages").orderBy("time") .addSnapshotListener { value, error ->

            value?.documentChanges?.forEach {


                if (it.type == DocumentChange.Type.ADDED || it.type == DocumentChange.Type.MODIFIED || it.type == DocumentChange.Type.REMOVED) {
                   // Log.d("ttt", "id: ${it.document.id.toString()}")
                   var msg = it.document.toObject(Message::class.java)
                    if(modefied==0){

                            messages.add(msg)
                            adapter.notifyItemInserted(messages.size - 1)
                            adapter.notifyItemRangeChanged(messages.size - 1, 1)
                            rv.scrollToPosition(messages.size - 1)

                    }

                    if (FirebaseAuth.getInstance().currentUser?.email.toString() != msg.username) {

                        if (msg.status != 4) {
                            msg.status = 4

                            firebaseFirestore.collection("messages").document(it.document.id)
                                .set(msg).addOnSuccessListener {
                                // Toast.makeText(view.context,"RRR",Toast.LENGTH_LONG).show()
                                    messages.add(msg)
                                    adapter.notifyDataSetChanged()

//                                    adapter.notifyItemInserted(messages.size - 1)
//                                    adapter.notifyItemRangeChanged(messages.size - 1, 1)
                                    rv.scrollToPosition(messages.size - 1)

                            }
                           // messages.remove(msg)

//                            adapter.notifyItemChanged(messages.size-1)
//                            adapter.notifyItemRangeChanged(messages.size-1,1)
//                            rv.scrollToPosition(messages.size-1)
                        }
//                        else{


//                        messages.add(msg)
//                        adapter.notifyItemInserted(messages.size - 1)
//                        adapter.notifyItemRangeChanged(messages.size - 1, 1)
//                        rv.scrollToPosition(messages.size - 1)





//                        }

//                    }else{
//                        if(msg.status==4){
//                        messages.add(msg)
//                            adapter.notifyItemInserted(messages.size-1)
//                            adapter.notifyItemRangeChanged(messages.size-1,1)
//                            rv.scrollToPosition(messages.size-1)}
//                    }



                    }

                    else if(FirebaseAuth.getInstance().currentUser?.email.toString() != msg.username){

                        messages.add(msg)
                        adapter.notifyItemInserted(messages.size - 1)
                        adapter.notifyItemRangeChanged(messages.size - 1, 1)
                        rv.scrollToPosition(messages.size - 1)
                    }


                }
            }
            modefied=1
        }
        /*   var products= arrayListOf<Product>()
           firebaseFirestore.collection("prodects").get().addOnCompleteListener {
               if(it.isSuccessful){
               it.result?.documents?.forEach {
                   var p=it.toObject(Product::class.java)
                   p?.id=it.id
                   products.add(p!!)



   //                Log.d("ttt",it["name"].toString())
   //                Log.d("ttt",it["price"].toString())
               }
                   for(i in products){
                       Log.d("ttt",i.name.toString())
                   }
               }

           }
           firebaseFirestore.collection("")
           var ctegorys= arrayListOf<Ctegory>()
           firebaseFirestore.collection("categorys").get().addOnCompleteListener {
               if(it.isSuccessful){
                   it.result?.documents?.forEach {
                      var category= it.toObject(Ctegory::class.java)
                       category?.id=it.id
                       ctegorys.add(category!!)
   //                    Log.d("ttt",it.id)
   //                    Log.d("ttt",it["name"].toString())
                   }
                   for(c in ctegorys){
                       Log.d("ttt",c.name.toString())
               }
           }

           }
           */
//firebaseFirestore.collection("prodects").whereEqualTo("categorys","MTs7ufI4Mi5WaytQb4BG").get().addOnSuccessListener {
//    it.documents.forEach {
//        var p =it.toObject(Product::class.java)
//        p?.id=it.id
//        Log.d("ttt",p?.name.toString())
//    }
//}

//
//        var product=Product(name="Apple",price = 20.0f,category = "MTs7ufI4Mi5WaytQb4BG")
//        firebaseFirestore.collection("prodects").document().set(product).addOnCompleteListener {
////            var decoument=firebaseFirestore.collection("prodects").document()
////            var id=decoument.id
////            Log.d()
//            if(it.isSuccessful){
//                Log.d("ttt","Success")
//            }else{
//                Log.d("ttt",it.exception.toString())
//            }
//        }



  //      firebaseFirestore.collection("prodects").document("0U4prXNjXaToyBN8OIqG").delete()

        auth = FirebaseAuth.getInstance()
       username=auth.currentUser?.displayName.toString()

         rv = view.findViewById<RecyclerView>(R.id.recyclerView)
         messages = ArrayList<Message>()
        adapter = MessageAdapter(messages)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        var sendBtn = view.findViewById<Button>(R.id.sendMsgBtn)

        sendBtn.setOnClickListener {


            if(msgEd.text.toString().isNullOrEmpty() ){
                Toast.makeText(view.context,"Please Enter your messgae",Toast.LENGTH_LONG).show()
            }else{
           var  msg=Message(FirebaseAuth.getInstance().currentUser?.email,username, msgEd.text.toString(), System.currentTimeMillis())
           //     var msg=Message("m@m.com",username, msgEd.text.toString(), System.currentTimeMillis())
               messages.add(msg)
                adapter.notifyDataSetChanged()
            adapter.notifyItemInserted(messages.size-1)
            adapter.notifyItemRangeChanged(messages.size-1,1)
            rv.scrollToPosition(messages.size-1)
            msgEd.setText("")
                firebaseFirestore.collection("messages").document().set(msg)
                    .addOnSuccessListener {
                        msg.status=2
                        adapter.notifyDataSetChanged()
                    }
                    .addOnFailureListener {
                        msg.status=3
                        Toast.makeText(view.context,it.toString(),Toast.LENGTH_LONG).show()
                        adapter.notifyDataSetChanged()
                    }
                if(msg.status==4){
                    msg.status=4
                    adapter.notifyDataSetChanged()
                }
        }
    }

         attachBtn=view.findViewById<Button>(R.id.attachMsgBtn)
        attachBtn.setOnClickListener {
            val gallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery,111)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       if( requestCode==111){
           var imageUri= data?.data
           firebaseStorage= FirebaseStorage.getInstance()
          var file= firebaseStorage.reference.child("images").child("attcahedFiles").child(UUID.randomUUID().toString()+".png")

                   file.putFile(imageUri!!).addOnSuccessListener {
            attachBtn.isEnabled=true
               attachBtn.setText("attach")
               Toast.makeText(context,"File Uploaded",Toast.LENGTH_LONG).show()

               file.downloadUrl.addOnSuccessListener {

                     var   msg=Message(FirebaseAuth.getInstance().currentUser?.email,username,msgEd.text.toString(),System.currentTimeMillis(),2,2,it.toString())


                   messages.add(msg)
                   adapter.notifyDataSetChanged()
                   rv.scrollToPosition(messages.size-1)

                   firebaseFirestore.collection("messages").document().set(msg)
                       .addOnSuccessListener {
                           msg.status=2
                         adapter.notifyDataSetChanged()
                       }
                       .addOnFailureListener {
                           msg.status=3
                          adapter.notifyDataSetChanged()
                       }

//                   adapter.notifyItemInserted(messages.size-1)
//                   adapter.notifyItemRangeChanged(messages.size-1,1)
//                   rv.scrollToPosition(messages.size-1)
               }

           }
                      .addOnFailureListener {
               attachBtn.isEnabled=true
               attachBtn.setText("attach")
               Toast.makeText(context,"Failed To Upload",Toast.LENGTH_LONG).show()
           }
                       .addOnProgressListener {
               attachBtn.isEnabled=false
               attachBtn.setText("Uploading")

           }
       }

    }


}