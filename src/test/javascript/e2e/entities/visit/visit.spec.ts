import { browser, ExpectedConditions as ec /* , promise */ } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  VisitComponentsPage,
  /* VisitDeleteDialog,
   */ VisitUpdatePage
} from './visit.page-object';

const expect = chai.expect;

describe('Visit e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let visitComponentsPage: VisitComponentsPage;
  let visitUpdatePage: VisitUpdatePage;
  /* let visitDeleteDialog: VisitDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Visits', async () => {
    await navBarPage.goToEntity('visit');
    visitComponentsPage = new VisitComponentsPage();
    await browser.wait(ec.visibilityOf(visitComponentsPage.title), 5000);
    expect(await visitComponentsPage.getTitle()).to.eq('burgertuesdayApp.visit.home.title');
  });

  it('should load create Visit page', async () => {
    await visitComponentsPage.clickOnCreateButton();
    visitUpdatePage = new VisitUpdatePage();
    expect(await visitUpdatePage.getPageTitle()).to.eq('burgertuesdayApp.visit.home.createOrEditLabel');
    await visitUpdatePage.cancel();
  });

  /*  it('should create and save Visits', async () => {
        const nbButtonsBeforeCreate = await visitComponentsPage.countDeleteButtons();

        await visitComponentsPage.clickOnCreateButton();
        await promise.all([
            visitUpdatePage.setDateInput('2000-12-31'),
            visitUpdatePage.restaurantSelectLastOption(),
            visitUpdatePage.userSelectLastOption(),
        ]);
        expect(await visitUpdatePage.getDateInput()).to.eq('2000-12-31', 'Expected date value to be equals to 2000-12-31');
        const selectedSponsored = visitUpdatePage.getSponsoredInput();
        if (await selectedSponsored.isSelected()) {
            await visitUpdatePage.getSponsoredInput().click();
            expect(await visitUpdatePage.getSponsoredInput().isSelected(), 'Expected sponsored not to be selected').to.be.false;
        } else {
            await visitUpdatePage.getSponsoredInput().click();
            expect(await visitUpdatePage.getSponsoredInput().isSelected(), 'Expected sponsored to be selected').to.be.true;
        }
        await visitUpdatePage.save();
        expect(await visitUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await visitComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /*  it('should delete last Visit', async () => {
        const nbButtonsBeforeDelete = await visitComponentsPage.countDeleteButtons();
        await visitComponentsPage.clickOnLastDeleteButton();

        visitDeleteDialog = new VisitDeleteDialog();
        expect(await visitDeleteDialog.getDialogTitle())
            .to.eq('burgertuesdayApp.visit.delete.question');
        await visitDeleteDialog.clickOnConfirmButton();

        expect(await visitComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
