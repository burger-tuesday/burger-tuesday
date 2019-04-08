import {action, observable, reaction} from 'mobx';

class CommonStore {
  @observable public isLoggedIn = window.localStorage.getItem('isLoggedIn');

  constructor() {
    reaction(
        () => this.isLoggedIn,
        isLoggedIn => {
          if (isLoggedIn) {
            window.localStorage.setItem('isLoggedIn', 'true');
          } else {
            window.localStorage.removeItem('isLoggedIn');
          }
        }
    );
  }

  @action public setLoggedIn(isLoggedIn: boolean) {
    if (isLoggedIn) {
      this.isLoggedIn = 'true';
    } else {
      this.isLoggedIn = null;
    }
  }
}

export default new CommonStore();
