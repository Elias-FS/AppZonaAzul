import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const colPagamentos = db.collection("Pagamento");
const colTicket = db.collection("Ticket")
const colIrregularidades = db.collection("Irregularidade");

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


