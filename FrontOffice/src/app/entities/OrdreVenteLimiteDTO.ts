export class OrdreVenteLimiteDTO {
  id_ordre_v: number;
  prix_limite_v: number;
  quantite: number;
  date_ordre_v: string; // Vous devrez peut-être utiliser un type de date approprié
  statut: string; // Assurez-vous que les valeurs de l'énumération StatutOrdre correspondent à vos besoins
  duree_validite: string; // Assurez-vous que les valeurs de l'énumération DureeValidite correspondent à vos besoins
  symbole : string ;

}
