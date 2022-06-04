/*import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

const app = admin.initializeApp();
const db = app.firestore();
const collIrregularidades = db.collection("Irregularidade");

interface CallableResponse{
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
/*function analyzeIrregularidade(p: Irregularidade) : number {
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
  /*function getErrorMessage(code: number) : string {
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
      // criando o objeto que representa o produto (baseado nos parametros)
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
          "- Erro ao inserir novo produto:" +
          errorCode.toString()),

        result = {
          status: "ERROR",
          message: errorMessage,
          payload: JSON.parse(JSON.stringify({docId: null})),
        };
        console.log(result);
      } else {
        // cadastrar a irregularidade pois está ok.
        const docRef = await collIrregularidades.add(i);
        result = {
          status: "SUCCESS",
          message: "Irregularidade inserido com sucesso.",
          payload: JSON.parse(JSON.stringify({docId: docRef.id.toString()})),
        };
        functions.logger.error("addNewIrregularidade - Nova irregularidade inserida");
      }

      // Retornando o objeto result.
      return result;
    });

    

*/