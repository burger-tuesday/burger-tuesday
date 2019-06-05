import {faHamburger} from '@fortawesome/free-solid-svg-icons/faHamburger';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {observer} from 'mobx-react';
import * as React from 'react';
import Select from 'react-select';
import {Button, Col, Container, Form, FormGroup, Jumbotron, Row} from 'reactstrap';
import {withAuthorization} from '../../shared/auth/session-renewing-component';
import FixedRating from '../../shared/FixedRating';
import reviewStore, {ISelectOptions} from '../../stores/reviewStore';

@observer
class Review extends React.Component<{}, {}> {
  public componentDidMount(): void {
    reviewStore.getVisits()
  }

  public render() {
    const formState = reviewStore.formState;
    return (
        <Container>
          <Jumbotron>
            <Form onSubmit={(e) => {
              e.preventDefault();
              e.stopPropagation();
            }}>
              <FormGroup>
                <legend>Visit to review</legend>
                <Select id='visit' options={reviewStore.visitOptions}
                        value={formState.$.selectedVisit.$}
                        onChange={(value: ISelectOptions) => formState.$.selectedVisit.onChange(value == null ? undefined : value)}/>
              </FormGroup>
              <FormGroup>
                <legend>Taste of Burger?</legend>
                <Row className={'align-items-center'}>
                  <Col xs={1}>Meh</Col>
                  <Col xs={'auto'}><FixedRating
                      emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                    color={'#777777'}
                                                    className={'fa-2x'}/>}
                      fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                   color={'#6e4600'}
                                                   className={'fa-2x'}/>}
                      stop={10} onChange={(c) => formState.$.taste.onChange(c)}/></Col>
                  <Col xs={1}>O-M-G</Col>
                </Row>
              </FormGroup>
              <FormGroup>
                <legend>How Do You like the Restaurant?</legend>
                <Row className={'align-items-center'}>
                  <Col xs={1}>Not even Toilets</Col>
                  <Col xs={'auto'}><FixedRating
                      emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                    color={'#777777'}
                                                    className={'fa-2x'}/>}
                      fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                   color={'#6e4600'}
                                                   className={'fa-2x'}/>}
                      onChange={(c) => formState.$.likeness.onChange(c)}/></Col>
                  <Col xs={1}>Literally Heaven</Col>
                </Row>
              </FormGroup>
              <FormGroup>
                <legend>How Diverse Is the Menu?</legend>
                <Row className={'align-items-center'}>
                  <Col xs={1}>Only a single Burger</Col>
                  <Col xs={'auto'}><FixedRating
                      emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                    color={'#777777'}
                                                    className={'fa-2x'}/>}
                      fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                   color={'#6e4600'}
                                                   className={'fa-2x'}/>}
                      onChange={(c) => formState.$.menuDiversity.onChange(c)}/></Col>
                  <Col xs={1}>Truffel Unicorn Steak Burger</Col>
                </Row>
              </FormGroup>
              <FormGroup>
                <legend>How was the Service?</legend>
                <Row className={'align-items-center'}>
                  <Col xs={1}>Still waiting for the Burger</Col>
                  <Col xs={'auto'}><FixedRating
                      emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                    color={'#777777'}
                                                    className={'fa-2x'}/>}
                      fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                   color={'#6e4600'}
                                                   className={'fa-2x'}/>}
                      onChange={(c) => formState.$.service.onChange(c)}/></Col>
                  <Col xs={1}>Door8 Level</Col>
                </Row>
              </FormGroup>
              <FormGroup>
                <legend>How are the Prices?</legend>
                <Row className={'align-items-center'}>
                  <Col xs={1}>Cheap ($)</Col>
                  <Col xs={'auto'}><FixedRating
                      emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                    color={'#777777'}
                                                    className={'fa-2x'}/>}
                      fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                   color={'#6e4600'}
                                                   className={'fa-2x'}/>}
                      stop={3} onChange={(c) => formState.$.priceLevel.onChange(c)}/></Col>
                  <Col xs={1}>Expensive ($$$)</Col>
                </Row>
              </FormGroup>
              <FormGroup>
                <legend>Would You Recommend/Come Again?</legend>
                <Select id='recommended' options={[{label: 'Yep! 😁', value: true}, {
                  label: 'Nope 😕',
                  value: false
                }]}
                        onChange={(value: { label: string, value: boolean }) => formState.$.recommended.onChange(!!value)}/>
              </FormGroup>
              <FormGroup>
                <legend>Anything more to say?</legend>
                <textarea onChange={value => formState.$.text.onChange(value.target.value)}
                          value={formState.$.text.$}/>
              </FormGroup>
              <Button color={'primary'} onClick={() => reviewStore.saveReview()}>Save</Button>
            </Form>
          </Jumbotron>
        </Container>
    );
  }
}

export default withAuthorization(Review);
