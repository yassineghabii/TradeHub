import {TypeOrdre} from "./OrdreAchat";
import {Transaction} from "./Transaction";
import {Titre} from "./Titre";

export class OrdreVente {
  idOrdreV: number;
  PrixAuMarcheV: number;
  prixLimiteV: number | null;
  Quantite: number;
  Date_ordreV: string; // Vous devrez peut-être utiliser le type Date approprié en fonction de votre besoin
  typeOrdre: TypeOrdre;
  Statut: StatutOrdre;
  transaction: Transaction;
  titre: Titre;


}
export enum StatutOrdre {
  EN_ATTENTE = 'EnAttente',
  EXECUTE = 'Executé',
  ANNULE = 'Annulé',
  PARTIELLEMENT_EXECUTE = 'Partiellement_Executé'
}
