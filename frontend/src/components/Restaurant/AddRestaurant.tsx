import {observer} from 'mobx-react';
import * as React from 'react';
import PlacesAutocomplete from 'react-places-autocomplete';
import {Container} from 'reactstrap';
import authStore from '../../stores/authStore';
import placesStore from '../../stores/placesStore';
import './AddRestaurant.css'

@observer
export default class AddRestaurant extends React.Component {
  public handleChange(value: string) {
    placesStore.address = value;
    console.log(value);
  }

  public handleSelect(address: string, placeId: string) {
    placesStore.address = address;
    placesStore.placeId = placeId;
    placesStore.savePlace();
  }

  public handleClear() {
    placesStore.address = '';
    placesStore.placeId = '';
  }

  public render() {
    if (!authStore.isAuthenticated || !authStore.isAuthorized('manage:restaurants')) {
      return (
          <Container>
            <h1>Forbidden</h1>
            You don't have permission to see this page.
          </Container>
      );
    }
    return (
        <PlacesAutocomplete
            value={placesStore.address}
            onChange={this.handleChange}
            onSelect={this.handleSelect}
            searchOptions={{
              types: ['establishment'],
              fields: ['place_id', 'name', 'types'],
              location: new google.maps.LatLng(48.1954495, 16.3708058),
              radius: 10000
            }}
            highlightFirstSuggestion={true}
            shouldFetchSuggestions={placesStore.address.length > 2}
        >
          {({getInputProps, suggestions, getSuggestionItemProps}) => {
            return (
                <div className='search-bar-container'>
                  <div className='search-input-container'>
                    <input
                        {...getInputProps({
                          placeholder: 'Search Places...',
                          className: 'search-input',
                        })}
                    />
                    {placesStore.address.length > 0 && (
                        <button
                            className='clear-button'
                            onClick={this.handleClear}
                        >
                          x
                        </button>
                    )}
                  </div>
                  {suggestions.length > 0 && (
                      <div className='autocomplete-container'>
                        {suggestions.map(suggestion => {
                          const className = this.classnames('suggestion-item', {
                            'suggestion-item--active': suggestion.active,
                          });

                          return (
                              /* eslint-disable react/jsx-key */
                              <div
                                  {...getSuggestionItemProps(suggestion, {className})}
                              >
                                <strong>
                                  {suggestion.formattedSuggestion.mainText}
                                </strong>{' '}
                                <small>
                                  {suggestion.formattedSuggestion.secondaryText}
                                </small>
                              </div>
                          );
                          /* eslint-enable react/jsx-key */
                        })}
                        <div className='dropdown-footer'>
                          <div>
                            <img
                                src={require('../../images/powered_by_google_default.png')}
                                className='dropdown-footer-image'
                            />
                          </div>
                        </div>
                      </div>
                  )}
                </div>
            );
          }}
        </PlacesAutocomplete>
    );
  }

  private isObject(val: any): boolean {
    return typeof val === 'object' && val !== null;
  };

  private classnames(...args: any[]): string {
    const classes: string[] = [];
    args.forEach(arg => {
      if (typeof arg === 'string') {
        classes.push(arg);
      } else if (this.isObject(arg)) {
        Object.keys(arg).forEach(key => {
          if (arg[key]) {
            classes.push(key);
          }
        });
      } else {
        throw new Error(
            '`classnames` only accepts string or object as arguments'
        );
      }
    });

    return classes.join(' ');
  }
}
