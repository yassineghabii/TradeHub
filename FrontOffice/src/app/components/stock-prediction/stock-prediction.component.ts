import {Component, OnInit} from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { StockPredictionService } from 'src/app/services/stock-prediction.service';

@Component({
  selector: 'app-stock-prediction',
  templateUrl: './stock-prediction.component.html',
  styleUrls: ['./stock-prediction.component.css']
})
export class StockPredictionComponent {

  streamlitUrl: SafeResourceUrl;
  
  constructor(private sanitizer: DomSanitizer) {
    this.streamlitUrl = this.sanitizer.bypassSecurityTrustResourceUrl('http://localhost:8502');
  }
}
