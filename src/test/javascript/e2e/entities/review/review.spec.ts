// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { ReviewComponentsPage, /* ReviewDeleteDialog, */ ReviewUpdatePage } from './review.page-object';

const expect = chai.expect;

describe('Review e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let reviewUpdatePage: ReviewUpdatePage;
  let reviewComponentsPage: ReviewComponentsPage;
  /* let reviewDeleteDialog: ReviewDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Reviews', async () => {
    await navBarPage.goToEntity('review');
    reviewComponentsPage = new ReviewComponentsPage();
    await browser.wait(ec.visibilityOf(reviewComponentsPage.title), 5000);
    expect(await reviewComponentsPage.getTitle()).to.eq('burgertuesdayApp.review.home.title');
  });

  it('should load create Review page', async () => {
    await reviewComponentsPage.clickOnCreateButton();
    reviewUpdatePage = new ReviewUpdatePage();
    expect(await reviewUpdatePage.getPageTitle()).to.eq('burgertuesdayApp.review.home.createOrEditLabel');
    await reviewUpdatePage.cancel();
  });

  /*  it('should create and save Reviews', async () => {
        const nbButtonsBeforeCreate = await reviewComponentsPage.countDeleteButtons();

        await reviewComponentsPage.clickOnCreateButton();
        await promise.all([
            reviewUpdatePage.setReviewInput('review'),
            reviewUpdatePage.setTasteInput('5'),
            reviewUpdatePage.setLikenessInput('5'),
            reviewUpdatePage.setMenuDiversityInput('5'),
            reviewUpdatePage.setServiceInput('5'),
            reviewUpdatePage.setPriceLevelInput('5'),
            reviewUpdatePage.visitSelectLastOption(),
            reviewUpdatePage.userSelectLastOption(),
        ]);
        expect(await reviewUpdatePage.getReviewInput()).to.eq('review', 'Expected Review value to be equals to review');
        expect(await reviewUpdatePage.getTasteInput()).to.eq('5', 'Expected taste value to be equals to 5');
        expect(await reviewUpdatePage.getLikenessInput()).to.eq('5', 'Expected likeness value to be equals to 5');
        expect(await reviewUpdatePage.getMenuDiversityInput()).to.eq('5', 'Expected menuDiversity value to be equals to 5');
        expect(await reviewUpdatePage.getServiceInput()).to.eq('5', 'Expected service value to be equals to 5');
        expect(await reviewUpdatePage.getPriceLevelInput()).to.eq('5', 'Expected priceLevel value to be equals to 5');
        const selectedRecommended = reviewUpdatePage.getRecommendedInput();
        if (await selectedRecommended.isSelected()) {
            await reviewUpdatePage.getRecommendedInput().click();
            expect(await reviewUpdatePage.getRecommendedInput().isSelected(), 'Expected recommended not to be selected').to.be.false;
        } else {
            await reviewUpdatePage.getRecommendedInput().click();
            expect(await reviewUpdatePage.getRecommendedInput().isSelected(), 'Expected recommended to be selected').to.be.true;
        }
        await reviewUpdatePage.save();
        expect(await reviewUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

        expect(await reviewComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
    }); */

  /*  it('should delete last Review', async () => {
        const nbButtonsBeforeDelete = await reviewComponentsPage.countDeleteButtons();
        await reviewComponentsPage.clickOnLastDeleteButton();

        reviewDeleteDialog = new ReviewDeleteDialog();
        expect(await reviewDeleteDialog.getDialogTitle())
            .to.eq('burgertuesdayApp.review.delete.question');
        await reviewDeleteDialog.clickOnConfirmButton();

        expect(await reviewComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
