import { Component } from '@angular/core';

@Component({
  selector: 'app-latestblog',
  templateUrl: './latestblog.component.html',
  styleUrls: ['./latestblog.component.css'],
})
export class LatestblogComponent {
  servcies: any = [
    {
      icon1: 'bi bi-person me-2',
      icon2: 'bi bi-bookmarks me-2',
      day: '01',
      month: 'Jan',
      year: '2045',
      text: 'Magna sea dolor ipsum amet lorem eos',
      img: '../../../assets/images/blog-1.jpg',
    },
    {
      icon1: 'bi bi-person me-2',
      icon2: 'bi bi-bookmarks me-2',
      day: '01',
      month: 'Jan',
      year: '2045',
      text: 'Magna sea dolor ipsum amet lorem eos',
      img: '../../../assets/images/blog-2.jpg',
    },
    {
      icon1: 'bi bi-person me-2',
      icon2: 'bi bi-bookmarks me-2',
      day: '01',
      month: 'Jan',
      year: '2045',
      text: 'Magna sea dolor ipsum amet lorem eos',
      img: '../../../assets/images/blog-3.jpg',
    },
  ];
}
