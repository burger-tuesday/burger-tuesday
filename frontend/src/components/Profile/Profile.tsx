import {Auth0UserProfile} from 'auth0-js';
import {observer} from 'mobx-react';
import * as React from 'react';
import {Card, CardBody, CardHeader, Container} from 'reactstrap';
import {withAuthorization} from '../../shared/auth/session-renewing-component';
import authStore from '../../stores/authStore';

@observer
class Profile extends React.Component {
  public render() {
    const profile = authStore.userProfile || {} as Auth0UserProfile;
    return (
        <Container fluid={true}>
          <h1>{profile.name}</h1>
          <Card>
            <CardHeader>Profile</CardHeader>
            <CardBody>
              <img src={profile.picture} alt='profile'/>
              <pre>{JSON.stringify(profile, null, 2)}</pre>
            </CardBody>
          </Card>
        </Container>
    );
  }
}

export default withAuthorization(Profile)
