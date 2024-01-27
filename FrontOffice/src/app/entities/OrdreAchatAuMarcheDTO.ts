import {Titre} from "./Titre";

export class OrdreAchatAuMarcheDTO {
  idOrdreA: number;
  prix_au_marche_a: number ;
  quantite: number;
  date_ordre_a: string; // Vous devrez peut-être utiliser un type de date approprié
  statut: string; // Assurez-vous que les valeurs de l'énumération StatutOrdre correspondent à vos besoins
  symbole : string ;
}
