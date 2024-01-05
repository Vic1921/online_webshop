import { Injectable, OnDestroy } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService{
  public useNoSQL: boolean = false;
  constructor() { 
    this.useNoSQL = JSON.parse(localStorage.getItem('useNoSQL') ?? 'false');
  }

  setUseNoSQL(value: boolean): void {
    this.useNoSQL = value;
    localStorage.setItem('useNoSQL', JSON.stringify(value));
  }

  removeUseNoSQL(): void{
    localStorage.removeItem('useNoSQL');
  }

  ngOnDestroy() {
    localStorage.removeItem('useNoSQL');
    this.useNoSQL = false;

  }
}
