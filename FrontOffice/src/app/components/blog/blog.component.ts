import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { PredictService } from '../../services/risk.service';
import { PredictionResult } from '../../entities/prediction-result';
import { FormBuilder, FormGroup, Validators, FormControl } from '@angular/forms';
import { timer } from 'rxjs';
import { tap } from 'rxjs/operators';
import Typed from 'typed.js';
import * as ace from 'ace-builds';
import * as pdfMake from 'pdfmake/build/pdfmake';
import * as pdfFonts from 'pdfmake/build/vfs_fonts';
pdfMake.vfs = pdfFonts.pdfMake.vfs;

@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.css'],
})
export class BlogComponent implements OnInit {

  creditForm: FormGroup;
  predictionResult: PredictionResult | null = null;
  isLoading: boolean = false;
  typingInProgress: boolean = false;
  fullText: string = '';
  showEditor: boolean = false;

  isVisible = {
    predictionStatus: false,
    probabilityDefault: false,
    probabilityRepayment: false,
    message: false,
    observations: false
  };
  editor: any;

  constructor(private cdr: ChangeDetectorRef,
    private formBuilder: FormBuilder,
    private predictService: PredictService
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    // Ajuster le chemin de base pour ACE
    ace.config.set('basePath', 'assets/ace/');

    this.editor = ace.edit('editor', {
      mode: 'ace/mode/html',
      theme: 'ace/theme/chrome', // Changé de 'monokai' à 'chrome' pour un thème clair
      selectionStyle: 'text'

    });
    this.editor.setReadOnly(true);  // Rend l'éditeur en lecture seule
    this.editor.setFontSize(14);
    this.editor.session.setUseWrapMode(true);
    this.editor.session.setWrapLimitRange(null, null);
    this.editor.renderer.setScrollMargin(10, 10);
    this.editor.setOptions({
      autoScrollEditorIntoView: true
    });
    this.editor.setValue('');
    this.editor.clearSelection();
    this.editor.renderer.updateFull();  // Force un rafraîchissement complet de l'éditeur

  }

  private initForm(): void {
    this.creditForm = this.formBuilder.group({
      age: ['', [Validators.required, Validators.min(25), Validators.max(59)]],
      ed: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      employ: ['', Validators.required],
      address: ['', Validators.required],
      income: ['', Validators.required],
      debtinc: ['', Validators.required],
      creddebt: ['', Validators.required],
      othdebt: ['', Validators.required],
    });
  }

  get ageControl(): FormControl {
    return this.creditForm.get('age') as FormControl;
  }

  get edControl(): FormControl {
    return this.creditForm.get('ed') as FormControl;
  }

  get employControl(): FormControl {
    return this.creditForm.get('employ') as FormControl;
  }

  get addressControl(): FormControl {
    return this.creditForm.get('address') as FormControl;
  }

  get incomeControl(): FormControl {
    return this.creditForm.get('income') as FormControl;
  }

  get debtincControl(): FormControl {
    return this.creditForm.get('debtinc') as FormControl;
  }

  get creddebtControl(): FormControl {
    return this.creditForm.get('creddebt') as FormControl;
  }

  get othdebtControl(): FormControl {
    return this.creditForm.get('othdebt') as FormControl;
  }

  onPredict(): void {
    if (this.creditForm.valid) {
      this.isLoading = true;
      const credit = this.creditForm.value;
      this.predictService.createLoanApplication(credit).subscribe(
        (predictionResult: PredictionResult) => {
          this.predictionResult = predictionResult;
          this.isLoading = false;
          const resultAsString = this.formatPredictionResultAsString(this.predictionResult);
          this.typeTextIntoEditor(resultAsString);
          this.showEditor = true; // Ajoutez cette ligne
          this.cdr.detectChanges();

        },
        (error) => {
          console.error('Error:', error);
          this.isLoading = false;
        }
      );
    }
  }

  startTypingEffect() {
    // Set the default state
    this.resetVisibility();

    const stringsArray = [
      `<strong>Statut de la demande de prêt :</strong>
    ${this.predictionResult.Prediction === 1 ? 'Accepté' : 'Refusé'}
`,
      `<strong>Probabilité de Défaut de Paiement :</strong>
    ${this.predictionResult["Probabilité de défaut de paiement"]}`,
      `<strong>Probabilité de Remboursement du Prêt :</strong>
    ${this.predictionResult["Probabilité de remboursement du prêt"]}`,
      `<strong>Étude de Votre Cas :</strong>
    ${this.predictionResult.Observations.map(observation => `<li>${observation}</li>`).join('\n    ')}`
    ];

    const typeNextString = (index: number) => {
      if (index >= stringsArray.length) return; // No more strings to type

      const options = {
        strings: [stringsArray[index]],
        typeSpeed: 50,
        showCursor: true,
        cursorChar: "|",
        contentType: 'html',
        onComplete: function () {
          // Once a string is fully typed, move to the next one
          typeNextString(index + 1);
        }
      };

      // The element in which Typed.js will type the strings
      const typedElement = document.createElement('div');
      document.querySelector('.result-content').appendChild(typedElement);
      const typed = new Typed(typedElement, options);
    }

    // Start typing the first string
    typeNextString(0);
  }


  resetVisibility() {
    this.isVisible = {
      predictionStatus: false,
      probabilityDefault: false,
      probabilityRepayment: false,
      message: false,
      observations: false
    };
  }

  resetFormAndResult(): void {
    this.creditForm.reset();
    this.predictionResult = null;
    this.resetVisibility();

    // Réinitialise également l'éditeur
    this.resetTyping();
  }

  formatPredictionResultAsString(prediction: PredictionResult): string {
    let result = `Résultats de la prédiction:
---------------------------
Veuillez noter que cette prédiction est basée sur notre modèle et ne garantit pas les résultats réels auprès des banques. Elle est conçue pour vous donner une idée générale de la façon dont une banque pourrait interpréter votre demande de prêt.
-----------------------------------------------------------------------------------------------------

- Statut de la demande de prêt: ${prediction.Prediction === 1 ? 'Crédit Attribué' : 'Crédit Non Attribué'}

- Probabilité de Défaut de Paiement: ${prediction["Probabilité de défaut de paiement"]}

- Probabilité de Remboursement du Prêt: ${prediction["Probabilité de remboursement du prêt"]}

- Résultat: ${prediction.Message}

------------------------------------------------------------------------------------------------
Étude de Votre Cas:
------------------------------------------------------------------------------------------------

`;

    if (prediction.Observations && prediction.Observations.length > 0) {
      result += prediction.Observations.map(observation => `- ${observation}`).join('\n');
    }

    return result;
  }

  typeTextIntoEditor(text: string): void {
    this.fullText = text; // Stockez le texte complet
    this.typingInProgress = true;
    this.animateTypingIntoEditor(0); // Commencez à taper à partir de l'index 0
  }

  animateTypingIntoEditor(index: number = 0): void {
    if (!this.typingInProgress) return;
    if (index > this.fullText.length) return; // Utilisez fullText au lieu de text

    const currentText = this.fullText.substring(0, index);
    this.editor.setValue(currentText);
    this.editor.clearSelection();

    setTimeout(() => {
      this.animateTypingIntoEditor(index + 1);  // Passez à l'index suivant
    }, 25);
  }
  getImageDataURL(url, callback) {
    var xhr = new XMLHttpRequest();
    xhr.onload = function() {
      var reader = new FileReader();
      reader.onloadend = function() {
        callback(reader.result);
      };
      reader.readAsDataURL(xhr.response);
    };
    xhr.open('GET', url);
    xhr.responseType = 'blob';
    xhr.send();
  }

  generatePDF(): void {

    console.log("La fonction generatePDF est appelée.");
    const content = this.editor.getValue();
    this.getImageDataURL('https://universitesesame.com/wp-content/uploads/2022/09/Logo-vermeg.png', function (dataUrl) {

      const docDefinition = {
        // Metadatas
        info: {
          title: 'ESTIMA-PRET',
          author: 'vermeg',
          subject: 'Rapport de prédiction de crédit',
        },

        // Contenu du document
        content: [
          // En-tête
          {
            image: dataUrl,
            width: 150,
            alignment: 'center',
            margin: [0, 0, 0, 10] // marge inférieure de 10 pour un espacement
          },
          {
            text: 'vermeg',
            fontSize: 24,
            bold: true,
            alignment: 'center',
            margin: [0, 0, 0, 5] // marge inférieure de 5 pour un espacement
          },
          {
            text: 'ESTIMA-PRET',
            fontSize: 20,
            bold: true,
            alignment: 'center',
            margin: [0, 0, 0, 20] // marge inférieure de 20 pour un espacement plus grand
          },
          {
            text: 'Rapport de prédiction de crédit',
            fontSize: 18,
            alignment: 'center',
            margin: [0, 0, 0, 30] // marge inférieure de 30 pour un espacement encore plus grand
          },

          // Corps
          {
            text: content,
            style: 'normalText',
            alignment: 'justify'
          },

          // Pied de page (peut être également réalisé en utilisant le footer property pour chaque page)
          {
            text: '© 2023 vermeg - Tous droits réservés.',
            fontSize: 10,
            alignment: 'center',
            margin: [0, 50, 0, 0] // marge supérieure de 50 pour un espacement
          }
        ],
        styles: {
          normalText: {
            fontSize: 12
          }
        }
      };

      pdfMake.createPdf(docDefinition).download('resultat.pdf');
    });
  }
  stopTyping(): void {
    this.typingInProgress = false;
  }

  resetTyping(): void {
    this.stopTyping();
    this.editor.setValue('');
    this.editor.clearSelection();
  }

  continueTyping(): void {
    if (!this.typingInProgress) {
      this.typingInProgress = true;
      const currentTextLength = this.editor.getValue().length;
      this.animateTypingIntoEditor(currentTextLength);
    }
  }

}
