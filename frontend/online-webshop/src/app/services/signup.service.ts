import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ConfigService } from '../config.service';

@Injectable({
  providedIn: 'root'
})
export class SignupService {
  private apiUrl = 'http://localhost:8089';

  constructor(private configService : ConfigService, private http: HttpClient) { }

  signUp(registerData : any) : Observable<any> {
    if(this.configService.useNoSQL == false){
      return this.signUpSQL(registerData);
    }

    return this.signUpNoSQL(registerData);
  }
  signUpSQL(registerData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/sql/customers/register`, registerData);
  }

  signUpNoSQL(registerData: any): Observable<any>{
    return this.http.post(`${this.apiUrl}/api/nosql/customers/register`, registerData);
  }



}
