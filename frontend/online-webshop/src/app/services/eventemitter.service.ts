import { Injectable, EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class EventEmitterService {
  cartUpdated = new EventEmitter<void>();
  reviewUpdates = new EventEmitter<void>();

  constructor() { }

  emitCartUpdated(): void {
    this.cartUpdated.emit();
  }

  reviewUpdated() : void{
    this.reviewUpdates.emit();
  }
}
