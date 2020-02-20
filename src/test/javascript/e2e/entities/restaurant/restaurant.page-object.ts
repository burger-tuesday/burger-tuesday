import { element, by, ElementFinder } from 'protractor';

export class RestaurantComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-restaurant div table .btn-danger'));
  title = element.all(by.css('jhi-restaurant div h2#page-heading span')).first();

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class RestaurantUpdatePage {
  pageTitle = element(by.id('jhi-restaurant-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  placeIdInput = element(by.id('field_placeId'));
  nameInput = element(by.id('field_name'));
  addressInput = element(by.id('field_address'));
  vicinityInput = element(by.id('field_vicinity'));
  urlInput = element(by.id('field_url'));
  websiteInput = element(by.id('field_website'));
  googleRatingInput = element(by.id('field_googleRating'));
  btRatingInput = element(by.id('field_btRating'));
  numberOfReviewsInput = element(by.id('field_numberOfReviews'));
  priceLevelInput = element(by.id('field_priceLevel'));
  permanentlyClosedInput = element(by.id('field_permanentlyClosed'));
  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setPlaceIdInput(placeId: string): Promise<void> {
    await this.placeIdInput.sendKeys(placeId);
  }

  async getPlaceIdInput(): Promise<string> {
    return await this.placeIdInput.getAttribute('value');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setAddressInput(address: string): Promise<void> {
    await this.addressInput.sendKeys(address);
  }

  async getAddressInput(): Promise<string> {
    return await this.addressInput.getAttribute('value');
  }

  async setVicinityInput(vicinity: string): Promise<void> {
    await this.vicinityInput.sendKeys(vicinity);
  }

  async getVicinityInput(): Promise<string> {
    return await this.vicinityInput.getAttribute('value');
  }

  async setUrlInput(url: string): Promise<void> {
    await this.urlInput.sendKeys(url);
  }

  async getUrlInput(): Promise<string> {
    return await this.urlInput.getAttribute('value');
  }

  async setWebsiteInput(website: string): Promise<void> {
    await this.websiteInput.sendKeys(website);
  }

  async getWebsiteInput(): Promise<string> {
    return await this.websiteInput.getAttribute('value');
  }

  async setGoogleRatingInput(googleRating: string): Promise<void> {
    await this.googleRatingInput.sendKeys(googleRating);
  }

  async getGoogleRatingInput(): Promise<string> {
    return await this.googleRatingInput.getAttribute('value');
  }

  async setBtRatingInput(btRating: string): Promise<void> {
    await this.btRatingInput.sendKeys(btRating);
  }

  async getBtRatingInput(): Promise<string> {
    return await this.btRatingInput.getAttribute('value');
  }

  async setNumberOfReviewsInput(numberOfReviews: string): Promise<void> {
    await this.numberOfReviewsInput.sendKeys(numberOfReviews);
  }

  async getNumberOfReviewsInput(): Promise<string> {
    return await this.numberOfReviewsInput.getAttribute('value');
  }

  async setPriceLevelInput(priceLevel: string): Promise<void> {
    await this.priceLevelInput.sendKeys(priceLevel);
  }

  async getPriceLevelInput(): Promise<string> {
    return await this.priceLevelInput.getAttribute('value');
  }

  getPermanentlyClosedInput(): ElementFinder {
    return this.permanentlyClosedInput;
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class RestaurantDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-restaurant-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-restaurant'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
