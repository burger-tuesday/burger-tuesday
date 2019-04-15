import {faHamburger} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {observer} from 'mobx-react';
import {ReactNode} from 'react';
import * as React from 'react';
import {NavLink as RRNavLink} from 'react-router-dom';
import {Collapse, Nav, Navbar, NavbarBrand, NavbarToggler, NavItem, NavLink,} from 'reactstrap';
import authStore from '../../stores/authStore';
import navigationStore from '../../stores/navigationStore';
import UserDropdown from './UserDropdown';

@observer
export default class NavigationMenu extends React.Component {
  public render() {

    return (

        <div>
          <Navbar color="light" light={true} expand="lg">
            {/* Brandname */}
            <NavbarBrand tag={RRNavLink} to={'/home'}>
              <FontAwesomeIcon icon={faHamburger}/> Tuesday
            </NavbarBrand>
            {/* Add toggler to auto-collapse */}

            <NavbarToggler onClick={() => navigationStore.toggleOpen()}/>
            <Collapse isOpen={navigationStore.isOpen} navbar={true}>
              {/*Pull left */}
              <Nav navbar={true}>
                <NavItem>
                  <NavLink tag={RRNavLink} to={'/home'}>
                    Home
                  </NavLink>
                </NavItem>
                {this.authenticatedLink('/restaurants', 'Restaurants', 'manage:restaurants')}
                {this.authenticatedLink('/review', 'Reviews')}
              </Nav>


              {/* Pull right */}
              <UserDropdown/>
            </Collapse>
          </Navbar>
        </div>
    )
  }

  private authenticatedLink(route: string, name: string, scope: string | null = null): ReactNode | null {
    if (!authStore.isAuthenticated) {
      return null;
    }
    if (scope == null || authStore.isAuthorized(scope)) {
      return (
          <NavItem>
            <NavLink tag={RRNavLink} to={route}>
              {name}
            </NavLink>
          </NavItem>
      )
    }
    return null;
  }
}
