import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../services/authservices";
import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import {WalletEnum} from "../../entities/Wallet";

@Component({
  selector: 'app-choose',
  templateUrl: './choose.component.html',
  styleUrls: ['./choose.component.css'],
})
export class ChooseComponent implements OnInit {
  updateForm: FormGroup;
  selectedFile: File;
  errorMessage: string;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initializeUpdateForm();
  }

  initializeUpdateForm(): void {
    this.updateForm = this.formBuilder.group({
      chosenPwd: ['', Validators.required],
      verificationPwd: ['', Validators.required],
      type: [Object.values(WalletEnum), Validators.required], // Rendre le champ 'type' obligatoire
      profileImage: ['', Validators.required],
      cin: ['', Validators.required],
      phone_number: ['', Validators.required],
      address: ['', Validators.required],
      role: ['player'],
    }, {
      validator: this.mustMatch('chosenPwd', 'verificationPwd')
    });

  }

  onFileChange(event): void {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors["mustMatch"]) {
        return;
      }

      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
    };
  }

  onSubmit(): void {
    if (!this.updateForm.valid) {
      this.errorMessage = 'Il y a eu une erreur lors du traitement du formulaire.';
      return; // Si le formulaire n'est pas valide, nous sortons de la méthode ici.
    }

    const userId = this.getUserId();
    if (!userId) {
      console.error('ID d\'utilisateur non trouvé en session.');
      return;
    }

    this.authService.updateClient(
      userId,
      this.updateForm.value.chosenPwd,
      this.selectedFile,
      this.updateForm.value.cin,
      this.updateForm.value.phone_number,
      this.updateForm.value.address,
      this.updateForm.value.role,
      this.updateForm.value.type,

    ).subscribe(
      response => {
        console.log("Réponse du serveur:", response);
        this.router.navigate(['/']);
      },
      error => {
        console.error("Erreur du serveur:", error);
      }
    );
  }

  getUserId(): number | null {
    const userIdStr = sessionStorage.getItem('id');
    return userIdStr ? Number(userIdStr) : null;
  }

  protected readonly Object = Object;
  protected readonly WalletEnum = WalletEnum;
}
