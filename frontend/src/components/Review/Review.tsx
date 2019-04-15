import {faHamburger} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {observer} from 'mobx-react';
import * as React from 'react';
import Rating from 'react-rating';
import Select from 'react-select';
import {Button, Container, Form, FormGroup, Jumbotron} from 'reactstrap';
import {withAuthorization} from '../../shared/auth/session-renewing-component';
import reviewStore from '../../stores/reviewStore';

@observer
class Review extends React.Component<{}, {}> {
  public componentDidMount(): void {
    reviewStore.getVisits()
  }

  public render() {
    return (
        <Container>
          <Jumbotron>
            <Form onSubmit={(e) => {
              e.preventDefault();
              e.stopPropagation();
            }}>
              <FormGroup>
                <legend>Visit to review</legend>
                <Select id="visit" options={reviewStore.visitOptions} value={reviewStore.selectedVisit}
                        onChange={value => reviewStore.selectedVisit = value ? value : undefined}/>
              </FormGroup>
              <FormGroup>
                <legend>Taste of Burger?</legend>
                Meh{' '}<Rating stop={10}
                                emptySymbol={<FontAwesomeIcon icon={faHamburger} color={'#777777'}
                                                              className={'fa-3x'}/>}
                                fullSymbol={<FontAwesomeIcon icon={faHamburger} color={'#6e4600'}
                                                             className={'fa-3x'}/>}
                                onChange={(c) => reviewStore.taste = c}/>{' '}O-M-G 🍔
              </FormGroup>
              <FormGroup>
                <legend>How Do You like the Restaurant?</legend>
                Not even Toilets{' '}<Rating
                  emptySymbol={<FontAwesomeIcon icon={faHamburger} color={'#777777'}
                                                className={'fa-3x'}/>}
                  fullSymbol={<FontAwesomeIcon icon={faHamburger} color={'#6e4600'}
                                               className={'fa-3x'}/>}
                  onChange={(c) => reviewStore.likeness = c}/>{' '}💯💯💯
              </FormGroup>
              <FormGroup>
                <legend>How Diverse Is the Menu?</legend>
                Only a single Burger{' '}<Rating
                  emptySymbol={<FontAwesomeIcon icon={faHamburger} color={'#777777'}
                                                className={'fa-3x'}/>}
                  fullSymbol={<FontAwesomeIcon icon={faHamburger} color={'#6e4600'}
                                               className={'fa-3x'}/>}
                  onChange={(c) => reviewStore.menuDiversity = c}/>{' '}Truffel Unicorn Steak Burger
              </FormGroup>
              <FormGroup>
                <legend>How was the Service?</legend>
                Still waiting for the Burger{' '}<Rating
                  emptySymbol={<FontAwesomeIcon icon={faHamburger} color={'#777777'}
                                                className={'fa-3x'}/>}
                  fullSymbol={<FontAwesomeIcon icon={faHamburger} color={'#6e4600'}
                                               className={'fa-3x'}/>}
                  onChange={(c) => reviewStore.service = c}/>{' '}Door8 Level
              </FormGroup>
              <FormGroup>
                <legend>How are the Prices?</legend>
                Cheap ($){' '}<Rating stop={3} emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                                             color={'#777777'}
                                                                             className={'fa-3x'}/>}
                                      fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                                   color={'#6e4600'}
                                                                   className={'fa-3x'}/>}
                                      onClick={(c) => reviewStore.priceLevel = c}/>{' '}Expensive
                ($$$)
              </FormGroup>
              <FormGroup>
                <legend>Would You Recommend/Come Again?</legend>
                <Select id="recommended" options={[{label: 'Yep! 😁', value: true}, {label: 'Nope 😕', value: false}]}
                        onChange={value => reviewStore.recommended = !!value}/>
              </FormGroup>
              <Button color={'primary'} onClick={() => reviewStore.saveReview()}>Save</Button>
            </Form>
          </Jumbotron>
        </Container>
    );
  }
}

export default withAuthorization(Review);
