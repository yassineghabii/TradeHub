
export class Transaction {
  id_transaction: number;
  prix_execution: number;
  date_execution: string; // Vous devrez peut-être utiliser le type Date approprié en fonction de votre besoin
  quantite_execute: number;
  type_transaction: TypeTransaction;

}
export enum TypeTransaction {
  ACHAT_AU_MARCHE = 'ACHAT_AU_MARCHE',
  VENTE_AU_MARCHE = 'VENTE_AU_MARCHE',
  ACHAT_LIMITE = 'ACHAT_LIMITE',
  VENTE_LIMITE = 'VENTE_LIMITE'
}
