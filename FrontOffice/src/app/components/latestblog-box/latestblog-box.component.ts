import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-latestblog-box',
  templateUrl: './latestblog-box.component.html',
  styleUrls: ['./latestblog-box.component.css'],
})
export class LatestblogBoxComponent implements OnInit {
  @Input() data: any;

  ngOnInit(): void {
    console.log(this.data);
  }
}
