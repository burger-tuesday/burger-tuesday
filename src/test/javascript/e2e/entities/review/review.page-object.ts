import { element, by, ElementFinder } from 'protractor';

export class ReviewComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-review div table .btn-danger'));
  title = element.all(by.css('jhi-review div h2#page-heading span')).first();

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

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setReviewInput(review: string): Promise<void> {
    await this.reviewInput.sendKeys(review);
  }

  async getReviewInput(): Promise<string> {
    return await this.reviewInput.getAttribute('value');
  }

  async setTasteInput(taste: string): Promise<void> {
    await this.tasteInput.sendKeys(taste);
  }

  async getTasteInput(): Promise<string> {
    return await this.tasteInput.getAttribute('value');
  }

  async setLikenessInput(likeness: string): Promise<void> {
    await this.likenessInput.sendKeys(likeness);
  }

  async getLikenessInput(): Promise<string> {
    return await this.likenessInput.getAttribute('value');
  }

  async setMenuDiversityInput(menuDiversity: string): Promise<void> {
    await this.menuDiversityInput.sendKeys(menuDiversity);
  }

  async getMenuDiversityInput(): Promise<string> {
    return await this.menuDiversityInput.getAttribute('value');
  }

  async setServiceInput(service: string): Promise<void> {
    await this.serviceInput.sendKeys(service);
  }

  async getServiceInput(): Promise<string> {
    return await this.serviceInput.getAttribute('value');
  }

  async setPriceLevelInput(priceLevel: string): Promise<void> {
    await this.priceLevelInput.sendKeys(priceLevel);
  }

  async getPriceLevelInput(): Promise<string> {
    return await this.priceLevelInput.getAttribute('value');
  }

  getRecommendedInput(): ElementFinder {
    return this.recommendedInput;
  }

  async visitSelectLastOption(): Promise<void> {
    await this.visitSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async visitSelectOption(option: string): Promise<void> {
    await this.visitSelect.sendKeys(option);
  }

  getVisitSelect(): ElementFinder {
    return this.visitSelect;
  }

  async getVisitSelectedOption(): Promise<string> {
    return await this.visitSelect.element(by.css('option:checked')).getText();
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

export class ReviewDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-review-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-review'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
