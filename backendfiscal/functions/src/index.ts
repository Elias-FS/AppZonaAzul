import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const colTicket = db.collection("Ticket")
const collIrregularidades = db.collection("Irregularidade");




/*function placa_aleatória(): String {
  return (
    +
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[Math.floor(Math.random() * 26)] +
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[Math.floor(Math.random() * 26)] +
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[Math.floor(Math.random() * 26)] +
    '-' +
    Math.floor(Math.random() * 9999))

}*/



interface CallableResponse {
  status: string,
  message: string,
  payload: JSON
}

/**
* Função que analisa se um produto é válido para ser gravado no banco.
* Exemplo de validação na entrada. Complemente com as regras que achar
* importante.
* @param {Irregularidade} p - Objeto produto a ser validado.
* @return {number} - Retorna 0 se válido ou o código de erro.
**/
function analyzeIrregularidade(p: Irregularidade): number {
  if (!p.photo) {
    return 1;
  }
  if (!p.status) {
    return 2;
  }
  return 0;
}

/**
 * Função que dado o código de erro obtido na analyzeIrregularidade,
 * devolve uma mensagem
 * @param {number} code - Código do erro
 * @return {string} - String com a mensagem de erro.
 */
function getErrorMessage(code: number): string {
  let message = "";
  switch (code) {
    case 1: {
      message = "Foto não foi inserida";
      break;
    }
    case 2: {
      message = "Seleciona o status da irregularidade";
      break;
    }
  }
  return message;
}




export const addNewIrregularidade = functions
  .region("southamerica-east1")
  .https.onCall(async (data, context) => {
    let result: CallableResponse;

    // com o uso do logger, podemos monitorar os erros e o que há.
    functions.logger.info("addNewIrregularidade - Iniciada.");
    // criando o objeto que representa a irregularidade (baseado nos parametros)
    const i = {
      photo: data.photo,
      status: data.status,
    };
    // inclua aqui a validacao.
    const errorCode = analyzeIrregularidade(i);
    const errorMessage = getErrorMessage(errorCode);
    if (errorCode > 0) {
      // gravar o erro no log e preparar o retorno.
      functions.logger.error("addNewIrregularidade " +
        "- Erro ao inserir nova irregularidade:" +
        errorCode.toString()),

        result = {
          status: "ERROR",
          message: errorMessage,
          payload: JSON.parse(JSON.stringify({ docId: null })),
        };
      console.log(result);
    } else {
      // cadastrar a irregularidade pois está ok.
      const docRef = await collIrregularidades.add(i);
      result = {
        status: "SUCCESS",
        message: "Irregularidade inserido com sucesso.",
        payload: JSON.parse(JSON.stringify({ docId: docRef.id.toString() })),
      };
      functions.logger.error("addNewIrregularidade - Nova irregularidade inserida");
    }

    // Retornando o objeto result.
    return result;
  });


//Pegar ticket pela placa
export const findByPlate = functions
  .region("southamerica-east1")
  .https.onCall(async (data, context) => {
    let result: CallableResponse

    //Array de tickets
   // const ticket: Array<Tickets> = [];

    const snapshot = await colTicket.get()

    const p = {
      placaVeiculo: data.placaVeiculo
    }

    result = {
      status: "ERROR",
      message: "Não foi encontrado",
      payload: JSON.parse(JSON.stringify({ placa: null })),
    };

    snapshot.forEach((doc) => {
      const d = doc.data()
      let plateTicket: Tickets = {
        placaVeiculo: d.placaVeiculo,
        horaInicio: d.horaInicio,
        horaFim: d.horaFim
      }

      if (p.placaVeiculo === plateTicket.placaVeiculo) {
        result = {
          status: "SUCCESS",
          message: "Placa encontrada",
          payload: JSON.parse(JSON.stringify({
            placa: plateTicket.placaVeiculo,
            horaEntrada: plateTicket.horaInicio,
            horaSaida: plateTicket.horaFim
          })),
        };
      }
    })
    return result;
  });

//Pegar todos os tickets
export const getAllTickets = functions
  .region("southamerica-east1")
  .https.onCall(async () => {
    // retorna todos os tickets
    const ticket: Array<Tickets> = [];
    const snapshot = await colTicket.get();

    let tempTicket: Tickets;
    snapshot.forEach((doc) => {
      const d = doc.data();
      tempTicket = {
        horaInicio: d.horaInicio,
        horaFim: d.horaFim,
        placaVeiculo: d.placaVeiculo
      };
      ticket.push(tempTicket);
    });
    return ticket;
  });


/**
* Função que analisa se um produto é válido para ser gravado no banco.
* Exemplo de validação na entrada. Complemente com as regras que achar
* importante.
* @param {Tickets} p - Objeto produto a ser validado.
* @return {number} - Retorna 0 se válido ou o código de erro.
**/
function analyzeTicket(p: Tickets): number {
  if (!p.placaVeiculo) {
    return 1;
  }
  return 0;
}

/**
 * Função que dado o código de erro obtido na analyzeIrregularidade,
 * devolve uma mensagem
 * @param {number} code - Código do erro
 * @return {string} - String com a mensagem de erro.
 */
function getErrorMessageTicket(code: number): string {
  let message = "";
  switch (code) {
    case 1: {
      message = "Ticket não foi inserido";
      break;
    }
  }
  return message;
}

//Adicionar ticket
export const addNewTicket = functions
  .region("southamerica-east1")
  .https.onCall(async (data, context) => {
    let result: CallableResponse;

    //Calcular hora da estadia
    const hour: FirebaseFirestore.Timestamp =
      admin.firestore.Timestamp.now();
    const saida: number = hour.toDate().getHours() + data.estadia;

    // com o uso do logger, podemos monitorar os erros e o que há.
    functions.logger.info("addNewTicket - Iniciada.");
    // criando o objeto que representa a irregularidade (baseado nos parametros)
    const i: Tickets = {
      horaInicio: hour.toDate(),
      horaFim: hour.toDate(),
      placaVeiculo: data.placaVeiculo,
    };
    i.horaFim.setHours(saida);

    // inclua aqui a validacao.
    const errorCode = analyzeTicket(i);
    const errorMessage = getErrorMessageTicket(errorCode);
    if (errorCode > 0) {
      // gravar oerro no log e preparar o retorno.
      functions.logger.error("addNewTicket " +
        "- Erro ao inserir novo ticket:" +
        errorCode.toString()),

        result = {
          status: "ERROR",
          message: errorMessage,
          payload: JSON.parse(JSON.stringify({ docId: null })),
        };
      console.log(result);
    } else {
      // cadastrar a ticket pois está ok.
      const docRef = await colTicket.add(i);
      result = {
        status: "SUCCESS",
        message: "Ticket inserido com sucesso.",
        payload: JSON.parse(JSON.stringify({ docId: docRef.id.toString() })),
      };
      functions.logger.error("addNewTicket - Nova Ticket inserida");
    }
    // Retornando o objeto result.
    return result;
  });
