// src/app/nom-du-composant/nom-du-composant.component.ts
import { Component, OnInit } from '@angular/core';
import { FlaskService } from '../flask.service';

@Component({
  selector: 'app-flask',
  templateUrl: './flask.component.html',
  styleUrls: ['./flask.component.css']
})
export class FlaskComponent implements OnInit {
  constructor(private flaskservice: FlaskService) {}

  ngOnInit(): void {}

  lancerScript(): void {
    this.flaskservice.lancerScript().subscribe(() => {
      console.log('Script lancé avec succès');
      // Ajoutez ici toute logique supplémentaire après le lancement du script
    });
  }
}
