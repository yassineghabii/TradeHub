import { OrdreAchat } from './OrdreAchat'; // Assurez-vous de spécifier le chemin correct
import {OrdreVente} from "./OrdreVente";
export class Titre {
  id_titre: number;
  symbole: string;
  nom: string;
  date_creation: string; // Vous devrez peut-être utiliser le type Date approprié en fonction de votre besoin
  prix_ouverture: number;
  prix_plus_haut: number;
  prix_plus_bas: number;
  prix_actuel: number;
  prix_cloture: number | null;
  quantite: number;
  date_maj: string; // Vous devrez peut-être utiliser le type Date approprié en fonction de votre besoin
  ordresAchat: OrdreAchat[];
  ordresVente: OrdreVente[];
}
