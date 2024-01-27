import {Component, Input, OnInit, SimpleChanges} from '@angular/core';
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {combineChange} from "@angular/fire/compat/firestore";
import {OrdreAchat} from "../../entities/OrdreAchat";
import {OrdreVente} from "../../entities/OrdreVente";
import {CarnetOrdre} from "../../entities/CarnetOrdre";
import {LigneCarnet} from "../../entities/LigneCarnet";
import {Titre} from "../../entities/Titre";
import {MariemService} from "../../services/mariem.service";

@Component({
  selector: 'app-carnet-ordres-modal-content',
  templateUrl: './carnet-ordres-modal-content.component.html',
  styleUrls: ['./carnet-ordres-modal-content.component.css']
})
export class CarnetOrdresModalContentComponent {
  @Input() combinedOrders: LigneCarnet[];
  titres: Titre[] = [];
  selectedSymbol: string | undefined; // Ajoutez cette ligne pour stocker le symbole


  getTotal(column: string): number {
    return this.combinedOrders.reduce((total, order) => total + order[column], 0);
  }

  constructor(public activeModal: NgbActiveModal , private mariemService: MariemService) {}


  protected readonly combineChange = combineChange;
  getPercentage(key: string) {
    const total = this.getTotal(key);
    const totalAchat = this.getTotal('nb_ordres_achat');
    const totalVente = this.getTotal('nb_ordres_vente');
    const totaux = totalAchat + totalVente;
    return (total / totaux) * 100;
  }




}
