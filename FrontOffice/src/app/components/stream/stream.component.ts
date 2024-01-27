import { Component } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-stream',
  templateUrl: './stream.component.html',
  styleUrls: ['./stream.component.css']
})
export class StreamComponent {
  streamlitUrl: SafeResourceUrl;


  constructor(private sanitizer: DomSanitizer) {
    // Assuming your Streamlit app is running on http://localhost:8501
    this.streamlitUrl = this.sanitizer.bypassSecurityTrustResourceUrl('http://localhost:8508');
  }
}

