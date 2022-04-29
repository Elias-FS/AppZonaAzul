import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();

const colTicket = db.collection("Ticket")

//Endpoint pegar todos os tickets
export const getAllTickets = functions
    .region("southamerica-east1")
    .https.onCall(async () => {
      // retorna todos os produtos
      const ticket : Array<Tickets> = [];
      const snapshot = await colTicket.get();
      let tempProduct: Tickets;
      snapshot.forEach((doc) => {
        const d = doc.data();
        tempProduct = {
          horaInicio: d.horaInicio,
          horaFim: d.horaFim,
          placaVeiculo: d.placaVeiculo
        };
        ticket.push(tempProduct);
      });
      return ticket;
    });

   
    //Adicionar novo ticket
    export const addNewTicket = functions
    .region("southamerica-east1")
    .https.onCall(async (data, context) => {
        
    });  


const ticket: Array<Tickets> = [];

function findByPlates(plate: String) {
    for (let i = 0; i < ticket.length; i++) {
        if (plate === ticket[i].placaVeiculo) {
            return 1;
        }
    }
    return 0;
}


    //Pegar ticket pela placa
    export const findByPlate = functions
    .region("southamerica-east1")
    .https.onCall(async () => {
      for(let i = 0; i < ticket.length; i++) {
          const plate = ticket[i].placaVeiculo
          const fBP = findByPlates(plate)
          return fBP
      }
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

