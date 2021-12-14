import { element, by, ElementFinder } from 'protractor';

export class RameauComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-rameau div table .btn-danger'));
  title = element.all(by.css('perma-rameau div h2#page-heading span')).first();
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
    return this.title.getText();
  }
}

export class RameauUpdatePage {
  pageTitle = element(by.id('perma-rameau-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  sousRegneSelect = element(by.id('field_sousRegne'));
  rameauSelect = element(by.id('field_rameau'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setNomFrInput(nomFr: string): Promise<void> {
    await this.nomFrInput.sendKeys(nomFr);
  }

  async getNomFrInput(): Promise<string> {
    return await this.nomFrInput.getAttribute('value');
  }

  async setNomLatinInput(nomLatin: string): Promise<void> {
    await this.nomLatinInput.sendKeys(nomLatin);
  }

  async getNomLatinInput(): Promise<string> {
    return await this.nomLatinInput.getAttribute('value');
  }

  async sousRegneSelectLastOption(): Promise<void> {
    await this.sousRegneSelect.all(by.tagName('option')).last().click();
  }

  async sousRegneSelectOption(option: string): Promise<void> {
    await this.sousRegneSelect.sendKeys(option);
  }

  getSousRegneSelect(): ElementFinder {
    return this.sousRegneSelect;
  }

  async getSousRegneSelectedOption(): Promise<string> {
    return await this.sousRegneSelect.element(by.css('option:checked')).getText();
  }

  async rameauSelectLastOption(): Promise<void> {
    await this.rameauSelect.all(by.tagName('option')).last().click();
  }

  async rameauSelectOption(option: string): Promise<void> {
    await this.rameauSelect.sendKeys(option);
  }

  getRameauSelect(): ElementFinder {
    return this.rameauSelect;
  }

  async getRameauSelectedOption(): Promise<string> {
    return await this.rameauSelect.element(by.css('option:checked')).getText();
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

export class RameauDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-rameau-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-rameau'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
