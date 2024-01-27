import {Component, OnInit} from '@angular/core';
import {WalletService} from "../../services/walletservice";
import {Card} from "../../entities/Card";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent implements OnInit {
  cardDetails: any; // Assure-toi de définir correctement le type pour les détails de la carte
  userCards: Card[] = []; // Assure-toi que ce tableau contient les informations des cartes des utilisateurs

  constructor(private walletService: WalletService) { }

  ngOnInit(): void {
    const walletId = this.getWalletId();
    if (walletId) {
      this.getCardDetails(walletId);
    }
  }

  getWalletId(): number | null {
    const walletId = sessionStorage.getItem('id_wallet');
    return walletId ? Number(walletId) : null;
  }

  getCardDetails(walletId: number): void {
    this.walletService.getCardByIdWallet(walletId).subscribe(
      (data) => {
        this.cardDetails = data;
        // Maintenant, tu peux utiliser this.cardDetails pour afficher les détails de la carte dans ton template HTML
      },
      (error) => {
        console.error('Une erreur s\'est produite lors de la récupération des détails de la carte : ', error);
        // Gérer l'erreur, par exemple afficher un message d'erreur dans l'interface utilisateur
      }
    );
  }
}
