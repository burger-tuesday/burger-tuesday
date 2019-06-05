import {faCheck} from '@fortawesome/free-solid-svg-icons/faCheck';
import {faEuroSign} from '@fortawesome/free-solid-svg-icons/faEuroSign';
import {faHamburger} from '@fortawesome/free-solid-svg-icons/faHamburger';
import {faTimes} from '@fortawesome/free-solid-svg-icons/faTimes';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import * as _ from 'lodash';
import {observer} from 'mobx-react';
import * as React from 'react';
import {RouteComponentProps, withRouter} from 'react-router';
import ReactTable from 'react-table';
import {Card, CardBody, CardHeader, Container} from 'reactstrap';
import {withAuthorization} from '../../shared/auth/session-renewing-component';
import FixedRating from '../../shared/FixedRating';
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
                      Header: 'Date',
                      columns: [
                        {
                          Header: 'Date',
                          accessor: 'date',
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
                          Aggregated: row => (<span>{row.value}/1 (avg)</span>),
                          Cell: row => (row.value ? <FontAwesomeIcon icon={faCheck}/> :
                              <FontAwesomeIcon icon={faTimes}/>)
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
                      return {...r, date: v.date}
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
}

export default withAuthorization(withRouter(RestaurantDetails));
