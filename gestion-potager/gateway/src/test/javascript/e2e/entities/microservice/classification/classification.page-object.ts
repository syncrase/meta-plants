import { element, by, ElementFinder } from 'protractor';

export class ClassificationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('gp-classification div table .btn-danger'));
  title = element.all(by.css('gp-classification div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

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

export class ClassificationUpdatePage {
  pageTitle = element(by.id('gp-classification-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  raunkierSelect = element(by.id('field_raunkier'));
  cronquistSelect = element(by.id('field_cronquist'));
  apg1Select = element(by.id('field_apg1'));
  apg2Select = element(by.id('field_apg2'));
  apg3Select = element(by.id('field_apg3'));
  apg4Select = element(by.id('field_apg4'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async raunkierSelectLastOption(): Promise<void> {
    await this.raunkierSelect.all(by.tagName('option')).last().click();
  }

  async raunkierSelectOption(option: string): Promise<void> {
    await this.raunkierSelect.sendKeys(option);
  }

  getRaunkierSelect(): ElementFinder {
    return this.raunkierSelect;
  }

  async getRaunkierSelectedOption(): Promise<string> {
    return await this.raunkierSelect.element(by.css('option:checked')).getText();
  }

  async cronquistSelectLastOption(): Promise<void> {
    await this.cronquistSelect.all(by.tagName('option')).last().click();
  }

  async cronquistSelectOption(option: string): Promise<void> {
    await this.cronquistSelect.sendKeys(option);
  }

  getCronquistSelect(): ElementFinder {
    return this.cronquistSelect;
  }

  async getCronquistSelectedOption(): Promise<string> {
    return await this.cronquistSelect.element(by.css('option:checked')).getText();
  }

  async apg1SelectLastOption(): Promise<void> {
    await this.apg1Select.all(by.tagName('option')).last().click();
  }

  async apg1SelectOption(option: string): Promise<void> {
    await this.apg1Select.sendKeys(option);
  }

  getApg1Select(): ElementFinder {
    return this.apg1Select;
  }

  async getApg1SelectedOption(): Promise<string> {
    return await this.apg1Select.element(by.css('option:checked')).getText();
  }

  async apg2SelectLastOption(): Promise<void> {
    await this.apg2Select.all(by.tagName('option')).last().click();
  }

  async apg2SelectOption(option: string): Promise<void> {
    await this.apg2Select.sendKeys(option);
  }

  getApg2Select(): ElementFinder {
    return this.apg2Select;
  }

  async getApg2SelectedOption(): Promise<string> {
    return await this.apg2Select.element(by.css('option:checked')).getText();
  }

  async apg3SelectLastOption(): Promise<void> {
    await this.apg3Select.all(by.tagName('option')).last().click();
  }

  async apg3SelectOption(option: string): Promise<void> {
    await this.apg3Select.sendKeys(option);
  }

  getApg3Select(): ElementFinder {
    return this.apg3Select;
  }

  async getApg3SelectedOption(): Promise<string> {
    return await this.apg3Select.element(by.css('option:checked')).getText();
  }

  async apg4SelectLastOption(): Promise<void> {
    await this.apg4Select.all(by.tagName('option')).last().click();
  }

  async apg4SelectOption(option: string): Promise<void> {
    await this.apg4Select.sendKeys(option);
  }

  getApg4Select(): ElementFinder {
    return this.apg4Select;
  }

  async getApg4SelectedOption(): Promise<string> {
    return await this.apg4Select.element(by.css('option:checked')).getText();
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

export class ClassificationDeleteDialog {
  private dialogTitle = element(by.id('gp-delete-classification-heading'));
  private confirmButton = element(by.id('gp-confirm-delete-classification'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
