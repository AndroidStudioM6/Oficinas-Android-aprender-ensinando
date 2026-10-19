package com.example.oficina_android
import androidx.room.TypeConverter

class PrioridadeConverter {

    @TypeConverter
    fun prioridadeParaString(prioridade: Prioridade): String {
        return prioridade.name
    }

    @TypeConverter
    fun stringParaPrioridade(valor: String): Prioridade {
        return Prioridade.valueOf(valor)
    }
}