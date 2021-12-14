import { element, by, ElementFinder } from 'protractor';

export class SuperOrdreComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('perma-super-ordre div table .btn-danger'));
  title = element.all(by.css('perma-super-ordre div h2#page-heading span')).first();
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

export class SuperOrdreUpdatePage {
  pageTitle = element(by.id('perma-super-ordre-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  nomFrInput = element(by.id('field_nomFr'));
  nomLatinInput = element(by.id('field_nomLatin'));

  infraClasseSelect = element(by.id('field_infraClasse'));
  superOrdreSelect = element(by.id('field_superOrdre'));

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

  async infraClasseSelectLastOption(): Promise<void> {
    await this.infraClasseSelect.all(by.tagName('option')).last().click();
  }

  async infraClasseSelectOption(option: string): Promise<void> {
    await this.infraClasseSelect.sendKeys(option);
  }

  getInfraClasseSelect(): ElementFinder {
    return this.infraClasseSelect;
  }

  async getInfraClasseSelectedOption(): Promise<string> {
    return await this.infraClasseSelect.element(by.css('option:checked')).getText();
  }

  async superOrdreSelectLastOption(): Promise<void> {
    await this.superOrdreSelect.all(by.tagName('option')).last().click();
  }

  async superOrdreSelectOption(option: string): Promise<void> {
    await this.superOrdreSelect.sendKeys(option);
  }

  getSuperOrdreSelect(): ElementFinder {
    return this.superOrdreSelect;
  }

  async getSuperOrdreSelectedOption(): Promise<string> {
    return await this.superOrdreSelect.element(by.css('option:checked')).getText();
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

export class SuperOrdreDeleteDialog {
  private dialogTitle = element(by.id('perma-delete-superOrdre-heading'));
  private confirmButton = element(by.id('perma-confirm-delete-superOrdre'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
