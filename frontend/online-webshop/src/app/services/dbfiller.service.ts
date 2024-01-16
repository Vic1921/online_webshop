import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DbfillerService {
  private apiUrl = 'https://localhost:8089';

  constructor(private http : HttpClient) { }

  fillDatabase(): Observable<string>{
    return this.http.get(`${this.apiUrl}/api/fill`, {responseType: 'text'});
  }
}
