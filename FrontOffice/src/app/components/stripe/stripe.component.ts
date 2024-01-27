// stripe.component.ts

import { Component, Renderer2 } from '@angular/core';
import { WalletService } from '../../services/walletservice';
import { FormsModule } from '@angular/forms';  // Importez FormsModule

@Component({
  selector: 'app-stripe',
  templateUrl: './stripe.component.html',
  styleUrls: ['./stripe.component.css']
})
export class StripeComponent {

  cardDetails: any = {
    number: '',
    expMonth: null,
    expYear: null,
    cvc: '',
    amount: null,
    currency: ''
  };
  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }
  constructor(private walletService: WalletService) {}


  chargeWallet(): void {

    // Votre logique pour appeler le service chargeWallet avec les détails de la carte
    this.walletService.chargeWallet(this.getUserId(), this.cardDetails).subscribe(response => {
      // Gérez la réponse du service, par exemple, affichez un message de succès
      console.log('Paiement réussi !', response);
    });
  }
  formatCardNumber(event: Event): void {
    const inputValue = (event.target as HTMLInputElement).value;

    // Supprimez tout sauf les chiffres
    const sanitizedCardNumber = inputValue.replace(/\D/g, '');

    // Limitez la longueur à 16 chiffres
    const formattedCardNumber = sanitizedCardNumber.slice(0, 16);

    // Ajoutez un espace tous les 4 chiffres
    const spacedCardNumber = formattedCardNumber.replace(/(\d{4})/g, '$1 ');

    // Mettez à jour le modèle avec la valeur formatée
    this.cardDetails.number = spacedCardNumber;

    // Vérifiez si la longueur est égale à 16 chiffres
    if (this.cardDetails.number.length === 16) {
      // Autres actions à effectuer si nécessaire
    }
  }

}
