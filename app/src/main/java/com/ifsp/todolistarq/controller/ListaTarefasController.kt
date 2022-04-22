package com.ifsp.todolistarq.controller

import androidx.room.Room
import com.ifsp.todolistarq.model.database.ToDoListArqDatabase
import com.ifsp.todolistarq.model.entity.Tarefa
import com.ifsp.todolistarq.view.ListaTarefasFragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListaTarefasController(private val listaTarefasFragment: ListaTarefasFragment) {
    private val database: ToDoListArqDatabase

    init {
        database = Room.databaseBuilder(
            listaTarefasFragment.requireContext(),
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()
    }

    fun buscarTarefas() {
        GlobalScope.launch {
            val listaTarefas = database.getTarefaDao().recuperarTarefas()
            listaTarefasFragment.atualizarListaTarefas(listaTarefas.toMutableList())
        }
    }

    fun removerTarefa(tarefa: Tarefa) {
        GlobalScope.launch {
            database.getTarefaDao().removerTarefa(tarefa)
        }
    }
}