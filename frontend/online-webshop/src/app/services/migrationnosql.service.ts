import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MigrationNoSQLService {
  private apiUrl = 'https://localhost:8089';
  constructor(private http : HttpClient) { }

  
  migrateNoSQL() : Observable<any>{
    return this.http.get(`${this.apiUrl}/api/migratedatabase`);
  }
}
