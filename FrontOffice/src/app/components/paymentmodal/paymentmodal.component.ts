import {AfterViewInit, Component, ElementRef, HostListener} from '@angular/core';
import {WalletService} from "../../services/walletservice";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {StripeCurrency} from "../../entities/CardDetailsDTO";
import {NotificationService} from "../wallet/NotificationService";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-paymentmodal',
  templateUrl: './paymentmodal.component.html',
  styleUrls: ['./paymentmodal.component.css']
})
export class PaymentmodalComponent implements AfterViewInit {
  private countdownTimer: any;
  remainingTime: number = 20*60; // 20 minutes en secondes
  timerInterval: any;
  formatTime(seconds: number): string {
    const minutes: number = Math.floor(seconds / 60);
    const remainingSeconds: number = seconds % 60;
    return `${this.pad(minutes)}:${this.pad(remainingSeconds)}`;
  }

  pad(val: number): string {
    return val < 10 ? `0${val}` : `${val}`;
  }

  paymentForm: FormGroup;
  private dialogRefElement: HTMLElement;
  supportedCurrencies: StripeCurrency[] = Object.values(StripeCurrency);

  cardDetails: any = {
    number: '',
    exp_month: null,
    exp_year: null,
    cvc: '',
    amount: null,
    currency: ''
  };

  constructor(
    private walletService: WalletService,
    public dialogRef: MatDialogRef<PaymentmodalComponent>,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,private elRef: ElementRef ,
    private router: Router ,
    private notificationService: NotificationService
  ) {
    this.dialogRefElement = this.elRef.nativeElement.closest('.mat-dialog-container');

    this.paymentForm = this.formBuilder.group({
      number: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      exp_month: ['', [Validators.required, Validators.pattern(/^\d{2}$/)]],
      exp_year: ['', [Validators.required, Validators.pattern(/^\d{2}$/)]],
      cvc: ['', [Validators.required, Validators.pattern(/^\d{3}$/)]],
      amount: ['', [Validators.required, Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
      currency: ['', [Validators.required]],

    });
  }

  getWalletId(): number | null {
    const walletId = sessionStorage.getItem('id_wallet');
    return walletId ? Number(walletId) : null;
  }  isOpen: boolean = false;

  onNoClick(): void {
    this.dialogRef.close();
  }

  chargeWallet(): void {
    const confirmationMessage = 'Êtes-vous sûr de vouloir effectuer le paiement ?'; // Message de confirmation

    const confirmed = confirm(confirmationMessage); // Affichage de la confirmation

    if (confirmed) {
      console.log('Tentative de paiement...');
      console.log('Détails de la carte :', this.cardDetails);

      // Appel du service chargeWallet avec les détails de la carte
      this.walletService.chargeWallet(this.getWalletId(), this.paymentForm.value).subscribe(
        () => {
          console.log('Le paiement a été effectué avec succès'); // Message de succès dans la console
          window.location.reload();
          this.notificationService.showSuccess('Le paiement a été effectué avec succès'); // Affichage du message de succès
        },
        error => {
          console.error('Une erreur s\'est produite lors du paiement :', error); // Message d'erreur dans la console
          this.notificationService.showError('Une erreur s\'est produite lors du paiement'); // Affichage du message d'erreur
        }
      );
    }
  }

  openNotification(message: string): void {
    // Affichage de la notification (implémentation de la logique spécifique à votre application)
    // Vous pouvez utiliser un service de notification ou un composant de notification pour afficher le message à l'utilisateur.
    console.log(message); // Ici, j'utilise console.log pour représenter l'affichage d'une notification.
  }

  openPaymentModal(): void {
    // Logique supplémentaire à exécuter lors de l'ouverture du modal
    console.log('Le modal est ouvert !');

    this.isOpen = true;
    this.startTimer(); // Démarre le chronomètre au moment de l'ouverture du modal
  }
  // Fonction pour démarrer le compte à rebours
  startTimer() {
    this.timerInterval = setInterval(() => {
      this.remainingTime--;
      if (this.remainingTime <= 0) {
        clearInterval(this.timerInterval);
        this.navigateToWallet(); // Redirection vers /wallet une fois le temps écoulé

      }
    }, 1000); // Mettre à jour le compte à rebours chaque seconde
  }

  stopTimer(): void {
    clearInterval(this.countdownTimer);
  }

  closePaymentModal(): void {
    if (this.dialogRef && this.dialogRef.componentInstance === this) {
      this.dialogRef.close();
    }
  }
  navigateToWallet(): void {
    this.dialogRef.close();
    this.router.navigate(['/wallet']);
  }

  ngOnDestroy(): void {
    this.stopTimer();
    console.log('Le composant PaymentmodalComponent est détruit.');
  }
  ngOnInit(): void {
    this.startTimer();
  }

  ngAfterViewInit(): void {
    this.dialogRefElement = this.elRef.nativeElement.closest('.mat-dialog-container');
  }
}
