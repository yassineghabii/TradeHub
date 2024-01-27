import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FinancialProfileService } from '../FinancialProfileService';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-financial-profile',
  templateUrl: './financial-profile.component.html',
  styleUrls: ['./financial-profile.component.css'],
})
export class FinancialProfileComponent implements OnInit {
  financialProfileForm: FormGroup;
  showMessage: boolean = false;
  message: string = '';
  clientId: number | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private financialProfileService: FinancialProfileService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.initializeFinancialProfileForm();
    this.getClientId();
  }

  initializeFinancialProfileForm() {
    this.financialProfileForm = this.formBuilder.group({
      age: [null, Validators.required],
      gender: [null, Validators.required],
      job: [null, Validators.required],
      housing: [null, Validators.required],
      saving_accounts: [null, Validators.required],
      credit_history: [null, Validators.required],
      credit_amount: [null, Validators.required],
      duration: [null, Validators.required],
      purpose: [null, Validators.required],
      risk: [null, Validators.required],
    });
  }
  

  getClientId() {
    const clientIdFromSession = sessionStorage.getItem('id_user');

    if (clientIdFromSession) {
      this.clientId = +clientIdFromSession;
    } else {
      console.error("ID du client introuvable dans la session.");
    }
  }

  updateFinancialProfile() {
    if (this.financialProfileForm.valid && this.clientId !== null) {
      const financialProfileData = this.financialProfileForm.value;

      if (this.clientId) {
        // Si clientId existe, mettez à jour le profil financier
        this.financialProfileService.saveOrUpdateFinancialProfile(this.clientId, financialProfileData).subscribe(
          (data) => {
            console.log('Profil financier mis à jour avec succès :', data);
            this.showMessage = true;
            this.message = 'Financial Profile mis à jour avec succès';

            this.financialProfileForm.reset();

            setTimeout(() => {
              this.showMessage = false;
            }, 5000);
          },
          (error) => {
            console.error('Erreur lors de la mise à jour du profil financier :', error);
            this.message = 'Erreur lors de la mise à jour du profil financier';
          }
        );
      } else {
        console.error('ID du client introuvable.');
      }
    }
  }
}
