import {action, observable} from 'mobx';

class NavigationStore {
  @observable public isOpen = false;

  @action public toggleOpen() {
    this.isOpen = !this.isOpen;
  }
}

export default new NavigationStore();
