import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EventEmitterService {
  cartUpdated = new EventEmitter<void>();

  constructor() { }

  emitCartUpdated(): void {
    this.cartUpdated.emit();
  }
}
