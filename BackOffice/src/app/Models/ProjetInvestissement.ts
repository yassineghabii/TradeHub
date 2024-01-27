export enum StatutProjet {
    ENCOURS = "ENCOURS",
    ANNULE = "ANNULE",
    ENATTENTE = "ENATTENTE",
    TERMINE="TERMINE"
  }

export class ProjetInvestissement {
    id: any;
    nom: any;
    description:any;
    date_debut:any;
    date_fin:any;
    cout_initial:any;
    localisation:any;
    responsable_projet:any; 
    taux_interet: any;
    statutProjet:StatutProjet;

}
