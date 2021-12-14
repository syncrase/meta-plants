import { element, by, ElementFinder } from 'protractor';

export class SousVarieteComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-sous-variete div table .btn-danger'));
  title = element.all(by.css('perma-sous-variete div h2#page-heading span')).first();
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

export class SousVarieteUpdatePage {
  pageTitle = element(by.id('perma-sous-variete-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  varieteSelect = element(by.id('field_variete'));
  sousVarieteSelect = element(by.id('field_sousVariete'));

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

  async varieteSelectLastOption(): Promise<void> {
    await this.varieteSelect.all(by.tagName('option')).last().click();
  }

  async varieteSelectOption(option: string): Promise<void> {
    await this.varieteSelect.sendKeys(option);
  }

  getVarieteSelect(): ElementFinder {
    return this.varieteSelect;
  }

  async getVarieteSelectedOption(): Promise<string> {
    return await this.varieteSelect.element(by.css('option:checked')).getText();
  }

  async sousVarieteSelectLastOption(): Promise<void> {
    await this.sousVarieteSelect.all(by.tagName('option')).last().click();
  }

  async sousVarieteSelectOption(option: string): Promise<void> {
    await this.sousVarieteSelect.sendKeys(option);
  }

  getSousVarieteSelect(): ElementFinder {
    return this.sousVarieteSelect;
  }

  async getSousVarieteSelectedOption(): Promise<string> {
    return await this.sousVarieteSelect.element(by.css('option:checked')).getText();
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

export class SousVarieteDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-sousVariete-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-sousVariete'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
