import {faUser} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {observer} from 'mobx-react';
import * as React from 'react';
import {NavLink as RRNavLink} from 'react-router-dom';
import {DropdownItem, DropdownMenu, DropdownToggle, Nav, UncontrolledDropdown} from 'reactstrap';
import authStore from '../../stores/authStore';

@observer
export default class UserDropdown extends React.Component {
  public render() {
    if (authStore.isAuthenticated) {
      return (
          <Nav className='ml-auto' navbar={true}>
            <UncontrolledDropdown inNavbar={true}>

              <DropdownToggle data-toggle='dropdown' nav={true} caret={true}>
                <FontAwesomeIcon icon={faUser} /> {authStore.userProfile != null ? authStore.userProfile.name : '...'}
              </DropdownToggle>

              <DropdownMenu right={true}>
                <DropdownItem tag={RRNavLink} to={'/profile'}>
                  Account
                </DropdownItem>
                <DropdownItem divider={true}/>
                <DropdownItem onClick={() => authStore.logout()}>
                  Logout
                </DropdownItem>
              </DropdownMenu>
            </UncontrolledDropdown>
          </Nav>
      )
    }
    return (
        <Nav className='ml-auto' navbar={true}>
          <UncontrolledDropdown inNavbar={true}>


            <DropdownToggle data-toggle='dropdown' nav={true} caret={true}>
              Not logged in
            </DropdownToggle>

            <DropdownMenu right={true}>
              <DropdownItem onClick={() => authStore.login()}>
                Login
              </DropdownItem>
              <DropdownItem onClick={() => authStore.login()}>
                Sign up
              </DropdownItem>
            </DropdownMenu>
          </UncontrolledDropdown>
        </Nav>
    )
  }
}
