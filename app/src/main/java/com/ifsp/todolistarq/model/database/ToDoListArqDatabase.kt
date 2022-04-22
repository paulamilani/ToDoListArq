package com.ifsp.todolistarq.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ifsp.todolistarq.model.dao.TarefaDao
import com.ifsp.todolistarq.model.entity.Tarefa

@Database(entities = arrayOf(Tarefa::class), version = 1)
abstract class ToDoListArqDatabase : RoomDatabase() {
    object Constantes {
        val DB_NAME = "to_do_list_arq_database"
    }

    abstract fun getTarefaDao(): TarefaDao
}