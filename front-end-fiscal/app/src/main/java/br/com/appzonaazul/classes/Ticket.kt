package br.com.appzonaazul.classes

import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase
import java.util.*

data class Ticket (
    var placaVeiculo: String,
    var horaInicio: Timestamp?,
    var horaFim: Timestamp?
){
    fun timeStringFormat(h:Int,m:Int,s:Int):String{
        var string = ""
        if(h.toString().length==1) string = "0" + h.toString() + ":"
        else string = h.toString() + ":"
        if(m.toString().length==1) string = string + "0" + m.toString() + ":"
        else string = string + m.toString() + ":"
        if(s.toString().length==1) string = string + "0" + s.toString()
        else string = string + s.toString()
        return string
    }

    /*fun tempoDecorrido():String{
        val hInicio = horaInicio.subSequence(0,2).toString().toInt()
        val mInicio = horaInicio.subSequence(3,5).toString().toInt()
        val sInicio = horaInicio.subSequence(6,8).toString().toInt()
        val tempoAtual = Date().toString()
        println(tempoAtual)
        println(tempoAtual.length)
        var hFim = tempoAtual.subSequence(11,13).toString().toInt() -1
        var mFim = tempoAtual.subSequence(14,16).toString().toInt() + 59
        val sFim = tempoAtual.subSequence(17,19).toString().toInt() + 60
        if (hFim<hInicio) hFim = hFim+ 24

        var sDecorrido = sFim - sInicio
        if (sDecorrido>=60){
            sDecorrido = sDecorrido%60
            mFim = mFim+1 }

        var mDecorrido = mFim - mInicio
        if (mDecorrido>=60){
            mDecorrido = mDecorrido%60
            hFim = hFim + 1}

        var hDecorrido = hFim - hInicio

        return timeStringFormat(hDecorrido,mDecorrido,sDecorrido)
    }*/


}

