export class OrdreVenteAuMarcheDTO {
  id_ordre_v: number;
  prix_au_marche_v: number; // Changer le type en number si nécessaire
  quantite: number;
  date_ordre_v: string; // Vous devrez peut-être utiliser un type de date approprié
  statut: string; // Assurez-vous que les valeurs de l'énumération StatutOrdre correspondent à vos besoins
  symbole : string ;

}
