import {Component, OnInit, ViewChild} from '@angular/core';
import { ChatbotService } from '../chatbotservice';
import {ChatResponse} from "../entities/ChatResponse";  // Assurez-vous que le chemin est correct

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent implements OnInit {

  public userMessage: string = '';
  public allMessages: any[] = [];  // Ici, nous stockerons tous les messages
recognition: any;
  isRecording = false;

  constructor(private chatbotService: ChatbotService) {}

  ngOnInit(): void {
    this.resetChatbot();
    // Initialisez la reconnaissance vocale
    this.recognition = new (window as any).webkitSpeechRecognition();
    this.recognition.continuous = true;
    this.recognition.interimResults = true;

    this.recognition.onstart = () => {
      this.isRecording = true;
    };

    this.recognition.onend = () => {
      this.isRecording = false;
    };

    this.recognition.onresult = (event: any) => {
      const transcript = event.results[event.results.length - 1][0].transcript;
      this.userMessage = transcript;
    };
  }

  startSpeechRecognition(): void {
    if (!this.isRecording) {
      this.recognition.start();
    } else {
      this.recognition.stop();
    }
  }
  onSendMessage(): void {
    // Ajoutons le message de l'utilisateur à allMessages
    this.allMessages.push({
      sender: 'user',
      message: this.userMessage,
      time: new Date().toLocaleTimeString() // Ajoutons un horodatage
    });

    this.chatbotService.chatWithPython(this.userMessage).subscribe(
      response => {
        // Ajoutons la réponse du bot à allMessages
        this.allMessages.push({
          sender: 'bot',
          message: response.response, // Assurez-vous que la clé de réponse est correcte
          time: new Date().toLocaleTimeString() // Ajoutons un horodatage
        });
      },
      error => {
        console.error('Erreur lors de la récupération de la réponse du chatbot:', error);
      }
    );

    // Effaçons le message de l'utilisateur pour une nouvelle saisie
    this.userMessage = '';
  }
  @ViewChild('fileInput') fileInput;

  onUploadFile(): void {
    const file: File = this.fileInput.nativeElement.files[0];
    if (file) {
      console.log("File chosen: ", file.name);
      console.log("File size: ", file.size);
      if (file.name.endsWith('.csv')) {
        this.chatbotService.uploadFileToPython(file).subscribe(
          response => {
            // Ajout de la réponse du serveur Flask à allMessages
            const adviceMessage = response.advice;
            this.allMessages.push({
              sender: 'bot',
              message: adviceMessage,
              time: new Date().toLocaleTimeString()
            });
          },
          error => {
            console.error('Erreur lors de l\'envoi du fichier:', error);
            // Afficher une erreur dans le chatbot en cas d'échec
            this.allMessages.push({
              sender: 'bot',
              message: "Erreur lors de l'analyse du fichier.",
              time: new Date().toLocaleTimeString()
            });
          }
        );
      } else {
        console.error('Format de fichier invalide. Veuillez télécharger un fichier .csv.');
        // Afficher un message d'erreur dans le chatbot
        this.allMessages.push({
          sender: 'bot',
          message: "Format de fichier invalide. Veuillez télécharger un fichier .csv.",
          time: new Date().toLocaleTimeString()
        });
      }
    } else {
      console.error('Aucun fichier sélectionné.');
      // Afficher un message d'erreur dans le chatbot
      this.allMessages.push({
        sender: 'bot',
        message: "Aucun fichier sélectionné.",
        time: new Date().toLocaleTimeString()
      });
    }
  }

  resetChatbot(): void {
    this.chatbotService.resetChatbot().subscribe(
      response => {
        console.log('Chatbot reset successful:', response);
        // Vous pouvez également effectuer d'autres actions après la réinitialisation ici
      },
      error => {
        console.error('Error resetting chatbot:', error);
      }
    );
  }
  
} 