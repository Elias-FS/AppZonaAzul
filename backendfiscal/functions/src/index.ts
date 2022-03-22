import * as functions from "firebase-functions";
import * as admin from "firebase-admin";



const app = admin.initializeApp();
const db = app.firestore();
const colPagamentos = db.collection("Pagamento");
const colTicket = db.collection("Ticket")
const colIrregularidades = db.collection("Irregularidade");

function placa_aleatória():String {
  return (
  "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[Math.floor(Math.random()*26)] +
  "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[Math.floor(Math.random()*26)] +
  "ABCDEFGHIJKLMNOPQRSTUVWXYZ"[Math.floor(Math.random()*26)] +
  '-' +
  Math.floor(Math.random()*9999))

}
//Adicionar irregularidade
export const addIrregularidades = functions
    .region("southamerica-east1")
    .https.onRequest(async (request, response) => {
      const cliente = {
        imagens: "imagens.png",
      };
      try {
        const docRef = colIrregularidades.add(cliente);
        response.send("Cliente inserido com sucesso: " + (await docRef).id);
      } catch (e) {
        functions.logger.error("Erro ao inserir o pneu de exemplo.");
        response.send("Erro ao inserir o cliente de exemplo");
      }
    });

// Adicionar pagamentos
export const addPagamentos = functions
    .region("southamerica-east1")
    .https.onRequest(async (request, response) => {
      const pagamentos = {
        placa_veiculo: "ABC-1234",
        cpf: "333.222.333-49",
        cnpj: "22222/333-4",
      };
      try {
        const docRef = colPagamentos.add(pagamentos);
        response.send("Cliente inserido com sucesso: " + (await docRef).id);
      } catch (e) {
        functions.logger.error("Erro ao inserir o pneu de exemplo.");
        response.send("Erro ao inserir o cliente de exemplo");
      }
    });

// Pegar todos os pagamentos
export const getAll = functions
    .region("southamerica-east1")
    .https.onRequest(async (request, response) => {
      const tires : FirebaseFirestore.DocumentData = [];
      const snapshot = await colPagamentos.get();
      snapshot.forEach((doc) => {
        tires.push(doc.data());
      });
      response.status(200).json(tires);
    });

export const getTickets = functions
    .region("southamerica-east1")
    .https.onRequest(async (request, response) => {
      const tickets : FirebaseFirestore.DocumentData = [];
      const snapshot = await colTicket.get();
      snapshot.forEach((doc) => {
        
        tickets.push(doc.data());
      });
      response.status(200).json(tickets);
    });


// Insert Ticket

export const addTicket = functions
    .region("southamerica-east1")
    .https.onRequest(async (request, response) => {
      const ticket = {
        placaVeiculo: placa_aleatória(),         // horário de brasília
        horaInicio: new Date(new Date().getTime() - 3600000 * 3).toUTCString().slice(17,26),
                                                // tempo limite: 1 hora
        horaFim: new Date(new Date().getTime() + 3600000).toUTCString().slice(17,26),
      };
      try {
        const docRef = colTicket.add(ticket);
        response.send([ticket,await docRef]);        
      } catch (e) {
        functions.logger.error("Erro na função addTicket");
        response.send("Erro na função addTicket");
      }
    });
