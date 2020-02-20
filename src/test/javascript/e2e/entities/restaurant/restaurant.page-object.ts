import { element, by, ElementFinder } from 'protractor';

export class RestaurantComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-restaurant div table .btn-danger'));
  title = element.all(by.css('jhi-restaurant div h2#page-heading span')).first();

  async clickOnCreateButton(timeout?: number) {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(timeout?: number) {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons() {
    return this.deleteButtons.count();
  }

  async getTitle() {
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
  priceLevelInput = element(by.id('field_priceLevel'));
  permanentlyClosedInput = element(by.id('field_permanentlyClosed'));
  userSelect = element(by.id('field_user'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setPlaceIdInput(placeId) {
    await this.placeIdInput.sendKeys(placeId);
  }

  async getPlaceIdInput() {
    return await this.placeIdInput.getAttribute('value');
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return await this.nameInput.getAttribute('value');
  }

  async setAddressInput(address) {
    await this.addressInput.sendKeys(address);
  }

  async getAddressInput() {
    return await this.addressInput.getAttribute('value');
  }

  async setVicinityInput(vicinity) {
    await this.vicinityInput.sendKeys(vicinity);
  }

  async getVicinityInput() {
    return await this.vicinityInput.getAttribute('value');
  }

  async setUrlInput(url) {
    await this.urlInput.sendKeys(url);
  }

  async getUrlInput() {
    return await this.urlInput.getAttribute('value');
  }

  async setWebsiteInput(website) {
    await this.websiteInput.sendKeys(website);
  }

  async getWebsiteInput() {
    return await this.websiteInput.getAttribute('value');
  }

  async setGoogleRatingInput(googleRating) {
    await this.googleRatingInput.sendKeys(googleRating);
  }

  async getGoogleRatingInput() {
    return await this.googleRatingInput.getAttribute('value');
  }

  async setPriceLevelInput(priceLevel) {
    await this.priceLevelInput.sendKeys(priceLevel);
  }

  async getPriceLevelInput() {
    return await this.priceLevelInput.getAttribute('value');
  }

  getPermanentlyClosedInput(timeout?: number) {
    return this.permanentlyClosedInput;
  }

  async userSelectLastOption(timeout?: number) {
    await this.userSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userSelectOption(option) {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption() {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async save(timeout?: number) {
    await this.saveButton.click();
  }

  async cancel(timeout?: number) {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class RestaurantDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-restaurant-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-restaurant'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
