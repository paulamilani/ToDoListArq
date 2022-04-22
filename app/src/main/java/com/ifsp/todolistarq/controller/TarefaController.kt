package com.ifsp.todolistarq.controller

import androidx.room.Room
import com.ifsp.todolistarq.model.database.ToDoListArqDatabase
import com.ifsp.todolistarq.model.entity.Tarefa
import com.ifsp.todolistarq.view.TarefaFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TarefaController(private val tarefaFragment: TarefaFragment) {
    private val database: ToDoListArqDatabase
    init {
        database = Room.databaseBuilder(
            tarefaFragment.requireContext(),
            ToDoListArqDatabase::class.java,
            ToDoListArqDatabase.Constantes.DB_NAME
        ).build()
    }

    private val escopoCorrotina = CoroutineScope(Dispatchers.IO)

    fun atualizaTarefa(tarefa: Tarefa){
        escopoCorrotina.launch {
            database.getTarefaDao().atualizarTarefa(tarefa)
            tarefaFragment.retornaTarefa(tarefa)
        }
    }

    fun insereTarefa(tarefa: Tarefa){
        escopoCorrotina.launch {
            database.getTarefaDao().inserirTarefa(tarefa)
            tarefaFragment.retornaTarefa(
                Tarefa(
                    tarefa.id,
                    tarefa.nome,
                    tarefa.realizada
                )
            )
        }
    }
}