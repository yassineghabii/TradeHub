export class LigneCarnet {
  prix_achat: number;
  quantite_achat: number;
  nb_ordres_achat: number;
  prix_vente: number;
  quantite_vente: number;
  nb_ordres_vente: number;

  constructor(
    prix_achat: number,
    quantite_achat: number,
    nb_ordres_achat: number,
    prix_vente: number,
    quantite_vente: number,
    nb_ordres_vente: number
  ) {
    this.prix_achat = prix_achat;
    this.quantite_achat = quantite_achat;
    this.nb_ordres_achat = nb_ordres_achat;
    this.prix_vente = prix_vente;
    this.quantite_vente = quantite_vente;
    this.nb_ordres_vente = nb_ordres_vente;
  }
}
