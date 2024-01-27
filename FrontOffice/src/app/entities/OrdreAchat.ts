import firebase from "firebase/compat";

export class OrdreAchat {
  idOrdreA: number;
  PrixAuMarcheA: number;
  prix_limitea: number | null;
  quantite: number;
  Date_ordreA: string;
  type_ordre: TypeOrdre;
  Statut: string;
}
export enum TypeOrdre {
  AU_MARCHE = 'AU_MARCHE',
  LIMITE = 'LIMITE'
}
