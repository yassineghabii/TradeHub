import { Component, OnInit ,ViewChild } from '@angular/core';
import { MariemService } from "../../services/mariem.service";
import { Titre } from "../../entities/Titre"
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {OrdreAchat} from "../../entities/OrdreAchat";
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {CarnetOrdresModalContentComponent} from "../carnet-ordres-modal-content/carnet-ordres-modal-content.component";
import {OrdreVente} from "../../entities/OrdreVente";
import {CarnetOrdre} from "../../entities/CarnetOrdre";
import {LigneCarnet} from "../../entities/LigneCarnet";
import {SymboleDTO} from "../../entities/SymboleDTO";


@Component({
  selector: 'app-trade',
  templateUrl: './trade.component.html',
  styleUrls: ['./trade.component.css']
})
export class TradeComponent implements OnInit {
  @ViewChild('confirmationModal') private confirmationModal;
  combinedOrders: LigneCarnet[] = [];

  ordreFormGroup: FormGroup;
  modalRef: NgbModalRef; // Ajoutez cette propriété à la classe de votre composant.
  hasSelectedScenario: boolean = false;


  titres: Titre[] = [];
  selectedTitre: Titre | undefined;
  messageSucces: string = '';
  errorMessage: string = ''; // Ajoutez cette ligne pour déclarer la variable errorMessage
  ordreForm: any = {
    quantite: 0,
    type_ordre: 'AU_MARCHE',
    prix_limite_a: 0.001, // Champs pour le prix limite d'achat
    prix_limite_v: 0.001, // Champs pour le prix limite de vente
    duree_validite: 'JOUR_MEME'
  };
  ordreScenario: string = '';


  constructor(private mariemService: MariemService, private modalService: NgbModal, private router: Router, private fb: FormBuilder) {
  }


  ngOnInit(): void {
    this.getAllTitres();
    this.createForm();
    this.ordreScenario = null;
    this.hasSelectedScenario = false; // S'assurer que le drapeau est faux au début
    console.log('OnInit ordreScenario: ', this.ordreScenario); // Vérifier la valeur initiale
  }

  get ordreFormControls() {
    return this.ordreFormGroup.controls;
  }

  closeModal(): void {
    if (this.modalRef) {
      this.modalRef.close();
    }
  }

  openCarnetOrdresModal(idTitre: number, event: Event): void {
    event.stopPropagation();
    this.mariemService.getCarnetOrdres(idTitre).subscribe(
      (carnetOrdres: LigneCarnet[]) => {
        this.combinedOrders = carnetOrdres;

        console.log('Données de carnet d\'ordres récupérées :', carnetOrdres);

        // Récupérer le symbole correspondant à l'ID du titre
        this.mariemService.obtenirSymboleParIdTitre(idTitre).subscribe(
          (symboleDTO: SymboleDTO) => {
            const modalRef = this.modalService.open(CarnetOrdresModalContentComponent, {centered: true});
            modalRef.componentInstance.combinedOrders = carnetOrdres;
            modalRef.componentInstance.selectedSymbol = symboleDTO.symbole; // Utiliser symboleDTO.symbole pour envoyer le symbole à votre modal
          },
          error => {
            console.error('Erreur lors de la récupération du symbole :', error);
          }
        );
      },
      error => {
        console.error('Erreur lors de la récupération du carnet d\'ordres :', error);
      }
    );
  }

  openConfirmationModal() {
    this.modalRef = this.modalService.open(this.confirmationModal, {centered: true});
  }

  dismissModal(event?: Event): void {
    if (event) {
      event.preventDefault();
    }
    if (this.modalRef) {
      this.modalRef.dismiss();
    }
  }

  createForm() {
    this.ordreFormGroup = this.fb.group({
      type_ordre: ['AU_MARCHE', Validators.required], // Liste déroulante pour le type d'ordre
      duree_validite: ['JOUR_MEME', Validators.required], // Liste déroulante pour la durée de validité
      prix_limite_a: [0.001, [Validators.required, Validators.min(0.001)]],
      prix_limite_v: [0.001, [Validators.required, Validators.min(0.001)]],
      quantite: [0, [Validators.required, Validators.min(1)]],
      ordreScenario: [null, Validators.required],
      // Ajoutez d'autres champs que vous souhaitez valider
    });

  }


  getAllTitres(): void {
    this.mariemService.getAllTitres().subscribe(
      titres => {
        this.titres = titres;
        console.log('Liste de titres:', this.titres);
      },
      error => {
        console.error('Erreur lors de la récupération des titres:', error);
      }
    );
  }

  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }


  open(content: any) {
    this.modalService.open(content, {centered: true});
  }


  openOrdreModal(content: any, titre: Titre): void {
    console.log('Before openOrdreModal ordreScenario: ', this.ordreScenario); // Vérifier avant exécution


    this.selectedTitre = titre;
    this.ordreScenario = ''; // Réinitialiser à une chaîne vide
    this.hasSelectedScenario = false; // Réinitialiser le flag à faux


    this.ordreForm = new OrdreAchat();
    this.messageSucces = ''; // Réinitialisation de messageSucces
    this.errorMessage = ''; // Réinitialisation de errorMessage


    this.modalRef = this.modalService.open(content, {centered: true});
    this.modalRef.result.then((result) => {
      // Pas besoin de réinitialiser ici car le formulaire est supposé être soumis avec succès.
      this.router.navigate(['/trade']);
    }, (reason) => {
      // Assurez-vous que le scénario et les messages sont réinitialisés même lorsque le modal est fermé ou annulé
      this.ordreScenario = ''; // Réinitialiser à une chaîne vide
      this.hasSelectedScenario = false; // Réinitialiser le flag à faux
      this.resetForm();
      this.router.navigate(['/trade']);
    });
  }

  resetForm(): void {
    // Réinitialise complètement le formulaire, y compris le scénario.
    this.ordreFormGroup.reset({
      type_ordre: 'AU_MARCHE',
      duree_validite: 'JOUR_MEME',
      prix_limite_a: 0.001,
      prix_limite_v: 0.001,
      quantite: 0,
      ordreScenario: null // ou ''
    });


    // Réinititalise également le drapeau de sélection du scénario
    this.hasSelectedScenario = false;


    // Réinitialise les messages et le titre sélectionné
    this.messageSucces = '';
    this.errorMessage = '';
  }


  selectionnerScenario(scenario: string) {
    this.hasSelectedScenario = true;
    this.ordreScenario = scenario;


    console.log('Before selectionnerScenario ordreScenario: ', this.ordreScenario);


    this.resetForm();
    this.messageSucces = ''; // Si vous souhaitez également réinitialiser ce message
    this.ordreFormGroup.controls['ordreScenario'].setValue(scenario);


    const isAchat = scenario === 'ACHAT';
    this.ordreFormGroup.get('prix_limite_a').reset(0.001);
    this.ordreFormGroup.get('prix_limite_v').reset(0.001);


    if (isAchat) {
      this.ordreFormGroup.get('prix_limite_v').disable();
      this.ordreFormGroup.get('prix_limite_a').enable();
    } else {
      this.ordreFormGroup.get('prix_limite_a').disable();
      this.ordreFormGroup.get('prix_limite_v').enable();
    }
  }

  getPrixLimiteFieldName(): string {
    return this.ordreScenario === 'ACHAT' ? 'prix_limite_a' : 'prix_limite_v';
  }


  validerOrdre(): void {
    console.log('Scénario :', this.ordreScenario);


    // Logique commune à l'achat et à la vente
    // ...
    if (this.ordreFormGroup.valid) {
      this.openConfirmationModal();
    }


  }

  confirmOrder(): void {
    this.closeModal(); // Fermez d'abord le modal de confirmation
    if (this.ordreScenario === 'ACHAT') {
      this.validerOrdreAchat();
    } else if (this.ordreScenario === 'VENTE') {
      this.validerOrdreVente();
    }
  }


  // Logique spécifique à l'achat
  private validerOrdreAchat(): void {
    // Ajoutez la logique spécifique à l'achat ici
    console.log('Logique spécifique à l\'achat');


    // Appel de la méthode passerOrdreAchat de votre service MariemService avec les données du formulaire
    this.mariemService.passerOrdreAchat(this.selectedTitre.id_titre, this.ordreFormGroup.getRawValue(), this.getUserId()).subscribe(
      (response) => {
        console.log('Réponse après avoir passé l\'ordre d\'achat :', response);
        this.messageSucces = 'Ordre d\'achat passé avec succès.';
        this.router.navigate(['/trade']);
      },
      (error) => {
        console.error('Erreur lors de la soumission de l\'ordre d\'achat :', error);
        this.router.navigate(['/trade']);
      }
    );
  }


// Logique spécifique à la vente
  private validerOrdreVente(): void {
    // Ajoutez la logique spécifique à la vente ici
    console.log('Logique spécifique à la vente');

    // Appel de la méthode passerOrdreVente de votre service MariemService avec les données du formulaire
    this.mariemService.passerOrdreVente(this.selectedTitre.id_titre, this.ordreFormGroup.getRawValue(), this.getUserId()).subscribe(
      (response) => {
        console.log('Réponse après avoir passé l\'ordre de vente :', response);
        this.messageSucces = 'Ordre de vente passé avec succès.';
        this.router.navigate(['/trade']);
      },
      (error) => {
        console.error('Erreur lors de la soumission de l\'ordre de vente :', error);

        // Gérer l'erreur selon le statut HTTP reçu
        switch (error.status) {
          case 400:
            this.errorMessage = ' Verifie la quantité et l\'existance du titre dans votre portefeuille ';
            break;
          case 404:
            this.errorMessage = '  Ressource non trouvée. Veuillez réessayer plus tard';
            break;
          case 500:
            this.errorMessage = ' Une erreur est survenue. Veuillez réessayer plus tard ';
            break;
          default:
            this.errorMessage = ' Une erreur est survenue. Veuillez réessayer plus tard.';
            break;
        }
      }
    ); // Ajout de la parenthèse fermante pour la méthode subscribe
  } // Ajout de l'accolade fermante pour la fonction validerOrdreVente
}
