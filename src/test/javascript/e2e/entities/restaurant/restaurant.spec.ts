// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { RestaurantComponentsPage, /* RestaurantDeleteDialog, */ RestaurantUpdatePage } from './restaurant.page-object';

const expect = chai.expect;

describe('Restaurant e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let restaurantUpdatePage: RestaurantUpdatePage;
  let restaurantComponentsPage: RestaurantComponentsPage;
  /* let restaurantDeleteDialog: RestaurantDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Restaurants', async () => {
    await navBarPage.goToEntity('restaurant');
    restaurantComponentsPage = new RestaurantComponentsPage();
    await browser.wait(ec.visibilityOf(restaurantComponentsPage.title), 5000);
    expect(await restaurantComponentsPage.getTitle()).to.eq('burgertuesdayApp.restaurant.home.title');
  });

  it('should load create Restaurant page', async () => {
    await restaurantComponentsPage.clickOnCreateButton();
    restaurantUpdatePage = new RestaurantUpdatePage();
    expect(await restaurantUpdatePage.getPageTitle()).to.eq('burgertuesdayApp.restaurant.home.createOrEditLabel');
    await restaurantUpdatePage.cancel();
  });

  /*  it('should create and save Restaurants', async () => {
        const nbButtonsBeforeCreate = await restaurantComponentsPage.countDeleteButtons();

        await restaurantComponentsPage.clickOnCreateButton();
        await promise.all([
            restaurantUpdatePage.setPlaceIdInput('placeId'),
            restaurantUpdatePage.setNameInput('name'),
            restaurantUpdatePage.setAddressInput('address'),
            restaurantUpdatePage.setVicinityInput('vicinity'),
            restaurantUpdatePage.setUrlInput('url'),
            restaurantUpdatePage.setWebsiteInput('website'),
            restaurantUpdatePage.setGoogleRatingInput('5'),
            restaurantUpdatePage.setPriceLevelInput('priceLevel'),
            restaurantUpdatePage.userSelectLastOption(),
        ]);
        expect(await restaurantUpdatePage.getPlaceIdInput()).to.eq('placeId', 'Expected PlaceId value to be equals to placeId');
        expect(await restaurantUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
        expect(await restaurantUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');
        expect(await restaurantUpdatePage.getVicinityInput()).to.eq('vicinity', 'Expected Vicinity value to be equals to vicinity');
        expect(await restaurantUpdatePage.getUrlInput()).to.eq('url', 'Expected Url value to be equals to url');
        expect(await restaurantUpdatePage.getWebsiteInput()).to.eq('website', 'Expected Website value to be equals to website');
        expect(await restaurantUpdatePage.getGoogleRatingInput()).to.eq('5', 'Expected googleRating value to be equals to 5');
        expect(await restaurantUpdatePage.getPriceLevelInput()).to.eq('priceLevel', 'Expected PriceLevel value to be equals to priceLevel');
        const selectedPermanentlyClosed = restaurantUpdatePage.getPermanentlyClosedInput();
        if (await selectedPermanentlyClosed.isSelected()) {
            await restaurantUpdatePage.getPermanentlyClosedInput().click();
            expect(await restaurantUpdatePage.getPermanentlyClosedInput().isSelected(), 'Expected permanentlyClosed not to be selected').to.be.false;
        } else {
            await restaurantUpdatePage.getPermanentlyClosedInput().click();
            expect(await restaurantUpdatePage.getPermanentlyClosedInput().isSelected(), 'Expected permanentlyClosed to be selected').to.be.true;
        }
        await restaurantUpdatePage.save();
        expect(await restaurantUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await restaurantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /*  it('should delete last Restaurant', async () => {
        const nbButtonsBeforeDelete = await restaurantComponentsPage.countDeleteButtons();
        await restaurantComponentsPage.clickOnLastDeleteButton();

        restaurantDeleteDialog = new RestaurantDeleteDialog();
        expect(await restaurantDeleteDialog.getDialogTitle())
            .to.eq('burgertuesdayApp.restaurant.delete.question');
        await restaurantDeleteDialog.clickOnConfirmButton();

        expect(await restaurantComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
