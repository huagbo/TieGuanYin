package com.bennyhuo.tieguanyin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bennyhuo.tieguanyin.R.layout
import com.bennyhuo.tieguanyin.utils.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var x = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        setTitle(this.javaClass.simpleName)

        openJavaActivity.setOnClickListener {
            x ++
//            JavaActivityBuilder.start(this@MainActivity, 1234, true){
//                java, kotlin ->
//                toast("Result From JavaActivity: java=$java, kotlin=$kotlin" )
//                textView.text = "Result From JavaActivity: java=$java, kotlin=$kotlin, x=$x"
//            }
        }

        openKotlinActivity.setOnClickListener {
            Log.d("Main", "leave: "+this@MainActivity.toString())
            startKotlinActivity(1234){
                java, kotlin ->
                toast("Result From JavaActivity: java=$java, kotlin=$kotlin" )
            }
        }

        openGenerateBothActivity.setOnClickListener {
            startGenerateBothActivity(30, "bennyhuo", num = 1234)
        }

        openFragmentContainerActivity.setOnClickListener {
            startFragmentContainerActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("test", "resultCode: $resultCode, data: $data")
        textView.text = "onActivityResult -- "
    }
}
