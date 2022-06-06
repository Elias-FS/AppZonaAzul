package br.com.appzonaazul.classes

import com.google.gson.annotations.SerializedName

class PayloadGenericResponse {


    enum class StatusType(val type: String) {
        ERROR("ERROR"),
        SUCCESS("SUCCESS");
    }

    @SerializedName("placa")
    var placaVeiculo: String? = null;
    @SerializedName("horaEntrada")
    var horaInicio: Any? = null;
    @SerializedName("horaSaida")
    var horaFim: Any? = null;

    override fun equals(other: Any?): Boolean {
        if (this === other)  return true
        if (javaClass != other?.javaClass) return false

        other as PayloadGenericResponse

        if (placaVeiculo != other.placaVeiculo) return false
        if (horaInicio != other.horaInicio) return false
        if (horaFim != other.horaFim) return false

        return true
    }

    override fun hashCode(): Int {
        var result = placaVeiculo?.hashCode() ?: 0
        result = 31 * result + (horaInicio?.hashCode() ?: 0)
        result = 31 * result + (horaFim?.hashCode() ?: 0)
        return result
    }
}

