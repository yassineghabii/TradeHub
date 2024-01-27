import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material/dialog';
import { FormsModule, FormBuilder, FormGroup, AbstractControl, Validators } from '@angular/forms';
import { Client } from "../../Models/Client";

@Component({
  selector: 'app-confirm-modal',
  templateUrl: './confirm-modal.component.html',
  styleUrls: ['./confirm-modal.component.scss']
})
export class ConfirmModalComponent {
  mode: 'update' | 'delete' | 'confirmUpdate' | 'add' | 'confirmAdd' | 'activite';
  adminUpdates: Partial<Client> = {};
  newAdmin: Partial<Client> = {};
  loginForm: FormGroup;

  constructor(
      public dialogRef: MatDialogRef<ConfirmModalComponent>,
      @Inject(MAT_DIALOG_DATA) public data: { action: string, client: string, mode?: 'update' | 'delete' | 'confirmUpdate', admin?: Client },
      private dialog: MatDialog,
      private formBuilder: FormBuilder
  ) {
    if (data.mode) {
      this.mode = data.mode;
    }

    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, this.phoneNumberValidator]],
      cin: ['', [Validators.required, this.cinValidator]]
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  confirmUpdate() {
      // Les contrôles de saisie sont valides, vous pouvez procéder à la confirmation
      const dialogRef = this.dialog.open(ConfirmModalComponent, {
        data: {
          action: 'modifier',
          client: `${this.data.admin.firstname} ${this.data.admin.lastname}`,
          mode: 'confirmUpdate'
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          // Vous pouvez envoyer `adminUpdates` à votre service pour effectuer la mise à jour
          this.dialogRef.close(this.adminUpdates); // renvoyer les données mises à jour
        }
      });
    }


  directDelete() {
    this.mode = 'delete';
    this.dialogRef.close(true);
  }

  Actif() {
    this.mode = 'activite';
    this.dialogRef.close(true);
  }

  confirmAdd() {

    const dialogRef = this.dialog.open(ConfirmModalComponent, {
      data: {
        action: 'ajouter',
        client: `${this.newAdmin.firstname} ${this.newAdmin.lastname}`,
        mode: 'confirmAdd'
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.dialogRef.close(this.newAdmin);
      }
    });
  }

  // Fonction de validation personnalisée pour le numéro de téléphone
  phoneNumberValidator(control: AbstractControl): { [key: string]: any } | null {
    const phoneNumberPattern = /^(50|51|52|53|54|55|56|57|58|59|20|21|22|23|24|25|26|27|28|29|70|71|72|73|74|75|76|77|78|79|90|91|92|93|94|95|96|97|98|99)\d{6}$/;
    if (control.value && !phoneNumberPattern.test(control.value)) {
      return { 'invalidPhoneNumber': true };
    }
    return null;
  }

  // Fonction de validation personnalisée pour le CIN (Code d'Identification Nationale)
  cinValidator(control: AbstractControl): { [key: string]: any } | null {
    const cinPattern = /^\d{8}$/;
    if (control.value && !cinPattern.test(control.value)) {
      return { 'invalidCin': true };
    }
    return null;
  }
}
