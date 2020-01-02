package com.mobile.fkpn.content.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobile.fkpn.R
import de.blox.treeview.TreeView

class BinaryActivity : AppCompatActivity() {

    private var nodeCount = 0
    private lateinit var treeView: TreeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binary)

//        treeView = findViewById(R.id.treeView)
    }
}
