import { element, by, ElementFinder } from 'protractor';

export class ReviewComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-review div table .btn-danger'));
  title = element.all(by.css('jhi-review div h2#page-heading span')).first();

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

export class ReviewUpdatePage {
  pageTitle = element(by.id('jhi-review-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));
  reviewInput = element(by.id('field_review'));
  tasteInput = element(by.id('field_taste'));
  likenessInput = element(by.id('field_likeness'));
  menuDiversityInput = element(by.id('field_menuDiversity'));
  serviceInput = element(by.id('field_service'));
  priceLevelInput = element(by.id('field_priceLevel'));
  recommendedInput = element(by.id('field_recommended'));
  visitSelect = element(by.id('field_visit'));
  userSelect = element(by.id('field_user'));

  async getPageTitle() {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setReviewInput(review) {
    await this.reviewInput.sendKeys(review);
  }

  async getReviewInput() {
    return await this.reviewInput.getAttribute('value');
  }

  async setTasteInput(taste) {
    await this.tasteInput.sendKeys(taste);
  }

  async getTasteInput() {
    return await this.tasteInput.getAttribute('value');
  }

  async setLikenessInput(likeness) {
    await this.likenessInput.sendKeys(likeness);
  }

  async getLikenessInput() {
    return await this.likenessInput.getAttribute('value');
  }

  async setMenuDiversityInput(menuDiversity) {
    await this.menuDiversityInput.sendKeys(menuDiversity);
  }

  async getMenuDiversityInput() {
    return await this.menuDiversityInput.getAttribute('value');
  }

  async setServiceInput(service) {
    await this.serviceInput.sendKeys(service);
  }

  async getServiceInput() {
    return await this.serviceInput.getAttribute('value');
  }

  async setPriceLevelInput(priceLevel) {
    await this.priceLevelInput.sendKeys(priceLevel);
  }

  async getPriceLevelInput() {
    return await this.priceLevelInput.getAttribute('value');
  }

  getRecommendedInput(timeout?: number) {
    return this.recommendedInput;
  }

  async visitSelectLastOption(timeout?: number) {
    await this.visitSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async visitSelectOption(option) {
    await this.visitSelect.sendKeys(option);
  }

  getVisitSelect(): ElementFinder {
    return this.visitSelect;
  }

  async getVisitSelectedOption() {
    return await this.visitSelect.element(by.css('option:checked')).getText();
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

export class ReviewDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-review-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-review'));

  async getDialogTitle() {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(timeout?: number) {
    await this.confirmButton.click();
  }
}
