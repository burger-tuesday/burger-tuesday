import * as React from 'react';
import './App.css';
import NavigationMenu from './components/Navigation/Navigation';

import authStore from './stores/authStore';
import commonStore from './stores/commonStore';

class App extends React.Component<{}, {}> {
  public componentDidMount(): void {
    if (commonStore.isLoggedIn) {
      authStore.renewSession();
    }
  }

  public render() {
    return (
        <div>
          <NavigationMenu/>
          {this.props.children}
        </div>
    );
  }
}

export default App;
