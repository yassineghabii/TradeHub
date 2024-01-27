import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { WalletService } from '../../services/walletservice';
import { Wallet } from "../../entities/Wallet";
declare var Stripe: any;
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import { PaymentmodalComponent } from '../paymentmodal/paymentmodal.component';
import {NavigationStart, Router} from "@angular/router";
import {StripeCurrency} from "../../entities/CardDetailsDTO";
import {NgControl} from "@angular/forms";
import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
import {HistoriqueChargement} from "../../entities/HistoriqueChargement";
import {NotificationService} from "./NotificationService";
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import {Portfolio} from "../../entities/Portfolio";

pdfMake.vfs = pdfFonts.pdfMake.vfs;

@Component({
  selector: 'app-wallet',
  templateUrl: './wallet.component.html',
  styleUrls: ['./wallet.component.css']
})
export class WalletComponent implements OnInit {
  dialogRef: MatDialogRef<PaymentmodalComponent> | null;
  safeIframeUrl: SafeResourceUrl;
  portfolio: Portfolio; // Cette variable contiendra les données du portfolio

  resultFromBackend: any;
  deviseSourceField: StripeCurrency | undefined;
  deviseCibleField: StripeCurrency | undefined;
  pieChartSegments: { startAngle: number; endAngle: number;color: string;symbol: string; percent: number;   }[] = [];


  wallet: Wallet = new Wallet();
  montant: number | undefined;
  deviseSource: StripeCurrency | undefined;
  deviseCible: StripeCurrency | undefined;
  stripeCurrencies: string[] = Object.values(StripeCurrency);

  constructor(private walletService: WalletService,     private domSanitizer: DomSanitizer,
    private dialog: MatDialog, private router: Router,private notificationService: NotificationService) {
    this.router.events.subscribe(event => {
      console.log('Router Event:', event);
      this.safeIframeUrl = this.domSanitizer.bypassSecurityTrustResourceUrl("https://ssltools.investing.com/profit-calculator/index.php?force_lang=1&acc=12&pair=1");

      if (event instanceof NavigationStart && this.dialogRef) {
        console.log('Closing Dialog');
        this.dialogRef.close();
      }
    });  }
  updateMontant(event: any): void {
    this.montant = parseFloat(event.target.value);
  }
  describeArc(x: number, y: number, radius: number, startAngle: number, endAngle: number): string {
    const start = this.polarToCartesian(x, y, radius, endAngle);
    const end = this.polarToCartesian(x, y, radius, startAngle);
    const largeArcFlag = endAngle - startAngle <= 180 ? '0' : '1';

    const d = [
      'M', start.x, start.y,
      'A', radius, radius, 0, largeArcFlag, 0, end.x, end.y,
      'L', x, y,
      'Z' // Close the path
    ].join(' ');

    return d;
  }

  polarToCartesian(centerX: number, centerY: number, radius: number, angleInDegrees: number) {
    const angleInRadians = (angleInDegrees - 90) * Math.PI / 180.0;
    return {
      x: centerX + radius * Math.cos(angleInRadians),
      y: centerY + radius * Math.sin(angleInRadians)
    };
  }

  // Fonction pour obtenir les coordonnées où placer le label, si le pourcentage est suffisamment grand pour afficher le label
  getLabelCoordinates(x: number, y: number, radius: number, angleInDegrees: number) {
    const angleInRadians = (angleInDegrees * Math.PI) / 180.0;
    return {
      x: x + radius * Math.cos(angleInRadians),
      y: y + radius * Math.sin(angleInRadians)
    };
  }

  private loadPortfolio(): void {
    const id = this.getUserId(); // L'id de l'utilisateur - à modifier selon le code
    this.walletService.getPortfolioById(id).subscribe((data) => {
      this.portfolio = data;
      // une fois les données récupérées, transformez-les pour le graphique
      this.preparePieChart(this.portfolio);
    }, (error) => {
      console.error('Erreur lors de la récupération du portfolio:', error);
    });
  }
  preparePieChart(portfolio: Portfolio) {
    const total = portfolio.quantites.reduce((sum, current) => sum + current, 0);
    const colors = ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF']; // Palette de couleurs pour les segments du graphique
    let currentAngle = 0; // Start angle for the first segment

    this.pieChartSegments = portfolio.symboles.map((symbol, index) => {
      const value = portfolio.quantites[index];
      const percent = (value / total) * 100;

      const segment = {
        startAngle: currentAngle,
        endAngle: currentAngle + (percent / 100) * 360,
        color: colors[index % colors.length],
        symbol,
        percent
      };

      currentAngle += (percent / 100) * 360;
      return segment;
    });
  }
  getSvgArc(segment: any): string {
    return this.describeArc(16, 16, 15.5, segment.offset, segment.offset + segment.percent);
  }

  getSvgLabelCoordinates(segment: any) {
    return this.getLabelCoordinates(16, 16, 12, segment.offset + segment.percent / 2);
  }
  convertirDevise(): void {
    if (this.montant && this.deviseSource && this.deviseCible) {
      this.walletService.convertirDevise(this.montant, this.deviseSource, this.deviseCible)
        .subscribe(result => {
          this.resultFromBackend = result; // Stocker la valeur résultante
          console.log('Résultat de la conversion : ', result);
        }, error => {
          console.error('Erreur lors de la conversion : ', error);
        });
    } else {
      console.error('Veuillez saisir les détails de conversion');
    }
  }

  ngOnInit(): void {
    const walletId = this.getWalletId();
    this.loadPortfolio();


    if (walletId) {
      this.loadWallet(walletId);
    }
    this.deviseSourceField = this.deviseSourceField;
    this.deviseCibleField = this.deviseCibleField;

  }

  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }
  getWalletId(): number | null {
    const walletId = sessionStorage.getItem('id_wallet');
    return walletId ? Number(walletId) : null;
  }
// Dans votre WalletComponent
// wallet.component.ts
  openPaymentModal(): void {
    this.dialogRef = this.dialog.open(PaymentmodalComponent, {
      width: '400px',
      disableClose: true,
    });

    this.dialogRef.afterClosed().subscribe(result => {
      console.log('Modal fermé avec le résultat :', result);
      this.dialogRef = null;
    });
  }


  loadWallet(userId: number): void {
    this.walletService.getWalletById(userId).subscribe(response => {
      console.log(response); // Log du JSON reçu du serveur
      if (response) {
        this.wallet = response as Wallet; // Assurez-vous que les données JSON sont correctement castées en tant qu'objet Wallet
      }
    });
  }
  loadHistorique(): void {
    const userId = this.getUserId();

    if (userId !== null) {
      this.walletService.getHistoriqueByUserId(userId).subscribe(
        historique => {
          console.log(historique);

          // Filtrer les éléments de l'historique ayant des valeurs indéfinies
          const historiqueFiltre = historique.filter(transaction =>
            transaction.id && transaction.stripeChargeId && transaction.amount &&
            transaction.currency && transaction.dateTransaction
          );

          // Appel de la méthode pour générer le PDF avec les données filtrées
          this.generatePDF(historiqueFiltre);

          // Autres actions avec l'historique si nécessaire
        },
        error => {
          console.error('Erreur lors de la récupération de l\'historique : ', error);
        }
      );
    } else {
      console.error('ID utilisateur non trouvé dans la session.');
    }
  }
  chargerHistorique(): void {
    this.loadHistorique();
  }

// Ajoutez cette méthode à votre WalletComponent
  generatePDF(historique: HistoriqueChargement[]): void {

    const documentDefinition = {
      info: {
        title: 'Historique des Transactions',
        author: 'Votre Entreprise',
        subject: 'Informations sur les Transactions',
        keywords: 'transactions, historique, facturation'
      },
      content: [
        {
          text: 'Votre Historique',
          style: 'header'
        },
        {
          text: 'Cet historique présente les détails de vos transactions de chargement passées.',
          style: 'subheader'
        },
        {
          style: 'tableExample',
          table: {
            headerRows: 1,
            widths: ['auto', 'auto', 'auto', 'auto','auto'],
            body: [
              [
                { text: 'ID de Paiement Stripe', style: 'tableHeader' ,alignment: 'center' },
                { text: 'Montant Versé', style: 'tableHeader',alignment: 'center'  },
                { text: 'Devise', style: 'tableHeader',alignment: 'center'  },
                { text: 'Date de Chargement', style: 'tableHeader',alignment: 'center'  },
                { text: 'Montant Converti', style: 'tableHeader',alignment: 'center'  }

              ],

              ...historique.map(transaction => [
                { text: transaction.stripeChargeId, alignment: 'center' },
                { text: transaction.amount.toString(), alignment: 'center' },
                { text: transaction.currency.toUpperCase(), alignment: 'center' }, // Convertit en majuscules
                { text: this.formatDate(transaction.dateTransaction), alignment: 'center' },
                { text: (transaction.amount_conv/ 100).toFixed(2) + ' $', alignment: 'center' },

              ])
            ]
          }
        }
      ],
      styles: {
        header: {
          fontSize: 22,
          bold: true,
          margin: [0, 0, 0, 10]
        },
        subheader: {
          fontSize: 14,
          margin: [0, 0, 0, 5]
        },
        tableExample: {
          margin: [0, 15, 0, 15]
        },
        tableHeader: {
          bold: true,
          fontSize: 12,
          fillColor: '#EEEEEE'
        }
      },
      // Définition de la police Helvetica avec ses styles


    };

    pdfMake.createPdf(documentDefinition).download('historique_transactions.pdf');
  }



// Fonction pour formater la date
  formatDate(dateArray: Date): string {
    const date = new Date(dateArray[0], dateArray[1] - 1, dateArray[2], dateArray[3], dateArray[4], dateArray[5]);
    const formattedDate = `${date.getFullYear()} / ${this.padZero(date.getMonth() + 1)} / ${this.padZero(date.getDate())} - ${this.padZero(date.getHours())}:${this.padZero(date.getMinutes())}:${this.padZero(date.getSeconds())}`;
    return formattedDate;
  }

// Fonction utilitaire pour ajouter un zéro pour les valeurs < 10
  padZero(num: number): string {
    return num < 10 ? '0' + num : num.toString();
  }


  toggleWalletStatus(): void {
    if (this.wallet.id_wallet) {
      const action = this.wallet.isActive ? 'désactiver' : 'activer';
      const confirmationMessage = `Êtes-vous sûr de vouloir ${action} votre portefeuille ?`;

      const confirmed = confirm(confirmationMessage);

      if (confirmed) {
        if (this.wallet.isActive) {
          this.walletService.deactivateWallet(this.wallet.id_wallet).subscribe(
            () => {
              this.wallet.isActive = false;
              console.log('Le portefeuille a été désactivé avec succès'); // Ajout d'un console.log pour vérifier l'exécution
              this.notificationService.showSuccess('Le portefeuille a été désactivé avec succès');
            },
            error => {
              console.error(error);
              console.error('Une erreur s\'est produite lors de la désactivation du portefeuille');
              this.notificationService.showError('Une erreur s\'est produite lors de la désactivation du portefeuille');
            }
          );
        } else {
          this.walletService.activateWallet(this.wallet.id_wallet).subscribe(
            () => {
              this.wallet.isActive = true;
              console.log('Le portefeuille a été activé avec succès'); // Ajout d'un console.log pour vérifier l'exécution
              this.notificationService.showSuccess('Le portefeuille a été activé avec succès');
            },
            error => {
              console.error(error);
              console.error('Une erreur s\'est produite lors de l\'activation du portefeuille');
              this.notificationService.showError('Une erreur s\'est produite lors de l\'activation du portefeuille');
            }
          );
        }
      }
    }
  }

}
