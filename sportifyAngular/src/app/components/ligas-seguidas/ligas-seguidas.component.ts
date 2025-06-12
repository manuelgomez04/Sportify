import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-ligas-seguidas',
  templateUrl: './ligas-seguidas.component.html',
  styleUrls: ['./ligas-seguidas.component.css']
})
export class LigasSeguidasComponent implements OnInit {
  ligas: any[] = [];
  visibleCards = 4;
  currentIndex = 0;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any>('/ligasFavoritas?page=0&size=10').subscribe({
      next: resp => {
        this.ligas = resp.ligasFavoritas?.content || [];
      }
    });
  }

  getLigaImg(nombreLiga: string): string {
    return '/assets/ligas/' + nombreLiga.toLowerCase().replace(/ /g, '-') + '.png';
  }

  get visibleLigas() {
    return this.ligas.slice(this.currentIndex, this.currentIndex + this.visibleCards);
  }

  scrollLeft() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  scrollRight() {
    if (this.currentIndex < this.ligas.length - this.visibleCards) {
      this.currentIndex++;
    }
  }

  get atStart() {
    return this.currentIndex === 0;
  }

  get atEnd() {
    return this.currentIndex >= this.ligas.length - this.visibleCards || this.ligas.length <= this.visibleCards;
  }

  getTransform() {
    return `translateX(-${this.currentIndex * 196}px)`; // 180px card + 16px gap
  }
}
