import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const colTicket = db.collection("Ticket")
const collIrregularidades = db.collection("Irregularidade");
const colZonaAzul = db.collection("ZonaAzul");
const payment = db.collection("payment");



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


//Pegar itinerario pela placa
export const findByPlate = functions
  .region("southamerica-east1")
  .https.onCall(async (data, context) => {
    let result: CallableResponse

    //Array de tickets
    // const itinerario: Array<Tickets> = [];

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
    const itinerario: Array<Tickets> = [];
    const snapshot = await colTicket.get();

    let tempMarker: Tickets;
    snapshot.forEach((doc) => {
      const d = doc.data();
      tempMarker = {
        horaInicio: d.horaInicio,
        horaFim: d.horaFim,
        placaVeiculo: d.placaVeiculo
      };
      itinerario.push(tempMarker);
    });
    return itinerario;
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


//Adicionar itinerario
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
        "- Erro ao inserir novo itinerario:" +
        errorCode.toString()),

        result = {
          status: "ERROR",
          message: errorMessage,
          payload: JSON.parse(JSON.stringify({ docId: null })),
        };
      console.log(result);
    } else {
      // cadastrar a itinerario pois está ok.
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

export const getTickets = functions
  .region("southamerica-east1")
  .https.onRequest(async (request, response) => {
    const tickets: FirebaseFirestore.DocumentData = [];
    const snapshot = await colTicket.get();
    snapshot.forEach((doc) => {
      tickets.push(doc.data());
    });
    response.status(200).json(tickets);
  });


export const getZonaAzul = functions
  .region("southamerica-east1")
  .https.onRequest(async (request, response) => {
    const itinerario: FirebaseFirestore.DocumentData = [];
    const snapshot = await colZonaAzul.get();
    snapshot.forEach((doc) => {

      itinerario.push(doc.data());
    });
    response.status(200).json(itinerario);
  });

function getRandomInt(min: number, max: number) {
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random()*(max - min + 1)) + min;
}

function verifyCard(card: cardInformation) {
  let error: number;
  functions.logger.info(card.cardNumber.toString().length);
  error = 0;
  if (card.cardNumber == null ||  card.cardNumber.toString().length != 16) {
    error = 1;
  }
  if (card.cvv == null || card.cvv.toString().length != 3) {
    error = 1;
  }
  if (card.date == null) {
    error = 1;
  }
  if (card.name == null) {
    error = 1;
  }
  if (card.value == null) {
    error = 1;
  }
  return error;
}

function generateToken(size: number) {
  let token = "";
  const alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
    "abcdefghijklmnopqrstuvwxyz0123456789";
  const n = Math.floor(size);
  if (n >= 8) {
    let count = 0;
    while (count < n) {
      token += alfabeto.charAt(getRandomInt(0, (alfabeto.length -1)));
      count++;
    }
    return token;
  } else {
    return "";
  }
}

/**
 * Verifica se houve algum erro no cartão
 * @param {number} error - código de erro
 * @return {string} mensagem de erro ou sucesso
 */
function errorPayment(error: number) {
  let message: string;
  message = "";
  if (error === 0) {
    message = "Sucesso";
  }
  if (error === 1) {
    message = "Dados do cartão inválidos";
  }

  return message;
}

export const paymentSimulator = functions
    .region("southamerica-east1")
    .https.onCall((data, context) => {
      const tokenGenerated = generateToken(64);
      const resp : SimulatorResponse = {
        token: tokenGenerated,
        type: "",
        message: "",
      };
      const card: cardInformation = {
        cardNumber: data.cardNumber,
        cvv: data.cvv,
        name: data.name,
        date: data.date,
        value: data.value,
      };

      resp.message = errorPayment(verifyCard(card));

      if (resp.message === "Sucesso") {
        if (getRandomInt(0, 1) === 0) {
          resp.type = "TRANSACAO_NAO_AUTORIZADA";
          resp.message = "Transação negada";
        } else {
          resp.type = "TRANSACAO_AUTORIZADA";
        }
      } else {
        resp.type = "TRANSACAO_NAO_EFETUADA";
      }
      payment.add(resp);
      return resp;
    });
export const addMessagingToken = functions
    .region("southamerica-east1")
    .https.onCall(async (data,context) => {
      let result: CallableResponse;

    // com o uso do logger, podemos monitorar os erros e o que há.
      functions.logger.info("addMessagingToken - Iniciada.");

      const i = data.token
      // inclua aqui a validacao.
      const errorCode = !i;
      if (errorCode) {
        // gravar o erro no log e preparar o retorno.
        functions.logger.error("addMessagingToken " +
          "- Erro ao registrar token:"),
          result = {
            status: "ERROR",
            message: 'No token',
            payload: JSON.parse(JSON.stringify({ docId: null })),
          };
        console.log(result);
      }
      else {
        result = {
          status:"ERROR",
          message:"Bad Call",
          payload: JSON.parse(JSON.stringify({ docId: null })) + data.token.toString() + data.placaVeiculo.toString()
        }
        // registrar token pois está ok.
        const snapshot = await colTicket.get();
        snapshot.forEach(async (doc) => {
          const d = doc.data()
          let plateTicket: Tickets = {
            placaVeiculo: d.placaVeiculo,
            horaInicio: d.horaInicio,
            horaFim: d.horaFim
          }

          if (data.placaVeiculo === plateTicket.placaVeiculo) {
            const docRef = await colTicket.doc(doc.id)
            docRef.update({token: i})
            result = {
              status:"SUCCESS",
              message:"Token registrada",
              payload: JSON.parse(JSON.stringify({ docId: docRef}))
            }
          }
        });
      }
    return result;
  }
)
    export const funcaoTeste = functions.
    region("southamerica-east1").
    https.onCall((data, context) => {
      functions.logger.info("Hello logs!");
      const p ={
        teste: "teste",
      };
      payment.add(p);
    });


    function validateTime(p: Tickets): CallableResponse {
      let result: CallableResponse;
    
      const now: FirebaseFirestore.Timestamp =
      admin.firestore.Timestamp.now();
    
      if (p.horaFim.getSeconds() < now.seconds) {
        result = {
          status: "ERROR",
          message: "Tempo expirado",
          payload: JSON.parse(JSON.stringify({
            placa: p.placaVeiculo,
            horaEntrada: p.horaInicio,
            horaSaida: p.horaFim,
          })),
        };
      } else {
        result = {
          status: "SUCCESS",
          message: "Placa válida",
          payload: JSON.parse(JSON.stringify({
            placa: p.placaVeiculo,
            horaEntrada: p.horaInicio,
            horaSaida: p.horaFim,
          })),
        };
      }
      return result;
    }
    

    
    export const searchTicket = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
      const snapshot = await colTicket.get();
      let result: CallableResponse;

      let p: Tickets;

      result = {
        status: "NOTFOUND",
        message: "Veículo não registrado",
        payload: JSON.parse(JSON.stringify({placa: null})),
      };

      snapshot.forEach((doc) => {
        const d = doc.data();
        const plateTicket: Tickets = {
          placaVeiculo: d.placaVeiculo,
          horaInicio: d.horaEntrada,
          horaFim: d.horaSaida,
        };

        if (data.placa === plateTicket.placaVeiculo) {
          p = {
            placaVeiculo: plateTicket.placaVeiculo,
            horaInicio: plateTicket.horaInicio,
            horaFim: plateTicket.horaFim,
          };
          result = validateTime(p);
        }
      });

      return result;
    });