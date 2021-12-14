import { element, by, ElementFinder } from 'protractor';

export class SousFormeComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-sous-forme div table .btn-danger'));
  title = element.all(by.css('perma-sous-forme div h2#page-heading span')).first();
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

export class SousFormeUpdatePage {
  pageTitle = element(by.id('perma-sous-forme-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  formeSelect = element(by.id('field_forme'));
  sousFormeSelect = element(by.id('field_sousForme'));

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

  async formeSelectLastOption(): Promise<void> {
    await this.formeSelect.all(by.tagName('option')).last().click();
  }

  async formeSelectOption(option: string): Promise<void> {
    await this.formeSelect.sendKeys(option);
  }

  getFormeSelect(): ElementFinder {
    return this.formeSelect;
  }

  async getFormeSelectedOption(): Promise<string> {
    return await this.formeSelect.element(by.css('option:checked')).getText();
  }

  async sousFormeSelectLastOption(): Promise<void> {
    await this.sousFormeSelect.all(by.tagName('option')).last().click();
  }

  async sousFormeSelectOption(option: string): Promise<void> {
    await this.sousFormeSelect.sendKeys(option);
  }

  getSousFormeSelect(): ElementFinder {
    return this.sousFormeSelect;
  }

  async getSousFormeSelectedOption(): Promise<string> {
    return await this.sousFormeSelect.element(by.css('option:checked')).getText();
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

export class SousFormeDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-sousForme-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-sousForme'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
