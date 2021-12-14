import { element, by, ElementFinder } from 'protractor';

export class EspeceComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-espece div table .btn-danger'));
  title = element.all(by.css('perma-espece div h2#page-heading span')).first();
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

export class EspeceUpdatePage {
  pageTitle = element(by.id('perma-espece-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  sousSectionSelect = element(by.id('field_sousSection'));
  especeSelect = element(by.id('field_espece'));

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

  async sousSectionSelectLastOption(): Promise<void> {
    await this.sousSectionSelect.all(by.tagName('option')).last().click();
  }

  async sousSectionSelectOption(option: string): Promise<void> {
    await this.sousSectionSelect.sendKeys(option);
  }

  getSousSectionSelect(): ElementFinder {
    return this.sousSectionSelect;
  }

  async getSousSectionSelectedOption(): Promise<string> {
    return await this.sousSectionSelect.element(by.css('option:checked')).getText();
  }

  async especeSelectLastOption(): Promise<void> {
    await this.especeSelect.all(by.tagName('option')).last().click();
  }

  async especeSelectOption(option: string): Promise<void> {
    await this.especeSelect.sendKeys(option);
  }

  getEspeceSelect(): ElementFinder {
    return this.especeSelect;
  }

  async getEspeceSelectedOption(): Promise<string> {
    return await this.especeSelect.element(by.css('option:checked')).getText();
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

export class EspeceDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-espece-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-espece'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
