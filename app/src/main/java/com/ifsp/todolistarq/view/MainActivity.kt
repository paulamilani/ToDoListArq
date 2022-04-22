package com.ifsp.todolistarq.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ifsp.todolistarq.R
import com.ifsp.todolistarq.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.principalFcv, ListaTarefasFragment(), "ListaTarefasFragment")
        }

        activityMainBinding.novaTarefaFab.setOnClickListener {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack("Tarefa")
                replace(R.id.principalFcv, TarefaFragment(), "TarefaFragment")
            }
        }
    }
}