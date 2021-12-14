import { element, by, ElementFinder } from 'protractor';

export class SousTribuComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-sous-tribu div table .btn-danger'));
  title = element.all(by.css('perma-sous-tribu div h2#page-heading span')).first();
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

export class SousTribuUpdatePage {
  pageTitle = element(by.id('perma-sous-tribu-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  tribuSelect = element(by.id('field_tribu'));
  sousTribuSelect = element(by.id('field_sousTribu'));

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

  async sousTribuSelectLastOption(): Promise<void> {
    await this.sousTribuSelect.all(by.tagName('option')).last().click();
  }

  async sousTribuSelectOption(option: string): Promise<void> {
    await this.sousTribuSelect.sendKeys(option);
  }

  getSousTribuSelect(): ElementFinder {
    return this.sousTribuSelect;
  }

  async getSousTribuSelectedOption(): Promise<string> {
    return await this.sousTribuSelect.element(by.css('option:checked')).getText();
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

export class SousTribuDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-sousTribu-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-sousTribu'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
