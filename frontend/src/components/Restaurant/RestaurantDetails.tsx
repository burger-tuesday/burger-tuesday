import {faEuroSign} from '@fortawesome/free-solid-svg-icons/faEuroSign';
import {faHamburger} from '@fortawesome/free-solid-svg-icons/faHamburger';
import {faThumbsDown} from '@fortawesome/free-solid-svg-icons/faThumbsDown';
import {faThumbsUp} from '@fortawesome/free-solid-svg-icons/faThumbsUp';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import * as _ from 'lodash';
import {observer} from 'mobx-react';
import * as React from 'react';
import {ReactNode} from 'react';
import {SingleDatePicker} from 'react-dates';
import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';
import {RouteComponentProps, withRouter} from 'react-router';
import Select from 'react-select';
import ReactTable from 'react-table';
import {Button, Card, CardBody, CardHeader, Container, Form, FormGroup} from 'reactstrap';
import {withAuthorization} from '../../shared/auth/session-renewing-component';
import FixedRating from '../../shared/FixedRating';
import authStore from '../../stores/authStore';
import restaurantStore from '../../stores/restaurantStore';


interface IRestaurantProps {
  id: string;
}

@observer
class RestaurantDetails extends React.Component<RouteComponentProps<IRestaurantProps>, {}> {
  constructor(props: RouteComponentProps<IRestaurantProps>) {
    super(props);
  }

  public componentDidMount(): void {
    restaurantStore.getRestaurant(this.props.match.params.id);
  }

  public render() {
    return (
        <Container fluid={true}>
          <h1>{restaurantStore.restaurant.map(r => r.name).toNullable()}</h1>
          {this.addVisitComponent()}
          <Card>
            <CardHeader>Details</CardHeader>
            <CardBody>
              <dl>
                <dt>
                  <span id='address'>Address</span>
                </dt>
                <dd>{restaurantStore.restaurant.map(r => r.address).toNullable()}</dd>
                <dt>
                  <span id='vicinity'>Vicinity</span>
                </dt>
                <dd>{restaurantStore.restaurant.map(r => r.vicinity).toNullable()}</dd>
                <dt>
                  <span id='url'>Url</span>
                </dt>
                <dd><a
                    href={restaurantStore.restaurant.map(r => r.url).toUndefined()}>{restaurantStore.restaurant.map(r => r.url).toNullable()}</a>
                </dd>
                <dt>
                  <span id='website'>Website</span>
                </dt>
                <dd><a
                    href={restaurantStore.restaurant.map(r => r.website).toUndefined()}>{restaurantStore.restaurant.map(r => r.website).toNullable()}</a>
                </dd>
                <dt>
                  <span id='googleRating'>Google Rating</span>
                </dt>
                <dd>{restaurantStore.restaurant.map(r => r.googleRating).toNullable()}</dd>
                <dt>
                  <span id='priceLevel'>Price Level</span>
                </dt>
                <dd>{restaurantStore.restaurant.map(r => r.priceLevel).toNullable()}</dd>
                <dt>
                  <span id='permanentlyClosed'>Permanently Closed</span>
                </dt>
                <dd>{restaurantStore.restaurant.map(r => r.permanentlyClosed ? 'true' : 'false').toNullable()}</dd>
              </dl>
            </CardBody>
          </Card>
          <br/>
          <Card>
            <CardHeader>Visits & Reviews</CardHeader>
            <CardBody>
              <ReactTable
                  columns={[
                    {
                      Header: 'Visit',
                      columns: [
                        {
                          Header: 'Visit',
                          accessor: 'date'
                        }
                      ]
                    },
                    {
                      Header: 'Review',
                      columns: [
                        {
                          Header: 'Taste',
                          accessor: 'taste',
                          aggregate: vals => _.round(_.mean(vals), 2),
                          Aggregated: row => (<span>{row.value}/10 (avg)</span>),
                          Cell: row => <FixedRating readonly={true} stop={10}
                                                    initialRating={row.value}
                                                    emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                                                  color={'#777777'}/>}
                                                    fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                                                 color={'#6e4600'}/>}/>
                        },
                        {
                          Header: 'Likeness',
                          accessor: 'likeness',
                          aggregate: vals => _.round(_.mean(vals), 2),
                          Aggregated: row => (<span>{row.value}/5 (avg)</span>),
                          Cell: row => <FixedRating readonly={true} initialRating={row.value}
                                                    emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                                                  color={'#777777'}/>}
                                                    fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                                                 color={'#6e4600'}/>}/>
                        },
                        {
                          Header: 'Menu Diversity',
                          accessor: 'menuDiversity',
                          aggregate: vals => _.round(_.mean(vals), 2),
                          Aggregated: row => (<span>{row.value}/5 (avg)</span>),
                          Cell: row => <FixedRating readonly={true} initialRating={row.value}
                                                    emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                                                  color={'#777777'}/>}
                                                    fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                                                 color={'#6e4600'}/>}/>
                        },
                        {
                          Header: 'Service',
                          accessor: 'service',
                          aggregate: vals => _.round(_.mean(vals), 2),
                          Aggregated: row => (<span>{row.value}/5 (avg)</span>),
                          Cell: row => <FixedRating readonly={true} initialRating={row.value}
                                                    emptySymbol={<FontAwesomeIcon icon={faHamburger}
                                                                                  color={'#777777'}/>}
                                                    fullSymbol={<FontAwesomeIcon icon={faHamburger}
                                                                                 color={'#6e4600'}/>}/>
                        },
                        {
                          Header: 'Price Level',
                          accessor: 'priceLevel',
                          aggregate: vals => _.round(_.mean(vals), 2),
                          Aggregated: row => (<span>{row.value}/3 (avg)</span>),
                          Cell: row => _.times(row.value, (n) => <FontAwesomeIcon icon={faEuroSign}
                                                                                  key={n}/>)
                        },
                        {
                          Header: 'Recommended',
                          accessor: 'recommended',
                          aggregate: vals => _.round(_.mean(vals), 2),
                          Aggregated: row => (<span>{row.value > 0.5 ?
                              <FontAwesomeIcon icon={faThumbsUp} title={row.value.toString()}/> :
                              <FontAwesomeIcon icon={faThumbsDown}
                                               title={row.value.toString()}/>} (avg)</span>),
                          Cell: row => (row.value ? <FontAwesomeIcon icon={faThumbsUp}/> :
                              <FontAwesomeIcon icon={faThumbsDown}/>)
                        },
                        {
                          Header: 'Notes',
                          accessor: 'text',
                          Aggregated: () => null,
                        },
                        {
                          Header: 'Author',
                          accessor: 'createdByName',
                          aggregate: (vals: string[]) => _.map(vals, v => v.split(' ').map((n) => n[0].toLocaleUpperCase()).join('')).join(', ')
                        }
                      ]
                    }
                  ]}
                  data={restaurantStore.restaurant.mapNullable(x => _.flatMap(x.visits, v => {
                    return _.map(v.reviews, r => {
                      return {...r, date: v.date, sponsored: v.sponsored}
                    });
                  })).getOrElse([])}
                  pivotBy={['date']}
                  className='-highlight'
                  defaultPageSize={5}
              />
            </CardBody>
          </Card>
        </Container>
    );
  }

  private addVisitComponent(): ReactNode | null {
    if (authStore.isAuthorized('manage:visits')) {
      return (<Card>
        <CardHeader>Add Visit</CardHeader>
        <CardBody>
          <Form onSubmit={(e) => {
            e.preventDefault();
            e.stopPropagation();
          }}>
            <FormGroup>
              <legend>Date</legend>
              <SingleDatePicker
                  date={restaurantStore.date}
                  onDateChange={date => restaurantStore.date = date}
                  focused={restaurantStore.focused || false}
                  onFocusChange={({focused}) => restaurantStore.focused = focused}
                  id="date"
                  enableOutsideDays={true}
              />
              <FormGroup>
                <legend>Sponsored</legend>
                <Select id='sponsored'
                        options={[{label: 'Yep! 😁', value: true}, {label: 'Nope 😕', value: false}]}
                        onChange={(value: { label: string, value: boolean }) => restaurantStore.sponsored = value.value}/>
              </FormGroup>
              <Button color={'primary'}
                      onClick={() => restaurantStore.addVisit(this.props.match.params.id)}>Add
                visit</Button>
            </FormGroup>
          </Form>
        </CardBody>
      </Card>)
    }
    return null;
  }
}

export default withAuthorization(withRouter(RestaurantDetails));
