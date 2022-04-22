package com.ifsp.todolistarq.model.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ifsp.todolistarq.model.entity.Tarefa

interface TarefaDao {
    @Insert
    fun inserirTarefa(tarefa: Tarefa): Long

    @Delete
    fun removerTarefa(tarefa: Tarefa)

    @Delete
    fun removerTarefas(vararg tarefa: Tarefa)

    @Update
    fun atualizarTarefa(tarefa: Tarefa)

    @Query("SELECT * FROM tarefa")
    fun recuperarTarefas(): List<Tarefa>

    @Query("SELECT * FROM tarefa WHERE id = :tarefaId")
    fun recuperaTarefa(tarefaId: Int): Tarefa
}