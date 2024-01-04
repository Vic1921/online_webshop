import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
  public useNoSQL: boolean;
  constructor() { 
    this.useNoSQL = JSON.parse(localStorage.getItem('useNoSQL') ?? 'false');
  }

  setUseNoSQL(value: boolean): void {
    this.useNoSQL = value;
    localStorage.setItem('useNoSQL', JSON.stringify(value));
  }
}
