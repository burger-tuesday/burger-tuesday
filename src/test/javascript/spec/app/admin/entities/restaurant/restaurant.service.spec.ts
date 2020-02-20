import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take, map } from 'rxjs/operators';
import { RestaurantService } from 'app/admin/entities/restaurant/restaurant.service';
import { IRestaurant, Restaurant } from 'app/shared/model/restaurant.model';

describe('Service Tests', () => {
  describe('Restaurant Service', () => {
    let injector: TestBed;
    let service: RestaurantService;
    let httpMock: HttpTestingController;
    let elemDefault: IRestaurant;
    let expectedResult: IRestaurant | IRestaurant[] | boolean | null;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(RestaurantService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Restaurant(0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', 0, 0, 0, 'AAAAAAA', false);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Restaurant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .create(new Restaurant())
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Restaurant', () => {
        const returnedFromService = Object.assign(
          {
            placeId: 'BBBBBB',
            name: 'BBBBBB',
            address: 'BBBBBB',
            vicinity: 'BBBBBB',
            url: 'BBBBBB',
            website: 'BBBBBB',
            googleRating: 1,
            btRating: 1,
            numberOfReviews: 1,
            priceLevel: 'BBBBBB',
            permanentlyClosed: true
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp.body));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Restaurant', () => {
        const returnedFromService = Object.assign(
          {
            placeId: 'BBBBBB',
            name: 'BBBBBB',
            address: 'BBBBBB',
            vicinity: 'BBBBBB',
            url: 'BBBBBB',
            website: 'BBBBBB',
            googleRating: 1,
            btRating: 1,
            numberOfReviews: 1,
            priceLevel: 'BBBBBB',
            permanentlyClosed: true
          },
          elemDefault
        );
        const expected = Object.assign({}, returnedFromService);
        service
          .query()
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Restaurant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
