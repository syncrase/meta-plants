import { element, by, ElementFinder } from 'protractor';

export class SousRegneComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-sous-regne div table .btn-danger'));
  title = element.all(by.css('perma-sous-regne div h2#page-heading span')).first();
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

export class SousRegneUpdatePage {
  pageTitle = element(by.id('perma-sous-regne-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  regneSelect = element(by.id('field_regne'));
  sousRegneSelect = element(by.id('field_sousRegne'));

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

  async regneSelectLastOption(): Promise<void> {
    await this.regneSelect.all(by.tagName('option')).last().click();
  }

  async regneSelectOption(option: string): Promise<void> {
    await this.regneSelect.sendKeys(option);
  }

  getRegneSelect(): ElementFinder {
    return this.regneSelect;
  }

  async getRegneSelectedOption(): Promise<string> {
    return await this.regneSelect.element(by.css('option:checked')).getText();
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

export class SousRegneDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-sousRegne-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-sousRegne'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
