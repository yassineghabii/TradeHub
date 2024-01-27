import { Component, OnInit } from '@angular/core';
import { ChtibaService } from '../../services/chtiba.service';
import {ConfirmModalComponent} from "../confirm-modal/confirm-modal.component";
import {MatDialog} from "@angular/material/dialog";

@Component({
  selector: 'app-cards',
  templateUrl: './cards.component.html',
  styleUrls: ['./cards.component.scss']
})
export class CardsComponent implements OnInit {
  listCards: any[] = [];
  searchStr: string = '';

  constructor(private chtibaService: ChtibaService,public dialog: MatDialog) { }

  ngOnInit(): void {
    this.getAllCards();
  }

  getAllCards() {
    this.chtibaService.getAllCards().subscribe(res => {
      console.log(res);
      this.listCards = res;
    });
  }
  deleteCard(id_card: number) {
    const cardToDelete = this.listCards.find(card => card.id === id_card);
    const cardInfo = cardToDelete ? `${cardToDelete.number}` : "";

    this.dialog.open(ConfirmModalComponent, {
      width: '400px',
      data: {
        action: 'supprimer',
        client: cardInfo,
        mode: 'delete'
      }
    }).afterClosed().subscribe(confirmResult => {
      if (confirmResult) {
        this.chtibaService.deleteCard(id_card).subscribe(() => {
          this.getAllCards();  // Recharger la liste après la suppression
        });
      }
    });
  }

  searchCards() {
    if (this.searchStr) {
      this.listCards = this.listCards.filter(card =>
          card.wallet.user.firstname.toLowerCase().includes(this.searchStr.toLowerCase()) ||
          card.wallet.user.lastname.toLowerCase().includes(this.searchStr.toLowerCase()) ||
          card.number.slice(-4).includes(this.searchStr)
      );
    } else {
      this.getAllCards();  // Si la barre de recherche est vide, affichez toutes les cartes
    }
  }

}
