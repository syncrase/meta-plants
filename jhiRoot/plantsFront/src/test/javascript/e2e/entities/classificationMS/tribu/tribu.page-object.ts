import { element, by, ElementFinder } from 'protractor';

export class TribuComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-tribu div table .btn-danger'));
  title = element.all(by.css('perma-tribu div h2#page-heading span')).first();
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

export class TribuUpdatePage {
  pageTitle = element(by.id('perma-tribu-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  sousFamilleSelect = element(by.id('field_sousFamille'));
  tribuSelect = element(by.id('field_tribu'));

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

  async sousFamilleSelectLastOption(): Promise<void> {
    await this.sousFamilleSelect.all(by.tagName('option')).last().click();
  }

  async sousFamilleSelectOption(option: string): Promise<void> {
    await this.sousFamilleSelect.sendKeys(option);
  }

  getSousFamilleSelect(): ElementFinder {
    return this.sousFamilleSelect;
  }

  async getSousFamilleSelectedOption(): Promise<string> {
    return await this.sousFamilleSelect.element(by.css('option:checked')).getText();
  }

  async tribuSelectLastOption(): Promise<void> {
    await this.tribuSelect.all(by.tagName('option')).last().click();
  }

  async tribuSelectOption(option: string): Promise<void> {
    await this.tribuSelect.sendKeys(option);
  }

  getTribuSelect(): ElementFinder {
    return this.tribuSelect;
  }

  async getTribuSelectedOption(): Promise<string> {
    return await this.tribuSelect.element(by.css('option:checked')).getText();
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

export class TribuDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-tribu-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-tribu'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
