import { Component } from '@angular/core';

@Component({
  selector: 'loading-indicator',
  template: `
    <div class="app-loading">
      <div class="lds-css ng-scope">
        <div class="lds-pacman">
          <div><div></div><div></div><div></div></div>
          <div><div></div><div></div><div></div></div>
        </div>
      </div>
    </div>
  `
})
export class LoadingIndicatorComponent {
  constructor() { }
}
